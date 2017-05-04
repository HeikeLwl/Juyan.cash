package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackCashierCancelEvent {
    private String orderId;

    public SnackCashierCancelEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public SnackCashierCancelEvent() {
    }
}
