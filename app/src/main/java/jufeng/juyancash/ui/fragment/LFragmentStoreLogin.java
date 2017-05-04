package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.Partner;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.myinterface.OnKeyBoardShowInterface;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/8/18.
 */
public class LFragmentStoreLogin extends BaseFragment {
    private EditText etPartnerCode, etPassword;
    private Button btnConfirm, btnCancle;
    private OnFragmentStoreLoginListener mOnFragmentStoreLoginListener;
    private OnKeyBoardShowInterface mOnKeyBoardShowInterface;

    public static LFragmentStoreLogin newInstance(){
        LFragmentStoreLogin fragment = new LFragmentStoreLogin();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnFragmentStoreLoginListener = (OnFragmentStoreLoginListener) context;
            mOnKeyBoardShowInterface = (OnKeyBoardShowInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_store_login, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        etPartnerCode = (EditText) view.findViewById(R.id.et_partnercode);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
    }

    public void setNewParam(){
        initData();
    }

    private void initData(){
        CustomMethod.setMyInputType(etPartnerCode,getActivity());
        CustomMethod.setMyInputType(etPassword,getActivity());

        etPartnerCode.requestFocus();
        if(mOnKeyBoardShowInterface != null){
            mOnKeyBoardShowInterface.showKeyboard(etPartnerCode,1);
        }

        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        if (partnerCode == null) {
            btnCancle.setText("关闭");
        } else {
            etPartnerCode.setText(partnerCode);
            btnCancle.setText("取消");
        }
    }

    public void setListener() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentStoreLoginListener.showProgress();
                Map<String, String> map = new HashMap<>();
                map.put("loginName", etPartnerCode.getText().toString());
                map.put("password", MD5Util.getMD5String(etPassword.getText().toString()));
                map.put("jpushId", etPartnerCode.getText().toString() +etPartnerCode.getText().toString()+etPartnerCode.getText().toString());
                VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.LOGIN_URL), "LOGIN_URL", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                    @Override
                    public void onSuccess(String arg0) {
                        Log.d("###", "onSuccess: " + arg0);
                        try {
                            PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                            if (publicModule.getCode() == 0) {
                                if (publicModule.getData() != null) {
                                    Partner partner = JSON.parseObject(publicModule.getData(), Partner.class);
                                    mOnFragmentStoreLoginListener.storeLoginSuccess(etPartnerCode.getText().toString(), partner.getName());
                                }
                            } else {
                                mOnFragmentStoreLoginListener.storeLoginFaild();
                                CustomMethod.showMessage(getContext(),publicModule.getMessage());
                            }
                        }catch (Exception e){
                            mOnFragmentStoreLoginListener.storeLoginFaild();
                            CustomMethod.showMessage(getContext(),"商家登录失败，请重新尝试");
                        }
                    }

                    @Override
                    public void onError(VolleyError arg0) {
                        arg0.printStackTrace();
                        mOnFragmentStoreLoginListener.storeLoginFaild();
                        CustomMethod.showMessage(getContext(),"商家登录失败，请重新尝试");
                    }
                });
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentStoreLoginListener.storeLoginCancle();
            }
        });

        etPartnerCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPartnerCode.requestFocus();
                if(mOnKeyBoardShowInterface != null){
                    mOnKeyBoardShowInterface.showKeyboard(etPartnerCode,1);
                }
                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPassword.requestFocus();
                if(mOnKeyBoardShowInterface != null){
                    mOnKeyBoardShowInterface.showKeyboard(etPassword,0);
                }
                return false;
            }
        });
    }

    public interface OnFragmentStoreLoginListener {
        void showProgress();

        void storeLoginSuccess(String partnerCode,String name);

        void storeLoginFaild();

        void storeLoginCancle();
    }
}
