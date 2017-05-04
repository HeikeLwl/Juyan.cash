package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackCashConfirmEvent {
    private String payMoney;

    public SnackCashConfirmEvent(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}
