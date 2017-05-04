package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class SnackOpenOrderedTaocanEvent {
    private OrderDishEntity mOrderDishEntity;
    private int type;

    public SnackOpenOrderedTaocanEvent(OrderDishEntity orderDishEntity, int type) {
        mOrderDishEntity = orderDishEntity;
        this.type = type;
    }

    public OrderDishEntity getOrderDishEntity() {
        return mOrderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
