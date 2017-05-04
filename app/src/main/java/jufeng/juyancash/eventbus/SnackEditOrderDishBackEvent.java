package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/28.
 */

public class SnackEditOrderDishBackEvent {
    private String orderId;

    public SnackEditOrderDishBackEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
