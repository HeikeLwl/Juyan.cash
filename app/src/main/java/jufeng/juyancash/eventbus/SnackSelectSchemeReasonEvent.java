package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.DiscountHistoryEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectSchemeReasonEvent {
    private DiscountHistoryEntity mDiscountHistoryEntity;

    public SnackSelectSchemeReasonEvent(DiscountHistoryEntity discountHistoryEntity) {
        mDiscountHistoryEntity = discountHistoryEntity;
    }

    public DiscountHistoryEntity getDiscountHistoryEntity() {
        return mDiscountHistoryEntity;
    }

    public void setDiscountHistoryEntity(DiscountHistoryEntity discountHistoryEntity) {
        mDiscountHistoryEntity = discountHistoryEntity;
    }
}
