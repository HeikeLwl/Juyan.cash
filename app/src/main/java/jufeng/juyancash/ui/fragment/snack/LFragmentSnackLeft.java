package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.bean.SnackOrderItemClickEvent;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackCashierOverEvent;
import jufeng.juyancash.eventbus.SnackCashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.SnackCashierTopRightRefreshEvent;
import jufeng.juyancash.eventbus.SnackEditDishConfirmEvent;
import jufeng.juyancash.eventbus.SnackEditDishEvent;
import jufeng.juyancash.eventbus.SnackEditDishOrderBackEvent;
import jufeng.juyancash.eventbus.SnackEditDishOrderConfirmEvent;
import jufeng.juyancash.eventbus.SnackEditOrderDishBackEvent;
import jufeng.juyancash.eventbus.SnackNewOrderEvent;
import jufeng.juyancash.eventbus.SnackOpenOrderedTaocanEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackOrderListRefreshEvent;
import jufeng.juyancash.eventbus.SnackTaocanCancelEvent;
import jufeng.juyancash.eventbus.SnackTaocanConfirmEvent;
import jufeng.juyancash.eventbus.SnackTaocanDeleteEvent;
import jufeng.juyancash.eventbus.SnackTaocanDetailEvent;
import jufeng.juyancash.ui.fragment.BaseFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class LFragmentSnackLeft extends BaseFragment {

    public static LFragmentSnackLeft newInstance(){
        return new LFragmentSnackLeft();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack_left, container, false);
        return view;
    }

    //打开菜品详情页
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void openOrderDetial(String orderId){
        start(LFragmentSnackOrderDetail.newInstance(orderId),SupportFragment.SINGLETASK);
        EventBus.getDefault().post(new SnackOrderDetailChangeEvent(orderId, 1));
    }

    //打开套餐订单页
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void openOrderedTaocan(OrderDishEntity orderDishEntity, int type){
        start(LFragmentSnackOrderedTaocan.newInstance(orderDishEntity, type),SupportFragment.SINGLETASK);
    }

    //打开菜品编辑页
    private void openEditOrderDish(String orderId,int type){
        start(LFragmentSnackEditOrderDish.newInstance(type, orderId),SupportFragment.SINGLETASK);
    }

    //打开菜品编辑订单页
    private void openEditDishOrder(String orderId, ArrayList<OrderDishEntity> orderDishEntities, int type){
        start(LFragmentSnackEditDishOrder.newInstance(orderId, orderDishEntities, type),SupportFragment.SINGLETASK);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNewOrder(SnackNewOrderEvent event){
        if(event != null && event.getOrderId() != null){
            openOrderDetial(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanDetail(SnackTaocanDetailEvent event) {
        if (event != null && event.getOrderDishEntity() != null) {
            openOrderedTaocan(event.getOrderDishEntity(),1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenOrderdTaocan(SnackOpenOrderedTaocanEvent event){
        if(event != null && event.getOrderDishEntity() != null){
            openOrderedTaocan(event.getOrderDishEntity(),event.getType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSnackOrderItemClick(SnackOrderItemClickEvent event) {
        if (event != null && event.getOrderId() != null) {
            openOrderDetial(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelOrderSuccess(SnackCancelOrderSuccessEvent event) {
        if (event != null) {
            openOrderDetial(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashierOver(SnackCashierOverEvent event) {
        if (event != null) {
            openOrderDetial(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEditDish(SnackEditDishEvent event) {
        if (event != null) {
            openEditOrderDish(event.getOrderId(),event.getType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEditBack(SnackEditOrderDishBackEvent event) {
        if (event != null) {
            openOrderDetial(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEditConfirm(SnackEditDishConfirmEvent event) {
        if (event != null) {
            openEditDishOrder(event.getOrderId(),event.getOrderDishEntities(),event.getType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEditOrderBack(SnackEditDishOrderBackEvent event) {
        if (event != null) {
            openEditOrderDish(event.getOrderId(),event.getType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEditOrderConfirm(SnackEditDishOrderConfirmEvent event) {
        if (event != null) {
            openOrderDetial(event.getOrderId());
            if (event.getType() != 2) {
                EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                EventBus.getDefault().post(new SnackOpenTopRightEvent());
                EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
                EventBus.getDefault().post(new SnackOrderListRefreshEvent(event.getOrderId()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanCancel(SnackTaocanCancelEvent event) {
        if (event != null) {
            openOrderDetial(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanDelete(SnackTaocanDeleteEvent event) {
        if (event != null) {
            openOrderDetial(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanConfirm(SnackTaocanConfirmEvent event) {
        if (event != null) {
            openOrderDetial(event.getOrderId());
        }
    }
}
