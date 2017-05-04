package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RetreatOrderAdapter;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.SpecialEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeRadioGroup1;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/4.
 */
public class LFragmentPresentOrder extends BaseFragment {
    private TextView tvOrderNumber;
    private TextView tvSeatNumber;
    private TextView tvSeatCount;
    private TextView tvTime;
    private Button btnCancle;
    private Button btnConfirm;
    private RecyclerView mRecyclerView;
    private CustomeRadioGroup1 mRadioGroup1;
    private RetreatOrderAdapter adapter;
    private TextView tvMore;
    private String orderId;
    private MainFragmentListener mOnPresentOrderClickListener;
    private ArrayList<OrderDishEntity> presentDishes;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnPresentOrderClickListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_present_order_layout, container, false);
        initView(mView);
        initData();
        setSpecial(4);
        setListener();
        setAdapter();
        return mView;
    }

    private void initView(View view) {
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
        tvSeatCount = (TextView) view.findViewById(R.id.tv_seat_count);
        tvTime = (TextView) view.findViewById(R.id.tv_create_time);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRadioGroup1 = (CustomeRadioGroup1) view.findViewById(R.id.crg_retreat);
        tvMore = (TextView) view.findViewById(R.id.tv_more);
        orderId = getArguments().getString("orderId");
        presentDishes = getArguments().getParcelableArrayList("retreatDishes");
    }

    public void setNewParam(String orderId,ArrayList<OrderDishEntity> presentDishes){
        this.orderId = orderId;
        this.presentDishes = presentDishes;

        initData();
        if(adapter != null){
            adapter.updateData(presentDishes);
        }
    }

    private void initData(){
        OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(orderId);
        tvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
        tvSeatNumber.setText("座号：" + DBHelper.getInstance(getActivity().getApplicationContext()).getTableNameByTableId(orderEntity.getTableId()));
        tvSeatCount.setText("人数：" + orderEntity.getOrderGuests() + "人");
        tvTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
    }

    private void setListener() {
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPresentOrderClickListener.onPresentOrderCancle(orderId);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
                if (employeeEntity != null && employeeEntity.getAuthPresent() == 1) {
                    presentDish();
                } else {
                    final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 4);
                    dialog.setArguments(bundle);
                    dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if (employeeEntity != null && employeeEntity.getAuthPresent() == 1) {
                                //该员工可以退菜，需要判断赠菜是否会不满足优惠券的使用
                                presentDish();
                                dialog.dismiss();
                            } else {
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "");
                }
            }
        });
    }

    private void presentDish(){
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        boolean canUseCoupon = DBHelper.getInstance(getContext().getApplicationContext()).canUseCouponPresent(orderId,presentDishes);
        if(canUseCoupon){
            //赠菜后订单金额仍然满足优惠券使用条件
            DBHelper.getInstance(getActivity().getApplicationContext()).presentOrderDish(orderId, presentDishes);
            DBHelper.getInstance(getContext()).dealWithVoucher(orderEntity, 1);
            mOnPresentOrderClickListener.onPresentOrderConfirm(orderId);
        }else{
            //赠菜后订单金额无法满足优惠券使用条件
            CustomMethod.showMessage(getContext(),"赠菜后订单金额将不满足优惠券使用条件，请先取消优惠券的使用！");
        }
    }

    private void setAdapter() {
        adapter = new RetreatOrderAdapter(getActivity().getApplicationContext(), presentDishes);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setSpecial(int type) {
        ArrayList<SpecialEntity> specialEntities = DBHelper.getInstance(getActivity().getApplicationContext()).queryAllSpecialByType(type);
        for (int i = 0; i < specialEntities.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(specialEntities.get(i).getSpecialName());
            radioButton.setTextColor(getResources().getColor(R.color.dark));
            radioButton.setId(i);
            radioButton.setTag(specialEntities.get(i).getSpecialId());
            mRadioGroup1.addView(radioButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
