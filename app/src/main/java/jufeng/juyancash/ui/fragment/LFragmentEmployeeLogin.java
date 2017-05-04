package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.CashierVersionVo;
import jufeng.juyancash.bean.ChangeStoreVersion;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.eventbus.ChangeStoreEvent;
import jufeng.juyancash.eventbus.EmployeeLoginSuccessEvent;
import jufeng.juyancash.eventbus.LoadingDialogEvent;
import jufeng.juyancash.myinterface.OnKeyBoardShowInterface;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2016/8/18.
 */
public class LFragmentEmployeeLogin extends BaseFragment {
    private Spinner mSpinner;
    private EditText etEmployeeName, etEmployeePwd;
    private TextView tvStoreName;
    private Button btnLogin;
    private CheckBox cbSavePwd;
    private OnKeyBoardShowInterface mOnKeyBoardShowInterface;

    public static LFragmentEmployeeLogin newInstance() {
        LFragmentEmployeeLogin fragment = new LFragmentEmployeeLogin();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnKeyBoardShowInterface = (OnKeyBoardShowInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_employee_login, container, false);
        initView(mView);
        getStoreVersion();
        setListener();
        return mView;
    }

    @Override
    public void onDestroyView() {
        mOnKeyBoardShowInterface = null;
        super.onDestroyView();
    }

    private void initView(View view) {
        tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        etEmployeeName = (EditText) view.findViewById(R.id.et_employee_name);
        etEmployeePwd = (EditText) view.findViewById(R.id.et_employee_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        cbSavePwd = (CheckBox) view.findViewById(R.id.cb_save_pwd);
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
    }

    public void setNewParam() {
        initData();
    }

    public void getStoreVersion() {
        EventBus.getDefault().post(new LoadingDialogEvent(true, "正在获取营业模式..."));
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.GET_STORE_VERSION), String.valueOf(System.currentTimeMillis()), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        CashierVersionVo vo = JSON.parseObject(publicModule.getData(), CashierVersionVo.class);
                        if (vo != null) {
                            SharedPreferences spf = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putInt("storeversion", vo.getCashierVersion());
                            editor.putString("partnerName", vo.getPartnerName());
                            editor.commit();

                        }
                    }
                } catch (Exception e) {

                }
                EventBus.getDefault().post(new LoadingDialogEvent(false, ""));
                initData();
            }

            @Override
            public void onError(VolleyError arg0) {
                EventBus.getDefault().post(new LoadingDialogEvent(false, ""));
                initData();
            }
        });
    }

    private void initData() {
        CustomMethod.setMyInputType(etEmployeeName, getActivity());
        CustomMethod.setMyInputType(etEmployeePwd, getActivity());

        etEmployeeName.requestFocus();
        if (mOnKeyBoardShowInterface != null) {
            mOnKeyBoardShowInterface.showKeyboard(etEmployeeName, 0);
        }

        SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        mSpinner.setSelection(spf.getInt("storeversion", 0));
        String partnerName = spf.getString("partnerName", null);
        if (partnerName != null) {
            tvStoreName.setText(partnerName);
        }
        String employeeName = spf.getString("employeeName", null);
        if (employeeName != null) {
            etEmployeeName.setText(employeeName);
        }
        String employeePwd = spf.getString("employeePsd", null);
        if (employeePwd != null) {
            cbSavePwd.setChecked(true);
            etEmployeePwd.setText(employeePwd);
        } else {
            cbSavePwd.setChecked(false);
        }
    }

    private void setListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = etEmployeeName.getText().toString();
                String employeePwd = etEmployeePwd.getText().toString();
                if (!employeeName.isEmpty() && !employeePwd.isEmpty()) {
                    Object[] params = {employeeName, employeePwd, cbSavePwd.isChecked(), mSpinner.getSelectedItemPosition()};
                    new EmployeeAsyncTask().execute(params);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeStoreEvent());
            }
        });

        etEmployeeName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etEmployeeName.requestFocus();
                if (mOnKeyBoardShowInterface != null) {
                    mOnKeyBoardShowInterface.showKeyboard(etEmployeeName, 0);
                }
                return false;
            }
        });

        etEmployeePwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etEmployeePwd.requestFocus();
                if (mOnKeyBoardShowInterface != null) {
                    mOnKeyBoardShowInterface.showKeyboard(etEmployeePwd, 0);
                }
                return false;
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("###", "onItemSelected: " + i);
                if (mSpinner.getTag() == null) {
                    mSpinner.setTag(i);
                } else {
                    syncStoreVersion(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //修改营业模式
    private void syncStoreVersion(int version) {
        showLoadingAnim("正在同步营业模式...");
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        long ts = System.currentTimeMillis();
        ChangeStoreVersion changeStoreVersion = new ChangeStoreVersion(version, partnerCode);
        String data = JSON.toJSONString(changeStoreVersion);
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("ts", String.valueOf(ts));
        map.put("partnerCode", partnerCode);
        map.put("data", data);
        map.put("sign", sign);
        VolleyRequest.RequestPost(getContext(), getResources().getString(R.string.CHANGE_STORE_VERSION), String.valueOf(ts), map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                hideLoadingAnim();
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        CustomMethod.showMessage(getContext(), "营业模式同步成功");
                    } else {
                        String message = publicModule.getMessage() == null ? "营业模式同步失败，请重新尝试！" : publicModule.getMessage();
                        CustomMethod.showMessage(getContext(), message);
                    }
                } catch (Exception e) {
                    CustomMethod.showMessage(getContext(), "营业模式同步失败，请重新尝试！");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(), "同步营业模式失败，请重新尝试！");
            }
        });
    }

    public class EmployeeAsyncTask extends AsyncTask<Object, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingAnim("员工登陆...");
        }

        @Override
        protected Integer doInBackground(Object[] params) {
            EmployeeEntity employeeEntity = DBHelper.getInstance(getActivity().getApplicationContext()).employeeLogin((String) params[0], MD5Util.getMD5String((String) params[1]));
            if (employeeEntity == null) {
                return -1;
            } else {
                if (employeeEntity.getAuthCashier() == 1) {
                    SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("employeeName", employeeEntity.getLoginName());
                    editor.putString("employeeId", employeeEntity.getEmployeeId());
                    if ((Boolean) params[2]) {
                        editor.putString("employeePsd", (String) params[1]);
                    } else {
                        editor.putString("employeePsd", null);
                    }
                    editor.putInt("storeversion", (Integer) params[3]);
                    editor.commit();
                    return 0;
                } else {
                    return -2;
                }
            }
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            hideLoadingAnim();
            switch (result) {
                case -2:
                    Snackbar.make(etEmployeeName, "该员工无收银权限", Snackbar.LENGTH_LONG).show();
                    break;
                case -1:
                    Snackbar.make(etEmployeePwd, "员工账号或密码错误", Snackbar.LENGTH_LONG).show();
                    break;
                case 0:
                    EventBus.getDefault().post(new EmployeeLoginSuccessEvent());
                    break;
            }
        }
    }
}
