package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.ShopOrderVo;
import jufeng.juyancash.ui.activity.LoginActivity;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/10/5.
 */

public class LFragmentUpdate extends BaseFragment {
    private Button btnDownLoadData,btnUpLoadData;
    private String partnerCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_update,container,false);
        initView(mView);
        setListener();
        return mView;
    }

    private void initView(View view){
        btnDownLoadData = (Button) view.findViewById(R.id.btn_download);
        btnUpLoadData = (Button) view.findViewById(R.id.btn_upload);
        partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
    }

    private void setListener(){
        btnDownLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("isUpdate",true);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnUpLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData(){
        ArrayList<String> orderIds = new ArrayList<>();
        ArrayList<String> orderIds1 = new ArrayList<>();
        showLoadingAnim("正在上传订单...");
        orderIds.addAll(DBHelper.getInstance(getContext()).getAllOrderIds());
        findOrder(orderIds,orderIds1);
    }

    public void findOrder(final ArrayList<String> orderIds,final ArrayList<String> orderIds1){
        if(orderIds.size() > 0) {
            showLoadingAnim("还剩下"+orderIds.size()+"单需要检测，请稍等...");
            Map<String, String> map = new HashMap<>();
            map.put("id", orderIds.get(0));
            VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.FIND_ORDER), System.currentTimeMillis() + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "查找订单："+arg0);
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() != 0) {
                            //订单不存在
                            orderIds1.add(orderIds.get(0));
                        }
                        orderIds.remove(0);
                    } catch (Exception e) {
                        //订单不存在
                        orderIds1.add(orderIds.get(0));
                        orderIds.remove(0);
                    }
                    findOrder(orderIds,orderIds1);
                }

                @Override
                public void onError(VolleyError arg0) {
                    //订单不存在
                    orderIds1.add(orderIds.get(0));
                    orderIds.remove(0);
                    findOrder(orderIds,orderIds1);
                }
            });
        }else{
            upload(orderIds1);
        }
    }

    public void upload(final ArrayList<String> orderIds){
        if(orderIds.size() > 0){
            showLoadingAnim("还剩下"+orderIds.size()+"单需要上传，请稍等...");
            try {
                long ts = System.currentTimeMillis();
                ShopOrderVo shopOrderVo = new ShopOrderVo(getContext().getApplicationContext(),orderIds.get(0));
                String data = JSON.toJSONString(shopOrderVo);
                Log.d("###", "run: "+data);
                String sign = MD5Util.getMD5String(partnerCode+data+ts+ getContext().getResources().getString(R.string.APP_KEY));
                Map<String,String> map = new HashMap<>();
                map.put("partnerCode",partnerCode);
                map.put("data", data);
                map.put("ts",String.valueOf(ts));
                map.put("sign",sign);
                VolleyRequest.RequestPost(getContext().getApplicationContext(), getContext().getResources().getString(R.string.UPLOAD_SHOPDATA), ts+"", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
                    @Override
                    public void onSuccess(String arg0) {
                        Log.d("###", "上传营业数据："+arg0);
                        try{
                            PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                            if(publicModule.getCode() == 0){
                                DBHelper.getInstance(getContext().getApplicationContext()).clearUploadData(orderIds.get(0));
                            }
                            orderIds.remove(0);
                        }catch (JSONException e){
                            orderIds.remove(0);
                        }
                        upload(orderIds);
                    }

                    @Override
                    public void onError(VolleyError arg0) {
                        orderIds.remove(0);
                        upload(orderIds);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            hideLoadingAnim();
        }
    }

}
