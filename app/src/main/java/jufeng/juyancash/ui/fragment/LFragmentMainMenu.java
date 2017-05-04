package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.bean.ShopOrderVo;
import jufeng.juyancash.eventbus.OnEventSelectMenuEvent;
import jufeng.juyancash.eventbus.RefreshStockEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.LoginActivity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentMainMenu extends BaseFragment implements LCustomeRadioGroup.OnCheckedChangeListener {
    private LinearLayout mLinearLayout;
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private MainFragmentListener mOnMenuSelectedListener;
    private Drawable drawable = null;
    private RadioButton radioButton = null;
    private static int oldPosition;
    private static int[] ids = {R.id.radiobutton_0, R.id.radiobutton_1, R.id.radiobutton_2,
            R.id.radiobutton_3, R.id.radiobutton_4, R.id.radiobutton_5,
            R.id.radiobutton_6, R.id.radiobutton_7, R.id.radiobutton_8,
            R.id.radiobutton_9};
    private static ArrayList<Integer> idList;
    private TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    private static int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10};
    private static int[] imageReds = {R.drawable.a1_copy, R.drawable.a2_copy, R.drawable.a3_copy, R.drawable.a4_copy, R.drawable.a5_copy, R.drawable.a6_copy, R.drawable.a7_copy, R.drawable.a8_copy, R.drawable.a9_copy, R.drawable.a10_copy};
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mLCustomeRadioGroup.check(ids[msg.getData().getInt("checkId")], msg.getData());
                    break;
                case 1://有外卖订单
                    int count = DBHelper.getInstance(getActivity().getApplicationContext()).getOnCheckTakeOutOrderCount();
                    if (count == 0) {
                        tv3.setVisibility(TextView.GONE);
                    } else {
                        tv3.setVisibility(TextView.VISIBLE);
                        tv3.setText(String.valueOf(count));
                    }
                    break;
                case 2://消息中心
                    int count1 = DBHelper.getInstance(getActivity().getApplicationContext()).getUnreadMessageCount();
                    if (count1 == 0) {
                        tv6.setVisibility(TextView.GONE);
                    } else {
                        tv6.setVisibility(TextView.VISIBLE);
                        tv6.setText(String.valueOf(count1));
                    }
                    break;
                case 3://刷新打印机连接状态
                    int unConnectCount = DBHelper.getInstance(getActivity().getApplicationContext()).getUnConnectPrinterCount();
                    if (unConnectCount == 0) {
                        tv8.setVisibility(TextView.GONE);
                    } else {
                        tv8.setVisibility(TextView.VISIBLE);
                        tv8.setText(unConnectCount + "个未连接");
                    }
                    break;
                case 4://刷新排号
                    int count2 = DBHelper.getInstance(getActivity().getApplicationContext()).getOnCheckArrangeCount();
                    if (count2 == 0) {
                        tv2.setVisibility(TextView.GONE);
                    } else {
                        tv2.setVisibility(TextView.VISIBLE);
                        tv2.setText(String.valueOf(count2));
                    }
                    break;
                case 5://刷新预定
                    int count3 = DBHelper.getInstance(getActivity().getApplicationContext()).getOnCheckScheduleCount();
                    if (count3 == 0) {
                        tv1.setVisibility(TextView.GONE);
                    } else {
                        tv1.setVisibility(TextView.VISIBLE);
                        tv1.setText(String.valueOf(count3));
                    }
                    break;
                case 6://刷新库存预警状态
                    boolean isWaring = DBHelper.getInstance(getContext().getApplicationContext()).isStockWaring();
                    if (isWaring) {
                        tv4.setVisibility(TextView.VISIBLE);
                        tv4.setText("预警");
                    } else {
                        tv4.setVisibility(TextView.GONE);
                    }
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshStock(RefreshStockEvent event) {
        if (event != null) {
            boolean isWaring = DBHelper.getInstance(getContext().getApplicationContext()).isStockWaring();
            if (isWaring) {
                tv4.setVisibility(TextView.VISIBLE);
                tv4.setText("预警");
            } else {
                tv4.setVisibility(TextView.GONE);
            }
        }
    }

    static {
        oldPosition = 0;
        idList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            idList.add(ids[i]);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setMainMenuHandler(mHandler);
        try {
            mOnMenuSelectedListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "需要实现该接口");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initView(mView);
        setListener();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(1);
        mHandler.sendEmptyMessage(2);
        mHandler.sendEmptyMessage(4);
        mHandler.sendEmptyMessage(5);
        mHandler.sendEmptyMessage(6);
    }

    private void initView(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        tv0 = (TextView) view.findViewById(R.id.tv_0);
        tv1 = (TextView) view.findViewById(R.id.tv_1);
        tv2 = (TextView) view.findViewById(R.id.tv_2);
        tv3 = (TextView) view.findViewById(R.id.tv_3);
        tv4 = (TextView) view.findViewById(R.id.tv_4);
        tv5 = (TextView) view.findViewById(R.id.tv_5);
        tv6 = (TextView) view.findViewById(R.id.tv_6);
        tv7 = (TextView) view.findViewById(R.id.tv_7);
        tv8 = (TextView) view.findViewById(R.id.tv_8);
        tv9 = (TextView) view.findViewById(R.id.tv_9);
        setRadioButtonImg(0);
    }

    private void setListener() {
        mLCustomeRadioGroup.setOnCheckedChangeListener(this);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setIcon(getContext().getResources().getDrawable(R.drawable.logo));
                dialog.setTitle("提示");
                dialog.setMessage("退出前，建议进行检测订单并上传未同步的订单！");
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "直接退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        onLogOut();
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "进行检测", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        uploadData();
                    }
                });
                dialog.show();
            }
        });
    }

    private void uploadData() {
        ArrayList<String> orderIds = new ArrayList<>();
        ArrayList<String> orderIds1 = new ArrayList<>();
        showLoadingAnim("正在上传订单...");
        orderIds.addAll(DBHelper.getInstance(getContext()).getAllOrderIds());
        findOrder(orderIds, orderIds1);
    }

    public void findOrder(final ArrayList<String> orderIds, final ArrayList<String> orderIds1) {
        if (orderIds.size() > 0) {
            showLoadingAnim("还剩下" + orderIds.size() + "单需要检测，请稍等...");
            Map<String, String> map = new HashMap<>();
            map.put("id", orderIds.get(0));
            VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.FIND_ORDER), System.currentTimeMillis() + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                @Override
                public void onSuccess(String arg0) {
                    Log.d("###", "查找订单：" + arg0);
                    try {
                        PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                        if (publicModule.getCode() != 0) {
                            //订单不存在
                            orderIds1.add(orderIds.get(0));
                        }
                        orderIds.remove(0);
                    } catch (Exception e) {
                        //订单不存在
                        orderIds1.add(orderIds.get(0));
                        orderIds.remove(0);
                    }
                    findOrder(orderIds, orderIds1);
                }

                @Override
                public void onError(VolleyError arg0) {
                    //订单不存在
                    orderIds1.add(orderIds.get(0));
                    orderIds.remove(0);
                    findOrder(orderIds, orderIds1);
                }
            });
        } else {
            String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
            upload(partnerCode, orderIds1);
        }
    }

    public void upload(final String partnerCode, final ArrayList<String> orderIds) {
        if (orderIds.size() > 0) {
            showLoadingAnim("还剩下" + orderIds.size() + "单需要上传，请稍等...");
            try {
                long ts = System.currentTimeMillis();
                ShopOrderVo shopOrderVo = new ShopOrderVo(getContext().getApplicationContext(), orderIds.get(0));
                String data = JSON.toJSONString(shopOrderVo);
                Log.d("###", "run: " + data);
                String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
                Map<String, String> map = new HashMap<>();
                map.put("partnerCode", partnerCode);
                map.put("data", data);
                map.put("ts", String.valueOf(ts));
                map.put("sign", sign);
                VolleyRequest.RequestPost(getContext().getApplicationContext(), getContext().getResources().getString(R.string.UPLOAD_SHOPDATA), ts + "", map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                    @Override
                    public void onSuccess(String arg0) {
                        Log.d("###", "上传营业数据：" + arg0);
                        try {
                            PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                            if (publicModule.getCode() == 0) {
                                DBHelper.getInstance(getContext().getApplicationContext()).clearUploadData(orderIds.get(0));
                            }
                            orderIds.remove(0);
                        } catch (JSONException e) {
                            orderIds.remove(0);
                        }
                        upload(partnerCode, orderIds);
                    }

                    @Override
                    public void onError(VolleyError arg0) {
                        orderIds.remove(0);
                        upload(partnerCode, orderIds);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            onLogOut();
            hideLoadingAnim();
        }
    }

    //注销
    public void onLogOut() {
        try {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("isUpdate", false);
            startActivity(intent);
            getActivity().finish();
        } catch (Exception e) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
        int index = idList.indexOf(checkedId);
        if (index != oldPosition) {
//            mOnMenuSelectedListener.selectMenu(index, bundle);
            EventBus.getDefault().post(new OnEventSelectMenuEvent(index, bundle));
            setRadioButtonImg(index);
        }
    }

    //设置radiobutton的图片
    private void setRadioButtonImg(int index) {
        if (oldPosition >= 0 && index >= 0) {
            radioButton = (RadioButton) mLCustomeRadioGroup.getChildAt(oldPosition).findViewById(ids[oldPosition]);
            drawable = getResources().getDrawable(images[oldPosition]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }
        if (index >= 0) {
            radioButton = (RadioButton) mLCustomeRadioGroup.getChildAt(index).findViewById(ids[index]);
            drawable = getResources().getDrawable(imageReds[index]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }
        oldPosition = index;
    }
}
