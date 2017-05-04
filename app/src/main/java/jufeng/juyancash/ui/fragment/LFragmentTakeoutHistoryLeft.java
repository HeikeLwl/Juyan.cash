package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.TakeOutDishAdapter;
import jufeng.juyancash.bean.ChangeTakeoutState;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.TakeOutMessageBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.SendPersonEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.ui.customview.CustomeCancelMeituanOrderReasonDialog;
import jufeng.juyancash.ui.customview.CustomeSelectEmployeeDialog;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;
import jufeng.juyancash.util.ReasonCodes;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentTakeoutHistoryLeft extends BaseFragment implements View.OnClickListener {
    private TextView tvTitle, tvOrderNumber, tvName, tvPhone, tvMark, tvTime, tvCount, tvSpend, tvSendEmployee, tvBoxFee, tvSendFee, tvAddress, tvSendPhone;
    private RecyclerView mRecyclerView;
    private Button btnCancle;
    private Button btnTransfer;
    private Button btnOrder;
    private Button btnOver;
    private Button btnPass;
    private Button btnPrint;
    private TakeOutDishAdapter adapter;
    private TakeOutOrderEntity mTakeOutOrderEntity;
    private MainFragmentListener mOnTakeOutHistoryLeftClickListener;
    private Drawable mDrawable, mDrawable1, mDrawable2, mDrawable3;
    private CustomLoadingProgress mLoadingProgress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mTakeOutOrderEntity = msg.getData().getParcelable("takeOutOrderEntity");
                    initData(mTakeOutOrderEntity, msg.getData().getInt("type"));
                    break;
                case 1://下单
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).changeTakeOutOrderStatus(mTakeOutOrderEntity, 1);
                        DBHelper.getInstance(getActivity().getApplicationContext()).deleteUnLoadTakeOutMessage(mTakeOutOrderEntity.getOrderId());
                        mOnTakeOutHistoryLeftClickListener.onOrderClick(mTakeOutOrderEntity);
                        mOnTakeOutHistoryLeftClickListener.onPrintData(mTakeOutOrderEntity.getOrderId());
                        mOnTakeOutHistoryLeftClickListener.onChangeTakeoutStatusCount();
                    }
                    break;
                case 2://送餐
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).changeTakeOutOrderStatus(mTakeOutOrderEntity, 2);
                        mOnTakeOutHistoryLeftClickListener.onTransferClick(mTakeOutOrderEntity);
                        mOnTakeOutHistoryLeftClickListener.onChangeTakeoutStatusCount();
                    }
                    break;
                case 3://完成
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).changeTakeOutOrderStatus(mTakeOutOrderEntity, 3);
                        DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mTakeOutOrderEntity.getOrderId(), mTakeOutOrderEntity.getOrderId(), 7);
                        mOnTakeOutHistoryLeftClickListener.onOverClick(mTakeOutOrderEntity);
                        mOnTakeOutHistoryLeftClickListener.onChangeTakeoutStatusCount();
                        mOnTakeOutHistoryLeftClickListener.refreshStock();
                    }
                    break;
                case 4://接单
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).changeTakeOutOrderStatus(mTakeOutOrderEntity, 4);
                        DBHelper.getInstance(getActivity().getApplicationContext()).deleteUnLoadTakeOutMessage(mTakeOutOrderEntity.getOrderId());
                        mOnTakeOutHistoryLeftClickListener.onPassClick(mTakeOutOrderEntity);
                        mOnTakeOutHistoryLeftClickListener.onChangeTakeoutStatusCount();
                    }
                    break;
                case 5://取消
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).changeTakeOutOrderStatus(mTakeOutOrderEntity, 5);
                        mOnTakeOutHistoryLeftClickListener.onCancleClick(mTakeOutOrderEntity);
                        mOnTakeOutHistoryLeftClickListener.onChangeTakeoutStatusCount();
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setTakeoutOrderHandler(mHandler);
        try {
            mOnTakeOutHistoryLeftClickListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_takeout_history_left, container, false);
        initView(mView);
        setListener();
        initData(mTakeOutOrderEntity, 0);
        return mView;
    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        tvMark = (TextView) view.findViewById(R.id.tv_mark);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        tvSpend = (TextView) view.findViewById(R.id.tv_spend);
        tvBoxFee = (TextView) view.findViewById(R.id.tv_box_fee);
        tvSendFee = (TextView) view.findViewById(R.id.tv_send_fee);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle_order);
        btnTransfer = (Button) view.findViewById(R.id.btn_transfer_dish);
        btnOrder = (Button) view.findViewById(R.id.btn_ordered);
        btnOver = (Button) view.findViewById(R.id.btn_over);
        btnPass = (Button) view.findViewById(R.id.btn_pass);
        btnPrint = (Button) view.findViewById(R.id.btn_print_order);
        tvSendEmployee = (TextView) view.findViewById(R.id.tv_send_employee);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvSendPhone = (TextView) view.findViewById(R.id.tv_send_phone);
        mTakeOutOrderEntity = null;
        mDrawable = getResources().getDrawable(R.drawable.schedule1);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        mDrawable1 = getResources().getDrawable(R.drawable.hope_time);
        mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(), mDrawable1.getMinimumHeight());
        mDrawable2 = getContext().getResources().getDrawable(R.drawable.wechat_takeout);
        mDrawable2.setBounds(0, 0, mDrawable2.getMinimumWidth(), mDrawable2.getMinimumHeight());
        mDrawable3 = getContext().getResources().getDrawable(R.drawable.meituan_takeount);
        mDrawable3.setBounds(0, 0, mDrawable3.getMinimumWidth(), mDrawable3.getMinimumHeight());
    }

    private void initData(TakeOutOrderEntity takeOutOrderEntity, int type) {
        if (takeOutOrderEntity != null) {
            if (takeOutOrderEntity.getTakeoutFrom() == 0) {
                tvTitle.setCompoundDrawables(mDrawable2, null, null, null);
            } else if (takeOutOrderEntity.getTakeoutFrom() == 1) {
                tvTitle.setCompoundDrawables(mDrawable3, null, null, null);
            } else {
                tvTitle.setCompoundDrawables(null, null, null, null);
            }
            OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(takeOutOrderEntity.getOrderId());
            tvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
            tvName.setText(takeOutOrderEntity.getGuestName());
            tvPhone.setText(takeOutOrderEntity.getGuestPhone());
            tvMark.setTag(takeOutOrderEntity.getTakeoutMark());
            tvTime.setText(CustomMethod.parseTime(takeOutOrderEntity.getTakeoutTime(), "yyyy-MM-dd HH:mm"));
            tvBoxFee.setText("餐盒费：" + AmountUtils.multiply(takeOutOrderEntity.getBoxFee(), 0.01) + "元");
            tvSendFee.setText("配送费：" + AmountUtils.multiply(takeOutOrderEntity.getDispatchFee(), 0.01) + "元");
            tvAddress.setText(takeOutOrderEntity.getTakeoutAddress());
            if (TextUtils.isEmpty(orderEntity.getDispacherName())) {
                tvSendEmployee.setText("配送员：无");
            } else {
                tvSendEmployee.setText("配送员：" + orderEntity.getDispacherName());
            }

            if (TextUtils.isEmpty(orderEntity.getDispacherTel())) {
                tvSendPhone.setText("电话：无");
            } else {
                tvSendPhone.setText("电话：" + orderEntity.getDispacherTel());
            }
            if (CustomMethod.isTomorrow(takeOutOrderEntity.getTakeoutTime())) {
                tvTime.setCompoundDrawables(mDrawable1, null, mDrawable, null);
            } else {
                tvTime.setCompoundDrawables(mDrawable1, null, null, null);
            }
            tvCount.setText("共" + AmountUtils.multiply("" + DBHelper.getInstance(getActivity().getApplicationContext()).getOrderedDishCountByOrderId(takeOutOrderEntity.getOrderId()), "1") + "项");
            String payDetail = "";
            String totalMoney = AmountUtils.changeF2Y(orderEntity.getTotalMoney());
            String discountTotalMoney = AmountUtils.changeF2Y(orderEntity.getDiscountTotalMoney());
            String payMoney = AmountUtils.changeF2Y(orderEntity.getCloseMoney());
            payDetail += "合计：<font color=\"#ff5e48\" size=\"16\">￥" + totalMoney +
                    "</font><br>优惠金额：<font color=\"#ff5e48\" size=\"16\">￥" + discountTotalMoney + "</font><br>支付金额：<font color=\"#ff5e48\" size=\"16\">￥" + payMoney + "</font>";
            ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
            payModeEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getPayModeByOrderId(orderEntity.getOrderId()));
            if (payModeEntities.size() > 0) {
                payDetail += "<br>";
            }
            for (PayModeEntity payModeEntity :
                    payModeEntities) {
                payDetail += payModeEntity.getPaymentName() + "：<font color=\"#ff5e48\" size=\"16\">￥" + AmountUtils.multiply1(payModeEntity.getPayMoney() + "", "1") + "</font>";
            }
            tvSpend.setText(Html.fromHtml(payDetail));
            switch (type) {
                case 0:
                    //待审核
                    if (CustomMethod.isTomorrow(takeOutOrderEntity.getTakeoutTime())) {
                        //预订单只接单不打印
                        setButtonVisible(true, false, false, false, true, true);
                    } else {
                        //当日单接单并打印
                        setButtonVisible(true, false, true, false, false, true);
                    }
                    break;
                case 1:
                    //已下单
                    setButtonVisible(true, true, false, false, false, true);
                    break;
                case 2:
                    //送餐中
                    setButtonVisible(false, false, false, true, false, true);
                    break;
                case 3://完成
                    setButtonVisible(false, false, false, false, false, true);
                    break;
                case 4://接单
                    setButtonVisible(true, false, true, false, false, true);
                    break;
                case 5://取消
                    setButtonVisible(false, false, false, false, false, false);
                    break;
            }
        } else {
            tvTitle.setCompoundDrawables(null, null, null, null);
            tvOrderNumber.setText("");
            tvName.setText("客户姓名");
            tvPhone.setText("联系电话");
            tvMark.setTag("无");
            tvTime.setText("送餐时间");
            tvCount.setText("共   项");
            tvSpend.setText("合计：");
            tvSendEmployee.setText("配送员：");
            tvAddress.setText("配送地址");
            tvSendPhone.setText("电话：");
            tvBoxFee.setText("餐盒费：");
            tvSendFee.setText("配送费：");
            tvTime.setCompoundDrawables(mDrawable1, null, null, null);
            setButtonVisible(false, false, false, false, false, false);
        }
        setAdapter();
    }

    private void setButtonVisible(boolean btn0, boolean btn1, boolean btn2, boolean btn3, boolean btn4, boolean btn5) {
        btnCancle.setVisibility(btn0 ? Button.VISIBLE : Button.GONE);
        btnTransfer.setVisibility(btn1 ? Button.VISIBLE : Button.GONE);
        btnOrder.setVisibility(btn2 ? Button.VISIBLE : Button.GONE);
        btnOver.setVisibility(btn3 ? Button.VISIBLE : Button.GONE);
        btnPass.setVisibility(btn4 ? Button.VISIBLE : Button.GONE);
        btnPrint.setVisibility(btn5 ? Button.VISIBLE : Button.GONE);
    }

    private void setListener() {
        btnOver.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnPass.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        tvMark.setOnClickListener(this);
    }

    private void setAdapter() {
        if (mTakeOutOrderEntity != null) {
            if (adapter == null) {
                adapter = new TakeOutDishAdapter(getActivity().getApplicationContext(), mTakeOutOrderEntity.getOrderId(), 1);
                mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(mTakeOutOrderEntity.getOrderId(), 1);
            }
        } else {
            if (adapter != null) {
                adapter = null;
                mRecyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle_order://取消订单
                if (mTakeOutOrderEntity != null) {
                    switch (mTakeOutOrderEntity.getTakeoutFrom()) {
                        case 0://微信外卖
                            changeTakeOut(mTakeOutOrderEntity, 5);
                            break;
                        case 1://美团外卖
                            final CustomeCancelMeituanOrderReasonDialog dialog = new CustomeCancelMeituanOrderReasonDialog(getContext());
                            dialog.setOnReturnOrderReasonSelectedListener(new CustomeCancelMeituanOrderReasonDialog.OnMeituanCancelReasonSelectedListener() {
                                @Override
                                public void onCancleClick() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onConfirmClick(int reasonCode) {
                                    dialog.dismiss();
                                    onMeituanTakeOutOrderCancel(reasonCode);
                                }
                            });
                            break;
                    }
                }

                break;
            case R.id.btn_transfer_dish://开始送餐
                if(mTakeOutOrderEntity != null){
                    dispach();
                }
                break;
            case R.id.btn_ordered://下单
                if (mTakeOutOrderEntity != null) {
                    if (mTakeOutOrderEntity.getTakeoutStatus() == 4) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                        switch (mTakeOutOrderEntity.getTakeoutFrom()) {
                            case 0:
                                changeTakeOut(mTakeOutOrderEntity, 1);
                                break;
                            case 1:
                                onMeituanTakeOutOrderConfirm(mTakeOutOrderEntity,1);
                                break;
                        }
                    }
                }
                break;
            case R.id.btn_over://完成
                if(mTakeOutOrderEntity != null){
                    dispachOver();
                }
                break;
            case R.id.btn_pass://接单
                switch (mTakeOutOrderEntity.getTakeoutFrom()) {
                    case 0:
                        changeTakeOut(mTakeOutOrderEntity, 4);
                        break;
                    case 1:
                        onMeituanTakeOutOrderConfirm(mTakeOutOrderEntity,4);
                        break;
                }
                break;
            case R.id.btn_print_order://打印外卖单
                if (mTakeOutOrderEntity != null)
                    mOnTakeOutHistoryLeftClickListener.onPrintTakeOutOrder(mTakeOutOrderEntity.getOrderId());
                break;
            case R.id.tv_mark:
                if (tvMark.getTag() != null) {
                    CustomMethod.showMessage(getContext(), "外卖单备注", (String) tvMark.getTag());
                }
                break;
        }
    }

    //配送
    private void dispach(){
        switch (mTakeOutOrderEntity.getTakeoutFrom()){
            case 0://微信外卖
                dispachStore(0);
                break;
            case 1://美团外卖
                onMoreDispach(new String[]{"商家自配送","美团专送","美团众包配送"});
                break;
        }
    }

    //配送完成
    private void dispachOver(){
        switch (mTakeOutOrderEntity.getTakeoutFrom()){
            case 0://微信外卖
                changeTakeOut(mTakeOutOrderEntity, 3);
                break;
            case 1://美团外卖
                onMeituanDeliverStoreOver();
                break;
        }
    }

    //商家自配送
    public void dispachStore(final int type) {
        final CustomeSelectEmployeeDialog dialog = new CustomeSelectEmployeeDialog(getContext(),type);
        dialog.setOnPaymentSelectedListener(new CustomeSelectEmployeeDialog.OnEmployeeSelectedListener() {
            @Override
            public void onUseEmployee(SendPersonEntity sendPersonEntity) {
                switch (type) {
                    case 0:
                        DBHelper.getInstance(getContext().getApplicationContext()).addEmployeeForTakeout(mTakeOutOrderEntity, sendPersonEntity);
                        changeTakeOut(mTakeOutOrderEntity, 2);
                        if (sendPersonEntity.getIsSendMessage() == 1) {
                            sendMSG(sendPersonEntity.getSendPersonPhone());
                        }
                        break;
                    case 1:
                        meituanDispachStore(mTakeOutOrderEntity.getOtherOrderId(), sendPersonEntity);
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onUnuseEmployee() {
                if(type == 0){
                    changeTakeOut(mTakeOutOrderEntity, 2);
                    dialog.dismiss();
                }
            }
        });
    }

    //平台配送
    private void dispachSelf() {

    }

    //第三方配送
    private void dispachThird() {

    }

    //美团外卖商家自配送
    private void meituanDispachStore(String orderId, final SendPersonEntity sendPersonEntity) {
        showLoadingAnim("正在分配送餐员...");
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        long ts = System.currentTimeMillis();
        String data = "{\"orderId\":" + orderId + ",\"courierName\":\"" + sendPersonEntity.getSendPersonName() + "\",\"courierPhone\":\"" + sendPersonEntity.getSendPersonPhone() + "\"}";
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("ts", ts + "");
        map.put("sign", sign);
        map.put("data", data);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_DELIVER_STORE), ts + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                hideLoadingAnim();
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        //自配送成功
                        DBHelper.getInstance(getContext().getApplicationContext()).addEmployeeForTakeout(mTakeOutOrderEntity, sendPersonEntity);
                        mHandler.sendEmptyMessage(2);
                    } else {
                        String message = TextUtils.isEmpty(publicModule.getMessage()) ? "美团外卖自配送失败，请稍后重试" : publicModule.getMessage();
                        CustomMethod.showMessage(getContext(), message);
                    }
                } catch (Exception e) {
                    CustomMethod.showMessage(getContext(), "美团外卖自配送失败，请稍后重试");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(), "美团外卖自配送失败，请稍后重试");
            }
        });
    }

    //美团外卖自配送完成
    private void onMeituanDeliverStoreOver(){
        showLoadingAnim("同步美团外卖状态...");
        String data = "{\"orderId\":"+mTakeOutOrderEntity.getOtherOrderId()+"}";
        long ts = System.currentTimeMillis();
        String partnerCode = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerCode",null);
        String sign = MD5Util.getMD5String(partnerCode+data+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("data",data);
        map.put("ts",ts+"");
        map.put("partnerCode",partnerCode);
        map.put("sign",sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_DELIVER_STORE_OVER), ts + "", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                hideLoadingAnim();
                try{
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        mHandler.sendEmptyMessage(3);
                    }else{
                        String message = TextUtils.isEmpty(publicModule.getMessage()) ? "同步美团外卖状态失败，请稍后重试":publicModule.getMessage();
                        CustomMethod.showMessage(getContext(),message);
                    }
                }catch (Exception e){
                    CustomMethod.showMessage(getContext(),"同步美团外卖状态失败，请稍后重试");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"同步美团外卖状态失败，请稍后重试");
            }
        });
    }

    //美团外卖美团专送
    private void meituanDispachSelf() {
        showLoadingAnim("申请美团专送...");
        long ts = System.currentTimeMillis();
        String partnerCode = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerCode",null);
        String data = "{\"orderId\":"+mTakeOutOrderEntity.getOtherOrderId()+"}";
        String sign = MD5Util.getMD5String(partnerCode+data+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("ts",ts+"");
        map.put("data",data);
        map.put("sign",sign);
        map.put("partnerCode",partnerCode);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_DELIVER_SELF), ts + "", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                hideLoadingAnim();
                try{
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        mHandler.sendEmptyMessage(3);
                    }else{
                        String message = TextUtils.isEmpty(publicModule.getMessage()) ? "美团专送失败，请稍后重试":publicModule.getMessage();
                        CustomMethod.showMessage(getContext(),message);
                    }
                }catch (Exception e){
                    CustomMethod.showMessage(getContext(),"美团专送失败，请稍后重试");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"美团专送失败，请稍后重试");
            }
        });
    }

    //美团外卖众包配送
    private void meituanDispachThird() {

    }

    //多种配送方式
    public void onMoreDispach(String[] dispachTypes) {
        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("选择配送方式")
                .setIcon(getContext().getResources().getDrawable(R.drawable.meituan_takeount))
                .setItems(dispachTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0://商家自配送
                                dispachStore(1);
                                break;
                            case 1://平台自配送
                                meituanDispachSelf();
                                break;
                            case 2://众包配送
                                meituanDispachThird();
                                break;
                        }
                    }
                }).create();
        dialog.show();
    }

    public void onMeituanTakeOutOrderCancel(int reasonCode) {
        if (mTakeOutOrderEntity != null && mTakeOutOrderEntity.getTakeoutFrom() == 1) {
            showLoadingAnim("正在取消美团外卖订单...");
            String data = "{\"orderId\":\"" + mTakeOutOrderEntity.getOtherOrderId() + "\",\"reasonCode\":\"" + reasonCode + "\",\"reason\":\"" + ReasonCodes.getValueByKey(reasonCode) + "\"}";
            String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
            String ts = System.currentTimeMillis() + "";
            String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("data", data);
            map.put("ts", ts);
            map.put("sign", sign);
            map.put("partnerCode", partnerCode);
            VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.CANCEL_MEITUAN_TAKEOUT_ORDER), ts, map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "美团取消外卖订单：" + arg0);
                    hideLoadingAnim();
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            mHandler.sendEmptyMessage(5);
                        } else {
                            String message = publicModule.getMessage() == null ? "美团外卖订单取消失败，请稍后再试" : publicModule.getMessage();
                            CustomMethod.showMessage(getContext(), message);
                        }
                    } catch (Exception e) {
                        CustomMethod.showMessage(getContext(), "美团外卖订单取消失败，请稍后再试");
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    hideLoadingAnim();
                    CustomMethod.showMessage(getContext(), "美团外卖订单取消失败，请稍后再试");
                }
            });
        }
    }

    public void onMeituanTakeOutOrderConfirm(TakeOutOrderEntity takeOutOrderEntity, final int status) {
        showLoadingAnim("美团外卖接单...");
        if (takeOutOrderEntity != null) {
            String data = "{\"orderId\":\"" + takeOutOrderEntity.getOtherOrderId() + "\"}";
            long ts = System.currentTimeMillis();
            String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
            String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("ts", ts + "");
            map.put("partnerCode", partnerCode);
            map.put("sign", sign);
            map.put("data", data);
            VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.CONFIRM_MEITUAN_TAKEOUT_ORDER), ts + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "美团外卖接单： " + arg0);
                    hideLoadingAnim();
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            mHandler.sendEmptyMessage(status);
                        } else {
                            CustomMethod.showMessage(getContext(), publicModule.getMessage() == null ? "同步外卖单失败，请稍后重试！" : publicModule.getMessage());
                        }
                    } catch (Exception e) {
                        CustomMethod.showMessage(getContext(), "美团外卖接单失败，请稍后重试！");
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    arg0.printStackTrace();
                    CustomMethod.showMessage(getContext(), "美团外卖接单失败，请稍后重试！");
                    hideLoadingAnim();
                }
            });
        } else {
            hideLoadingAnim();
            CustomMethod.showMessage(getContext(), "美团下单失败，请稍后重试！");
        }
    }

    //请求服务器更改外卖状态
    private void changeTakeOut(TakeOutOrderEntity takeOutOrderEntity, final int status) {
        if (takeOutOrderEntity != null) {
            showLoadingProgressBar("同步外卖单...");
            long ts = System.currentTimeMillis();
            String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
            String orderId = takeOutOrderEntity.getOrderId();
            String state = null;
            switch (status) {
                case 1:
                    state = "1";
                    break;
                case 2:
                    state = "2";
                    break;
                case 3:
                    state = "3";
                    break;
                case 4:
                    state = "1";
                    break;
                case 5:
                    state = "4";
                    break;
            }
            ChangeTakeoutState changeTakeoutState = new ChangeTakeoutState(orderId, state);
            String data = JSON.toJSONString(changeTakeoutState);
            String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("partnerCode", partnerCode);
            map.put("ts", String.valueOf(ts));
            map.put("data", data);
            map.put("sign", sign);
            VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.CHANGE_TAKEOUT_BY_ID), "CHANGE_TAKEOUT_BY_ID", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                     Log.d("###", "修改外卖： " + arg0);
                    hideLoadingProgressBar();
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {
                            mHandler.sendEmptyMessage(status);
                        } else {
                            CustomMethod.showMessage(getContext(), publicModule.getMessage() == null ? "同步外卖单失败，请稍后重试！" : publicModule.getMessage());
                        }
                    } catch (Exception e) {
                        CustomMethod.showMessage(getContext(), "同步外卖单失败，请稍后重试！");
                    }
                }

                @Override
                public void onError(VolleyError arg0) {
                    arg0.printStackTrace();
                    CustomMethod.showMessage(getContext(), "同步外卖单失败，请稍后重试！");
                    hideLoadingProgressBar();
                }
            });
        } else {
            CustomMethod.showMessage(getContext(), "同步外卖单失败，请稍后重试！");
        }
    }

    public void sendMSG(String phone) {
        try {
            if (mTakeOutOrderEntity != null) {
                String partnerName = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", null);
                OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mTakeOutOrderEntity.getOrderId());
                if (orderEntity != null) {
                    TakeOutMessageBean takeOutMessageBean = new TakeOutMessageBean(partnerName, orderEntity.getOrderNumber1(), mTakeOutOrderEntity.getTakeoutAddress(), CustomMethod.parseTime(mTakeOutOrderEntity.getTakeoutTime(), "HH:mm"), phone);
                    long ts = System.currentTimeMillis();
                    String data = JSON.toJSONString(takeOutMessageBean);
                    String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
                    String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
                    Map<String, String> map = new HashMap<>();
                    map.put("ts", ts + "");
                    map.put("partnerCode", partnerCode);
                    map.put("data", data);
                    map.put("sign", sign);
                    VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.SEND_TAKEOUT_MSG), ts + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                        @Override
                        public void onSuccess(String arg0) {
                            try {
                                PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                                if (publicModule.getCode() == 0) {
                                    Toast.makeText(getContext(), "配送短信发送成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "配送短信发送失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "配送短信发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError arg0) {
                            Toast.makeText(getContext(), "配送短信发送失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (Exception e) {

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
