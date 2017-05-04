package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanChangeDishEvent {
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackTaocanChangeDishEvent(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }

    public OrderTaocanGroupDishEntity getOrderTaocanGroupDishEntity() {
        return mOrderTaocanGroupDishEntity;
    }

    public void setOrderTaocanGroupDishEntity(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }
}
