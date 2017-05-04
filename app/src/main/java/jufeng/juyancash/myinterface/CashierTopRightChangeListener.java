package jufeng.juyancash.myinterface;

/**
 * Created by Administrator102 on 2016/12/21.
 */

public interface CashierTopRightChangeListener {
    void onTopRightCancle();
    void onTopRightConfirm(String orderId,boolean isOpenJoinOrder);
    void onChangeVip();
    void onChangeCouponStatus(String orderId,boolean isOpenJoinOrder);
    void onUseVoucher(String orderId,boolean isOpenJoinOrder);
}
