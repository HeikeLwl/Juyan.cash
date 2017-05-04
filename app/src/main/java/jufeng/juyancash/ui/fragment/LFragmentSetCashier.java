package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jufeng.juyancash.R;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/10/5.
 */

public class LFragmentSetCashier extends BaseFragment{
    private TextView tvIp;
    private EditText etPwd;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_set_cashier,container,false);
        initView(mView);
        setListener();
        return mView;
    }

    private void initView(View view){
        tvIp = (TextView) view.findViewById(R.id.tv_ip);
        etPwd = (EditText) view.findViewById(R.id.et_pwd);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        SharedPreferences spf = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String syncPwd = spf.getString("syncPwd","8888");
        etPwd.setText(syncPwd);
        String ip = CustomMethod.getLocalHostIp();
        tvIp.setText("本机IP地址："+ip);
    }

    private void setListener(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPwd.getText().toString() != null && etPwd.getText().toString().length() > 0){
                    SharedPreferences spf = getActivity().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("syncPwd",etPwd.getText().toString());
                    editor.commit();
                    Toast.makeText(getActivity(), "同步密码设置成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
