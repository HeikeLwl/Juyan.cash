package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/2/17.
 */

public class OrderCollectionBean {
    private String cashierId;//收银员ID
    private String cashierName;//收银员名称
    private String spendMoney;//收银总金额

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getSpendMoney() {
        return spendMoney;
    }

    public void setSpendMoney(String spendMoney) {
        this.spendMoney = spendMoney;
    }
}
