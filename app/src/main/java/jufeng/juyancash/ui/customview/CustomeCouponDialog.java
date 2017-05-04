package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.CouponStatus;
import jufeng.juyancash.bean.OffLineWxCouponModel;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.eventbus.CashierDishListRefreshEvent;
import jufeng.juyancash.eventbus.CashierRightRefreshEvent;
import jufeng.juyancash.eventbus.CashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.CashierTopRightRefreshEvent;
import jufeng.juyancash.eventbus.CouponAddConfirmEvent;
import jufeng.juyancash.eventbus.CouponChangeCancelEvent;
import jufeng.juyancash.eventbus.CouponChangeConfirmEvent;
import jufeng.juyancash.eventbus.CouponDetailChangeEvent;
import jufeng.juyancash.eventbus.CouponDetailConfirmEvent;
import jufeng.juyancash.eventbus.CouponDialogCloseEvent;
import jufeng.juyancash.eventbus.CouponShowMessageEvent;
import jufeng.juyancash.eventbus.LoadingDialogEvent;
import jufeng.juyancash.eventbus.SnackCashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailRefreshEvent;
import jufeng.juyancash.ui.fragment.LFragmentCouponAdd;
import jufeng.juyancash.ui.fragment.LFragmentCouponChange;
import jufeng.juyancash.ui.fragment.LFragmentCouponDetail;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeCouponDialog extends DialogFragment{
    private LFragmentCouponChange mLFragmentCouponChange;
    private LFragmentCouponDetail mLFragmentCouponDetail;
    private LFragmentCouponAdd mLFragmentCouponAdd;
    private String orderId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        View view = inflater.inflate(R.layout.dialog_coupon_layout, container);
        initData();
        return view;
    }

    private void initData(){
        if(getArguments() != null) {
            orderId = getArguments().getString("orderId");
            int type = getArguments().getInt("type");
            if(type == 0){//未绑定优惠券
                setFragment(2);
            }else{
                setFragment(0);
            }
        }else{
            setFragment(2);
        }
    }

    private void setFragment(int tag){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        if(tag == 0){
            if(mLFragmentCouponDetail == null){
                mLFragmentCouponDetail = new LFragmentCouponDetail();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                mLFragmentCouponDetail.setArguments(bundle);
                fragmentTransaction.add(R.id.container,mLFragmentCouponDetail);
            }else{
                fragmentTransaction.show(mLFragmentCouponDetail);
            }
        }else if(tag == 1){
            //显示账号授权界面
            if(mLFragmentCouponChange == null){
                mLFragmentCouponChange = new LFragmentCouponChange();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                mLFragmentCouponChange.setArguments(bundle);
                fragmentTransaction.add(R.id.container,mLFragmentCouponChange);
            }else{
                fragmentTransaction.show(mLFragmentCouponChange);
            }
        }else if(tag == 2){
            //显示账号授权界面
            if(mLFragmentCouponAdd == null){
                mLFragmentCouponAdd = new LFragmentCouponAdd();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                mLFragmentCouponAdd.setArguments(bundle);
                fragmentTransaction.add(R.id.container,mLFragmentCouponAdd);
            }else{
                fragmentTransaction.show(mLFragmentCouponAdd);
            }
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction ft){
        if(mLFragmentCouponDetail != null){
            ft.hide(mLFragmentCouponDetail);
        }
        if(mLFragmentCouponChange != null){
            ft.hide(mLFragmentCouponChange);
        }
        if(mLFragmentCouponAdd != null){
            ft.hide(mLFragmentCouponAdd);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDetailChangeCoupon(CouponDetailChangeEvent event){
        if(event != null){
            setFragment(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDetailConfirm(CouponDetailConfirmEvent event){
        if(event != null && event.getStatus() > -1){
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
            if(orderEntity != null) {
                if(event.getStatus() == 1) {//使用
                    boolean isUsedVip = orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1;
                    DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext()).getDiscount(orderId);
                    if (isUsedVip && orderEntity.getIsCouponWithVip() == 0) {
                        //不允许和会员卡同时使用
                        EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不允许和会员卡同时使用，请替换优惠券或取消会员卡的使用"));
                        return;
                    }
                    if (discountHistoryEntity != null) {
                        //当前使用了其他打折方案
                        EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不与其他打折方案同时使用，请取消其他打折方案"));
                        return;
                    }
                    boolean canUseCoupon = DBHelper.getInstance(getContext()).canUseCoupon(orderId);
                    if (!canUseCoupon) {
                        EventBus.getDefault().post(new CouponShowMessageEvent("该笔订单不满足优惠券使用条件"));
                        return;
                    }
                }
                updateCouponStatus(event.getStatus(),orderEntity);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeCancel(CouponChangeCancelEvent event){
        if(event != null){
            setFragment(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeConfirm(CouponChangeConfirmEvent event){
        if(event != null && orderId != null){
            if(event != null && event.getOffLineWxCouponModel() != null && orderId != null){
                OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
                if(orderEntity != null){
                    boolean isUsedVip = orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1;
                    DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext()).getDiscount(orderId);
                    if(isUsedVip && event.getOffLineWxCouponModel().getDisVip() == 0) {
                        //不允许和会员卡同时使用
                        EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不允许和会员卡同时使用，请替换优惠券或取消会员卡的使用"));
                        return;
                    }
                    if(discountHistoryEntity != null){
                        //当前使用了其他打折方案
                        EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不与其他打折方案同时使用，请取消其他打折方案"));
                        return;
                    }
                    boolean canUseCoupon = DBHelper.getInstance(getContext()).canUseCoupon(orderId,event.getOffLineWxCouponModel());
                    if(!canUseCoupon){
                        EventBus.getDefault().post(new CouponShowMessageEvent("该笔订单不满足优惠券使用条件"));
                        return;
                    }
                    useCoupon(event.getOffLineWxCouponModel(),1);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddConfirm(CouponAddConfirmEvent event){
        if(event != null && event.getOffLineWxCouponModel() != null && orderId != null){
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
            if(orderEntity != null){
                boolean isUsedVip = orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1;
                DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext()).getDiscount(orderId);
                if(isUsedVip && event.getOffLineWxCouponModel().getDisVip() == 0) {
                    //不允许和会员卡同时使用
                    EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不允许和会员卡同时使用，请替换优惠券或取消会员卡的使用"));
                    return;
                }
                if(discountHistoryEntity != null){
                    //当前使用了其他打折方案
                    EventBus.getDefault().post(new CouponShowMessageEvent("该优惠券不与其他打折方案同时使用，请取消其他打折方案"));
                    return;
                }
                boolean canUseCoupon = DBHelper.getInstance(getContext()).canUseCoupon(orderId,event.getOffLineWxCouponModel());
                if(!canUseCoupon){
                    EventBus.getDefault().post(new CouponShowMessageEvent("该笔订单不满足优惠券使用条件"));
                    return;
                }
                useCoupon(event.getOffLineWxCouponModel(),1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCouponStatus(final int status,OrderEntity orderEntity){
        EventBus.getDefault().post(new LoadingDialogEvent(true,"正在切换优惠券状态..."));
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        long ts = System.currentTimeMillis();
        CouponStatus couponStatus = new CouponStatus(orderEntity.getUserCouponId(),status,orderId);
        String data = JSON.toJSONString(couponStatus);
        String sign = MD5Util.getMD5String(partnerCode+data+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("ts",String.valueOf(ts));
        map.put("partnerCode",partnerCode);
        map.put("sign",sign);
        map.put("data",data);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.USE_COUPON), ts + "", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "优惠券状态切换："+arg0);
                try{
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        //允许使用该优惠券，修改该优惠券状态->关闭该对话框->刷新收银界面
                        DBHelper.getInstance(getContext()).changeCouponStatus(orderId,status);
                        EventBus.getDefault().post(new CouponDialogCloseEvent());
                        EventBus.getDefault().post(new CashierTopLeftRefreshEvent());
                        EventBus.getDefault().post(new CashierDishListRefreshEvent());
                        EventBus.getDefault().post(new CashierTopRightRefreshEvent());
                        EventBus.getDefault().post(new CashierRightRefreshEvent());
                        EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                        EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
                    }else{
                        String message = publicModule.getMessage() == null ? "":publicModule.getMessage();
                        EventBus.getDefault().post(new CouponShowMessageEvent(message));
                    }
                }catch (Exception e){
                    EventBus.getDefault().post(new CouponShowMessageEvent("优惠券状态切换失败，请稍后再试"));
                }
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onError(VolleyError arg0) {
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
                EventBus.getDefault().post(new CouponShowMessageEvent("优惠券状态切换失败，请稍后再试"));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void useCoupon(final OffLineWxCouponModel offLineWxCouponModel, int status){
        EventBus.getDefault().post(new LoadingDialogEvent(true,"正在请求使用优惠券..."));
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        long ts = System.currentTimeMillis();
        CouponStatus couponStatus = new CouponStatus(offLineWxCouponModel.getBatch(),status,orderId);
        String data = JSON.toJSONString(couponStatus);
        String sign = MD5Util.getMD5String(partnerCode+data+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("ts",String.valueOf(ts));
        map.put("partnerCode",partnerCode);
        map.put("sign",sign);
        map.put("data",data);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.USE_COUPON), ts + "", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "优惠券使用："+arg0);
                try{
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        //允许使用该优惠券，插入该优惠券到该订单->关闭该对话框->刷新收银界面
                        DBHelper.getInstance(getContext()).insertCoupon(orderId,offLineWxCouponModel);
                        EventBus.getDefault().post(new CouponDialogCloseEvent());
                        EventBus.getDefault().post(new CashierTopLeftRefreshEvent());
                        EventBus.getDefault().post(new CashierDishListRefreshEvent());
                        EventBus.getDefault().post(new CashierTopRightRefreshEvent());
                        EventBus.getDefault().post(new CashierRightRefreshEvent());
                        EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                        EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
                    }else{
                        String message = publicModule.getMessage() == null ? "":publicModule.getMessage();
                        EventBus.getDefault().post(new CouponShowMessageEvent(message));
                    }
                }catch (Exception e){
                    EventBus.getDefault().post(new CouponShowMessageEvent("优惠券请求使用失败，请稍后再试"));
                }
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onError(VolleyError arg0) {
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
                EventBus.getDefault().post(new CouponShowMessageEvent("优惠券请求使用失败，请稍后再试"));
            }
        });
    }
}
