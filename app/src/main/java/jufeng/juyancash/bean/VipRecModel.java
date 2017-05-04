package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/11/4.
 */

public class VipRecModel {
    private String vipNo;//会员卡编号
    private String partnerCode;//商家编号
    private Integer amount;//总金额
    private Integer payAmount;//充值金额
    private String out_trade_no;//微信支付操作ID
    private int voucher;//优惠金额
    private String payTime;//充值时间
    private String rechCouponId;//充值券ID
    private int rechargeType;//充值类型0-微信充值,1-支付宝充值,2-现金充值,3-银行卡充值


    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Integer payAmount) {
        this.payAmount = payAmount;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRechCouponId() {
        return rechCouponId;
    }

    public void setRechCouponId(String rechCouponId) {
        this.rechCouponId = rechCouponId;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }
}
