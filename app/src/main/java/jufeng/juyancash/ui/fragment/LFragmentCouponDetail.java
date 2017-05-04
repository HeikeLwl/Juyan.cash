package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.eventbus.CouponDetailChangeEvent;
import jufeng.juyancash.eventbus.CouponDetailConfirmEvent;
import jufeng.juyancash.eventbus.CouponDialogCloseEvent;
import jufeng.juyancash.eventbus.CouponShowMessageEvent;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by Administrator102 on 2017/3/9.
 */

public class LFragmentCouponDetail extends BaseFragment implements View.OnClickListener {
    private TextView tvType, tvFaceValue, tvMinMoney, tvIsWithVip, tvIsWithSpecialDish;
    private RadioGroup mRadioGroup;
    private TextView tvClose, tvConfirm, tvChange;
    private OrderEntity mOrderEntity;
    private TextView tvLabel1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_coupon_detail, container, false);
        initView(mView);
        setListener();
        initData();
        return mView;
    }

    private void initView(View view) {
        tvType = (TextView) view.findViewById(R.id.tv_coupon_type);
        tvFaceValue = (TextView) view.findViewById(R.id.tv_coupon_face_value);
        tvMinMoney = (TextView) view.findViewById(R.id.tv_coupon_min_money);
        tvIsWithVip = (TextView) view.findViewById(R.id.tv_coupon_with_vip);
        tvIsWithSpecialDish = (TextView) view.findViewById(R.id.tv_coupon_with_special_dish);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_coupon_status);
        tvClose = (TextView) view.findViewById(R.id.tv_coupon_close);
        tvConfirm = (TextView) view.findViewById(R.id.tv_coupon_confirm);
        tvChange = (TextView) view.findViewById(R.id.tv_coupon_change);
        tvLabel1 = (TextView) view.findViewById(R.id.label_1);
    }

    private void setListener() {
        tvClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvChange.setOnClickListener(this);
    }

    private void initData() {
        String orderId = getArguments().getString("orderId");
        if (orderId == null) {
            setViewClickable(false);
            setAllView("", "", "", "", "", -1);
            return;
        }
        mOrderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
        if (mOrderEntity == null) {
            setViewClickable(false);
            setAllView("", "", "", "", "", -1);
            return;
        }
        if (mOrderEntity.getUserCouponId() == null) {
            setViewClickable(false);
            setAllView("", "", "", "", "", -1);
            return;
        }
        try {
            int couponType = mOrderEntity.getCouponType();
            String couponTypeStr = "";
            String faceValue = "";
            String label = "优惠券面值（单位：元）：";
            switch (couponType) {
                case 0:
                    couponTypeStr = "满减券";
                    faceValue = AmountUtils.multiply(mOrderEntity.getCouponFaceValue() + "", "0.01");
                    break;
                case 1:
                    couponTypeStr = "代金券";
                    faceValue = AmountUtils.multiply(mOrderEntity.getCouponFaceValue() + "", "0.01");
                    break;
                case 2:
                    couponTypeStr = "折扣券";
                    faceValue = mOrderEntity.getCouponFaceValue() + "";
                    label = "优惠券面值（单位：%）：";
                    break;
            }
            tvLabel1.setText(label);
            String minMoney = AmountUtils.multiply(mOrderEntity.getCouponCondition() + "", "0.01");
            String withVip = mOrderEntity.getIsCouponWithVip() == 0 ? "否" : "是";
            String withSpecialDish = mOrderEntity.getIsCouponDiscountAll() == 0 ? "否" : "是";
            int checkId = (mOrderEntity.getIsCoupon() < 0 || mOrderEntity.getIsCoupon() > 1) ? -1 : mOrderEntity.getIsCoupon();
            setViewClickable(true);
            setAllView(couponTypeStr, faceValue, minMoney, withVip, withSpecialDish, checkId);
            return;
        } catch (Exception e) {
            setViewClickable(false);
            setAllView("", "", "", "", "", -1);
            return;
        }
    }

    private void setViewClickable(boolean clickable) {
        tvConfirm.setEnabled(clickable);
        tvChange.setEnabled(clickable);
    }

    private void setAllView(String couponType, String couponFaceValue, String couponMinMoney, String couponWithVip, String couponWithSpecialDish, int checkId) {
        tvType.setText(couponType);
        tvFaceValue.setText(couponFaceValue);
        tvMinMoney.setText(couponMinMoney);
        tvIsWithVip.setText(couponWithVip);
        tvIsWithSpecialDish.setText(couponWithSpecialDish);
        if (checkId == 0) {
            mRadioGroup.check(R.id.rb_unuse);
        } else if (checkId == 1) {
            mRadioGroup.check(R.id.rb_used);
        } else {
            mRadioGroup.clearCheck();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_coupon_close:
                EventBus.getDefault().post(new CouponDialogCloseEvent());
                break;
            case R.id.tv_coupon_confirm:
                int status = -1;
                status = mRadioGroup.getCheckedRadioButtonId() == R.id.rb_unuse ? 0 : status;
                Log.d("###", "onClick: "+status);
                status = mRadioGroup.getCheckedRadioButtonId() == R.id.rb_used ? 1 : status;
                Log.d("###", "onClick: "+status);
                if (status > -1 && mOrderEntity.getIsCoupon() != null && mOrderEntity.getIsCoupon() > -1 && status != mOrderEntity.getIsCoupon()) {
                    EventBus.getDefault().post(new CouponDetailConfirmEvent(status));
                }else{
                    EventBus.getDefault().post(new CouponDialogCloseEvent());
                }
                break;
            case R.id.tv_coupon_change:
                EventBus.getDefault().post(new CouponDetailChangeEvent());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCouponShowMessage(CouponShowMessageEvent event){
        if(event != null && event.getMessage() != null){
            Snackbar.make(tvType,event.getMessage(),Snackbar.LENGTH_SHORT).show();
        }
    }
}
