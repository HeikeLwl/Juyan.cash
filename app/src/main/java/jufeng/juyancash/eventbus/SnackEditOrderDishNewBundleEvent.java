package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/5/2.
 */

public class SnackEditOrderDishNewBundleEvent {
    private String orderId;
    private int type;

    public SnackEditOrderDishNewBundleEvent(String orderId, int type) {
        this.orderId = orderId;
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
