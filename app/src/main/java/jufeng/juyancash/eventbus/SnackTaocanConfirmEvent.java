package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackTaocanConfirmEvent {
    private String orderId;

    public SnackTaocanConfirmEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
