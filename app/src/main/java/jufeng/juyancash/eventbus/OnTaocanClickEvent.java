package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/17.
 */

public class OnTaocanClickEvent {
    private String taocanId;
    private String orderId;

    public OnTaocanClickEvent(String taocanId, String orderId) {
        this.taocanId = taocanId;
        this.orderId = orderId;
    }

    public String getTaocanId() {
        return taocanId;
    }

    public void setTaocanId(String taocanId) {
        this.taocanId = taocanId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
