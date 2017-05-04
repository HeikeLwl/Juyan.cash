package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/10/27.
 */

public class LFragmentTurnOverAccount extends BaseFragment {
    private EditText etAccount,etPWD;
    private Button btnConfirm;
    private OnTurnOverAccountListener mOnTurnOverAccountListener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover_account,container,false);
        initView(mView);
        setListener();
        return mView;
    }

    private void initView(View view){
        etAccount = (EditText) view.findViewById(R.id.et_account);
        etPWD = (EditText) view.findViewById(R.id.et_pwd);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
    }

    private void setListener(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = etAccount.getText().toString();
                String employeePwd = etPWD.getText().toString();
                if (mOnTurnOverAccountListener != null && !employeeName.isEmpty() && !employeePwd.isEmpty()) {
                    EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeByAccount(employeeName,MD5Util.getMD5String(employeePwd));
                    if(employeeEntity != null) {
                        mOnTurnOverAccountListener.onAccountTurnOverClick(employeeEntity);
                    }else{
                        Snackbar.make(btnConfirm,"用户名或密码错误",Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(btnConfirm,"请填写完整信息",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setOnTurnOverAccountListener(OnTurnOverAccountListener listener){
        mOnTurnOverAccountListener = listener;
    }

    public interface OnTurnOverAccountListener{
        void onAccountTurnOverClick(EmployeeEntity employeeEntity);
    }
}
