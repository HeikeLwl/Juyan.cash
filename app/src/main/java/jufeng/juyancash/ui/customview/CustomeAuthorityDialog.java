package jufeng.juyancash.ui.customview;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.TurnoverBean;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeAuthorityDialog extends DialogFragment{
    private ClearEditText etAccount, etPWD;
    private Button btnConfirm;
    private ImageButton ibCancle;
    private OnAuthorityListener mOnAuthorityListener;
    public static String ACTION_INTENT_AUTHORITY = "jufeng.juyancash.intent.authority";
    private ImageView ivQR;
    private int mType;
    private long ts;
    private String partnerCode;
    private AuthorityBroadcastReceiver mAuthorityBroadcastReceiver;
    private Bitmap mBitmap;
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onDestroy() {
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap = null;
        }
        getContext().unregisterReceiver(mAuthorityBroadcastReceiver);
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        View view = inflater.inflate(R.layout.dialog_authority_layout, container);
        initView(view);
        initData();
        setListener();
        registerBroadcastReceiver();
        return view;
    }

    private void initData(){
        ts = System.currentTimeMillis();
        mType = -1;
        if(getArguments() != null) {
            mType = getArguments().getInt("type");
        }
        partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode",null);
        if(partnerCode != null && mType > 0){
            setQr();
        }else{
            Snackbar.make(ivQR,"生成二维码失败",Snackbar.LENGTH_LONG).show();
        }
    }

    private void initView(View view) {
        etAccount = (ClearEditText) view.findViewById(R.id.et_account);
        etPWD = (ClearEditText) view.findViewById(R.id.et_pwd);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        ibCancle = (ImageButton) view.findViewById(R.id.ib_cancle);
        ivQR = (ImageView) view.findViewById(R.id.iv_qr);
    }

    private void setListener(){
        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAuthorityListener != null) {
                    mOnAuthorityListener.onAuthorityCancle();
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = etAccount.getText().toString();
                String employeePwd = etPWD.getText().toString();
                if (mOnAuthorityListener != null && !employeeName.isEmpty() && !employeePwd.isEmpty()) {
                    EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeByAccount(employeeName, MD5Util.getMD5String(employeePwd));
                    if (employeeEntity != null) {
                        mOnAuthorityListener.onAuthoritySuccess(etAccount,employeeEntity);
                    } else {
                        Snackbar.make(btnConfirm, "用户名或密码错误", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(btnConfirm, "请填写完整信息", Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
                    if(mOnAuthorityListener != null) {
                        mOnAuthorityListener.onAuthoritySuccess(ivQR, employeeEntity);
                    }
                }else{
                    Snackbar.make(ivQR,"权限验证失败，请重新尝试",Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    public void setOnAuthorityListener(OnAuthorityListener listener){
        this.mOnAuthorityListener = listener;
    }

    public interface OnAuthorityListener{
        void onAuthoritySuccess(View view,EmployeeEntity employeeEntity);
        void onAuthorityCancle();
    }
}
