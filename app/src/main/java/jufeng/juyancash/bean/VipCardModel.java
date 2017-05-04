package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/12/21.
 */

public class VipCardModel {
    private String id;
    /** 会员编号 */
    private String vipNo;
    /** 余额 */
    private int balance;
    /** 会员卡类型 0-银卡,1-金卡,2-砖石卡 */
    private int cardType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
