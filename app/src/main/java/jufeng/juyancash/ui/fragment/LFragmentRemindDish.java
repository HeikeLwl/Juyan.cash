package jufeng.juyancash.ui.fragment;

import android.app.Activity;
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

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RetreatDishAdapter;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

import static jufeng.juyancash.util.CustomMethod.showToast;

/**
 * Created by Administrator102 on 2016/8/1.
 */
public class LFragmentRemindDish extends BaseFragment implements View.OnClickListener {
    private TextView tvOrderNumber;
    private TextView tvSeatNumber;
    private TextView tvSeatCount;
    private TextView tvTime;
    private Button btnBack, btnRemindAll, btnRemind;
    private RecyclerView mRecyclerView;
    private RetreatDishAdapter adapter;
    private String orderId;
    private TextView tvOrderName;
    private LinearLayout layoutOrderDetial;
    private boolean mIsOpenJoinOrder;
    private MainFragmentListener mOnRemindDishClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnRemindDishClickListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_remind_dish_layout, container, false);
        initView(mView);
        initData();
        setListener();
        setAdapter();
        return mView;
    }

    private void initView(View view) {
        layoutOrderDetial = (LinearLayout) view.findViewById(R.id.layout_order_detail);
        tvOrderName = (TextView) view.findViewById(R.id.tv_order_name);
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
        tvSeatCount = (TextView) view.findViewById(R.id.tv_seat_count);
        tvTime = (TextView) view.findViewById(R.id.tv_create_time);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        btnRemindAll = (Button) view.findViewById(R.id.btn_remind_all);
        btnRemind = (Button) view.findViewById(R.id.btn_remind);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
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
            tvSeatCount.setText("人数："+orderEntity.getOrderGuests()+"人");
            tvTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(),"yyyy-MM-dd HH:mm"));
        }
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
        btnRemindAll.setOnClickListener(this);
        btnRemind.setOnClickListener(this);
    }

    private void setAdapter() {
        adapter = new RetreatDishAdapter(getActivity(), orderId, mIsOpenJoinOrder);
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
                mOnRemindDishClickListener.onRemindBackClick(orderId);
                break;
            case R.id.btn_remind_all:
                adapter.setReteatAll();
                printKitchen(adapter.getRetreatDishes());
                mOnRemindDishClickListener.onRemindAllClick(orderId,adapter.getRetreatDishes());
                break;
            case R.id.btn_remind:
                printKitchen(adapter.getRetreatDishes());
                mOnRemindDishClickListener.onRemindClick(orderId,adapter.getRetreatDishes());
                break;
        }
    }

    //落单厨打
    private void printKitchen(ArrayList<OrderDishEntity> retreatDishes) {
        if (orderId != null) {
            if(retreatDishes.size() > 0){
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getContext().getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if(dishEntity != null){
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity,unitName,0,0,new int[]{100,100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if(orderDishEntity.getType() == 1){
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if(dishEntity1 != null){
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish,printDishBean,unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if(printDishBeenes1.size() > 0) {
                        mOnRemindDishClickListener.onRemindPrint(printDishBeenes1,orderId,printKitchen,null);
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if(printCashierEntity.getIsPrintDish() == 1){
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if(dishEntity != null){
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity,unitName,0,0,new int[]{100,100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if(orderDishEntity.getType() == 1){
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if(dishEntity1 != null){
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish,printDishBean,unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if(printDishBeenes1.size() > 0) {
                        mOnRemindDishClickListener.onRemindPrint(printDishBeenes1,orderId,null,null);
                    }
                }
            }
        } else {
            showToast(getActivity().getApplicationContext(), "催菜打印失败");
        }
    }
}
