package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class SnackOrderDetailNewBundleEvent {
    private String orderId;

    public SnackOrderDetailNewBundleEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
