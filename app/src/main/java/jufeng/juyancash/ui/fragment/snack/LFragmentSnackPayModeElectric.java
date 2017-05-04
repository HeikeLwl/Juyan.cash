package jufeng.juyancash.ui.fragment.snack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
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
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.ReqParamVo;
import jufeng.juyancash.bean.SMZF003Vo;
import jufeng.juyancash.bean.SMZF006Vo;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.dao.VipCardEntity;
import jufeng.juyancash.eventbus.SnackElectricConfirmEvent;
import jufeng.juyancash.eventbus.SnackOpenCashBoxEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.eventbus.SnackUseVipEvent;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentSnackPayModeElectric extends BaseFragment {
    private RelativeLayout layoutIncome, layoutElectric;
    private TextView tvReceivable;
    private EditText etIncome;
    private EditText etElectricNumber;
    private TextView tvCancle, tvConfirm, tvName;
    private String orderId;
    private int type;
    private Object object;

    public static LFragmentSnackPayModeElectric newInstance(String orderId, Payment payment) {
        LFragmentSnackPayModeElectric fragment = new LFragmentSnackPayModeElectric();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("payment", payment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_paymode_electric, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvReceivable = (TextView) view.findViewById(R.id.tv_receivable_money);
        etIncome = (EditText) view.findViewById(R.id.et_income);
        etElectricNumber = (EditText) view.findViewById(R.id.et_electric_number);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        layoutIncome = (RelativeLayout) view.findViewById(R.id.layout_income);
        layoutElectric = (RelativeLayout) view.findViewById(R.id.layout_electric);
        tvName = (TextView) view.findViewById(R.id.tv_name);

        orderId = getArguments().getString("orderId");
        object = getArguments().getParcelable("payment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initData() {
        etElectricNumber.setText("");
        etElectricNumber.setInputType(InputType.TYPE_NULL);

        CustomMethod.setMyInputType1(etIncome, getActivity());
        CustomMethod.setMyInputType2(etElectricNumber, getActivity());
        if (object instanceof ShopPaymentEntity) {
            tvName.setText(((ShopPaymentEntity) object).getPaymentName());
            type = ((ShopPaymentEntity) object).getPaymentType();
        }

        tvReceivable.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
        etIncome.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));

        if (type == 2) {
            layoutElectric.setTag(1);
            layoutIncome.setTag(0);
            EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etElectricNumber));
            etIncome.setFocusable(false);
            etIncome.setClickable(false);
            etElectricNumber.requestFocus();
            setLayoutBackground();
        } else {
            layoutIncome.setTag(1);
            layoutElectric.setTag(0);
            EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
            etIncome.setFocusable(false);
            etIncome.setClickable(false);
            etIncome.requestFocus();
            setLayoutBackground();
        }
    }

    private void setLayoutBackground() {
        if ((int) layoutIncome.getTag() == 1) {
            layoutIncome.setBackgroundResource(R.drawable.edittext_focus_background);
            layoutElectric.setBackgroundResource(R.drawable.edittext_background);
        } else {
            layoutIncome.setBackgroundResource(R.drawable.edittext_background);
            layoutElectric.setBackgroundResource(R.drawable.edittext_focus_background);
        }
    }

    private void setListener() {
        layoutIncome.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                if (type != 2) {
                    layoutElectric.setTag(0);
                    layoutIncome.setTag(1);
                    EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
                    etIncome.requestFocus();
                    setLayoutBackground();
                }
            }
        });
        layoutElectric.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                layoutElectric.setTag(1);
                layoutIncome.setTag(0);
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etElectricNumber));
                etElectricNumber.requestFocus();
                setLayoutBackground();
            }
        });
        etElectricNumber.setOnTouchListener(new View.OnTouchListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layoutElectric.setTag(1);
                layoutIncome.setTag(0);
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etElectricNumber));
                etElectricNumber.requestFocus();
                setLayoutBackground();
                return false;
            }
        });
        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (type == 2) {

                } else {
                    layoutElectric.setTag(0);
                    layoutIncome.setTag(1);
                    EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
                    etIncome.requestFocus();
                    setLayoutBackground();
                }
                return false;
            }
        });
        etElectricNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (type == 2) {
                    String result = s.toString();
                    if (result.length() >= 18) {
                        final String vipType = result.substring(17, 18);
                        final String vipNo = result.substring(12, 17);
                        int rateVal = 100;
                        if (vipType != null) {
                            VipCardEntity vipCardEntity = DBHelper.getInstance(getContext().getApplicationContext()).getVipCardDetail(Integer.valueOf(vipType));
                            if (vipCardEntity != null) {
                                if (vipCardEntity.getVipCardDiscountType() == 1) {
                                    rateVal = vipCardEntity.getVipCardDiscountRate();
                                }
                            }
                        }
                        int isSameVip = DBHelper.getInstance(getContext().getApplicationContext()).isSameVip(orderId, vipNo);
                        switch (isSameVip) {
                            case 0://不是同一个vip
                                etElectricNumber.setText("");
                                showPayResultMessage("系统检测到正在支付的会员卡与订单使用的不是同一个会员卡，请告知顾客只能使用同一个会员卡支付");
                                break;
                            case 1://同一个vip

                                break;
                            case 2://未使用vip
                                final boolean isWithCoupon = DBHelper.getInstance(getContext().getApplicationContext()).isCouponWithVip(orderId);
                                final DiscountHistoryEntity discountHistoryEntity = DBHelper.getInstance(getContext().getApplicationContext()).getDiscount(orderId);
                                if (discountHistoryEntity != null || isWithCoupon) {//当前已使用优惠券或会员卡优惠，
                                    final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                    dialog.setTitle("提示信息");
                                    dialog.setMessage("当前订单已使用其他优惠方式并且该优惠方式不允许和会员卡优惠同时使用，若要使用会员卡优惠，请先清除其他优惠方式！");
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "仅使用会员卡支付", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            unUseVip(vipNo, Integer.valueOf(vipType));
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后再来支付", new DialogInterface.OnClickListener() {
                                        @Subscribe(threadMode = ThreadMode.MAIN)
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            EventBus.getDefault().post(new SnackOpenTopRightEvent());
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    useVip(vipNo, Integer.valueOf(vipType));
                                }
                                break;
                        }
                    }
                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackOpenTopRightEvent());
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIncome.getText() == null || etIncome.getText().length() <= 0) {
                    CustomMethod.showMessage(getContext(), "请输入实收金额");
                } else if (etElectricNumber.getText() == null || etElectricNumber.getText().length() <= 0) {
                    CustomMethod.showMessage(getContext(), "请输入付款码");
                } else {
                    float payMoney = Float.valueOf(etIncome.getText().toString());
                    String authCode = etElectricNumber.getText().toString();
                    float receivable = Float.valueOf(tvReceivable.getText().toString());
                    if (receivable < payMoney) {
                        CustomMethod.showMessage(getContext(), "实收金额不允许大于应收金额，请确认实收金额");
                    } else if (payMoney <= 0) {
                        CustomMethod.showMessage(getContext(), "实收金额不允许小于或者等于0，请确认实收金额");
                    } else {
                        if (type == 0) {
                            //微信支付
                            WxPay(authCode, payMoney);
                        } else if (type == 1) {
                            //支付宝支付
                            alipay(authCode, payMoney);
                        } else if (type == 2) {
                            //会员卡支付
                            vipCardMode(authCode, payMoney);
                        }
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void useVip(String vipNo, int vipType) {
        DBHelper.getInstance(getContext().getApplicationContext()).insertVipDetail(orderId, vipNo, vipType, 0);
        EventBus.getDefault().post(new SnackUseVipEvent(orderId));
        syncVip(vipNo);
    }

    private void unUseVip(String vipNo, int vipType) {
        DBHelper.getInstance(getContext().getApplicationContext()).insertVipDetailUnuse(orderId, vipNo, vipType, 0);
        syncVip(vipNo);
    }

    public void syncVip(String vipNo) {
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        if (orderEntity != null && orderEntity.getIsUpload() == 1) {
            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(orderId, vipNo, 16);
        }
    }

    /**
     * 会员卡支付
     */
    private void vipCardMode(String authCode, final float payMoney) {
        showLoadingProgressBar("会员卡支付中...");
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        String data = "{\"partnerCode\":" + partnerCode + ",\"payCode\":" + authCode + ",\"amount\":" + (int) (payMoney * 100) + "}";
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        Log.d("###", "vipCardMode: " + data + "\n" + sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.VIP_QR_PAY), "VIP_QR_PAY", map, new VolleyInterface( VolleyInterface.listener, VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "会员卡支付结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        hideLoadingProgressBar();
                        String vipNo = null;
                        if (orderId != null) {
                            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
                            if (orderEntity != null) {
                                vipNo = orderEntity.getVipNo();
                            }
                        }
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "5", "会员卡", 5, payMoney, 0, vipNo);
                        EventBus.getDefault().post(new SnackElectricConfirmEvent());
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isVipOpenCashBox()) {
                            EventBus.getDefault().post(new SnackOpenCashBoxEvent());
                        }
                    } else {
                        String message = publicModule.getMessage() == null ? "会员卡支付失败，请重新支付" : publicModule.getMessage();
                        showPayResultMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showPayResultMessage("会员卡支付失败，请重新支付");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                showPayResultMessage("会员卡支付失败，请重新支付");
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void alipay(final String authCode, final float payMoney) {
        showLoadingProgressBar("支付宝支付中...");
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String parnterCode = getContext().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        String subject = spf.getString("partnerName", "聚炎收银-支付宝") + "消费";
        long ts = System.currentTimeMillis();
        ReqParamVo reqParamVo = new ReqParamVo();
        reqParamVo.setOrderId(orderId);
        reqParamVo.setAuthCode(authCode);
        reqParamVo.setPartnerCode(parnterCode);
        reqParamVo.setSubject(subject);
        reqParamVo.setTotalAmount("" + payMoney);
        reqParamVo.setType("1");
        String data = JSON.toJSONString(reqParamVo);
        String sign = MD5Util.getMD5String(parnterCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", parnterCode);
        map.put("ts", String.valueOf(ts));
        map.put("data", data);
        map.put("sign", sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.ELECTRIC_PAY), "ELECTRIC_PAY" + System.currentTimeMillis(), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "支付结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        SMZF003Vo payResultBean = JSON.parseObject(publicModule.getData(), SMZF003Vo.class);
                        if (payResultBean.getRespCode().equals("000000") || payResultBean.getRespCode().equals("000090")) {
                            hideLoadingProgressBar();
                            DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "3", "支付宝", 3, payMoney, 0, payResultBean.getReqMsgId());
                            EventBus.getDefault().post(new SnackElectricConfirmEvent());
                        } else if (payResultBean.getRespType().equals("E")) {
                            String message = payResultBean.getRespMsg() == null ? "支付失败，请重新支付" : payResultBean.getRespMsg();
                            showPayResultMessage(message);
                        } else {
                            checkPay(payResultBean.getReqMsgId(), 1, payMoney, System.currentTimeMillis());
                        }
                    } else {
                        String message = publicModule.getMessage() == null ? "支付失败，请重新支付" : publicModule.getMessage();
                        showPayResultMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getReqMsgId(1,System.currentTimeMillis(),payMoney,authCode);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                getReqMsgId(1,System.currentTimeMillis(),payMoney,authCode);
            }
        });
    }

    /**
     * 微信支付
     */
    private void WxPay(final String authCode, final float payMoney) {
        showLoadingProgressBar("微信支付中...");
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String parnterCode = getContext().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        String subject = spf.getString("partnerName", "聚炎收银-微信") + "消费";
        long ts = System.currentTimeMillis();
        ReqParamVo reqParamVo = new ReqParamVo();
        reqParamVo.setOrderId(orderId);
        reqParamVo.setAuthCode(authCode);
        reqParamVo.setPartnerCode(parnterCode);
        reqParamVo.setSubject(subject);
        reqParamVo.setTotalAmount("" + payMoney);
        reqParamVo.setType("0");
        String data = JSON.toJSONString(reqParamVo);
        String sign = MD5Util.getMD5String(parnterCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", parnterCode);
        map.put("ts", String.valueOf(ts));
        map.put("data", data);
        map.put("sign", sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.ELECTRIC_PAY), "ELECTRIC_PAY" + System.currentTimeMillis(), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "支付结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        SMZF003Vo payResultBean = JSON.parseObject(publicModule.getData(), SMZF003Vo.class);
                        if (payResultBean.getRespCode().equals("000000") || payResultBean.getRespCode().equals("000090")) {
                            DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "2", "微信", 2, payMoney, 0, payResultBean.getReqMsgId());
                            EventBus.getDefault().post(new SnackElectricConfirmEvent());
                            hideLoadingProgressBar();
                        } else if (payResultBean.getRespType().equals("E")) {
                            String message = payResultBean.getRespMsg() == null ? "支付失败，请重新支付" : payResultBean.getRespMsg();
                            showPayResultMessage(message);
                        } else if (payResultBean.getRespType().equals("R")) {
                            checkPay(payResultBean.getReqMsgId(), 0, payMoney, System.currentTimeMillis());
                        }
                    } else {
                        String message = publicModule.getMessage() == null ? "支付失败，请重新支付" : publicModule.getMessage();
                        showPayResultMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getReqMsgId(0,System.currentTimeMillis(),payMoney,authCode);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                getReqMsgId(0,System.currentTimeMillis(),payMoney,authCode);
            }
        });
    }

    public void getReqMsgId(final int type,final long startTime,final float payMoney, final String payCode) {
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("authCode", payCode);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.GET_PAY_REQ_MSG_ID), System.currentTimeMillis() + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "onSuccess: "+arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        checkPay(publicModule.getData(),type,payMoney,startTime);
                    } else {
                        hideLoadingAnim();
                        CustomMethod.showMessage(getContext(),"该笔订单出现异常情况，请联系技术客服");
                    }
                } catch (Exception e) {
                    hideLoadingAnim();
                    CustomMethod.showMessage(getContext(),"该笔订单出现异常情况，请联系技术客服");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"该笔订单出现异常情况，请联系技术客服");
            }
        });
    }

    public void checkPay(final String oriReqMsgId, final int type, final float payMoney, final long startPayTime) {
        Map<String, String> map = new HashMap<>();
        map.put("oriReqMsgId", oriReqMsgId);
        long ts = System.currentTimeMillis();
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.CHECK_PAY), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "查询结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        final SMZF006Vo payResultBean = JSON.parseObject(publicModule.getData(), SMZF006Vo.class);
                        if (payResultBean.getOriRespType().equals("S")) {
                            if (type == 1) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "3", "支付宝", 3, payMoney, 0, oriReqMsgId);
                            } else if (type == 0) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "2", "微信", 2, payMoney, 0, oriReqMsgId);
                            }
                            EventBus.getDefault().post(new SnackElectricConfirmEvent());
                            hideLoadingProgressBar();
                        } else if (payResultBean.getOriRespType().equals("E")) {
                            String message = payResultBean.getRespMsg() == null ? "支付失败，请重新支付" : payResultBean.getRespMsg();
                            showPayResultMessage(message);
                        } else if (payResultBean.getOriRespType().equals("R")) {
                            if (type == 0) {
                                if (System.currentTimeMillis() - startPayTime > 30 * 1000) {
                                    //微信支付
                                    DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, "2", "微信", 2, payMoney, 1, oriReqMsgId);
                                    String message = "微信支付超时,请根据顾客端支付结果对该笔支付记录进行处理！";
                                    showPayResultMessage(message);
                                    EventBus.getDefault().post(new SnackElectricConfirmEvent());
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            //显示dialog
                                            checkPay(oriReqMsgId, type, payMoney, startPayTime);
                                        }
                                    }, 3000);
                                }
                            } else if (type == 1) {
                                if (System.currentTimeMillis() - startPayTime > 70 * 1000) {
                                    //支付宝支付
                                    String message = "支付宝支付超时，请重新发起支付！";
                                    showPayResultMessage(message);
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            //显示dialog
                                            checkPay(oriReqMsgId, type, payMoney, startPayTime);
                                        }
                                    }, 3000);
                                }
                            }

                        }
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                //显示dialog
                                checkPay(oriReqMsgId, type, payMoney, startPayTime);
                            }
                        }, 3000);
                    }
                } catch (JSONException e) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //显示dialog
                            checkPay(oriReqMsgId, type, payMoney, startPayTime);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //显示dialog
                        checkPay(oriReqMsgId, type, payMoney, startPayTime);
                    }
                }, 3000);
            }
        });
    }

    //显示支付结果消息
    private void showPayResultMessage(String payResultMessage) {
        hideLoadingProgressBar();
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("支付提示信息");
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
