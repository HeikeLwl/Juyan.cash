package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.BillAccountHistoryEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSelectBillPersonConfirmEvent {
    private String orderId;
    private BillAccountHistoryEntity mBillAccountHistoryEntity;

    public SnackSelectBillPersonConfirmEvent(String orderId, BillAccountHistoryEntity billAccountHistoryEntity) {
        this.orderId = orderId;
        mBillAccountHistoryEntity = billAccountHistoryEntity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BillAccountHistoryEntity getBillAccountHistoryEntity() {
        return mBillAccountHistoryEntity;
    }

    public void setBillAccountHistoryEntity(BillAccountHistoryEntity billAccountHistoryEntity) {
        mBillAccountHistoryEntity = billAccountHistoryEntity;
    }
}
