package jufeng.juyancash.service;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.util.SignUtils;

/**
 * Created by Administrator102 on 2017/2/13.
 */

public class MeiTuanHeartBeatThread implements Runnable{
    private String partnerCode;
    private Context mContext;

    public MeiTuanHeartBeatThread(Context context){
        this.mContext = context;
        partnerCode = context.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerCode",null);
    }

    @Override
    public void run() {
        Map<String, String> params = new HashMap<>();
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("ePoiId", partnerCode);
        jsonParams.put("developerId", mContext.getResources().getString(R.string.DEVELOPER_ID));
        jsonParams.put("time", System.currentTimeMillis());
        jsonParams.put("posId", partnerCode);
        params.put("data", jsonParams.toJSONString());
        String sign = SignUtils.createSign(mContext.getResources().getString(R.string.MEITUAN_SIGN_KEY), params);
        params.put("sign", sign);

        VolleyRequest.RequestPost(mContext, mContext.getResources().getString(R.string.MEITUAN_HEARTBEAT), "meituan_heartbeat", params, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
            }

            @Override
            public void onError(VolleyError arg0) {
            }
        });
    }
}
