package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackDiscountSomeConfirmEvent {
    private String orderId;

    public SnackDiscountSomeConfirmEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
