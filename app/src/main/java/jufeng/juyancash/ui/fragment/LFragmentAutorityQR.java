package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.TurnoverBean;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/10/27.
 */

public class LFragmentAutorityQR extends BaseFragment {
    public static String ACTION_INTENT_AUTHORITY = "jufeng.juyancash.intent.authority";
    private ImageView ivQR;
    private int mType;
    private long ts;
    private String partnerCode;
    private AuthorityBroadcastReceiver mAuthorityBroadcastReceiver;
    private OnQRAuthorityListener mOnQRAuthorityListener;
    private Bitmap mBitmap;

    @Override
    public void onDestroyView() {
        Log.d("###", "权限验证销毁");
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
        getContext().unregisterReceiver(mAuthorityBroadcastReceiver);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_authority_qr,container,false);
        initView(mView);
        registerBroadcastReceiver();
        return mView;
    }

    private void initView(View view) {
        ivQR = (ImageView) view.findViewById(R.id.iv_qr);
        ts = System.currentTimeMillis();
        mType = -1;
        if (getArguments() != null)
            mType = getArguments().getInt("type");
        initData();
    }

    private void initData(){
        partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        if(partnerCode != null && mType > 0){
            setQr();
        }else{
            Snackbar.make(ivQR,"生成二维码失败",Snackbar.LENGTH_LONG).show();
        }
    }

    private void setQr() {
        final String filePath = CustomMethod.getFileRoot(getContext()) + File.separator
                + "qr_turnover" + ".png";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = CustomMethod.createQRImage(getContext(), getContext().getResources().getStringArray(R.array.tags)[mType-1]+":"+partnerCode+","+mType+","+ts, 250, 250,
                        null,
                        filePath);
                if (success) {
                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBitmap = BitmapFactory.decodeFile(filePath);
                            ivQR.setImageBitmap(mBitmap);
                        }
                    });
                }
            }
        }).start();
    }

    //注册广播接收器
    private void registerBroadcastReceiver() {
        mAuthorityBroadcastReceiver = new AuthorityBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_AUTHORITY);
        getContext().registerReceiver(mAuthorityBroadcastReceiver, filter);
    }

    class AuthorityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_INTENT_AUTHORITY)){
                TurnoverBean turnoverBean = intent.getParcelableExtra("turnoverBean");
                EmployeeEntity employeeEntity;
                if(turnoverBean != null && turnoverBean.getType().equals(String.valueOf(mType)) && turnoverBean.getPartnerCode().equals(partnerCode)
                        && turnoverBean.getTs().equals(String.valueOf(ts)) && turnoverBean.getEmployeeId() != null
                        && (employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeById(turnoverBean.getEmployeeId())) != null){
                    if(mOnQRAuthorityListener != null) {
                        mOnQRAuthorityListener.onQRAuthoritySuccess(ivQR, employeeEntity);
                    }
                }else{
                    Snackbar.make(ivQR,"权限验证失败，请重新尝试",Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    public void setOnQRAuthorityListener(OnQRAuthorityListener listener){
        this.mOnQRAuthorityListener = listener;
    }

    public interface OnQRAuthorityListener{
        void onQRAuthoritySuccess(View view, EmployeeEntity employeeEntity);
    }

}
