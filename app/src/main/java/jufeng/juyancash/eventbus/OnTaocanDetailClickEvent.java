package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2017/3/17.
 */

public class OnTaocanDetailClickEvent {
    private OrderDishEntity mOrderDishEntity;
    private String taocanId;
    private String orderId;
    private String tag;
    private String tableId;

    public OnTaocanDetailClickEvent(OrderDishEntity orderDishEntity, String taocanId, String orderId,String tag,String tableId) {
        mOrderDishEntity = orderDishEntity;
        this.taocanId = taocanId;
        this.orderId = orderId;
        this.tag = tag;
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public OrderDishEntity getOrderDishEntity() {
        return mOrderDishEntity;
    }

    public void setOrderDishEntity(OrderDishEntity orderDishEntity) {
        mOrderDishEntity = orderDishEntity;
    }

    public String getTaocanId() {
        return taocanId;
    }

    public void setTaocanId(String taocanId) {
        this.taocanId = taocanId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
