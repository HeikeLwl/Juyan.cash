package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/24.
 */

public class SnackPrintKitchenEvent {
    private String orderId;

    public SnackPrintKitchenEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
