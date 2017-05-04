package jufeng.juyancash.ui.fragment.snack;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.BillAccountEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.eventbus.CashierRightRefreshEvent;
import jufeng.juyancash.eventbus.PrintCaiwulianEvent;
import jufeng.juyancash.eventbus.SnackAccountConfirmEvent;
import jufeng.juyancash.eventbus.SnackBankConfirmEvent;
import jufeng.juyancash.eventbus.SnackCashConfirmEvent;
import jufeng.juyancash.eventbus.SnackCashierCancelEvent;
import jufeng.juyancash.eventbus.SnackCashierOverEvent;
import jufeng.juyancash.eventbus.SnackCashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.SnackCashierTopRightRefreshEvent;
import jufeng.juyancash.eventbus.SnackContinuCashierEvent;
import jufeng.juyancash.eventbus.SnackDiscountAllConfirmEvent;
import jufeng.juyancash.eventbus.SnackDiscountSchemeConfirmEvent;
import jufeng.juyancash.eventbus.SnackDiscountSomeConfirmEvent;
import jufeng.juyancash.eventbus.SnackElectricConfirmEvent;
import jufeng.juyancash.eventbus.SnackGrouponConfirmEvent;
import jufeng.juyancash.eventbus.SnackMlClickEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailRefreshEvent;
import jufeng.juyancash.eventbus.SnackOrderMoneyChangedEvent;
import jufeng.juyancash.eventbus.SnackSelectAccountPeopleEvent;
import jufeng.juyancash.eventbus.SnackSelectAccountSignPeopleEvent;
import jufeng.juyancash.eventbus.SnackSelectAccountUnitEvent;
import jufeng.juyancash.eventbus.SnackSelectBillPersonConfirmEvent;
import jufeng.juyancash.eventbus.SnackSelectDiscountReasonConfirmEvent;
import jufeng.juyancash.eventbus.SnackSelectDiscountReasonEvent;
import jufeng.juyancash.eventbus.SnackSelectGrouponEvent;
import jufeng.juyancash.eventbus.SnackSelectGrouponTaocanConfrimEvent;
import jufeng.juyancash.eventbus.SnackSelectSchemeConfirmEvent;
import jufeng.juyancash.eventbus.SnackSelectSchemeEvent;
import jufeng.juyancash.eventbus.SnackSelectSchemeReasonEvent;
import jufeng.juyancash.eventbus.SnackSelectSomeGoodsConfirmEvent;
import jufeng.juyancash.eventbus.SnackSelectSomeGoodsEvent;
import jufeng.juyancash.eventbus.SnackSelectSomeReasonEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.eventbus.SnackUnreadMessageRefreshEvent;
import jufeng.juyancash.eventbus.SnackUseVipEvent;
import jufeng.juyancash.eventbus.SnackVipChangeEvent;
import jufeng.juyancash.eventbus.SnackVipDetailConfirmEvent;
import jufeng.juyancash.eventbus.SnackVipDiscountConfirmEvent;
import jufeng.juyancash.ui.customview.CashierKeyboardUtil;
import jufeng.juyancash.ui.customview.CashierKeyboardUtil1;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeClearAllDialog;
import jufeng.juyancash.ui.customview.CustomeSelectPaymentDialog;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class LFragmentSnackCashier extends BaseFragment {
    private String orderId;
    private CashierKeyboardUtil1 mCashierKeyboardUtil;

    public static LFragmentSnackCashier newInstance(String orderId) {
        LFragmentSnackCashier fragment = new LFragmentSnackCashier();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadRootFragment(R.id.containertopleft, LFragmentSnackCashierTopLeft.newInstance(orderId));
        loadRootFragment(R.id.containertopright, LFragmentSnackCashierTopRight.newInstance(orderId));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_snack_cashier, container, false);
        initView(mView);
        setCashierKeyboardUtilListener();
        return mView;
    }

    private void initView(View mView) {
        mCashierKeyboardUtil = new CashierKeyboardUtil1(getContext(), mView, null);
        mCashierKeyboardUtil.showKeyboard();
        orderId = getArguments().getString("orderId");
    }

    private void setCashierKeyboardUtilListener() {
        mCashierKeyboardUtil.setCashierKeyBoardClickListener(new CashierKeyboardUtil1.OnCashierKeyBoardClickListener() {
            @Override
            public void onCashierKeyClick(int keyCode) {
                switch (keyCode) {
                    case CashierKeyboardUtil.KEYCODE_ZDDZ://整单打折
                        onDiscountAllClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_BFDZ://部分打折
                        onDiscountSomeClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_FADZ://方案打折
                        onDiscountClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_QKSY://清空所有
                        onClearAll(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_XJ://现金支付
                        onCashClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_YHK://银行卡支付
                        onBankClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_WXZF://微信支付
                        onWXPayClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_ZFB://支付宝支付
                        onAliPayClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_TG://团购支付
                        onGroupClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_GZ://挂账支付
                        onAccountClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_HYZF://会员卡支付
                        onOtherClick(orderId);
                        break;
                    case CashierKeyboardUtil.KEYCODE_HYYH://会员卡优惠
                        if (orderId != null) {
                            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
                            if (orderEntity != null) {
                                onVipDiscountClick(orderEntity);
                            }
                        }
                        break;
                    case CashierKeyboardUtil.KEYCODE_FH://取消
                        onCancleClick();
                        break;
                    case CashierKeyboardUtil.KEYCODE_JZWB://结账完毕
                        onCashierOverClick(orderId);
                        break;
                }
            }
        });
    }

    //***********************************EventBus事件回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCouponRefresh(CashierRightRefreshEvent event) {
        if (event != null) {
            replaceLoadRootFragment(R.id.containertopright, LFragmentSnackCashierTopRight.newInstance(orderId), false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventContinuCashier(SnackContinuCashierEvent event){
        if(event != null){
            final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(getContext(), 0);
            dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
                @Override
                public void onPaymentSelected(Payment payment) {
                    ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeCash.newInstance(orderId,payModeEntity),false);
                    dialog.dismiss();
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMlClick(final SnackMlClickEvent event){
        if(event != null){
            if (event.getMoney() > 0) {
                boolean result = false;
                String employeeId = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", null);
                if (employeeId != null) {
                    EmployeeEntity employeeEntity = DBHelper.getInstance(getContext()).getEmployeeById(employeeId);
                    if (employeeEntity != null) {
                        result = DBHelper.getInstance(getContext()).setMLMoneyByOrderId(orderId, event.getMoney(), employeeEntity);
                    }
                }
                if (result) {
                    EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
                    EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
                } else {
                    //权限验证对话框
                    final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "");
                    dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if (employeeEntity != null) {
                                boolean result = DBHelper.getInstance(getContext()).setMLMoneyByOrderId(orderId, event.getMoney(), employeeEntity);
                                if (result) {
                                    EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
                                    EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
                                    dialog.dismiss();
                                } else {
                                    Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            } else {
                CustomMethod.showMessage(getContext(), "抹零金额不能小于0");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSetKeyboard(SnackSetKeyboardEdittextEvent event){
        if(event != null){
            mCashierKeyboardUtil.setEdittext(event.getEditText());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenTopRight(SnackOpenTopRightEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDiscountSelectReason(SnackSelectDiscountReasonEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectDiscountReason.newInstance(0,event.getDiscountHistoryEntity(),null),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventConfirmDiscountAll(SnackDiscountAllConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectDiscountSomeReason(SnackSelectSomeReasonEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectDiscountReason.newInstance(1,event.getDiscountHistoryEntity(),event.getSomeDiscountGoodsEntities()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectDiscountSomeGoods(SnackSelectSomeGoodsEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectSomeGoods.newInstance(event.getDiscountHistoryEntity(),event.getSomeDiscountGoodsEntities()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDiscountSomeConfirm(SnackDiscountSomeConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(event.getOrderId()),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectSchemeReason(SnackSelectSchemeReasonEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectDiscountReason.newInstance(2,event.getDiscountHistoryEntity(),null),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectScheme(SnackSelectSchemeEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectDiscountScheme.newInstance(event.getDiscountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDiscountSchemeConfirm(SnackDiscountSchemeConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(event.getOrderId()),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashConfirm(SnackCashConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBankConfirm(SnackBankConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUseVip(SnackUseVipEvent event){
        if(event != null){
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventElectricConfirm(SnackElectricConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectGroupon(SnackSelectGrouponEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectGrouponTaocan.newInstance(orderId,event.getGrouponEntity(),event.getGrouponTaocanEntity(),event.getPayModeEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGrouponConfirm(SnackGrouponConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAccountUnit(SnackSelectAccountUnitEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectBillPerson.newInstance(0,orderId,event.getBillAccountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAccountPerson(SnackSelectAccountPeopleEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectBillPerson.newInstance(1,orderId,event.getBillAccountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAccountSignPerson(SnackSelectAccountSignPeopleEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackSelectBillPerson.newInstance(2,orderId,event.getBillAccountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAccountConfirm(SnackAccountConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVipDetailConfirm(SnackVipDetailConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVipChange(SnackVipChangeEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackVipDiscount.newInstance(orderId),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVipDiscountConfirm(SnackVipDiscountConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
            EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectDiscountReasonConfirm(SnackSelectDiscountReasonConfirmEvent event){
        if(event != null){
            switch (event.getType()){
                case 0:
                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackDiscountAll.newInstance(orderId,event.getDiscountHistoryEntity()),false);
                    break;
                case 1:
                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackDiscountSome.newInstance(orderId,event.getSomeDiscountGoodsEntities(),event.getDiscountHistoryEntity()),false);
                    break;
                case 2:
                    replaceLoadRootFragment(R.id.containertopright,LFragmentSnackDiscountScheme.newInstance(orderId,event.getDiscountHistoryEntity()),false);
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectBillPersonConfirm(SnackSelectBillPersonConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeAccount.newInstance(orderId,null,event.getBillAccountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectGrouponTaocanConfirm(SnackSelectGrouponTaocanConfrimEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeGroupon.newInstance(orderId,event.getPayModeEntity(),event.getGrouponTaocanEntity(),event.getGrouponEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectSomeGoodsConfirm(SnackSelectSomeGoodsConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackDiscountSome.newInstance(orderId,event.getSomeDiscountGoodsEntities(),event.getDiscountHistoryEntity()),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectDiscountSchemeConfirm(SnackSelectSchemeConfirmEvent event){
        if(event != null){
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackDiscountScheme.newInstance(orderId,event.getDiscountHistoryEntity()),false);
        }
    }

    //打折界面
    public void onDiscountAllClick(String orderId) {
        replaceLoadRootFragment(R.id.containertopright, LFragmentSnackDiscountAll.newInstance(orderId, null), false);
    }

    public void onDiscountSomeClick(String orderId) {
        replaceLoadRootFragment(R.id.containertopright, LFragmentSnackDiscountSome.newInstance(orderId, null, null), false);
    }

    public void onDiscountClick(String orderId) {
        replaceLoadRootFragment(R.id.containertopright, LFragmentSnackDiscountScheme.newInstance(orderId, null), false);
    }

    public void onClearAll(final String orderId) {
        //清空所有支付和打折
        final CustomeClearAllDialog dialog = new CustomeClearAllDialog();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        dialog.setArguments(bundle);
        dialog.setOnAuthorityListener(new CustomeClearAllDialog.OnClearAllListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClearSuccess() {
                EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
                EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
                replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), "");
    }

    //支付方式界面
    public void onCashClick(final String orderId) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(getContext(), 0);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                replaceLoadRootFragment(R.id.containertopright, LFragmentSnackPayModeCash.newInstance(orderId, payModeEntity), false);
                dialog.dismiss();
            }
        });
    }

    //银行卡支付
    public void onBankClick(final String orderId) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(getContext(), 1);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeBank.newInstance(orderId,payModeEntity),false);
                dialog.dismiss();
            }
        });
    }

    //微信支付
    public void onWXPayClick(String orderId) {
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("微信");
        payModeEntity.setPaymentType(0);
        replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeElectric.newInstance(orderId,payModeEntity),false);
    }

    //支付宝支付
    public void onAliPayClick(String orderId) {
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("支付宝");
        payModeEntity.setPaymentType(1);
        replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeElectric.newInstance(orderId,payModeEntity),false);
    }

    //团购
    public void onGroupClick(final String orderId) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(getContext(), 2);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                GrouponEntity grouponEntity = (GrouponEntity) payment;
                replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeGroupon.newInstance(orderId,null,null,grouponEntity),false);
                dialog.dismiss();
            }
        });
    }

    //挂账
    public void onAccountClick(final String orderId) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(getContext(), 3);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                BillAccountEntity billAccountEntity = (BillAccountEntity) payment;
                replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeAccount.newInstance(orderId,billAccountEntity,null),false);
                dialog.dismiss();
            }
        });
    }

    //会员卡支付
    public void onOtherClick(String orderId) {
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("会员卡");
        payModeEntity.setPaymentType(2);
        replaceLoadRootFragment(R.id.containertopright,LFragmentSnackPayModeElectric.newInstance(orderId,payModeEntity),false);
    }

    //会员卡打折
    private void onVipDiscountClick(OrderEntity orderEntity) {
        if (orderEntity.getVipNo() != null) {
            //该订单已使用会员
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackVipDetail.newInstance(orderId),false);
        } else {
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackVipDiscount.newInstance(orderId),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCashierOverClick(String orderId) {
        boolean isHasOrderedDish = DBHelper.getInstance(getContext()).isHasSnackOrderedDish(orderId);
        if(isHasOrderedDish) {
            if (DBHelper.getInstance(getContext()).getReceivableMoneyByOrderId(orderId) == 0) {
                //点击结账完毕
                //子单
                boolean result = DBHelper.getInstance(getContext()).cashierOver(orderId);
                if (result) {
                    DBHelper.getInstance(getContext()).insertUploadData(orderId, orderId, 7);
                    EventBus.getDefault().post(new PrintCaiwulianEvent(orderId));
                    EventBus.getDefault().post(new SnackOrderDetailChangeEvent(null, 0));
                    EventBus.getDefault().post(new SnackCashierOverEvent());
                    OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
                    if (orderEntity != null) {
                        if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                            DBHelper.getInstance(getContext()).deleteWxOrderMessage(orderId, 1);
                        }
                        DBHelper.getInstance(getContext()).deleteWxOrderMessage(orderId, 2);
                        EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
                    }
                } else {
                    CustomMethod.showMessage(getContext(), "结账失败，请重新尝试");
                }
            } else {
                CustomMethod.showMessage(getContext(), "未结完账单，请继续结账");
            }
        }else{
            CustomMethod.showMessage(getContext(), "暂无有效商品，无法结账，可选择取消订单！");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCancleClick() {
        EventBus.getDefault().post(new SnackCashierCancelEvent(orderId));
        EventBus.getDefault().post(new SnackOrderDetailChangeEvent(null,1));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderMoneyChange(SnackOrderMoneyChangedEvent event) {
        if (event != null) {
            replaceLoadRootFragment(R.id.containertopright,LFragmentSnackCashierTopRight.newInstance(orderId),false);
        }
    }
}
