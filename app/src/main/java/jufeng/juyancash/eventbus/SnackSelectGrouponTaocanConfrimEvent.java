package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.PayModeEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectGrouponTaocanConfrimEvent {
    private String orderId;
    private GrouponEntity grouponEntity;
    private GrouponTaocanEntity grouponTaocanEntity;
    private PayModeEntity payModeEntity;

    public SnackSelectGrouponTaocanConfrimEvent(String orderId, GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity) {
        this.orderId = orderId;
        this.grouponEntity = grouponEntity;
        this.grouponTaocanEntity = grouponTaocanEntity;
        this.payModeEntity = payModeEntity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public GrouponEntity getGrouponEntity() {
        return grouponEntity;
    }

    public void setGrouponEntity(GrouponEntity grouponEntity) {
        this.grouponEntity = grouponEntity;
    }

    public GrouponTaocanEntity getGrouponTaocanEntity() {
        return grouponTaocanEntity;
    }

    public void setGrouponTaocanEntity(GrouponTaocanEntity grouponTaocanEntity) {
        this.grouponTaocanEntity = grouponTaocanEntity;
    }

    public PayModeEntity getPayModeEntity() {
        return payModeEntity;
    }

    public void setPayModeEntity(PayModeEntity payModeEntity) {
        this.payModeEntity = payModeEntity;
    }
}
