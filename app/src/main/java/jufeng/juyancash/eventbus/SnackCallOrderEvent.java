package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/8.
 */

public class SnackCallOrderEvent {
    private String orderId;

    public SnackCallOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
