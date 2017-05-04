package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.BillAccountEntity;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.eventbus.CashierRightRefreshEvent;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CashierKeyboardUtil;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class LCashierFragment extends BaseFragment implements InitKeyboardInterface{
    private LFragmentCashierTopLeft mLFragmentCashierTopLeft;
    private LFragmentCashierTopRight mLFragmentCashierTopRight;
    //付款方式的fragment
    private LFragmentPayModeCash mLFragmentPayModeCash;
    private LFragmentPayModeBank mLFragmentPayModeBank;
    private LFragmentPayModeAccount mLFragmentPayModeAccount;
    private LFragmentPayModeElectric mLFragmentPayModeElectric;
    private LFragmentPayModeGroupon mLFragmentPayModeGroupon;
    //打折的fragment
    private LFragmentDiscountAll mLFragmentDiscountAll;
    private LFragmentDiscountSome mLFragmentDiscountSome;
    private LFragmentDiscountScheme mLFragmentDiscountScheme;
    private LFragmentSelectDiscountReason mLFragmentSelectDiscountReason;
    private LFragmentSelectSomeGoods mLFragmentSelectSomeGoods;
    private LFragmentSelectDiscountScheme mLFragmentSelectDiscountScheme;
    //挂账记录
    private LFragmentSelectBillPerson mLFragmentSelectBillPerson;
    //团购
    private LFragmentSelectGrouponTaocan mLFragmentSelectGrouponTaocan;
    //会员卡优惠
    private LFragmentVipDiscount mLFragmentVipDiscount;
    //会员卡详情
    private LFragmentVipDetail mLFragmentVipDetail;

    private String orderId,tableId;
    private boolean isOpenJoinOrder;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    if(msg.obj != null)
                        mCashierKeyboardUtil.setEdittext((EditText) msg.obj);
                    break;
                case 0:
                    setTopRightFragment(0,null,null,null,null);
                    break;
                case 1:
                    //现金付款
                    ShopPaymentEntity payModeEntity = msg.getData().getParcelable("payment");
                    setTopRightFragment(1,null,null,payModeEntity,null);
                    break;
                case 2:
                    //银行卡付款
                    ShopPaymentEntity payModeEntity1 = msg.getData().getParcelable("payment");
                    setTopRightFragment(2,null,null,payModeEntity1,null);
                    break;
                case 3:
                    //电子支付付款
                    ShopPaymentEntity payModeEntity2 = msg.getData().getParcelable("payment");
                    setTopRightFragment(3,null,null,payModeEntity2,null);
                    break;
                case 4:
                    //挂账付款
                    BillAccountEntity billAccountEntity =  msg.getData().getParcelable("payment");
                    BillAccountHistoryEntity billAccountHistoryEntity =  msg.getData().getParcelable("billHistory");
                    setTopRightFragment(4,null,null,billAccountEntity,billAccountHistoryEntity);
                    break;
                case 5:
                    //整单打折
                    DiscountHistoryEntity discountHistoryEntity =  msg.getData().getParcelable("discountHistory");
                    setTopRightFragment(5,discountHistoryEntity,null,null,null);
                    break;
                case 6:
                    //部分打折
                    DiscountHistoryEntity discountHistoryEntity1 =  msg.getData().getParcelable("discountHistory");
                    ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities = msg.getData().getParcelableArrayList("someDiscountGoods");
                    setTopRightFragment(6,discountHistoryEntity1,someDiscountGoodsEntities,null,null);
                    break;
                case 7:
                    //打折方案
                    DiscountHistoryEntity discountHistoryEntity2 =  msg.getData().getParcelable("discountHistory");
                    setTopRightFragment(7,discountHistoryEntity2,null,null,null);
                    break;
                case 8:
                    //选择打折原因
                    DiscountHistoryEntity discountHistoryEntity3 =  msg.getData().getParcelable("discountHistory");
                    ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities2 = msg.getData().getParcelableArrayList("someDiscountGoods");
                    setTopRightFragment(8,discountHistoryEntity3,someDiscountGoodsEntities2,null,null);
                    break;
                case 9://
                    ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities1 = msg.getData().getParcelableArrayList("someDiscountGoods");
                    DiscountHistoryEntity discountHistoryEntity4 =  msg.getData().getParcelable("discountHistory");
                    setTopRightFragment(9,discountHistoryEntity4,someDiscountGoodsEntities1,null,null);
                    break;
                case 10:
                    DiscountHistoryEntity discountHistoryEntity5 =  msg.getData().getParcelable("discountHistory");
                    setTopRightFragment(10,discountHistoryEntity5,null,null,null);
                    break;
                case 11://团购
                    GrouponEntity grouponEntity =  msg.getData().getParcelable("groupon");
                    PayModeEntity payModeEntity3 =  msg.getData().getParcelable("payment");
                    GrouponTaocanEntity grouponTaocanEntity =  msg.getData().getParcelable("grouponTaocan");
                    setTopRightFragment(payModeEntity3,grouponEntity,grouponTaocanEntity);
                    break;
                case 12://选择挂账
                    BillAccountHistoryEntity billAccountHistoryEntity1 =  msg.getData().getParcelable("billHistory");
                    int billType = msg.getData().getInt("type");
                    setTopRightFragment(billAccountHistoryEntity1,billType);
                    break;
                case 13://选择团购套餐
                    GrouponEntity grouponEntity1 = msg.getData().getParcelable("grouponEntity");
                    GrouponTaocanEntity grouponTaocanEntity1 = msg.getData().getParcelable("grouponTaocan");
                    PayModeEntity payModeEntity4 = msg.getData().getParcelable("payment");
                    setTopRightFragment(grouponEntity1,grouponTaocanEntity1,payModeEntity4);
                    break;
                case 14://会员优惠
                    setTopRightFragment();
                    break;
                case 15://会员卡详情
                    setTopRightFragment1();
                    break;
                case 16://有支付消息
                    try{
                        if(orderId.equals(msg.obj)){
                            setTopRightFragment(0,null,null,null,null);
                        }
                    }catch (Exception e){

                    }
                    break;
            }
        }
    };
    private CashierKeyboardUtil mCashierKeyboardUtil;
    private MainFragmentListener mOnKeyBoardViewClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mActivity = (MainActivity) context;
        mActivity.setCashierHandler(mHandler);
        try{
            mOnKeyBoardViewClickListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_cashier,container,false);
        initView(mView);
        initData();
        setCashierKeyboardUtilListener();
        return mView;
    }

    private void initView(View mView){
        mCashierKeyboardUtil = new CashierKeyboardUtil(getContext(),mView,null);
        mCashierKeyboardUtil.showKeyboard();
        orderId = getArguments().getString("orderId");
        tableId = getArguments().getString("tableId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String orderId,String tableId,boolean isOpenJoinOrder){
        this.orderId = orderId;
        this.tableId = tableId;
        this.isOpenJoinOrder = isOpenJoinOrder;

        initData();
    }

    private void initData(){
        addFragment(orderId);
        setTopRightFragment(0,null,null,null,null);
    }

    private void addFragment(String orderId){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        if (mLFragmentCashierTopLeft == null) {
            mLFragmentCashierTopLeft = new LFragmentCashierTopLeft();
            Bundle b = new Bundle();
            b.putString("orderId", orderId);
            b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
            mLFragmentCashierTopLeft.setArguments(b);
            trans.add(R.id.containertopleft, mLFragmentCashierTopLeft);
        }else{
            mLFragmentCashierTopLeft.setNewParam(orderId,isOpenJoinOrder);
            trans.show(mLFragmentCashierTopLeft);
        }
        trans.commit();
    }

    //团购
    private void setTopRightFragment(PayModeEntity payModeEntity, GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        if (mLFragmentPayModeGroupon == null) {
            mLFragmentPayModeGroupon = new LFragmentPayModeGroupon();
            Bundle b = new Bundle();
            b.putString("orderId", orderId);
            b.putParcelable("payment",payModeEntity);
            b.putParcelable("groupon",grouponEntity);
            b.putParcelable("grouponTaocan",grouponTaocanEntity);
            b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
            mLFragmentPayModeGroupon.setArguments(b);
            trans.add(R.id.containertopright, mLFragmentPayModeGroupon);
        }else{
            mLFragmentPayModeGroupon.setNewParam(orderId,isOpenJoinOrder,payModeEntity,grouponTaocanEntity,grouponEntity);
            trans.show(mLFragmentPayModeGroupon);
        }
        trans.commit();
    }

    private void setTopRightFragment(int type, DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities, Payment payment, BillAccountHistoryEntity billAccountHistoryEntity){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        switch (type){
            case 0://收款界面
                if (mLFragmentCashierTopRight == null) {
                    mLFragmentCashierTopRight = new LFragmentCashierTopRight();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentCashierTopRight.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentCashierTopRight);
                }else{
                    mLFragmentCashierTopRight.setNewParam(orderId,isOpenJoinOrder);
                    trans.show(mLFragmentCashierTopRight);
                }
                break;
            case 1://现金支付界面
                if (mLFragmentPayModeCash == null) {
                    mLFragmentPayModeCash = new LFragmentPayModeCash();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("payment",payment);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentPayModeCash.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentPayModeCash);
                }else{
                    mLFragmentPayModeCash.setNewParam(orderId,isOpenJoinOrder,payment);
                    trans.show(mLFragmentPayModeCash);
                }
                break;
            case 2://银行卡支付界面
                if (mLFragmentPayModeBank == null) {
                    mLFragmentPayModeBank = new LFragmentPayModeBank();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("payment",payment);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentPayModeBank.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentPayModeBank);
                }else{
                    mLFragmentPayModeBank.setNewParam(orderId,isOpenJoinOrder,payment);
                    trans.show(mLFragmentPayModeBank);
                }
                break;
            case 3://电子支付界面
                if (mLFragmentPayModeElectric == null) {
                    mLFragmentPayModeElectric = new LFragmentPayModeElectric();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("payment",payment);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentPayModeElectric.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentPayModeElectric);
                }else{
                    mLFragmentPayModeElectric.setNewParam(orderId,isOpenJoinOrder,payment);
                    trans.show(mLFragmentPayModeElectric);
                }
                break;
            case 4://挂账支付界面
                if (mLFragmentPayModeAccount == null) {
                    mLFragmentPayModeAccount = new LFragmentPayModeAccount();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("payment",payment);
                    b.putParcelable("billHistory",billAccountHistoryEntity);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentPayModeAccount.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentPayModeAccount);
                }else{
                    mLFragmentPayModeAccount.setNewParam(orderId,isOpenJoinOrder,billAccountHistoryEntity,payment);
                    trans.show(mLFragmentPayModeAccount);
                }
                break;
            case 5://整单打折
                if (mLFragmentDiscountAll == null) {
                    mLFragmentDiscountAll = new LFragmentDiscountAll();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentDiscountAll.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentDiscountAll);
                }else{
                    mLFragmentDiscountAll.setNewParam(orderId,isOpenJoinOrder,discountHistoryEntity);
                    trans.show(mLFragmentDiscountAll);
                }
                break;
            case 6://部分打折
                if (mLFragmentDiscountSome == null) {
                    mLFragmentDiscountSome = new LFragmentDiscountSome();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putParcelableArrayList("someDiscountGoods",someDiscountGoodsEntities);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentDiscountSome.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentDiscountSome);
                }else{
                    mLFragmentDiscountSome.setNewParam(orderId,isOpenJoinOrder,someDiscountGoodsEntities,discountHistoryEntity);
                    trans.show(mLFragmentDiscountSome);
                }
                break;
            case 7://方案打折
                if (mLFragmentDiscountScheme == null) {
                    mLFragmentDiscountScheme = new LFragmentDiscountScheme();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentDiscountScheme.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentDiscountScheme);
                }else{
                    mLFragmentDiscountScheme.setNewParam(orderId,isOpenJoinOrder,discountHistoryEntity);
                    trans.show(mLFragmentDiscountScheme);
                }
                break;
            case 8://选择打折原因
                if (mLFragmentSelectDiscountReason == null) {
                    mLFragmentSelectDiscountReason = new LFragmentSelectDiscountReason();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putParcelableArrayList("someDiscountGoods",someDiscountGoodsEntities);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentSelectDiscountReason.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentSelectDiscountReason);
                }else{
                    mLFragmentSelectDiscountReason.setNewParam(discountHistoryEntity,someDiscountGoodsEntities);
                    trans.show(mLFragmentSelectDiscountReason);
                }
                break;
            case 9://选择部分打折商品
                if (mLFragmentSelectSomeGoods == null) {
                    mLFragmentSelectSomeGoods = new LFragmentSelectSomeGoods();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelableArrayList("someDiscountGoods",someDiscountGoodsEntities);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentSelectSomeGoods.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentSelectSomeGoods);
                }else{
                    mLFragmentSelectSomeGoods.setNewParam(discountHistoryEntity,someDiscountGoodsEntities,isOpenJoinOrder);
                    trans.show(mLFragmentSelectSomeGoods);
                }
                break;
            case 10://选择打折方案
                if (mLFragmentSelectDiscountScheme == null) {
                    mLFragmentSelectDiscountScheme = new LFragmentSelectDiscountScheme();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putParcelable("discountHistory",discountHistoryEntity);
                    b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
                    mLFragmentSelectDiscountScheme.setArguments(b);
                    trans.add(R.id.containertopright, mLFragmentSelectDiscountScheme);
                }else{
                    mLFragmentSelectDiscountScheme.setNewParam(discountHistoryEntity);
                    trans.show(mLFragmentSelectDiscountScheme);
                }
                break;
        }
        trans.commit();
    }

    //选择团购套餐
    private void setTopRightFragment(GrouponEntity grouponEntity,GrouponTaocanEntity grouponTaocanEntity,PayModeEntity payModeEntity) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        if (mLFragmentSelectGrouponTaocan == null) {
            mLFragmentSelectGrouponTaocan = new LFragmentSelectGrouponTaocan();
            Bundle b = new Bundle();
            b.putString("orderId",orderId);
            b.putParcelable("grouponEntity", grouponEntity);
            b.putParcelable("grouponTaocan",grouponTaocanEntity);
            b.putParcelable("payment",payModeEntity);
            mLFragmentSelectGrouponTaocan.setArguments(b);
            trans.add(R.id.containertopright, mLFragmentSelectGrouponTaocan);
        }else{
            mLFragmentSelectGrouponTaocan.setNewParam(orderId,grouponEntity,grouponTaocanEntity,payModeEntity);
            trans.show(mLFragmentSelectGrouponTaocan);
        }
        trans.commit();
    }

    //选择挂账参数
    private void setTopRightFragment(BillAccountHistoryEntity billAccountHistoryEntity,int billType) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        if (mLFragmentSelectBillPerson == null) {
            mLFragmentSelectBillPerson = new LFragmentSelectBillPerson();
            Bundle b = new Bundle();
            b.putInt("type", billType);
            b.putString("orderId",orderId);
            b.putParcelable("billHistory",billAccountHistoryEntity);
            mLFragmentSelectBillPerson.setArguments(b);
            trans.add(R.id.containertopright, mLFragmentSelectBillPerson);
        }else{
            mLFragmentSelectBillPerson.setNewParam(orderId,billAccountHistoryEntity,billType);
            trans.show(mLFragmentSelectBillPerson);
        }
        trans.commit();
    }

    //会员卡优惠
    private void setTopRightFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        if (mLFragmentVipDiscount == null) {
            mLFragmentVipDiscount = new LFragmentVipDiscount();
            Bundle b = new Bundle();
            b.putString("orderId", orderId);
            b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
            mLFragmentVipDiscount.setArguments(b);
            trans.add(R.id.containertopright, mLFragmentVipDiscount);
        }else{
            mLFragmentVipDiscount.setNewParam(orderId,isOpenJoinOrder);
            trans.show(mLFragmentVipDiscount);
        }
        trans.commit();
    }

    //会员卡详情
    private void setTopRightFragment1() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        if (mLFragmentVipDetail == null) {
            mLFragmentVipDetail = new LFragmentVipDetail();
            Bundle b = new Bundle();
            b.putString("orderId", orderId);
            b.putBoolean("isOpenJoinOrder",isOpenJoinOrder);
            mLFragmentVipDetail.setArguments(b);
            trans.add(R.id.containertopright, mLFragmentVipDetail);
        }else{
            mLFragmentVipDetail.setNewParam(orderId,isOpenJoinOrder);
            trans.show(mLFragmentVipDetail);
        }
        trans.commit();
    }

    private void hideFragment(FragmentTransaction trans){
        if(mLFragmentPayModeAccount != null){
            trans.hide(mLFragmentPayModeAccount);
        }
        if(mLFragmentPayModeBank != null){
            trans.hide(mLFragmentPayModeBank);
        }
        if(mLFragmentPayModeCash != null){
            trans.hide(mLFragmentPayModeCash);
        }
        if(mLFragmentPayModeElectric != null){
            trans.hide(mLFragmentPayModeElectric);
        }
        if(mLFragmentDiscountAll != null){
            trans.hide(mLFragmentDiscountAll);
        }
        if(mLFragmentDiscountSome != null){
            trans.hide(mLFragmentDiscountSome);
        }
        if(mLFragmentDiscountScheme != null){
            trans.hide(mLFragmentDiscountScheme);
        }
        if(mLFragmentSelectDiscountReason != null){
            trans.hide(mLFragmentSelectDiscountReason);
        }
        if(mLFragmentSelectSomeGoods != null){
            trans.hide(mLFragmentSelectSomeGoods);
        }
        if(mLFragmentSelectDiscountScheme != null){
            trans.hide(mLFragmentSelectDiscountScheme);
        }
        if(mLFragmentSelectBillPerson != null){
            trans.hide(mLFragmentSelectBillPerson);
        }
        if(mLFragmentPayModeGroupon != null){
            trans.hide(mLFragmentPayModeGroupon);
        }
        if(mLFragmentSelectGrouponTaocan != null){
            trans.hide(mLFragmentSelectGrouponTaocan);
        }
        if(mLFragmentVipDiscount != null){
            trans.hide(mLFragmentVipDiscount);
        }
        if(mLFragmentVipDetail != null){
            trans.hide(mLFragmentVipDetail);
        }
        if(mLFragmentCashierTopRight != null){
            trans.hide(mLFragmentCashierTopRight);
        }
    }

    @Override
    public void setEdittext(EditText et) {
        mCashierKeyboardUtil.setEdittext(et);
    }

    private void setCashierKeyboardUtilListener(){
        mCashierKeyboardUtil.setCashierKeyBoardClickListener(new CashierKeyboardUtil.OnCashierKeyBoardClickListener() {
            @Override
            public void onCashierKeyClick(int keyCode) {
                if(mOnKeyBoardViewClickListener != null)
                    mOnKeyBoardViewClickListener.onKeyClick(keyCode,orderId,isOpenJoinOrder,tableId);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(CashierRightRefreshEvent event){
        if(event != null) {
            mHandler.sendEmptyMessage(0);
        }
    }
}
