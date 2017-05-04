package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.eventbus.SearchDishClickEvent;
import jufeng.juyancash.eventbus.SnackAddDishEvent;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackCashierCancelEvent;
import jufeng.juyancash.eventbus.SnackCashierEvent;
import jufeng.juyancash.eventbus.SnackCashierOverEvent;
import jufeng.juyancash.eventbus.SnackCheckAllOrderEvent;
import jufeng.juyancash.eventbus.SnackNewOrderEvent;
import jufeng.juyancash.eventbus.SnackOpenOrderedTaocanEvent;
import jufeng.juyancash.eventbus.SnackTaocanCancelEvent;
import jufeng.juyancash.eventbus.SnackTaocanConfirmEvent;
import jufeng.juyancash.eventbus.SnackTaocanDeleteEvent;
import jufeng.juyancash.eventbus.SnackTaocanDetailEvent;
import jufeng.juyancash.ui.fragment.BaseFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class LFragmentSnackRight extends BaseFragment {
    public static LFragmentSnackRight newInstance() {
        return new LFragmentSnackRight();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack_right, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadRootFragment(R.id.container,LFragmentSnackOrderList.newInstance(null));
    }

    //打开订单列表页
    private void openOrderList(final String orderId) {
        start(LFragmentSnackOrderList.newInstance(orderId),SupportFragment.SINGLETASK);
    }

    //打开菜单页
    private void openDishMenu(final String orderId) {
        start(LFragmentSnackDishMenu.newInstance(orderId),SupportFragment.SINGLETASK);
    }

    //打开搜索菜品页
    private void openSearchDish(final String orderId) {
        start(LFragmentSnackSearchDish.newInstance(orderId),SupportFragment.SINGLETASK);
    }

    //打开套餐详情页
    private void openTaocanDetail(final OrderDishEntity orderDishEntity) {
        start(LFragmentSnackTaocanDetail.newInstance(orderDishEntity),SupportFragment.SINGLETASK);
    }

    //打开收银界面
    private void openSnackCashier(String orderId){
        start(LFragmentSnackCashier.newInstance(orderId),SupportFragment.SINGLETASK);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCheckAllOrder(SnackCheckAllOrderEvent event) {
        if (event == null || event.getOrderId() == null) {
            return;
        }
        openOrderList(event.getOrderId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddDish(SnackAddDishEvent event) {
        if (event == null && event.getOrderId() != null) {
            return;
        }
        openDishMenu(event.getOrderId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNewOrder(SnackNewOrderEvent event) {
        if (event != null && event.getOrderId() != null) {
            openDishMenu(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchDish(SearchDishClickEvent event) {
        if (event != null && event.getOrderId() != null) {
            openSearchDish(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanDetail(SnackTaocanDetailEvent event) {
        if (event != null && event.getOrderDishEntity() != null) {
            openTaocanDetail(event.getOrderDishEntity());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenOrderdTaocan(SnackOpenOrderedTaocanEvent event) {
        if (event != null && event.getOrderDishEntity() != null) {
            openTaocanDetail(event.getOrderDishEntity());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelOrderSuccess(SnackCancelOrderSuccessEvent event) {
        if (event != null) {
            openOrderList(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashier(SnackCashierEvent event) {
        if (event != null && event.getOrderId() != null) {
            openSnackCashier(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashierOver(SnackCashierOverEvent event) {
        if (event != null) {
            openOrderList(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashierCancel(SnackCashierCancelEvent event) {
        if (event != null && event.getOrderId() != null) {
            openOrderList(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanCancel(SnackTaocanCancelEvent event) {
        if (event != null) {
            openDishMenu(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanDelete(SnackTaocanDeleteEvent event) {
        if (event != null) {
            openDishMenu(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanConfirm(SnackTaocanConfirmEvent event) {
        if (event != null) {
            openDishMenu(event.getOrderId());
        }
    }
}
