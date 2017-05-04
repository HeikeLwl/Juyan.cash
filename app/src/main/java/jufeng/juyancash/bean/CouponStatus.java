package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/11.
 */

public class CouponStatus {
    private String couponNo;
    private int op;
    private String orderId;

    public CouponStatus(String couponNo, int op,String orderId) {
        this.couponNo = couponNo;
        this.op = op;
        this.orderId = orderId;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
