package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanDishConfigCancelEvent {
    private OrderDishEntity mOrderDishEntity;

    public SnackTaocanDishConfigCancelEvent(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }

    public OrderDishEntity getOrderDishEntity() {
        return mOrderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }
}
