package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RetreatDishAdapter;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/1.
 */
public class LFragmentRetreatDish extends BaseFragment implements View.OnClickListener {
    private TextView tvOrderNumber;
    private TextView tvSeatNumber;
    private TextView tvSeatCount;
    private TextView tvTime;
    private Button btnBack, btnRetreatAll, btnRetreat;
    private RecyclerView mRecyclerView;
    private RetreatDishAdapter adapter;
    private TextView tvOrderName;
    private LinearLayout layoutOrderDetial;
    private boolean mIsOpenJoinOrder;
    private String orderId;
    private MainFragmentListener mOnRetreatDishClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnRetreatDishClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_retreat_dish_layout, container, false);
        initView(mView);
        initData();
        setListener();
        setAdapter();
        return mView;
    }

    private void initView(View view) {
        layoutOrderDetial = (LinearLayout) view.findViewById(R.id.layout_order_detail);
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
        tvSeatCount = (TextView) view.findViewById(R.id.tv_seat_count);
        tvTime = (TextView) view.findViewById(R.id.tv_create_time);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        btnRetreatAll = (Button) view.findViewById(R.id.btn_retreat_all);
        btnRetreat = (Button) view.findViewById(R.id.btn_retreat);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvOrderName = (TextView) view.findViewById(R.id.tv_order_name);
        orderId = getArguments().getString("orderId");
        mIsOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String orderId,boolean isOpenJoinOrder){
        this.orderId = orderId;
        this.mIsOpenJoinOrder = isOpenJoinOrder;

        initData();
        if(adapter != null){
            adapter.updateData(this.orderId,this.mIsOpenJoinOrder);
        }
    }

    private void initData(){
        if(mIsOpenJoinOrder){
            //总单
            tvOrderName.setText("总账单");
            String joinName = DBHelper.getInstance(getActivity().getApplicationContext()).getJoinName(orderId);
            tvOrderNumber.setText(joinName);
            layoutOrderDetial.setVisibility(LinearLayout.GONE);
        }else{
            //子单
            tvOrderName.setText("账单");
            layoutOrderDetial.setVisibility(LinearLayout.VISIBLE);
            OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(orderId);
            tvOrderNumber.setText("NO."+orderEntity.getOrderNumber1());
            tvSeatNumber.setText("座号："+DBHelper.getInstance(getActivity().getApplicationContext()).getTableNameByTableId(orderEntity.getTableId()));
            tvSeatCount.setText("人数："+orderEntity.getOrderGuests());
            tvTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(),"yyyy-MM-dd HH:mm"));
        }
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
        btnRetreatAll.setOnClickListener(this);
        btnRetreat.setOnClickListener(this);
    }

    private void setAdapter() {
        adapter = new RetreatDishAdapter(getContext(), orderId, mIsOpenJoinOrder);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                mOnRetreatDishClickListener.onRetreatBackClick(orderId);
                break;
            case R.id.btn_retreat_all:
                adapter.setReteatAll();
                mOnRetreatDishClickListener.onRetreatAllClick(adapter.getRetreatDishes(),orderId);
                break;
            case R.id.btn_retreat:
                mOnRetreatDishClickListener.onRetreatClick(adapter.getRetreatDishes(),orderId);
                break;
        }
    }
}
