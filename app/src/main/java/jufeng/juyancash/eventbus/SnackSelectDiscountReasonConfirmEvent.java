package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectDiscountReasonConfirmEvent {
    private int type;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;

    public SnackSelectDiscountReasonConfirmEvent(int type,DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        this.type = type;
        mDiscountHistoryEntity = discountHistoryEntity;
        mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
