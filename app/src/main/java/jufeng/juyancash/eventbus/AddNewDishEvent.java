package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class AddNewDishEvent {
    private OrderDishEntity mOrderDishEntity;

    public AddNewDishEvent(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }

    public OrderDishEntity getOrderDishEntity() {
        return mOrderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }
}
