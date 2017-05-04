package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/28.
 */

public class SnackEditDishEvent {
    private String orderId;
    private int type;

    public SnackEditDishEvent(String orderId, int type) {
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
