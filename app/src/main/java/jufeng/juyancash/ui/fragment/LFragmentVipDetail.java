package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.VipCardEntity;
import jufeng.juyancash.myinterface.CashierTopRightChangeListener;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentVipDetail extends BaseFragment {
    private TextView tvCancle, tvConfirm,tvName,tvVipType,tvVipRate,tvVipNo,tvChangeVip;
    private String orderId;
    private boolean isOpenJoinOrder;
    private CustomLoadingProgress mLoadingProgress;
    private CashierTopRightChangeListener mCashierTopRightChangeListener;
    private RadioGroup mRadioGroup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCashierTopRightChangeListener = (CashierTopRightChangeListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_vip_detail,container,false);
        initView(mView);
        setListener();
        initData();
        return mView;
    }

    private void initView(View view) {
        tvVipNo = (TextView) view.findViewById(R.id.tv_vip_no);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvVipType = (TextView) view.findViewById(R.id.tv_vip_type);
        tvVipRate = (TextView) view.findViewById(R.id.tv_vip_rate);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        tvChangeVip = (TextView) view.findViewById(R.id.tv_change_vip);

        this.orderId = getArguments().getString("orderId");
        this.isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String orderId,boolean isOpenJoinOrder){
        this.orderId = orderId;
        this.isOpenJoinOrder = isOpenJoinOrder;
        initData();
    }

    private void initData(){
        try {
            OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
            if (orderEntity != null) {
                VipCardEntity vipCardEntity = DBHelper.getInstance(getContext().getApplicationContext()).getVipCardDetail(orderEntity.getVipType());
                tvVipNo.setText(orderEntity.getVipNo());
                tvVipType.setText(vipCardEntity.getVipCardName());
                if(vipCardEntity.getVipCardDiscountType() == 0){
                    tvVipRate.setText("100");
                }else {
                    tvVipRate.setText(""+vipCardEntity.getVipCardDiscountRate());
                }
                if(orderEntity.getIsVip() == 0){
                    //未使用
                    mRadioGroup.check(R.id.radio_1);
                }else{
                    //已使用
                    mRadioGroup.check(R.id.radio_0);
                }
            }
        }catch (Exception e){
            tvVipNo.setText("");
            tvVipType.setText("");
            tvVipRate.setText("");
            mRadioGroup.clearCheck();
        }
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCashierTopRightChangeListener != null){
                    mCashierTopRightChangeListener.onTopRightCancle();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCashierTopRightChangeListener != null){
                    switch (mRadioGroup.getCheckedRadioButtonId()){
                        case R.id.radio_0://使用会员卡优惠
                            final boolean isWithCoupon = DBHelper.getInstance(getContext().getApplicationContext()).isCouponWithVip(orderId);
                            final DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext().getApplicationContext()).getDiscount(orderId);
                            if(discountHistoryEntity != null || isWithCoupon){//当前已使用优惠券或会员卡优惠
                                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                dialog.setTitle("提示信息");
                                dialog.setMessage("当前订单已使用其他优惠方式并且该优惠方式不允许和会员卡优惠同时使用，请先清除其他优惠方式！");
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mCashierTopRightChangeListener.onTopRightCancle();
                                    }
                                });
                                dialog.show();
                            }else {
                                DBHelper.getInstance(getContext().getApplicationContext()).changeVipStatus(orderId,1);
                                mCashierTopRightChangeListener.onTopRightConfirm(orderId,isOpenJoinOrder);
                            }
                            break;
                        case R.id.radio_1://不使用会员卡优惠
                            DBHelper.getInstance(getContext().getApplicationContext()).changeVipStatus(orderId,0);
                            mCashierTopRightChangeListener.onTopRightConfirm(orderId,isOpenJoinOrder);
                            break;
                    }
                }
            }
        });
        tvChangeVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHasVipPay = DBHelper.getInstance(getContext().getApplicationContext()).isHasVipPay(orderId);
                if(isHasVipPay){
                    //当前订单已有会员卡支付，如需换卡请清除会员卡支付
                    CustomMethod.showMessage(getContext(),"当前订单已有会员卡支付，如需更换会员请先清除之前的会员卡支付");
                }else {
                    if (mCashierTopRightChangeListener != null) {
                        mCashierTopRightChangeListener.onChangeVip();
                    }
                }
            }
        });
    }
}
