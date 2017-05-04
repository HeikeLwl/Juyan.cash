package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class CouponDetailConfirmEvent {
    private int status;

    public CouponDetailConfirmEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
