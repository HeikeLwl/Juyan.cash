package jufeng.juyancash.eventbus;

import jufeng.juyancash.bean.OffLineWxCouponModel;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class CouponAddConfirmEvent {
    private OffLineWxCouponModel mOffLineWxCouponModel;

    public CouponAddConfirmEvent(OffLineWxCouponModel offLineWxCouponModel) {
        this.mOffLineWxCouponModel = offLineWxCouponModel;
    }

    public OffLineWxCouponModel getOffLineWxCouponModel() {
        return mOffLineWxCouponModel;
    }

    public void setOffLineWxCouponModel(OffLineWxCouponModel offLineWxCouponModel) {
        mOffLineWxCouponModel = offLineWxCouponModel;
    }
}
