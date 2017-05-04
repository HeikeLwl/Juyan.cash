package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class SnackPrintKHLEvent {
    private String orderId;

    public SnackPrintKHLEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
