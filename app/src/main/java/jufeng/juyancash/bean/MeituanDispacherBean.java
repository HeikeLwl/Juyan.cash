package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/4/7.
 */

public class MeituanDispacherBean {
    private long createTime;
    private String dispatcherMobile;
    private String dispatcherName;
    private String orderId;
    private int shippingStatus;//0-配送单发往配送;10-配送单已确认;20-骑手已取餐;40-骑手已送达;100-配送单已取消

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDispatcherMobile() {
        return dispatcherMobile;
    }

    public void setDispatcherMobile(String dispatcherMobile) {
        this.dispatcherMobile = dispatcherMobile;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
