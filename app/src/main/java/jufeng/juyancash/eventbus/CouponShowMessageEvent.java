package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class CouponShowMessageEvent {
    private String message;

    public CouponShowMessageEvent() {
        this.message = null;
    }

    public CouponShowMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
