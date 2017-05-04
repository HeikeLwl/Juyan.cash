package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class SnackOrderTaocanNewBundleEvent {
    private OrderDishEntity orderDishEntity;
    private int type;

    public SnackOrderTaocanNewBundleEvent(OrderDishEntity orderDishEntity, int type) {
        this.orderDishEntity = orderDishEntity;
        this.type = type;
    }

    public OrderDishEntity getOrderDishEntity() {
        return orderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        this.orderDishEntity = orderDishEntity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
