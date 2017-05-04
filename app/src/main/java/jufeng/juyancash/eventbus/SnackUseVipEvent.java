package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackUseVipEvent {
    private  String orderId;

    public SnackUseVipEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
