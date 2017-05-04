package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/2/14.
 */

public class MeituanPrepareBean {
    private int count;
    private double couponBuyPrice;
    private String couponCode;
    private String couponEndTime;
    private String dealBeginTime;
    private int dealId;
    private double dealPrice;
    private String dealTitle;
    private double dealValue;
    private String message;
    private int minConsume;
    private int result;
    private String dealMenu;

    public String getDealMenu() {
        return dealMenu;
    }

    public void setDealMenu(String dealMenu) {
        this.dealMenu = dealMenu;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCouponBuyPrice() {
        return couponBuyPrice;
    }

    public void setCouponBuyPrice(double couponBuyPrice) {
        this.couponBuyPrice = couponBuyPrice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getDealBeginTime() {
        return dealBeginTime;
    }

    public void setDealBeginTime(String dealBeginTime) {
        this.dealBeginTime = dealBeginTime;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public double getDealValue() {
        return dealValue;
    }

    public void setDealValue(double dealValue) {
        this.dealValue = dealValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMinConsume() {
        return minConsume;
    }

    public void setMinConsume(int minConsume) {
        this.minConsume = minConsume;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
