package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.OffLineWxCouponModel;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.eventbus.CouponChangeCancelEvent;
import jufeng.juyancash.eventbus.CouponChangeConfirmEvent;
import jufeng.juyancash.eventbus.CouponDialogCloseEvent;
import jufeng.juyancash.eventbus.CouponShowMessageEvent;
import jufeng.juyancash.eventbus.LoadingDialogEvent;
import jufeng.juyancash.ui.customview.ClearEditText;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2017/3/9.
 */

public class LFragmentCouponChange extends BaseFragment implements View.OnClickListener {
    private ClearEditText etCouponCode;
    private ImageButton ibSearch;
    private TextView tvType, tvFaceValue, tvMinMoney, tvIsWithVip, tvIsWithSpecialDish;
    private TextView tvClose, tvConfirm,tvCancel;
    private String orderId;
    private TextView tvLabel1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_coupon_change, container, false);
        initView(mView);
        setListener();
        initData(null);
        return mView;
    }

    private void initView(View view) {
        tvType = (TextView) view.findViewById(R.id.tv_coupon_type);
        tvFaceValue = (TextView) view.findViewById(R.id.tv_coupon_face_value);
        tvMinMoney = (TextView) view.findViewById(R.id.tv_coupon_min_money);
        tvIsWithVip = (TextView) view.findViewById(R.id.tv_coupon_with_vip);
        tvIsWithSpecialDish = (TextView) view.findViewById(R.id.tv_coupon_with_special_dish);
        tvClose = (TextView) view.findViewById(R.id.tv_coupon_close);
        tvConfirm = (TextView) view.findViewById(R.id.tv_coupon_confirm_change);
        tvCancel = (TextView) view.findViewById(R.id.tv_coupon_cancel_change);
        etCouponCode = (ClearEditText) view.findViewById(R.id.et_coupon_code);
        ibSearch = (ImageButton) view.findViewById(R.id.ib_search);
        tvLabel1 = (TextView) view.findViewById(R.id.label_1);
        orderId = getArguments().getString("orderId");
    }

    private void setListener() {
        tvClose.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
    }

    private void initData(OffLineWxCouponModel offLineWxCouponModel) {
        tvConfirm.setTag(offLineWxCouponModel);
        if(offLineWxCouponModel == null){
            setViewClickable(false);
            setAllView("", "", "", "", "");
            return;
        }
        try {
            int couponType = offLineWxCouponModel.getType();
            String couponTypeStr = "";
            String faceValue = "";
            String label = "优惠券面值（单位：元）：";
            switch (couponType){
                case 0:
                    couponTypeStr = "满减券";
                    faceValue = AmountUtils.multiply(offLineWxCouponModel.getFaceValue() + "", "0.01");
                    break;
                case 1:
                    couponTypeStr = "代金券";
                    faceValue = AmountUtils.multiply(offLineWxCouponModel.getFaceValue() + "", "0.01");
                    break;
                case 2:
                    couponTypeStr = "折扣券";
                    faceValue = offLineWxCouponModel.getFaceValue() + "";
                    label = "优惠券面值（单位：%）：";
                    break;
            }
            tvLabel1.setText(label);
            String minMoney = AmountUtils.multiply(offLineWxCouponModel.getFullCut() + "", "0.01");
            String withVip = offLineWxCouponModel.getDisVip() == 0 ? "否" : "是";
            String withSpecialDish = offLineWxCouponModel.getForceDis() == 0 ? "否" : "是";
            setViewClickable(true);
            setAllView(couponTypeStr, faceValue, minMoney, withVip, withSpecialDish);
            return;
        } catch (Exception e) {
            setViewClickable(false);
            setAllView("", "", "", "", "");
            return;
        }
    }

    private void setViewClickable(boolean clickable) {
        tvConfirm.setEnabled(clickable);
    }

    private void setAllView(String couponType, String couponFaceValue, String couponMinMoney, String couponWithVip, String couponWithSpecialDish) {
        tvType.setText(couponType);
        tvFaceValue.setText(couponFaceValue);
        tvMinMoney.setText(couponMinMoney);
        tvIsWithVip.setText(couponWithVip);
        tvIsWithSpecialDish.setText(couponWithSpecialDish);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_coupon_close:
                EventBus.getDefault().post(new CouponDialogCloseEvent());
                break;
            case R.id.tv_coupon_cancel_change:
                EventBus.getDefault().post(new CouponChangeCancelEvent());
                break;
            case R.id.tv_coupon_confirm_change:
                EventBus.getDefault().post(new CouponChangeConfirmEvent((OffLineWxCouponModel) tvConfirm.getTag()));
                break;
            case R.id.ib_search:
                hideSoftKeyboard(etCouponCode);
                if(etCouponCode.getText() != null && !etCouponCode.getText().toString().isEmpty()) {
                    searchCouponByCode(etCouponCode.getText().toString());
                }else{
                    searchFailed("请输入优惠券券码");
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void searchCouponByCode(String couponCode){
        EventBus.getDefault().post(new LoadingDialogEvent(true,"正在查询优惠券..."));
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode+couponCode+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        map.put("couponNo",couponCode);
        map.put("ts",String.valueOf(ts));
        map.put("sign",sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.FIND_COUPON), ""+ts, map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                try{
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        OffLineWxCouponModel offLineWxCouponModel = JSON.parseObject(publicModule.getData(),OffLineWxCouponModel.class);
                        if(offLineWxCouponModel != null){
                            searchSuccess(offLineWxCouponModel);
                        }else{
                            searchFailed("优惠券查询失败！");
                        }
                    }else{
                        String message = publicModule.getMessage() == null ? "优惠券查询失败！":publicModule.getMessage();
                        searchFailed(message);
                    }
                }catch (Exception e){
                    searchFailed("优惠券查询失败！");
                }
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onError(VolleyError arg0) {
                searchFailed("优惠券查询失败！");
                EventBus.getDefault().post(new LoadingDialogEvent(false,""));
            }
        });
    }

    //查询优惠券失败
    private void searchFailed(String message){
        Snackbar.make(etCouponCode,message,Snackbar.LENGTH_SHORT).show();
    }

    //查询优惠券成功
    private void searchSuccess(OffLineWxCouponModel offLineWxCouponModel){
        initData(offLineWxCouponModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowMessage(CouponShowMessageEvent event){
        if(event != null && event.getMessage() != null){
            searchFailed(event.getMessage());
        }
    }
}
