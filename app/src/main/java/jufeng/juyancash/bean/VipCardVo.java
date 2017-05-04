package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/10/28.
 */

public class VipCardVo {

    /** 会员编号 */
    private String vipNo;
    private String oldVipNo;//老会员卡号
    private int initAmount;//会员卡初始金额
    private String partnerCode;
    private int cardType;
    private int initIntegral;

    public int getInitIntegral() {
        return initIntegral;
    }

    public void setInitIntegral(int initIntegral) {
        this.initIntegral = initIntegral;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getOldVipNo() {
        return oldVipNo;
    }

    public void setOldVipNo(String oldVipNo) {
        this.oldVipNo = oldVipNo;
    }

    public int getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(int initAmount) {
        this.initAmount = initAmount;
    }


}
