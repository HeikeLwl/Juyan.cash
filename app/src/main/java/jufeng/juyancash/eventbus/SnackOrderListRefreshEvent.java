package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/25.
 */

public class SnackOrderListRefreshEvent {
    private String orderId;

    public SnackOrderListRefreshEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
