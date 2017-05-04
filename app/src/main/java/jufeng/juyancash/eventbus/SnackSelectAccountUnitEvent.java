package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.BillAccountHistoryEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectAccountUnitEvent {
    private BillAccountHistoryEntity mBillAccountHistoryEntity;

    public SnackSelectAccountUnitEvent(BillAccountHistoryEntity BillAccountHistoryEntity) {
        mBillAccountHistoryEntity = BillAccountHistoryEntity;
    }

    public BillAccountHistoryEntity getBillAccountHistoryEntity() {
        return mBillAccountHistoryEntity;
    }

    public void setBillAccountHistoryEntity(BillAccountHistoryEntity BillAccountHistoryEntity) {
        mBillAccountHistoryEntity = BillAccountHistoryEntity;
    }
}
