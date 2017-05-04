package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/18.
 */

public class ChangeTakeoutState {
    private String orderId;
    private String state;

    public ChangeTakeoutState(String orderId, String state) {
        this.orderId = orderId;
        this.state = state;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
