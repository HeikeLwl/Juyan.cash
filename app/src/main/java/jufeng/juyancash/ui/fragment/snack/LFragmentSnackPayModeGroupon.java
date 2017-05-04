package jufeng.juyancash.ui.fragment.snack;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.MeituanConsumeBean;
import jufeng.juyancash.bean.MeituanPrepareBean;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.eventbus.SnackGrouponConfirmEvent;
import jufeng.juyancash.eventbus.SnackOpenCashBoxEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackSelectGrouponEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentSnackPayModeGroupon extends BaseFragment implements View.OnClickListener {
    private TextView tvName;
    private TextView tvReceivable;
    private TextView tvIncome;
    private TextView tvGrouponTaocan;
    private TextView tvCancle, tvConfirm;
    private RelativeLayout mLayoutCount, mLayoutCouponCode;
    private EditText mEtCount, mEtCouponCode;
    private String orderId;
    private GrouponTaocanEntity mGrouponTaocanEntity;
    private PayModeEntity mPayModeEntity;
    private GrouponEntity grouponEntity;
    private CustomLoadingProgress mLoadingProgress;
    private boolean isStoreIn = true;//商家是否入驻

    public static LFragmentSnackPayModeGroupon newInstance(String orderId, PayModeEntity payModeEntity, GrouponTaocanEntity grouponTaocan, GrouponEntity groupon) {
        LFragmentSnackPayModeGroupon fragment = new LFragmentSnackPayModeGroupon();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("payment", payModeEntity);
        bundle.putParcelable("grouponTaocan", grouponTaocan);
        bundle.putParcelable("groupon", groupon);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_paymode_groupon, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvReceivable = (TextView) view.findViewById(R.id.tv_receivable_money);
        tvIncome = (TextView) view.findViewById(R.id.tv_income);
        tvGrouponTaocan = (TextView) view.findViewById(R.id.tv_groupon_taocan);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mLayoutCount = (RelativeLayout) view.findViewById(R.id.layout_count);
        mLayoutCouponCode = (RelativeLayout) view.findViewById(R.id.layout_coupon_code);
        mEtCount = (EditText) view.findViewById(R.id.et_count);
        mEtCouponCode = (EditText) view.findViewById(R.id.et_coupon_code);

        orderId = getArguments().getString("orderId");
        mPayModeEntity = getArguments().getParcelable("payment");
        mGrouponTaocanEntity = getArguments().getParcelable("grouponTaocan");
        grouponEntity = getArguments().getParcelable("groupon");
    }

    private void initData() {
        tvConfirm.setTag(null);
        if (grouponEntity != null && grouponEntity.getGrouponCode() != null && grouponEntity.getGrouponCode().equals("1")) {
            isStoreIn = true;
        } else {
            isStoreIn = false;
        }
        /**
         * 判断当前的团购网站是否有入驻，如果有入驻，数量和券码显示，选择套餐按钮隐藏，
         */
        if (isStoreIn) {
            //商家已经入驻
            mLayoutCount.setVisibility(RelativeLayout.VISIBLE);
            mLayoutCouponCode.setVisibility(RelativeLayout.VISIBLE);
            tvGrouponTaocan.setVisibility(TextView.GONE);
            tvConfirm.setText("准备验券");

            mEtCouponCode.setText("");
            mEtCouponCode.setInputType(InputType.TYPE_NULL);
            mEtCount.setText("1");
            CustomMethod.setMyInputType1(mEtCount, getActivity());
            CustomMethod.setMyInputType2(mEtCouponCode, getActivity());
            setLayoutBackground(1);
        } else {
            //商家未入驻
            mLayoutCount.setVisibility(RelativeLayout.GONE);
            mLayoutCouponCode.setVisibility(RelativeLayout.GONE);
            tvGrouponTaocan.setVisibility(TextView.VISIBLE);
            tvConfirm.setText("确认");
        }

        if (mPayModeEntity == null) {
            mPayModeEntity = new PayModeEntity();
            mPayModeEntity.setPayModeId(UUID.randomUUID().toString());
            mPayModeEntity.setPaymentId(grouponEntity.getGrouponId());
            mPayModeEntity.setPaymentName(grouponEntity.getGrouponName());
            mPayModeEntity.setOrderId(orderId);
            try {
                mPayModeEntity.setPayMoney(Float.parseFloat("" + AmountUtils.changeF2Y("" + DBHelper.getInstance(getActivity()).getReceivableMoneyByOrderId(orderId)).doubleValue()));
            } catch (Exception e) {
                e.printStackTrace();
                mPayModeEntity.setPayMoney(((float) DBHelper.getInstance(getActivity()).getReceivableMoneyByOrderId(orderId)) / 100);
            }
        }
        if (mGrouponTaocanEntity != null) {
            mPayModeEntity.setPayMoney(mGrouponTaocanEntity.getGrouponTaocanPrice());
            mPayModeEntity.setPayBalance(mGrouponTaocanEntity.getGrouponTaocanBalancePrice());
            tvGrouponTaocan.setText(mGrouponTaocanEntity.getGrouponTaocanName());
        } else {
            tvGrouponTaocan.setText("选择套餐");
        }
        tvName.setText(mPayModeEntity.getPaymentName());
        tvReceivable.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity()).getReceivableMoneyByOrderId(orderId)));
        tvIncome.setText(String.valueOf(mPayModeEntity.getPayMoney()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void setLayoutBackground(int tag) {
        if (tag == 1) {
            EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(mEtCount));
            mLayoutCount.setBackgroundResource(R.drawable.edittext_focus_background);
            mLayoutCouponCode.setBackgroundResource(R.drawable.edittext_background);
            mEtCount.requestFocus();
        } else {
            EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(mEtCouponCode));
            mLayoutCount.setBackgroundResource(R.drawable.edittext_background);
            mLayoutCouponCode.setBackgroundResource(R.drawable.edittext_focus_background);
            mEtCouponCode.requestFocus();
        }
    }

    private void setListener() {
        tvGrouponTaocan.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        mLayoutCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayoutBackground(1);
            }
        });
        mEtCount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setLayoutBackground(1);
                return false;
            }
        });
        mLayoutCouponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayoutBackground(0);
            }
        });
        mEtCouponCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setLayoutBackground(0);
                return false;
            }
        });

        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    MeituanPrepareBean meituanBean = (MeituanPrepareBean) tvConfirm.getTag();
                    if (meituanBean != null) {
                        if (meituanBean.getDealMenu() == null) {
                            tvIncome.setText(AmountUtils.multiply("" + meituanBean.getDealValue(), s.toString()));
                        } else {
                            tvIncome.setText(AmountUtils.multiply("" + meituanBean.getCouponBuyPrice(), s.toString()));
                        }
                    } else {
                        tvIncome.setText("");
                    }
                } else {
                    tvIncome.setText("");
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_groupon_taocan:
                EventBus.getDefault().post(new SnackSelectGrouponEvent(grouponEntity, mGrouponTaocanEntity, mPayModeEntity));
                break;
            case R.id.tv_cancle:
                EventBus.getDefault().post(new SnackOpenTopRightEvent());
                break;
            case R.id.tv_confirm:
                if (isStoreIn) {
                    if (tvConfirm.getTag() == null) {
                        prepareMeituan();
                    } else {
                        confirmMeituan((MeituanPrepareBean) tvConfirm.getTag());
                    }
                } else {
                    confirmNotStoreInGroupon();
                }
                break;
        }
    }

    /**
     * 美团准备验券
     */
    private void prepareMeituan() {
        showLoadingProgressBar("美团验券准备中...");
        if (mEtCouponCode.getText() != null && !mEtCouponCode.getText().toString().isEmpty()) {
            String couponCode = mEtCouponCode.getText().toString();
            long ts = System.currentTimeMillis();
            String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
            String sign = MD5Util.getMD5String(partnerCode + couponCode + ts + getContext().getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("partnerCode", partnerCode);
            map.put("ts", String.valueOf(ts));
            map.put("couponCode", couponCode);
            map.put("sign", sign);
            VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_PREPARE), "MEITUAN_PREPARE", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "团购验券准备：" + arg0);
                    hideLoadingProgressBar();
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            MeituanPrepareBean meituanBean = JSON.parseObject(publicModule.getData(), MeituanPrepareBean.class);
                            if (meituanBean != null) {
                                if (meituanBean.getResult() == 0) {
                                    //验券准备成功
                                    if (meituanBean.getDealMenu() == null) {
                                        //代金券
                                        tvIncome.setText(String.valueOf(meituanBean.getDealValue()));
                                    } else {
                                        //团购券
                                        tvIncome.setText(String.valueOf(meituanBean.getCouponBuyPrice()));
                                    }
                                    tvConfirm.setTag(meituanBean);
                                    tvConfirm.setText("开始验券");
                                    CustomMethod.showMessage(getContext(), "美团验券准备成功，可以开始验券！");
                                } else {
                                    //验券准备失败
                                    String message = meituanBean.getMessage() == null ? "验券准备失败，请重新尝试" : meituanBean.getMessage();
                                    CustomMethod.showMessage(getContext(), message);
                                }
                            } else {
                                //验券准备失败
                                CustomMethod.showMessage(getContext(), "验券准备失败，请重新尝试");
                            }
                        } else {
                            CustomMethod.showMessage(getContext(), "验券准备失败，请重新尝试");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CustomMethod.showMessage(getContext(), "验券准备失败，请重新尝试");
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    arg0.printStackTrace();
                    hideLoadingProgressBar();
                    CustomMethod.showMessage(getContext(), "验券准备失败，请重新尝试");
                }
            });
        } else {
            hideLoadingProgressBar();
            CustomMethod.showMessage(getContext(), "提示信息", "请扫描或输入美团团购券二维码");
        }
    }

    /**
     * 美团确认验券
     */
    private void confirmMeituan(final MeituanPrepareBean meituanBean) {
        showLoadingProgressBar("美团执行验券中...");
        String count = mEtCount.getText().toString();
        if (count != null && !count.isEmpty()) {
            final String couponCode = mEtCouponCode.getText().toString();
            if (couponCode != null && !couponCode.isEmpty()) {
                if (couponCode.equals(meituanBean.getCouponCode())) {
                    if (meituanBean.getCount() >= Integer.valueOf(count) && meituanBean.getMinConsume() <= Integer.valueOf(count)) {
                        mPayModeEntity.setPayMoney(Float.valueOf(tvIncome.getText().toString()));
                        long ts = System.currentTimeMillis();
                        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
                        String eOrderId = partnerCode + ts;
                        String data = "{\"couponCode\":\"" + couponCode + "\",\"count\":\"" + count + "\",\"eOrderId\":\"" + eOrderId + "\"}";
                        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
                        Map<String, String> map = new HashMap<>();
                        map.put("ts", String.valueOf(ts));
                        map.put("partnerCode", partnerCode);
                        map.put("data", data);
                        map.put("sign", sign);
                        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_CONSUME), "MEITUAN_CONSUME", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                            @Subscribe(threadMode = ThreadMode.MAIN)
                            @Override
                            public void onSuccess(String arg0) {
                                Log.d("###", "美团执行验券结果：" + arg0);
                                hideLoadingProgressBar();
                                try {
                                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                                    if (publicModule.getCode() == 0 && publicModule.getData() != null) {
                                        MeituanConsumeBean meituanConsumeBean = JSON.parseObject(publicModule.getData(), MeituanConsumeBean.class);
                                        if (meituanConsumeBean.getResult() == 0) {
                                            //美团验券完成
                                            if (mPayModeEntity != null) {
                                                double needPayMoney = jufeng.juyancash.util.AmountUtils.multiply1(tvReceivable.getText().toString(), "1.0");
                                                if (mPayModeEntity.getPayMoney() != null && mPayModeEntity.getPayMoney() > needPayMoney) {
                                                    AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                                    dialog.setTitle("团购券使用提示信息");
                                                    dialog.setMessage("【" + mPayModeEntity.getPaymentName() + "】团购券面值大于应收金额，系统将在账单中自动添加【团购券凑整项】，是否继续使用该团购券？");
                                                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "不使用", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续使用", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mPayModeEntity.setElectricOrderSerial(couponCode);
                                                            mPayModeEntity.setPayTime(System.currentTimeMillis());
                                                            mPayModeEntity.setPaymentType(4);
                                                            mPayModeEntity.setPayBalance(mPayModeEntity.getPayMoney());
                                                            mPayModeEntity.setIsJoinOrderPay(0);
                                                            DBHelper.getInstance(getActivity()).insertPayMode(mPayModeEntity);
                                                            OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
                                                            if (orderEntity != null) {
                                                                DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity, 1);
                                                            }
                                                            EventBus.getDefault().post(new SnackGrouponConfirmEvent(orderId));
                                                            if (DBHelper.getInstance(getContext().getApplicationContext()).isGrouponOpenCashBox()) {
                                                                EventBus.getDefault().post(new SnackOpenCashBoxEvent());
                                                            }
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    dialog.show();
                                                } else {
                                                    mPayModeEntity.setElectricOrderSerial(couponCode);
                                                    mPayModeEntity.setPayTime(System.currentTimeMillis());
                                                    mPayModeEntity.setPaymentType(4);
                                                    mPayModeEntity.setPayBalance(mPayModeEntity.getPayMoney());
                                                    mPayModeEntity.setIsJoinOrderPay(0);
                                                    DBHelper.getInstance(getActivity()).insertPayMode(mPayModeEntity);
                                                    OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
                                                    if (orderEntity != null) {
                                                        DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity, 1);
                                                    }
                                                    EventBus.getDefault().post(new SnackGrouponConfirmEvent(orderId));
                                                }
                                            }
                                        } else {
                                            String message = meituanConsumeBean.getMessage() == null ? "美团验券失败，请重新尝试" : meituanConsumeBean.getMessage();
                                            CustomMethod.showMessage(getContext(), message);
                                        }
                                    } else {
                                        CustomMethod.showMessage(getContext(), "美团验券失败，请重新尝试");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    CustomMethod.showMessage(getContext(), "美团验券失败，请重新尝试");
                                }
                            }

                            @Override
                            public void onError(VolleyError arg0) {
                                arg0.printStackTrace();
                                hideLoadingProgressBar();
                                CustomMethod.showMessage(getContext(), "美团验券失败，请重新尝试");
                            }
                        });
                    } else {
                        hideLoadingProgressBar();
                        CustomMethod.showMessage(getContext(), "验券数量最多为" + meituanBean.getCount() + "张，最少为" + meituanBean.getMinConsume() + "张，请核对数量后再进行验券");
                    }
                } else {
                    hideLoadingProgressBar();
                    CustomMethod.showMessage(getContext(), "两次输入的团购券券码不一致，请修改后再进行验券");
                }
            } else {
                hideLoadingProgressBar();
                CustomMethod.showMessage(getContext(), "请扫描或输入团购券券码");
            }
        } else {
            hideLoadingProgressBar();
            CustomMethod.showMessage(getContext(), "请输入验券数量");
        }
    }

    /**
     * 未入驻的团购结算方式
     */
    private void confirmNotStoreInGroupon() {
        double needPayMoney = jufeng.juyancash.util.AmountUtils.multiply1(tvReceivable.getText().toString(), "1.0");
        if (mGrouponTaocanEntity == null) {
            CustomMethod.showMessage(getContext(), "请选择团购套餐");
        } else if (mPayModeEntity.getPayMoney() <= 0) {
            CustomMethod.showMessage(getContext(), "请确认团购套餐");
        } else if (mPayModeEntity.getPayMoney() > needPayMoney) {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setTitle("团购券使用提示信息");
            dialog.setMessage("【" + mPayModeEntity.getPaymentName() + "】团购券面值大于应收金额，系统将在账单中自动添加【团购券凑整项】，是否继续使用该团购券？");
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "不使用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续使用", new DialogInterface.OnClickListener() {
                @Subscribe(threadMode = ThreadMode.MAIN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPayModeEntity.setPayTime(System.currentTimeMillis());
                    mPayModeEntity.setPaymentType(4);
                    mPayModeEntity.setPayBalance(mGrouponTaocanEntity.getGrouponTaocanBalancePrice());
                    mPayModeEntity.setIsJoinOrderPay(0);
                    DBHelper.getInstance(getActivity()).insertPayMode(mPayModeEntity);
                    OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
                    if (orderEntity != null) {
                        DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity, 1);
                    }
                    EventBus.getDefault().post(new SnackGrouponConfirmEvent(orderId));
                    if (DBHelper.getInstance(getContext().getApplicationContext()).isGrouponOpenCashBox()) {
                        EventBus.getDefault().post(new SnackOpenCashBoxEvent());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            mPayModeEntity.setPayTime(System.currentTimeMillis());
            mPayModeEntity.setPaymentType(4);
            mPayModeEntity.setPayBalance(mGrouponTaocanEntity.getGrouponTaocanBalancePrice());
            mPayModeEntity.setIsJoinOrderPay(0);
            DBHelper.getInstance(getActivity()).insertPayMode(mPayModeEntity);
            OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
            if (orderEntity != null) {
                DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity, 1);
            }
            EventBus.getDefault().post(new SnackGrouponConfirmEvent(orderId));
        }
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
