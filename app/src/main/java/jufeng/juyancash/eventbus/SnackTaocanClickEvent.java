package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.TaocanEntity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanClickEvent {
    private TaocanEntity mTaocanEntity;
    private String taocanTypeId;

    public SnackTaocanClickEvent(TaocanEntity taocanEntity, String taocanTypeId) {
        mTaocanEntity = taocanEntity;
        this.taocanTypeId = taocanTypeId;
    }

    public TaocanEntity getTaocanEntity() {
        return mTaocanEntity;
    }

    public void setTaocanEntity(TaocanEntity taocanEntity) {
        mTaocanEntity = taocanEntity;
    }

    public String getTaocanTypeId() {
        return taocanTypeId;
    }

    public void setTaocanTypeId(String taocanTypeId) {
        this.taocanTypeId = taocanTypeId;
    }
}
