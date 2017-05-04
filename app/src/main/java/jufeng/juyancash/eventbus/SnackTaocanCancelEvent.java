package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanCancelEvent {
    private String orderId;

    public SnackTaocanCancelEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
