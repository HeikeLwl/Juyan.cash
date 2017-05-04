package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanItemDeleteEvent {
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackTaocanItemDeleteEvent(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }

    public OrderTaocanGroupDishEntity getOrderTaocanGroupDishEntity() {
        return mOrderTaocanGroupDishEntity;
    }

    public void setOrderTaocanGroupDishEntity(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }
}
