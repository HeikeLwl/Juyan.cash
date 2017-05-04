package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.RechCouponItemModel;
import jufeng.juyancash.bean.RechCouponModel;
import jufeng.juyancash.bean.SMZF003Vo;
import jufeng.juyancash.bean.VipCardVo;
import jufeng.juyancash.bean.VipRechargeVo;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.KeyboardUtil1;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/10/27.
 */

public class LFragmentVip extends BaseFragment {
    private EditText etCardNumber0, etRechargeMoney, etCode, etIncome, etChargeMoney;
    private TextView tvPresentMoney, tvActivity0, tvActivity1, tvActivity2;
    private AppCompatSpinner mAppCompatSpinner,mAppCompatSpinnerCardType;
    private LinearLayout mLayoutCode, mLayoutCash;
    private LinearLayout ticketLayout;
    private Button fabConfirm;
    private String ticketId;
    private KeyboardUtil1 mKeyboardUtil1;
    private EditText etCardNumber, etCardNumber1, etInitMoney,etIntegral;
    private Button fabConfirm1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ArrayList<RechCouponItemModel> rechCouponItemModels = (ArrayList<RechCouponItemModel>) msg.obj;
                    if (rechCouponItemModels.size() > 2) {
                        Collections.sort(rechCouponItemModels, new Comparator<RechCouponItemModel>() {
                            @Override
                            public int compare(RechCouponItemModel lhs, RechCouponItemModel rhs) {
                                return lhs.getMianzhi() > rhs.getMianzhi() ? 1 : -1;
                            }
                        });
                        tvActivity0.setTag(rechCouponItemModels.get(0));
                        tvActivity0.setText("充" + (rechCouponItemModels.get(0).getMianzhi() / 100) + "\n送" + rechCouponItemModels.get(0).getZengsong() / 100);
                        tvActivity1.setTag(rechCouponItemModels.get(1));
                        tvActivity1.setText("充" + rechCouponItemModels.get(1).getMianzhi() / 100 + "\n送" + rechCouponItemModels.get(1).getZengsong() / 100);
                        tvActivity2.setTag(rechCouponItemModels.get(2));
                        tvActivity2.setText("充" + rechCouponItemModels.get(2).getMianzhi() / 100 + "\n送" + rechCouponItemModels.get(2).getZengsong() / 100);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                    initData();
                    setListener();
                    setListener1();
                    break;
                case 1:
                    initTextTag();
                    ticketLayout.setVisibility(LinearLayout.GONE);
                    initData();
                    setListener();
                    setListener1();
                    break;
                case 2:
                    getTicket();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_vip, container, false);
        initView(mView);
        showLoadingAnim("获取充值活动...");
        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 1000);
        return mView;
    }

    private void initView(View view) {
        etCardNumber = (EditText) view.findViewById(R.id.et_card_number0);
        etCardNumber1 = (EditText) view.findViewById(R.id.et_card_number1);
        etInitMoney = (EditText) view.findViewById(R.id.et_init_money);
        etIntegral = (EditText) view.findViewById(R.id.et_init_integral);
        fabConfirm1 = (Button) view.findViewById(R.id.btn_confirm1);
        mAppCompatSpinnerCardType = (AppCompatSpinner) view.findViewById(R.id.appcompatspinner_cardtype);

        etCardNumber0 = (EditText) view.findViewById(R.id.et_card_number);
        etRechargeMoney = (EditText) view.findViewById(R.id.et_recharge_money);
        etIncome = (EditText) view.findViewById(R.id.et_income);
        etChargeMoney = (EditText) view.findViewById(R.id.et_charge);
        etCode = (EditText) view.findViewById(R.id.et_code);
        tvActivity0 = (TextView) view.findViewById(R.id.tv_activity_0);
        tvActivity1 = (TextView) view.findViewById(R.id.tv_activity_1);
        tvActivity2 = (TextView) view.findViewById(R.id.tv_activity_2);
        tvPresentMoney = (TextView) view.findViewById(R.id.tv_present_money);
        mAppCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.appcompatspinner);
        mLayoutCode = (LinearLayout) view.findViewById(R.id.layout_code);
        mLayoutCash = (LinearLayout) view.findViewById(R.id.layout_cash);
        ticketLayout = (LinearLayout) view.findViewById(R.id.layout);
        fabConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mKeyboardUtil1 = new KeyboardUtil1(view, getContext(), etCardNumber, 1, R.id.vip_keyboard);
    }

    public void setNewParam(){
        showLoadingAnim("获取充值活动...");
        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 1000);
    }

    private void initData() {
        CustomMethod.setMyInputType(etCardNumber, getActivity());
        CustomMethod.setMyInputType(etCardNumber1, getActivity());
        CustomMethod.setMyInputType(etInitMoney, getActivity());
        CustomMethod.setMyInputType(etIntegral,getActivity());
        CustomMethod.setMyInputType(etCardNumber0, getActivity());
        CustomMethod.setMyInputType(etRechargeMoney, getActivity());
        CustomMethod.setMyInputType(etIncome, getActivity());
        CustomMethod.setMyInputType(etCode, getActivity());
        ticketId = null;

        etCardNumber.requestFocus();
        mKeyboardUtil1.showKeyboard();
    }

    private void setListener() {
        etCardNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                if (result.length() > 5) {
                    String vipNo = result.substring(0, 5);
                    etCardNumber1.setText(vipNo);
                    etInitMoney.requestFocus();
                    mKeyboardUtil1.setEditText(etInitMoney);
                }
            }
        });
        fabConfirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCardNumber.getText().toString().isEmpty() || etCardNumber1.getText().toString().isEmpty() || etInitMoney.getText().toString().isEmpty()) {
                    CustomMethod.showMessage(getContext(), "请填写完整信息");
                } else {
                    authoBindVip(10);
                }
            }
        });

        etCardNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCardNumber.requestFocus();
                mKeyboardUtil1.setEditText(etCardNumber);
                return false;
            }
        });
        etCardNumber1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCardNumber1.requestFocus();
                mKeyboardUtil1.setEditText(etCardNumber1);
                return false;
            }
        });
        etInitMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etInitMoney.requestFocus();
                mKeyboardUtil1.setEditText(etInitMoney);
                return false;
            }
        });

        etIntegral.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etIntegral.requestFocus();
                mKeyboardUtil1.setEditText(etIntegral);
                return false;
            }
        });
    }

    //权限验证后进行反结账
    private void authoBindVip(int type){
        EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
        if (employeeEntity.getIsBindVip() == 1) {
            //当前员工有绑定老会员的权限
            bindVip();
        } else {
            //当前员工没有绑定老会员的权限
            final CustomeAuthorityDialog authorityDialog = new CustomeAuthorityDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            authorityDialog.setArguments(bundle);
            authorityDialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                @Override
                public void onAuthorityCancle() {
                    authorityDialog.dismiss();
                }

                @Override
                public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                    if (employeeEntity != null && employeeEntity.getIsBindVip() == 1) {
                        authorityDialog.dismiss();
                        bindVip();
                    } else {
                        Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
            authorityDialog.show(getFragmentManager(), "");
        }
    }

    private void bindVip() {
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        showLoadingAnim("绑定会员卡...");
        String cardNumber = etCardNumber.getText().toString();
        String cardNumber1 = etCardNumber1.getText().toString();
        String initMoney = etInitMoney.getText().toString();
        String initIntegral = etIntegral.getText().toString();
        int cardType = mAppCompatSpinnerCardType.getSelectedItemPosition();
        VipCardVo vipCardVo = new VipCardVo();
        vipCardVo.setInitAmount(Integer.parseInt(jufeng.juyancash.util.AmountUtils.changeY2F(initMoney)));
        vipCardVo.setOldVipNo(cardNumber);
        vipCardVo.setVipNo(cardNumber1);
        vipCardVo.setPartnerCode(partnerCode);
        vipCardVo.setCardType(cardType);
        vipCardVo.setInitIntegral(Integer.parseInt(jufeng.juyancash.util.AmountUtils.changeY2F(initIntegral)));
        long ts = System.currentTimeMillis();
        String data = JSON.toJSONString(vipCardVo);
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("ts", String.valueOf(ts));
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("sign", sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.BIND_VIP), "BIND_VIP", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "onSuccess: " + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        hideLoadingAnim();
                        CustomMethod.showMessage(getContext(), "会员卡绑定成功！");
                        resetBindView();
                    } else {
                        hideLoadingAnim();
                        CustomMethod.showMessage(getContext(), publicModule.getMessage());
                    }
                } catch (JSONException e) {
                    hideLoadingAnim();
                    CustomMethod.showMessage(getActivity().getApplicationContext(), "会员卡绑定失败，请稍后重试");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getActivity().getApplicationContext(), "会员卡绑定失败，请稍后重试");
            }
        });
    }

    private void resetBindView() {
        etCardNumber.setText("");
        etCardNumber1.setText("");
        etInitMoney.setText("");
        etIntegral.setText("");
    }

    private void setListener1() {
        etCardNumber0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                if (result.length() > 5) {
                    String vipNo = result.substring(0, 5);
                    etCardNumber0.setText(vipNo);
                    etRechargeMoney.requestFocus();
                    mKeyboardUtil1.setEditText(etRechargeMoney);
                }
            }
        });
        etRechargeMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etRechargeMoney.setText(s);
                        etRechargeMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etRechargeMoney.setText(s);
                    etRechargeMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etRechargeMoney.setText(s.subSequence(0, 1));
                        etRechargeMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (etRechargeMoney.getText().toString().length() > 0) {
                        if (tvActivity2.getTag() != null && Double.valueOf(s.toString()) >= ((RechCouponItemModel) tvActivity2.getTag()).getMianzhi() / 100) {
                            setCheckActivity(tvActivity2);
                            tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity2.getTag()).getZengsong() / 100));
                        } else if (tvActivity1.getTag() != null && Double.valueOf(s.toString()) >= ((RechCouponItemModel) tvActivity1.getTag()).getMianzhi() / 100) {
                            setCheckActivity(tvActivity1);
                            tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity1.getTag()).getZengsong() / 100));
                        } else if (tvActivity0.getTag() != null && Double.valueOf(s.toString()) >= ((RechCouponItemModel) tvActivity0.getTag()).getMianzhi() / 100) {
                            setCheckActivity(tvActivity0);
                            tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity0.getTag()).getZengsong() / 100));
                        } else {
                            setCheckActivity(null);
                            tvPresentMoney.setText("0");
                        }
                    } else {
                        setCheckActivity(null);
                        tvPresentMoney.setText("0");
                    }
                    etIncome.setText(s);
                }catch (Exception e){
                }
            }
        });

        etIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    try {
                        float income = Float.valueOf(s.toString());
                        if (etRechargeMoney.getText().toString().length() > 0) {
                            float rechargeMoney = Float.valueOf(etRechargeMoney.getText().toString());
                            if (rechargeMoney > income) {
                                etChargeMoney.setText(String.valueOf(0.0));
                            } else {
                                etChargeMoney.setText(String.valueOf(CustomMethod.decimalFloat(income - rechargeMoney)));
                            }
                        } else {
                            etChargeMoney.setText(String.valueOf(income));
                        }
                    }catch (Exception e){

                    }
                } else {
                    etChargeMoney.setText("");
                }
            }
        });

        tvActivity0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckActivity(tvActivity0);
                etRechargeMoney.requestFocus();
                mKeyboardUtil1.setEditText(etRechargeMoney);
                if (tvActivity0.getTag() != null) {
                    etRechargeMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity0.getTag()).getMianzhi() / 100));
                    tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity0.getTag()).getZengsong() / 100));
                }
            }
        });

        tvActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckActivity(tvActivity1);
                etRechargeMoney.requestFocus();
                mKeyboardUtil1.setEditText(etRechargeMoney);
                if (tvActivity1.getTag() != null) {
                    etRechargeMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity1.getTag()).getMianzhi() / 100));
                    tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity1.getTag()).getZengsong() / 100));
                }
            }
        });

        tvActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckActivity(tvActivity2);
                etRechargeMoney.requestFocus();
                mKeyboardUtil1.setEditText(etRechargeMoney);
                if (tvActivity2.getTag() != null) {
                    etRechargeMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity2.getTag()).getMianzhi() / 100));
                    tvPresentMoney.setText(String.valueOf(((RechCouponItemModel) tvActivity2.getTag()).getZengsong() / 100));
                }
            }
        });

        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://现金
                        mLayoutCash.setVisibility(LinearLayout.VISIBLE);
                        mLayoutCode.setVisibility(LinearLayout.GONE);
                        etCode.setText("");
                        break;
                    case 1://银行卡
                        mLayoutCode.setVisibility(LinearLayout.GONE);
                        mLayoutCash.setVisibility(LinearLayout.GONE);
                        etCode.setText("");
                        break;
                    case 2://微信
                        mLayoutCode.setVisibility(LinearLayout.VISIBLE);
                        mLayoutCash.setVisibility(LinearLayout.GONE);
                        etCode.setText("");
                        break;
                    case 3:
                        mLayoutCash.setVisibility(LinearLayout.GONE);
                        mLayoutCode.setVisibility(LinearLayout.VISIBLE);
                        etCode.setText("");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etCardNumber0.getText().toString().isEmpty() && !etRechargeMoney.getText().toString().isEmpty()) {
                    beginRecharge1();
                }
            }
        });

        etCardNumber0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCardNumber0.requestFocus();
                mKeyboardUtil1.setEditText(etCardNumber0);
                return false;
            }
        });
        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etIncome.requestFocus();
                mKeyboardUtil1.setEditText(etIncome);
                return false;
            }
        });
        etRechargeMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etRechargeMoney.requestFocus();
                mKeyboardUtil1.setEditText(etRechargeMoney);
                return false;
            }
        });
        etCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etCode.requestFocus();
                mKeyboardUtil1.setEditText(etCode);
                return false;
            }
        });
    }

    private void setCheckActivity(TextView textView) {
        tvActivity0.setBackgroundResource(R.drawable.my_activity_default);
        tvActivity1.setBackgroundResource(R.drawable.my_activity_default);
        tvActivity2.setBackgroundResource(R.drawable.my_activity_default);
        tvActivity0.setTextColor(getActivity().getResources().getColor(R.color.dark));
        tvActivity1.setTextColor(getActivity().getResources().getColor(R.color.dark));
        tvActivity2.setTextColor(getActivity().getResources().getColor(R.color.dark));
        if (textView != null) {
            textView.setBackgroundResource(R.drawable.my_activity_select);
            textView.setTextColor(getActivity().getResources().getColor(R.color.white));
            if (textView.getTag() != null) {
                ticketId = ((RechCouponItemModel) textView.getTag()).getId();
            } else {
                ticketId = null;
            }
        }
    }

    private void beginRecharge1() {
        try {
            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
            String partnerCode = spf.getString("partnerCode", null);
            String partnerName = spf.getString("partnerName", null);
            String payAmount = etRechargeMoney.getText().toString();
            String voucher = tvPresentMoney.getText().toString();
            VipRechargeVo vipRecModel = new VipRechargeVo();
            vipRecModel.setPartnerCode(partnerCode);
            vipRecModel.setPayAmount(payAmount);
            vipRecModel.setVoucher(voucher);
            vipRecModel.setPayTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            vipRecModel.setVipNo(etCardNumber0.getText().toString());
            vipRecModel.setRechCouponId(ticketId);
            switch (mAppCompatSpinner.getSelectedItemPosition()) {
                case 0://现金
                    String incomeMoney = etIncome.getText().toString();
                    if (Double.parseDouble(AmountUtils.multiply(incomeMoney, "1.0")) < Double.parseDouble(AmountUtils.multiply(payAmount, "1.0"))) {
                        CustomMethod.showMessage(getContext(), "实收金额不能小于充值金额，请确认！");
                    } else {
                        vipRecModel.setRechargeType(2);
                        showLoadingAnim("现金充值中...");
                        vipRecharge1(vipRecModel);
                    }
                    break;
                case 1://银行卡
                    vipRecModel.setRechargeType(3);
                    showLoadingAnim("银行卡充值中...");
                    vipRecharge1(vipRecModel);
                    break;
                case 2://微信
                    vipRecModel.setRechargeType(0);
                    vipRecModel.setAuthCode(etCode.getText().toString());
                    vipRecModel.setSubject(partnerName + "-会员卡充值");
                    showLoadingAnim("微信充值中...");
                    vipRecharge1(vipRecModel);
                    break;
                case 3://支付宝
                    vipRecModel.setRechargeType(1);
                    vipRecModel.setAuthCode(etCode.getText().toString());
                    vipRecModel.setSubject(partnerName + "-会员卡充值");
                    showLoadingAnim("支付宝充值中...");
                    vipRecharge1(vipRecModel);
                    break;
            }
        }catch (Exception e){

        }
    }

    private void vipRecharge1(final VipRechargeVo vipRecModel) {
        long ts = System.currentTimeMillis();
        String data = JSON.toJSONString(vipRecModel);
        String sign = MD5Util.getMD5String(vipRecModel.getPartnerCode() + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("ts", String.valueOf(ts));
        map.put("data", data);
        map.put("sign", sign);
        map.put("partnerCode", vipRecModel.getPartnerCode());
        VolleyRequest.RequestPost(getActivity(), getContext().getResources().getString(R.string.VIP_RECHARGE), "VIP_RECHARGE", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "会员卡充值结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        switch (vipRecModel.getRechargeType()) {
                            case 0:
                            case 1:
                                SMZF003Vo payResultBean = JSON.parseObject(publicModule.getData(), SMZF003Vo.class);
                                if (payResultBean.getRespType().equals("S")) {
                                    showPayResultMessage("会员充值成功");
                                    reset();
                                } else if (payResultBean.getRespType().equals("E")) {
                                    String message = publicModule.getMessage() == null ? "充值失败，请重新支付" : payResultBean.getRespMsg();
                                    showPayResultMessage(message);
                                } else if (payResultBean.getRespType().equals("R")) {
                                    checkPay(vipRecModel.getPartnerCode(), payResultBean.getReqMsgId(), System.currentTimeMillis());
                                }
                                break;
                            case 2:
                            case 3:
                                showPayResultMessage("会员充值成功");
                                reset();
                                break;
                        }

                    } else {
                        String msg = publicModule.getMessage() == null ? "会员充值失败" : publicModule.getMessage();
                        showPayResultMessage(msg);
                    }
                } catch (Exception e) {
                    showPayResultMessage("会员充值失败");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                showPayResultMessage("网络异常，请稍后重试");
            }
        });
    }

    public void checkPay(final String partnerCode, final String outTradeNo, final long startPayTime) {
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + outTradeNo + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("outTradeNo", outTradeNo);
        map.put("ts", String.valueOf(ts));
        map.put("partnerCode", partnerCode);
        map.put("sign", sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.VIP_RECHARGE_CHECK), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "查询结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    switch (publicModule.getCode()) {
                        case 0:
                            int status = Integer.parseInt(publicModule.getData());
                            switch (status) {
                                case 0:
                                    if (System.currentTimeMillis() - startPayTime > 80 * 1000) {
                                        String message = "充值超时，请重新发起支付！";
                                        showPayResultMessage(message);
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                //显示dialog
                                                checkPay(partnerCode, outTradeNo, startPayTime);
                                            }
                                        }, 3000);
                                    }
                                    break;
                                case 1:
                                    showPayResultMessage("会员充值成功");
                                    reset();
                                    break;
                                case 2:
                                    showPayResultMessage("充值失败，请重新尝试充值");
                                    break;
                            }
                            break;
                        case -1:
                            showPayResultMessage("充值失败，请重新尝试充值");
                            break;
                        default:
                            showPayResultMessage("充值失败，请重新尝试充值");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showPayResultMessage("充值失败，请重新尝试充值");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                showPayResultMessage("充值失败，请重新尝试充值");
            }
        });
    }


    //显示支付结果消息
    private void showPayResultMessage(String payResultMessage) {
        hideLoadingAnim();
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

    private void reset() {
        etCardNumber0.setText("");
        etIncome.setText("");
        etRechargeMoney.setText("");
        etCode.setText("");
        etChargeMoney.setText("");
        setCheckActivity(null);
        ticketId = null;
        mAppCompatSpinner.setSelection(0);
        mLayoutCash.setVisibility(LinearLayout.VISIBLE);
        mLayoutCode.setVisibility(LinearLayout.GONE);
        tvPresentMoney.setText("0");
        initData();
        setListener();
        setListener1();
    }

    private void initTextTag() {
        tvActivity0.setTag(null);
        tvActivity1.setTag(null);
        tvActivity2.setTag(null);
    }

    private void getTicket() {
        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        Map<String, String> map = new HashMap<>();
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.GET_RECHARGE_TICKET) + "?partnerCode=" + partnerCode, "GET_RECHARGE_TICKET", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "onSuccess: " + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        RechCouponModel rechCouponModel = JSON.parseObject(publicModule.getData(), RechCouponModel.class);
                        if (rechCouponModel != null) {
                            ticketLayout.setVisibility(LinearLayout.VISIBLE);
                            ArrayList<RechCouponItemModel> rechCouponItemModels = new ArrayList<>(rechCouponModel.getRechCouponItem());
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = rechCouponItemModels;
                            mHandler.sendMessage(msg);
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                        hideLoadingAnim();
                    } else {
                        mHandler.sendEmptyMessage(1);
                        String msg = publicModule.getMessage() == null ? "获取充值活动失败" : publicModule.getMessage();
                        showPayResultMessage(msg);
                    }
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(1);
                    showPayResultMessage("获取充值活动失败，请检测网络");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                mHandler.sendEmptyMessage(1);
                showPayResultMessage("获取充值活动失败，请检测网络");
            }
        });
    }
}