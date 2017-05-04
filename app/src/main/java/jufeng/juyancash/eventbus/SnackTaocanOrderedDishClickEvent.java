package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanOrderedDishClickEvent {
    private OrderDishEntity mOrderDishEntity;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackTaocanOrderedDishClickEvent(OrderDishEntity orderDishEntity,OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
        this.mOrderDishEntity = orderDishEntity;
    }

    public OrderDishEntity getOrderDishEntity() {
        return mOrderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }

    public OrderTaocanGroupDishEntity getOrderTaocanGroupDishEntity() {
        return mOrderTaocanGroupDishEntity;
    }

    public void setOrderTaocanGroupDishEntity(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }
}
