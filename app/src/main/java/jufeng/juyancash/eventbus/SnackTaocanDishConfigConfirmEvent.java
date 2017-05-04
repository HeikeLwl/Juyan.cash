package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanDishConfigConfirmEvent {
    private OrderDishEntity mOrderDishEntity;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    public SnackTaocanDishConfigConfirmEvent(OrderDishEntity orderDishEntity, OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        mOrderDishEntity = orderDishEntity;
        mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
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
