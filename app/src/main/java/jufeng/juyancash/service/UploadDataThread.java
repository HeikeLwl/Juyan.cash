package jufeng.juyancash.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
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
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/9/20.
 */
public class UploadDataThread implements Runnable {
    private Context mContext;
    private String partnerCode;
    private long currentTime;

    public UploadDataThread(Context context) {
        this.mContext = context;
        partnerCode = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
    }

    @Override
    public void run() {
        Log.d("###", "同步数据服务启动");
        currentTime = System.currentTimeMillis();
        ArrayList<OrderEntity> orderEntities = DBHelper.getInstance(mContext).getAllUnSyncOrders();
        for (OrderEntity orderEntity :
                orderEntities) {

        }
    }

    public void findOrder(String orderId){

    }

    //同步营业数据
    public void syncOrder(final String orderId) {
        try {
            long ts = System.currentTimeMillis();
            ShopOrderVo shopOrderVo = new ShopOrderVo(mContext, orderId);
            String data = JSON.toJSONString(shopOrderVo);
            Log.d("###", "run: " + data);
            String sign = MD5Util.getMD5String(partnerCode + data + ts + mContext.getResources().getString(R.string.APP_KEY));
            Map<String, String> map = new HashMap<>();
            map.put("partnerCode", partnerCode);
            map.put("data", data);
            map.put("ts", String.valueOf(ts));
            map.put("sign", sign);
            VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.UPLOAD_SHOPDATA), orderId, map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "上传营业数据：" + arg0);
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() == 0) {

                        } else {

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onError(VolleyError arg0) {

                }
            });
        } catch (Exception e) {

        }
    }
}
