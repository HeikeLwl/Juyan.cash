package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanItemClickEvent {
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackTaocanItemClickEvent(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }

    public OrderTaocanGroupDishEntity getOrderTaocanGroupDishEntity() {
        return mOrderTaocanGroupDishEntity;
    }

    public void setOrderTaocanGroupDishEntity(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }
}
