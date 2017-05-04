package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/16.
 */

public class SearchDishClickEvent {
    private String orderId;

    public SearchDishClickEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
