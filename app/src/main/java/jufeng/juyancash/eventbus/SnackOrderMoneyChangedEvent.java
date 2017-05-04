package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/18.
 */

public class SnackOrderMoneyChangedEvent {
    private String orderId;

    public SnackOrderMoneyChangedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
