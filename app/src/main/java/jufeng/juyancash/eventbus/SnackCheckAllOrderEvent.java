package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/8.
 */

public class SnackCheckAllOrderEvent {
    private String orderId;

    public SnackCheckAllOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
