package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/24.
 */

public class SnackOrderItemClickEvent {
    private String orderId;

    public SnackOrderItemClickEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
