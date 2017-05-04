package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.BillAccountHistoryEntity;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackAccountConfirmEvent {
    private String orderId;
    private BillAccountHistoryEntity mBillAccountHistoryEntity;

    public SnackAccountConfirmEvent(String orderId,BillAccountHistoryEntity BillAccountHistoryEntity) {
        mBillAccountHistoryEntity = BillAccountHistoryEntity;
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

    public void setBillAccountHistoryEntity(BillAccountHistoryEntity BillAccountHistoryEntity) {
        mBillAccountHistoryEntity = BillAccountHistoryEntity;
    }
}
