package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackOrderDetailChangeEvent {
    private int type;
    private String orderId;

    public SnackOrderDetailChangeEvent(String orderId,int type) {
        this.orderId = orderId;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
