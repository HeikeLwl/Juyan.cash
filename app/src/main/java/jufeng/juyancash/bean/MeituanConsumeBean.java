package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/2/14.
 */

public class MeituanConsumeBean {
    private String[] couponCodes;
    private String orderId;
    private String dealTitle;
    private int dealid;
    private String message;
    private String poiid;
    private int result;

    public String[] getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(String[] couponCodes) {
        this.couponCodes = couponCodes;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public int getDealid() {
        return dealid;
    }

    public void setDealid(int dealid) {
        this.dealid = dealid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPoiid() {
        return poiid;
    }

    public void setPoiid(String poiid) {
        this.poiid = poiid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
