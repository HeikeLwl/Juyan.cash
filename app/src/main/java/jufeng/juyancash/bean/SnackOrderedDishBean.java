package jufeng.juyancash.bean;

import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackOrderedDishBean {
    private int dishType;
    private OrderDishEntity mOrderDishEntity;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackOrderedDishBean(int dishType, OrderDishEntity orderDishEntity, OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        this.dishType = dishType;
        mOrderDishEntity = orderDishEntity;
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
    }

    public int getDishType() {
        return dishType;
    }

    public void setDishType(int dishType) {
        this.dishType = dishType;
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
