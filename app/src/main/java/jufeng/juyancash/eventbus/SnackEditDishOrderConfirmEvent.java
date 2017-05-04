package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/3/29.
 */

public class SnackEditDishOrderConfirmEvent {
    private int type;
    private String orderId;
    private ArrayList<OrderDishEntity> mOrderDishEntities;

    public SnackEditDishOrderConfirmEvent(int type, String orderId, ArrayList<OrderDishEntity> orderDishEntities) {
        this.type = type;
        this.orderId = orderId;
        mOrderDishEntities = orderDishEntities;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<OrderDishEntity> getOrderDishEntities() {
        return mOrderDishEntities;
    }

    public void setOrderDishEntities(ArrayList<OrderDishEntity> orderDishEntities) {
        mOrderDishEntities = orderDishEntities;
    }
}
