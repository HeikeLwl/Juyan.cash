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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RetreatOrderAdapter;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.SpecialEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeRadioGroup1;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

import static jufeng.juyancash.util.CustomMethod.showToast;

/**
 * Created by Administrator102 on 2016/8/4.
 */
public class LFragmentRetreatOrder extends BaseFragment {
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
    private MainFragmentListener mOnRetreatOrderClickListener;
    private ArrayList<OrderDishEntity> retreatDishes;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnRetreatOrderClickListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_retreat_order_layout, container, false);
        initView(mView);
        initData();
        setSpecial(0);
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
        retreatDishes = getArguments().getParcelableArrayList("retreatDishes");
    }

    public void setNewParam(String orderId,ArrayList<OrderDishEntity> retreatDishes){
        this.orderId = orderId;
        this.retreatDishes = retreatDishes;

        initData();
        if(adapter != null){
            adapter.updateData(retreatDishes);
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
        mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvMore.setText("");
            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioGroup1.getCheckedRadioButtonId() >= 0) {
                    mRadioGroup1.clearCheck();
                }
                CustomMethod.addNote(getContext(), tvMore);
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRetreatOrderClickListener.onRetreatOrderCancle(orderId);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAuthority = false;
                for (OrderDishEntity orderDish :
                        retreatDishes) {
                    if (orderDish.getIsRetreat() == 1) {
                        isAuthority = true;
                        break;
                    }
                }
                EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
                if (isAuthority) {
                    if (employeeEntity != null && employeeEntity.getAuthRetreat() == 1) {
                        retreatDish(employeeEntity.getEmployeeName());
                    } else {
                        final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 3);
                        dialog.setArguments(bundle);
                        dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                            @Override
                            public void onAuthorityCancle() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                                if (employeeEntity != null && employeeEntity.getAuthRetreat() == 1) {
                                    retreatDish(employeeEntity.getEmployeeName());
                                    dialog.dismiss();
                                } else {
                                    Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.show(getFragmentManager(), "");
                    }

                } else {
                    retreatDish(null);
                }
            }
        });
    }

    private void retreatDish(String employeeName) {
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        boolean canUseCoupon = DBHelper.getInstance(getContext().getApplicationContext()).canUseCouponRetreat(orderId,retreatDishes);
        if(canUseCoupon) {
            //退菜后优惠券仍然可以继续使用
            String reason = "无";
            if (mRadioGroup1.getCheckedRadioButtonId() >= 0) {
                RadioButton rb = (RadioButton) mRadioGroup1.findViewById(mRadioGroup1.getCheckedRadioButtonId());
                if (rb != null) {
                    reason = rb.getText().toString();
                }
            } else {
                reason = tvMore.getText() == null || tvMore.getText().toString().isEmpty() ? "无" : tvMore.getText().toString();
            }
            printKitchen(employeeName, reason);
            DBHelper.getInstance(getActivity().getApplicationContext()).retreatOrderDish(orderId, retreatDishes);
            DBHelper.getInstance(getContext()).dealWithVoucher(orderEntity, 1);
            mOnRetreatOrderClickListener.onRetreatOrderConfirm(orderId);
        }else{
            //退菜后订单金额将不满足优惠券使用条件
            CustomMethod.showMessage(getContext(),"退菜后订单金额将不满足优惠券使用条件，请先取消优惠券的使用！");
        }
    }

    //落单厨打
    private void printKitchen(String employeeName, String reason) {
        if (orderId != null) {
            if (retreatDishes.size() > 0) {
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getContext().getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        mOnRetreatOrderClickListener.onRetreatPrint(printDishBeenes1, orderId, printKitchen, employeeName, reason);
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity != null && printCashierEntity.getIsPrintDish() == 1) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        mOnRetreatOrderClickListener.onRetreatPrint(printDishBeenes1, orderId, null, employeeName, reason);
                    }
                }
            }
        } else {
            showToast(getActivity().getApplicationContext(), "退菜打印失败");
        }
    }

    private void setAdapter() {
        adapter = new RetreatOrderAdapter(getActivity().getApplicationContext(), retreatDishes);
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
