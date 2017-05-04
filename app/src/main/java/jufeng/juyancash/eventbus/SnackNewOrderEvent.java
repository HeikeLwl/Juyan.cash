package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class SnackNewOrderEvent {
    private String orderId;

    public SnackNewOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
