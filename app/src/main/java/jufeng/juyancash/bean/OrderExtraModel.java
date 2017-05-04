package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/31.
 */

public class OrderExtraModel {
    //商家编号
    private String partnerCode;
    // 打包费
    private int boxFee;
    // 配送费
    private int dispatchFee;
    // 配送距离(米)
    private int dispatchDistance;
    //创建时间
    private String createTime;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public int getBoxFee() {
        return boxFee;
    }

    public void setBoxFee(int boxFee) {
        this.boxFee = boxFee;
    }

    public int getDispatchFee() {
        return dispatchFee;
    }

    public void setDispatchFee(int dispatchFee) {
        this.dispatchFee = dispatchFee;
    }

    public int getDispatchDistance() {
        return dispatchDistance;
    }

    public void setDispatchDistance(int dispatchDistance) {
        this.dispatchDistance = dispatchDistance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
