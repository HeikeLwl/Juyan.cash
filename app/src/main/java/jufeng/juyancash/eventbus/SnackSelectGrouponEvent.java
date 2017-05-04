package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.PayModeEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectGrouponEvent {
    private GrouponEntity mGrouponEntity;
    private GrouponTaocanEntity mGrouponTaocanEntity;
    private PayModeEntity mPayModeEntity;

    public SnackSelectGrouponEvent(GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity) {
        mGrouponEntity = grouponEntity;
        mGrouponTaocanEntity = grouponTaocanEntity;
        mPayModeEntity = payModeEntity;
    }

    public GrouponEntity getGrouponEntity() {
        return mGrouponEntity;
    }

    public void setGrouponEntity(GrouponEntity grouponEntity) {
        mGrouponEntity = grouponEntity;
    }

    public GrouponTaocanEntity getGrouponTaocanEntity() {
        return mGrouponTaocanEntity;
    }

    public void setGrouponTaocanEntity(GrouponTaocanEntity grouponTaocanEntity) {
        mGrouponTaocanEntity = grouponTaocanEntity;
    }

    public PayModeEntity getPayModeEntity() {
        return mPayModeEntity;
    }

    public void setPayModeEntity(PayModeEntity payModeEntity) {
        mPayModeEntity = payModeEntity;
    }
}
