package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/8.
 */

public class SnackAddOrderEvent {
    private String orderId;

    public SnackAddOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
