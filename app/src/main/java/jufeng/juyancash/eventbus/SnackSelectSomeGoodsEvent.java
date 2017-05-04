package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectSomeGoodsEvent {
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;

    public SnackSelectSomeGoodsEvent(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        mDiscountHistoryEntity = discountHistoryEntity;
        mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
    }

    public DiscountHistoryEntity getDiscountHistoryEntity() {
        return mDiscountHistoryEntity;
    }

    public void setDiscountHistoryEntity(DiscountHistoryEntity discountHistoryEntity) {
        mDiscountHistoryEntity = discountHistoryEntity;
    }

    public ArrayList<SomeDiscountGoodsEntity> getSomeDiscountGoodsEntities() {
        return mSomeDiscountGoodsEntities;
    }

    public void setSomeDiscountGoodsEntities(ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
    }
}
