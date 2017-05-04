package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.VipCardModel;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.VipCardEntity;
import jufeng.juyancash.myinterface.CashierTopRightChangeListener;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentVipDiscount extends BaseFragment {
    private RelativeLayout layoutVipNo, layoutVipPhone;
    private EditText etVipNo, etVipPhone;
    private TextView tvCancle, tvConfirm, tvName, tvVipType, tvVipRate;
    private ImageButton ibSearch;
    private String orderId;
    private boolean isOpenJoinOrder;
    private CustomLoadingProgress mLoadingProgress;
    private InitKeyboardInterface mInitKeyboardInterface;
    private CashierTopRightChangeListener mCashierTopRightChangeListener;
    private VipCardEntity mVipCardEntity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mInitKeyboardInterface = (InitKeyboardInterface) context;
            mCashierTopRightChangeListener = (CashierTopRightChangeListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_vip_discount, container, false);
        initView(mView);
        setListener();
        initData();
        return mView;
    }

    private void initView(View view) {
        etVipNo = (EditText) view.findViewById(R.id.et_vip_no);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        layoutVipNo = (RelativeLayout) view.findViewById(R.id.layout_vip_no);
        tvVipType = (TextView) view.findViewById(R.id.tv_vip_type);
        tvVipRate = (TextView) view.findViewById(R.id.tv_vip_rate);
        ibSearch = (ImageButton) view.findViewById(R.id.ib_search);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        etVipPhone = (EditText) view.findViewById(R.id.et_vip_phone);
        layoutVipPhone = (RelativeLayout) view.findViewById(R.id.layout_vip_phone);

        mVipCardEntity = null;
        this.orderId = getArguments().getString("orderId");
        this.isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String orderId, boolean isOpenJoinOrder) {
        this.orderId = orderId;
        this.isOpenJoinOrder = isOpenJoinOrder;

        initData();
    }

    private void setFocusEdittext(EditText editText, RelativeLayout layout, boolean isInitKeyboard) {
        layoutVipNo.setBackgroundResource(R.drawable.edittext_background);
        layoutVipPhone.setBackgroundResource(R.drawable.edittext_background);
        layout.setBackgroundResource(R.drawable.edittext_focus_background);
        if (mInitKeyboardInterface != null) {
            if (isInitKeyboard) {
                mInitKeyboardInterface.setEdittext(editText);
            } else {
                mInitKeyboardInterface.setEdittext(null);
            }
        }
        editText.requestFocus();
    }

    private void initData() {
        etVipNo.setText("");
        etVipPhone.setText("");
        tvVipRate.setText("");
        tvVipType.setText("");
        CustomMethod.setMyInputType(etVipPhone, getActivity());
        CustomMethod.setMyInputType2(etVipNo, getActivity());

        setFocusEdittext(etVipNo, layoutVipNo, false);
    }

    private void setListener() {
        etVipNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("###", "before文本: " + s.toString() + "-" + start + "-" + count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("###", "change文本: " + s.toString() + "-" + start + "-" + before + "-" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String result = s.toString();
                    if (s.length() > 5) {
                        String vipNo = (String) result.subSequence(0, 5);
                        String vipType = (String) result.subSequence(5, 6);
                        etVipNo.setText(vipNo);
                        showVipDetail(Integer.valueOf(vipType));
                    } else {
                        tvVipRate.setText("");
                        tvVipType.setText("");
                    }
                } catch (Exception e) {
                    etVipPhone.setText("");
                    etVipNo.setText("");
                    tvVipRate.setText("");
                    tvVipType.setText("");
                    setFocusEdittext(etVipNo, layoutVipNo, false);
                }
            }
        });
        layoutVipNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocusEdittext(etVipNo, layoutVipNo, false);
            }
        });
        etVipNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusEdittext(etVipNo, layoutVipNo, false);
                return false;
            }
        });
        layoutVipPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocusEdittext(etVipPhone, layoutVipPhone, true);
            }
        });
        etVipPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusEdittext(etVipPhone, layoutVipPhone, true);
                return false;
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCashierTopRightChangeListener != null) {
                    mCashierTopRightChangeListener.onTopRightCancle();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate = tvVipRate.getText().toString();
                if (rate.length() > 1) {
                    final int rateVal = Integer.valueOf(rate);
                    if (rateVal <= 100) {
                        final boolean isWithCoupon = DBHelper.getInstance(getContext().getApplicationContext()).isCouponWithVip(orderId);
                        final DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext().getApplicationContext()).getDiscount(orderId);
                        if (discountHistoryEntity != null || isWithCoupon) {//当前已使用优惠券或已使用其他折扣，
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
                        } else {
                            authorityDiscountAll();
                        }
                    } else {
                        showMessage("折扣率不能大于100%");
                    }
                } else {
                    showMessage("会员卡信息有误");
                }
            }
        });
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVipPhone.getText() != null && etVipPhone.getText().length() > 0) {
                    vipSearch(etVipPhone.getText().toString());
                } else {
                    showMessage("请输入会员卡手机号");
                }
            }
        });
    }

    /**
     * 会员查询
     */
    private void vipSearch(String phone) {
        showLoadingProgressBar("会员卡支付中...");
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + phone + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("mobile", phone);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.VIP_CARD_DETAIL1), "VIP_CARD_DETAIL1", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "会员卡查询结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        VipCardModel vipCardModel = JSON.parseObject(publicModule.getData(), VipCardModel.class);
                        etVipNo.setText(vipCardModel.getVipNo());
                        showVipDetail(vipCardModel.getCardType());
                        hideLoadingProgressBar();
                    } else {
                        String message = publicModule.getMessage() == null ? "会员查询失败，请重新尝试" : publicModule.getMessage();
                        showMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("会员查询失败，请重新尝试");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                showMessage("会员查询失败，请重新尝试");
            }
        });
    }

    private void showVipDetail(int type) {
        mVipCardEntity = DBHelper.getInstance(getContext()).getVipCardDetail(type);
        tvVipType.setText(mVipCardEntity.getVipCardName());
        String rate = "";
        if (mVipCardEntity.getVipCardDiscountType() == 0) {
            //不打折
            rate = "100";
        } else {
            rate += mVipCardEntity.getVipCardDiscountRate();
        }
        tvVipRate.setText(rate);
    }

    private void authorityDiscountAll() {
        if (mCashierTopRightChangeListener != null) {
            DBHelper.getInstance(getContext().getApplicationContext()).insertVipDetail(orderId, etVipNo.getText().toString(), mVipCardEntity.getVipCardType(), 0);
            mCashierTopRightChangeListener.onTopRightConfirm(orderId, isOpenJoinOrder);
            syncVip(etVipNo.getText().toString());
        }
    }

    public void syncVip(String vipNo) {
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        if (orderEntity != null && orderEntity.getIsUpload() == 1) {
            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(orderId, vipNo, 16);
        }
    }

    //显示提示消息
    private void showMessage(String payResultMessage) {
        hideLoadingProgressBar();
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("提示信息");
        dialog.setMessage(payResultMessage);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //显示加载框
    private void showLoadingProgressBar(String message) {
        if (mLoadingProgress != null) {
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }
        mLoadingProgress = new CustomLoadingProgress(getContext());
        mLoadingProgress.setMessage(message);
    }

    //隐藏加载框
    private void hideLoadingProgressBar() {
        if (mLoadingProgress != null) {
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }
    }
}
