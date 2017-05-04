package jufeng.juyancash.ui.customview;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.ClearAllAdapter;
import jufeng.juyancash.bean.ClearModel;
import jufeng.juyancash.bean.MeituanCancelBean;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.SMZF004Vo;
import jufeng.juyancash.bean.SMZF006Vo;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeClearAllDialog extends DialogFragment{
    private TextView tvChange,tvCancle;
    private OnClearAllListener mOnClearAllListener;
    private ClearAllAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String orderId;
    private CustomLoadingProgress mLoadingProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_clear_all_layout, container);
        initView(view);
        setAdapter();
        setListener();
        return view;
    }

    private void initView(View window) {
        tvCancle = (TextView) window.findViewById(R.id.tv_cancle);
        tvChange = (TextView) window.findViewById(R.id.tv_clear_all);
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
        if(getArguments() != null) {
            orderId = getArguments().getString("orderId");
        }
    }

    private void setAdapter(){
        mAdapter = new ClearAllAdapter(getContext(),orderId);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingProgressBar("正在清除支付...");
                ArrayList<ClearModel> clearModels = new ArrayList<>();
                clearModels.addAll(mAdapter.getSelectedDatas());
                final Map<String,Integer> clearStatus = new HashMap<>();
                if(clearModels.size() > 0){
                    for (ClearModel clearModel :
                            clearModels) {
                        clearStatus.put(clearModel.getId(),-1);
                        switch (clearModel.getType()){
                            case 0:
                                if(clearModel.getPayType() == 2){//微信支付
                                    wxPayRetreat(clearModel.getMoney(),clearModel,clearStatus);
                                }else if(clearModel.getPayType() == 3){//支付宝支付
                                    wxPayRetreat(clearModel.getMoney(),clearModel,clearStatus);
                                }else if(clearModel.getPayType() == 5){//会员卡支付
                                    vipPayRetreat(clearModel.getMoney(),clearModel,clearStatus);
                                }else if(clearModel.getPayType() == 4 && clearModel.getName().equals("美团") && clearModel.getPaySerial() != null){
                                    cancelMeituan(clearModel,clearStatus);
                                }else{
                                    DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                                    clearStatus.put(clearModel.getId(),1);
                                }
                                break;
                            case 1://打折
                                DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                                clearStatus.put(clearModel.getId(),1);
                                break;
                            case 2://挂账
                                DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                                clearStatus.put(clearModel.getId(),1);
                                break;
                            case 3://抹零
                                DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                                clearStatus.put(clearModel.getId(),1);
                                break;
                        }
                    }
                    checkClear(clearStatus);
                    OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
                    if(orderEntity != null) {
                        DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity,1);
                    }
                }else{
                    hideLoadingProgressBar();
                }
            }
        });
    }

    private void checkClear(final Map<String,Integer> clearStatus){
        boolean isClearAll = true;
        for (Map.Entry<String, Integer> entity :
                clearStatus.entrySet()) {
            if (entity.getValue() == -1) {
                isClearAll = false;
                break;
            }
        }
        if(isClearAll){
            hideLoadingProgressBar();
            if(mOnClearAllListener != null)
                mOnClearAllListener.onClearSuccess();
        }else{
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //显示dialog
                    checkClear(clearStatus);
                }
            }, 2000);
        }
    }

    private void cancelMeituan(final ClearModel clearModel, final Map<String,Integer> clearStatus){
        long ts = System.currentTimeMillis();
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        String couponCode = clearModel.getPaySerial();
        String sign = MD5Util.getMD5String(partnerCode+couponCode+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("ts",String.valueOf(ts));
        map.put("partnerCode",partnerCode);
        map.put("couponCode",couponCode);
        map.put("sign",sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.MEITUAN_CANCEL), "MEITUAN_CANCEL", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "美团撤销验券结果："+arg0);
                try{
                    PublicModule publiModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publiModule.getCode() == 0 && publiModule.getData() != null){
                        MeituanCancelBean meituanCancel = JSON.parseObject(publiModule.getData(),MeituanCancelBean.class);
                        if(meituanCancel != null && meituanCancel.getResult() == 0){
                            DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                            clearStatus.put(clearModel.getId(),1);
                        }else{
                            clearStatus.put(clearModel.getId(),0);
                        }
                    }else{
                        clearStatus.put(clearModel.getId(),0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    clearStatus.put(clearModel.getId(),0);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                clearStatus.put(clearModel.getId(),0);
            }
        });
    }

    private void vipPayRetreat(int money, final ClearModel clearModel, final Map<String,Integer> clearStatus){
        long ts = System.currentTimeMillis();
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        String data = "{\"orderId\":\"" +orderId+
                "\",\"amount\":\"" +money+
                "\",\"vipNo\":\"" +clearModel.getPaySerial()+
                "\",\"partnerCode\":\"" +partnerCode+
                "\"}";
        String sign = MD5Util.getMD5String(partnerCode+data+ts+ getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        map.put("ts",String.valueOf(ts));
        map.put("data",data);
        map.put("sign",sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.VIP_CARD_RETREAT), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "会员卡退款结果："+arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0){
                        DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                        clearStatus.put(clearModel.getId(),1);
                    }else{
                        clearStatus.put(clearModel.getId(),0);
                    }
                }catch (Exception e){
                    clearStatus.put(clearModel.getId(),0);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                clearStatus.put(clearModel.getId(),0);
            }
        });
    }

    private void wxPayRetreat(int money, final ClearModel clearModel, final Map<String,Integer> clearStatus){
        long ts = System.currentTimeMillis();
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        String data = "{\"oriReqMsgId\":\"" +clearModel.getPaySerial()+
                "\",\"refundAmount\":\"" + AmountUtils.changeF2Y(money)+
                "\"}";
        String sign = MD5Util.getMD5String(partnerCode+data+ts+ getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        map.put("ts",String.valueOf(ts));
        map.put("data",data);
        map.put("sign",sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.REFUND_PAY), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信支付宝退款结果："+arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 && publicModule.getData() != null){
                        SMZF004Vo smzf004Vo = JSON.parseObject(publicModule.getData(),SMZF004Vo.class);
                        if(smzf004Vo.getRespType().equals("S")) {
                            DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                            clearStatus.put(clearModel.getId(), 1);
                        }else if(smzf004Vo.getRespType().equals("E")){
                            if(smzf004Vo.getRespCode().equals("200111")){
                                DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                                clearStatus.put(clearModel.getId(), 1);
                            }else {
                                clearStatus.put(clearModel.getId(), 0);
                            }
                        }else if(smzf004Vo.getRespType().equals("R")){
                            checkPay(smzf004Vo.getReqMsgId(),clearModel,clearStatus,System.currentTimeMillis());
                        }
                    }else{
                        clearStatus.put(clearModel.getId(),0);
                    }
                }catch (Exception e){
                    clearStatus.put(clearModel.getId(),0);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                clearStatus.put(clearModel.getId(),0);
            }
        });
    }

    public void checkPay(final String oriReqMsgId, final ClearModel clearModel, final Map<String,Integer> clearStatus, final long startPayTime){
        Map<String,String> map = new HashMap<>();
        map.put("oriReqMsgId",oriReqMsgId);
        long ts = System.currentTimeMillis();
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.CHECK_PAY), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "查询结果："+arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if(publicModule.getCode() == 0){
                        SMZF006Vo payResultBean = JSON.parseObject(publicModule.getData(),SMZF006Vo.class);
                        if(payResultBean.getOriRespType().equals("S")){
                            DBHelper.getInstance(getContext()).clearOnePay(clearModel);
                            clearStatus.put(clearModel.getId(), 1);
                        }else if(payResultBean.getOriRespType().equals("E")){
                            clearStatus.put(clearModel.getId(),0);
                        }else if(payResultBean.getOriRespType().equals("R")){
                            if(System.currentTimeMillis() - startPayTime > 70*1000){
                                clearStatus.put(clearModel.getId(),0);
                            }else{
                                new Handler().postDelayed(new Runnable(){
                                    public void run() {
                                        //显示dialog
                                        checkPay(oriReqMsgId,clearModel,clearStatus,startPayTime);
                                    }
                                }, 3000);
                            }
                        }
                    }else{
                        clearStatus.put(clearModel.getId(),0);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    clearStatus.put(clearModel.getId(),0);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                clearStatus.put(clearModel.getId(),0);
            }
        });
    }

    //显示加载框
    private void showLoadingProgressBar(String message){
        if(mLoadingProgress != null){
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }
        mLoadingProgress = new CustomLoadingProgress(getContext());
        mLoadingProgress.setMessage(message);
    }

    //隐藏加载框
    private void hideLoadingProgressBar(){
        if(mLoadingProgress != null){
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }
    }

    public void setOnAuthorityListener(OnClearAllListener listener){
        this.mOnClearAllListener = listener;
    }

    public interface OnClearAllListener{
        void onClearSuccess();
    }
}
