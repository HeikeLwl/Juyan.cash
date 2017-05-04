package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.R;
import jufeng.juyancash.UpdateManager;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/10/5.
 */

public class LFragmentUpload extends BaseFragment {
    private LinearLayout layoutUpdate;
    private TextView tvVersionName,tvUpdate;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    checkVersion();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_status,container,false);
        initView(mView);
        setListener();
        showLoadingAnim("版本检测中...");
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0),1000);
        return mView;
    }

    private void initView(View view){
        layoutUpdate = (LinearLayout) view.findViewById(R.id.layout);
        tvVersionName = (TextView) view.findViewById(R.id.tv_version_name);
        tvUpdate = (TextView) view.findViewById(R.id.tv_upload);

        tvVersionName.setText("v "+CustomMethod.getAppVersionName(getContext()));
    }

    public void setNewParam(){
        showLoadingAnim("版本检测中。。。");
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0),1000);
    }

    private void initData(boolean hasNewVersion){
        if(hasNewVersion){
            //有新的版本需要更新
            tvUpdate.setText("您有新的版本需要更新");
            tvUpdate.setTextColor(getResources().getColor(R.color.red));
            tvUpdate.setClickable(true);
        }else{
            tvUpdate.setText("已是最新版本");
            tvUpdate.setClickable(false);
        }
    }

    //检测版本
    private void checkVersion(){
        Map<String,String> map = new HashMap<>();
        map.put("type","3");
        map.put("version",""+CustomMethod.getAppVersionCode(getContext()));
        VolleyRequest.RequestPost(getContext(), getResources().getString(R.string.VERSION_UPDATE), "VERSION_UPDATE", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                try {
                    Log.d("###", "版本更新：" + arg0);
                    PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                    if(publicModule.getCode() == 0){
                        initData(true);
                        hideLoadingAnim();
                    }else {
                        initData(false);
                        hideLoadingAnim();
                    }
                }catch (Exception e){
                    initData(false);
                    hideLoadingAnim();
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                initData(false);
                hideLoadingAnim();
            }
        });
    }

    private void setListener(){
        layoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager updateManager = new UpdateManager(getContext());
                updateManager.checkUpdateInfo("您有新的版本需要更新，是否需要下载更新？");
            }
        });
    }
}
