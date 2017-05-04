package jufeng.juyancash.bean;

import jufeng.juyancash.dao.TaocanEntity;

/**
 * Created by Administrator102 on 2016/11/10.
 */

public class TaocanBean{
    private double taocanCount;
    private boolean isChing;
    private TaocanEntity mTaocanEntity;

    public TaocanEntity getTaocanEntity() {
        return mTaocanEntity;
    }

    public void setTaocanEntity(TaocanEntity taocanEntity) {
        mTaocanEntity = taocanEntity;
    }

    public double getTaocanCount() {
        return taocanCount;
    }

    public void setTaocanCount(double taocanCount) {
        this.taocanCount = taocanCount;
    }

    public boolean isChing() {
        return isChing;
    }

    public void setChing(boolean ching) {
        isChing = ching;
    }
}
