package jufeng.juyancash.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.UpdateManager;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.jdbc.JDBCSynchroData;
import jufeng.juyancash.myinterface.OnKeyBoardShowInterface;
import jufeng.juyancash.syncdata.SynchroVo;
import jufeng.juyancash.ui.customview.KeyboardUtil1;
import jufeng.juyancash.ui.customview.MyKeyboardView1;
import jufeng.juyancash.ui.fragment.LFragmentEmployeeLogin;
import jufeng.juyancash.ui.fragment.LFragmentStoreLogin;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/14 0014.
 */
public class LoginActivity extends BaseActivity implements OnKeyBoardShowInterface, LFragmentStoreLogin.OnFragmentStoreLoginListener, LFragmentEmployeeLogin.OnEmployeeLoginListener {
    private String partnerCode;
    private KeyboardUtil1 mKeyboardUtil1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -2:
                    hideLoadingAnim();
                    break;
                case -1:
                    setFragment(1);
                    hideLoadingAnim();
                    CustomMethod.showMessage(LoginActivity.this, msg.getData().getString("msg"));
                    break;
                case 0://同步数据中
                    showLoadingAnim("同步数据中，请稍后");
                    break;
                case 1://同步数据成功
                    System.gc();
                    hideLoadingAnim();
                    setFragment(1);
                    break;
                case 2://正在登陆
                    showLoadingAnim("正在登陆");
                    break;
                case 3://登录成功
                    String etPartnerCode = msg.getData().getString("partnerCode");
                    String name = msg.getData().getString("partnerName");
                    SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = spf.edit();
                    if (spf != null) {
                        String partnerCode = spf.getString("partnerCode", null);
                        if (partnerCode != null && !partnerCode.equals(etPartnerCode)) {
                            //更换商家清空数据库
                            DBHelper.getInstance(getApplicationContext()).dropAllTable();
                            editor1.clear();
                        }
                    }
                    editor1.putString("partnerName", name);
                    editor1.putString("partnerCode", etPartnerCode);
                    editor1.commit();
                    hideLoadingAnim();
                    syncData(etPartnerCode);
                    break;
                case 4:
                    //遍历账单，并将未完成的账单对应的桌位状态修改为使用中
                    new AsyncDataTask().execute();
                    break;
                case 5:
                    checkVersion();
                    break;
            }
        }
    };

    class AsyncDataTask extends AsyncTask<Object, Integer, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            DBHelper.getInstance(getApplicationContext()).deleteSomeData();
            DBHelper.getInstance(getApplicationContext()).queryAllOrderData();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            hideLoadingAnim();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("restart", true);
            startActivity(intent);
            finish();
        }
    }

    private void syncData(String partnerCode) {
        handler.sendEmptyMessage(0);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode + partnerCode + ts + getResources().getString(R.string.APP_KEY));
        HashMap<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("data", partnerCode);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(getApplicationContext(), getResources().getString(R.string.SYNC_ALL_DATA), "SYNC_ALL_DATA", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "onSuccess: " + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        SynchroVo synchroVo = JSON.parseObject(publicModule.getData(), SynchroVo.class);
                        if (synchroVo != null) {
                            new JDBCSynchroData(LoginActivity.this, synchroVo, handler).execute();
                        } else {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", "同步数据失败，请稍后重试");
                            msg.setData(bundle);
                            msg.what = -1;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", publicModule.getMessage());
                        msg.setData(bundle);
                        msg.what = -1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "同步数据失败，请稍后重试");
                    msg.setData(bundle);
                    msg.what = -1;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                Log.d("###", "同步数据失败");
                arg0.printStackTrace();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("msg", "同步数据失败，请稍后重试");
                msg.setData(bundle);
                msg.what = -1;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        setContentView(R.layout.view_null);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        partnerCode = spf.getString("partnerCode", null);
        if (partnerCode == null) {
            setFragment(0);
        } else {
            showLoadingAnim("版本检测...");
            handler.sendMessageDelayed(handler.obtainMessage(5), 1000);
        }
        showDoubleDisplay();
    }

    private void initView() {
        mKeyboardUtil1 = new KeyboardUtil1(LoginActivity.this, (MyKeyboardView1) findViewById(R.id.keyboard), null, 2);
    }

    private void initData() {
        boolean isUpdate = getIntent().getBooleanExtra("isUpdate", true);
        if (isUpdate) {
            syncData(partnerCode);
        } else {
            setFragment(1);
        }
    }

    //检测版本
    private void checkVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        map.put("version", "" + CustomMethod.getAppVersionCode(getApplicationContext()));
        VolleyRequest.RequestPost(getApplicationContext(), getResources().getString(R.string.VERSION_UPDATE), "VERSION_UPDATE", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "版本更新：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        hideLoadingAnim();
                        UpdateManager updateManager = new UpdateManager(LoginActivity.this);
                        updateManager.checkUpdateInfo(publicModule.getData(), new UpdateManager.OnUpdateManagerListener() {
                            @Override
                            public void onCancle() {
                                initData();
                            }
                        });
                    } else {
                        initData();
                        hideLoadingAnim();
                    }
                } catch (Exception e) {
                    initData();
                    hideLoadingAnim();
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                initData();
                hideLoadingAnim();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        if (partnerCode == null) {
            setFragment(0);
        } else {
            boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);
            if (isUpdate) {
                syncData(partnerCode);
            } else {
                setFragment(1);
            }
        }
    }

    private void setFragment(int tag) {
        switch (tag) {
            case 0:
                loadRootFragment(R.id.container, LFragmentStoreLogin.newInstance());
                break;
            case 1:
                loadRootFragment(R.id.container, LFragmentEmployeeLogin.newInstance());
                break;
        }
    }

    @Override
    public void showProgress() {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void storeLoginSuccess(String partnerCode, String name) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("partnerCode", partnerCode);
        bundle.putString("partnerName", name);
        msg.setData(bundle);
        msg.what = 3;
        handler.sendMessage(msg);
    }

    @Override
    public void storeLoginFaild() {
        handler.sendEmptyMessage(-2);
    }

    @Override
    public void storeLoginCancle() {
        SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        if (partnerCode == null) {
            finish();
        } else {
            setFragment(1);
        }
    }

    //员工登陆
    @Override
    public void employeeShowProgress() {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void employeeLoginSuccess(int version) {
        handler.sendEmptyMessage(4);
    }

    @Override
    public void employeeLoginFailed() {
        handler.sendEmptyMessage(-2);
    }

    @Override
    public void changeStore() {
        setFragment(0);
    }

    @Override
    public void showKeyboard(EditText editText, int type) {
        mKeyboardUtil1.setEditText(editText);
    }

    //双屏异显
    public void showDoubleDisplay() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        if (displays.length > 1) {
            DifferentDisplay mPresentation = new DifferentDisplay(getApplicationContext(), displays[1], 0, null, false);//displays[1]是副屏
            mPresentation.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mPresentation.show();
        }
    }
}
