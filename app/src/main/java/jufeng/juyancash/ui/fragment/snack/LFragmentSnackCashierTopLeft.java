package jufeng.juyancash.ui.fragment.snack;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import jufeng.juyancash.adapter.CashierHistoryAdapter;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.SMZF006Vo;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.eventbus.CouponDialogCloseEvent;
import jufeng.juyancash.eventbus.SnackCashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackOrderMoneyChangedEvent;
import jufeng.juyancash.ui.customview.CustomeCouponDialog;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class LFragmentSnackCashierTopLeft extends BaseFragment {
    private TextView tvTotle;
    private TextView tvBillMoney;
    private TextView tvReceivebal;
    private TextView tvSpend, tvLabel;
    private RecyclerView mRecyclerView;
    private TextView tvPayMoney;
    private TextView tvCoupon;
    private CustomeCouponDialog couponDialog;
    private CashierHistoryAdapter mAdapter;
    private String orderId;

    public static LFragmentSnackCashierTopLeft newInstance(String orderId){
        LFragmentSnackCashierTopLeft fragment = new LFragmentSnackCashierTopLeft();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAdapter.updateData(orderId, false);
                    refreshData();
                    break;
                case 1:
                    try {
                        String orderId1 = (String) msg.obj;
                        if (orderId1 != null && orderId != null && orderId1.equals(orderId)) {
                            //该笔订单有支付
                            mAdapter.updateData(orderId, false);
                            refreshData();
                        }
                    }catch (Exception e){

                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_cashier_topleft, container, false);
        initView(mView);
        setAdapter();
        refreshData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvTotle = (TextView) view.findViewById(R.id.tv_total);
        tvBillMoney = (TextView) view.findViewById(R.id.tv_bill_money);
        tvReceivebal = (TextView) view.findViewById(R.id.tv_receivable_money);
        tvSpend = (TextView) view.findViewById(R.id.tv_spend);
        tvLabel = (TextView) view.findViewById(R.id.tv_label);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvPayMoney = (TextView) view.findViewById(R.id.tv_pay_money);
        tvCoupon = (TextView) view.findViewById(R.id.tv_coupon);

        orderId = getArguments().getString("orderId");
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

    public void checkPay(final PayModeEntity payModeEntity, final long startPayTime) {
        Map<String, String> map = new HashMap<>();
        map.put("oriReqMsgId", payModeEntity.getElectricOrderSerial());
        long ts = System.currentTimeMillis();
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.CHECK_PAY), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "查询结果：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        final SMZF006Vo payResultBean = JSON.parseObject(publicModule.getData(), SMZF006Vo.class);
                        if (payResultBean.getOriRespType().equals("S")) {
                            DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(payModeEntity);
                            hideLoadingAnim();
                            EventBus.getDefault().post(new SnackOpenTopRightEvent());
                            mHandler.sendEmptyMessage(0);
                        } else if (payResultBean.getOriRespType().equals("E")) {
                            DBHelper.getInstance(getContext().getApplicationContext()).clearOnePay(payModeEntity.getPayModeId());
                            String message = publicModule.getMessage() == null ? "已确认该笔支付失败" : payResultBean.getRespMsg();
                            showPayResultMessage(message);
                            EventBus.getDefault().post(new SnackOpenTopRightEvent());
                            mHandler.sendEmptyMessage(0);
                        } else if (payResultBean.getOriRespType().equals("R")) {
                            if (System.currentTimeMillis() - startPayTime > 30 * 1000) {
                                //微信支付
                                String message = "刷新支付失败，请重新尝试";
                                showPayResultMessage(message);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        //显示dialog
                                        checkPay(payModeEntity, startPayTime);
                                    }
                                }, 3000);
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    //显示dialog
                                    checkPay(payModeEntity, startPayTime);
                                }
                            }, 3000);
                        }
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                //显示dialog
                                checkPay(payModeEntity, startPayTime);
                            }
                        }, 3000);
                    }
                } catch (Exception e) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //显示dialog
                            checkPay(payModeEntity, startPayTime);
                        }
                    }, 3000);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //显示dialog
                        checkPay(payModeEntity, startPayTime);
                    }
                }, 3000);
            }
        });
    }

    private void setListener() {
        mAdapter.setOnCashierHistoryClickListener(new CashierHistoryAdapter.OnCashierHistoryClickListener() {
            @Override
            public void onCashierHistoryClick(final PayModeEntity payModeEntity) {
                if (payModeEntity.getIsJoinOrderPay() != null && payModeEntity.getIsJoinOrderPay() == 1 && payModeEntity.getPaymentType() == 2 && payModeEntity.getElectricOrderSerial() != null) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                    dialog.setTitle("微信支付处理");
                    dialog.setMessage("对微信支付状态未知的记录进行处理，如果顾客手机端的支付结果为取消或失败，则可直接点击下方的‘确认支付失败’按钮；如果否则可点击下方的‘查询支付状态’按钮进行查询，查询成功将自动处理该笔支付");
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "确认支付失败", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBHelper.getInstance(getContext().getApplicationContext()).clearOnePay(payModeEntity.getPayModeId());
                            EventBus.getDefault().post(new SnackOpenTopRightEvent());
                            mHandler.sendEmptyMessage(0);
                        }
                    });
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "查询支付状态", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showLoadingAnim("微信支付处理中...");
                            checkPay(payModeEntity, System.currentTimeMillis());
                        }
                    });
                    dialog.show();
                }
            }
        });

        tvCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponDialog != null){
                    couponDialog.dismiss();
                    couponDialog = null;
                }
                couponDialog = new CustomeCouponDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("type", (Integer) tvCoupon.getTag());
                bundle.putString("orderId",orderId);
                couponDialog.setArguments(bundle);
                couponDialog.show(getFragmentManager(),"couponDialog");
            }
        });
    }

    private void setCoupon() {
        try {
            OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
            if (orderEntity.getUserCouponId() != null && orderEntity.getIsCoupon() != null) {
                setTvCoupon(true);
            } else {
                setTvCoupon(false);
            }
        } catch (Exception e) {
            setTvCoupon(false);
        }
    }

    private void setTvCoupon(boolean isUseCoupon){
        if(isUseCoupon){
            tvCoupon.setTag(1);
            tvCoupon.setText("查看优惠券详情");
        }else{
            tvCoupon.setTag(0);
            tvCoupon.setText("使用优惠券");
        }
    }

    private void setAdapter() {
        mAdapter = new CashierHistoryAdapter(getActivity().getApplicationContext(), orderId, false);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void refreshData() {
        setCoupon();
        double dishCount = 0;
        double billMoney = 0;
        double receivableMoney = 0;
        int treatmentMoney = 0;
        double hadPayMoney = 0;
        dishCount = DBHelper.getInstance(getActivity().getApplicationContext()).getDishCountByOrderId(orderId);
        billMoney = DBHelper.getInstance(getActivity().getApplicationContext()).getBillMoneyByOrderId(orderId, 1);
        receivableMoney = DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoney(orderId, 1);
        treatmentMoney = DBHelper.getInstance(getActivity().getApplicationContext()).getYHMoney(orderId);
        hadPayMoney = DBHelper.getInstance(getActivity().getApplicationContext()).getHadPayMoneyByOrderId(orderId);
        tvTotle.setText(AmountUtils.multiply(""+dishCount,"1") + "项");
        tvBillMoney.setText(String.valueOf(billMoney));
        tvReceivebal.setText(String.valueOf(receivableMoney));

        Map<String, String> map = DBHelper.getInstance(getContext().getApplicationContext()).getYHMoneyMap(orderId);
        String discountDetail = "\n" + AmountUtils.changeF2Y(treatmentMoney) + "\n";
        String labeles = "\n优惠总金额" + "\n";
        try {
            if (!map.get("couponDiscount").equals("0")) {
                discountDetail += AmountUtils.changeF2Y(map.get("couponDiscount")) + "\n";
                labeles += "优惠券：" + "\n";
            }
            if (!map.get("treatMentMoney").equals("0")) {
                labeles += "系统抹零和不吉利尾数：" + "\n";
                discountDetail += AmountUtils.changeF2Y(map.get("treatMentMoney")) + "\n";
            }
            if (!map.get("selfMoney").equals("0")) {
                labeles += "手动抹零：" + "\n";
                discountDetail += AmountUtils.changeF2Y(map.get("selfMoney")) + "\n";
            }
            if (!map.get("presentMoney").equals("0")) {
                labeles += "赠菜金额：" + "\n";
                discountDetail += AmountUtils.changeF2Y(map.get("presentMoney")) + "\n";
            }
            if (!map.get("discountMoney").equals("0")) {
                labeles += "打折金额：" + "\n";
                discountDetail += AmountUtils.changeF2Y(map.get("discountMoney")) + "\n";
            }
            if (!map.get("vipDiscount").equals("0")) {
                labeles += "会员卡打折金额：" + "\n";
                discountDetail += AmountUtils.changeF2Y(map.get("vipDiscount")) + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvSpend.setText(discountDetail);
        tvLabel.setText(labeles);
        tvPayMoney.setText(String.valueOf(hadPayMoney));
        syncMoney();
    }

    /**
     * 同步金额
     */
    private void syncMoney() {
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        if (orderEntity != null && orderEntity.getSerialNumber() != null && orderEntity.getIsUpload() == 1) {
            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(orderId, orderId, 3);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCouponDialogClose(CouponDialogCloseEvent event){
        if(event != null && couponDialog != null){
            couponDialog.dismiss();
            couponDialog = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(SnackCashierTopLeftRefreshEvent event){
        if(event != null){
            mHandler.sendEmptyMessage(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderMoneyChange(SnackOrderMoneyChangedEvent event){
        if(event != null){
            mHandler.sendEmptyMessage(0);
        }
    }
}
