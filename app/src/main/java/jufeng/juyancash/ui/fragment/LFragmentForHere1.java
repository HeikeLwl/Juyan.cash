package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.TablesAdapter;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.ui.customview.CustomeDialog;
import jufeng.juyancash.ui.customview.CustomeOpenDialog;
import jufeng.juyancash.ui.customview.CustomeOrderMessageDialog;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.PageIndicatorView;
import jufeng.juyancash.ui.customview.view.HorizontalPageLayoutManager;
import jufeng.juyancash.ui.customview.view.PagingScrollHelper;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2017/2/27.
 */

public class LFragmentForHere1 extends BaseFragment implements CustomeDialog.OnDialogItemClickListener,PagingScrollHelper.onPageChangeListener{
    PagingScrollHelper scrollHelper = new PagingScrollHelper();
    private LCustomeRadioGroup mLCustomeRadioGroup0;
    RecyclerView recyclerView;
    private ArrayList<AreaEntity> areas;
    private RadioButton[] mRadioButtons;
    private TextView tvUnreadMessage;
    private CardView mCardView;
    private Animation anim;
    private ArrayList<TableEntity> mAllTables;
    private CustomeDialog dialog;
    private MainFragmentListener mMainFragmentListener;
    private LCustomeRadioGroup mLCustomeRadioGroup;//选择不同状态的桌位
    private PageIndicatorView mPageIndicatorView;
    private TablesAdapter mTablesAdapter;
    private CustomLoadingProgress mCustomLoadingProgress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    new GetTablesAsyncTask().execute();
                    break;
                case 1:
                    setOrderMessage();
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            setOrderMessage();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setForHereHandler(mHandler);
        try {
            mMainFragmentListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_forhere_1, container, false);
        initView(mView);
        initDataes();
        addAreaMenus();
        setListener();
        setRecyclerView();
        new GetTablesAsyncTask().execute();
        return mView;
    }

    private void setRecyclerView(){
        recyclerView.setAdapter(mTablesAdapter);
        recyclerView.setFocusable(false);
        recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);
        HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(4,6);
        if (horizontalPageLayoutManager != null) {
            recyclerView.setLayoutManager(horizontalPageLayoutManager);
            scrollHelper.updateLayoutManger();
        }
    }

    class GetTablesAsyncTask extends AsyncTask<Object,Integer,ArrayList<TableEntity>>{
        String areaId;
        int status;

        public GetTablesAsyncTask(){
            int checkId = mLCustomeRadioGroup0.getCheckedRadioButtonId();
            if (mRadioButtons.length >= 0 && checkId >= 0) {
                areaId = (String) mRadioButtons[checkId].getTag();
            }
            int checkId1 = mLCustomeRadioGroup.getCheckedRadioButtonId();
            switch (checkId1){
                case R.id.radiobutton_0:
                    status = -1;
                    break;
                case R.id.radiobutton_1:
                    status = 0;
                    break;
                case R.id.radiobutton_2:
                    status = 1;
                    break;
                case R.id.radiobutton_3:
                    status = 2;
                    break;
                case R.id.radiobutton_4:
                    status = 3;
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<TableEntity> doInBackground(Object... params) {
            return DBHelper.getInstance(getActivity().getApplicationContext()).queryTableData(areaId,status);
        }

        @Override
        protected void onPostExecute(ArrayList<TableEntity> tableEntities) {
            mAllTables.clear();
            mAllTables.addAll(tableEntities);
            mTablesAdapter.update(tableEntities);
            refreshTableStatus();
            mPageIndicatorView.initIndicator(mAllTables.size()%24 == 0 ? mAllTables.size()/24:mAllTables.size()/24+1);
            mPageIndicatorView.setSelectedPage(0);
            super.onPostExecute(tableEntities);
        }
    }

    //获取区域和桌位数据
    private void initDataes() {
        anim = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.toast_anim);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        mAllTables = new ArrayList<>();
        areas = new ArrayList<>();
        areas.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryAreaData());
        mTablesAdapter = new TablesAdapter(getContext(),mAllTables);
    }

    private void setOrderMessage() {
        int unreadMessageCount = DBHelper.getInstance(getContext().getApplicationContext()).getAllWxOrderMessageCount();
        if (unreadMessageCount > 0) {
            mCardView.setVisibility(CardView.VISIBLE);
            tvUnreadMessage.setText(unreadMessageCount + "条订单消息");
            mCardView.setAnimation(anim);
            anim.start();
        } else {
            tvUnreadMessage.setText(unreadMessageCount + "条订单消息");
            anim.cancel();
            mCardView.clearAnimation();
            mCardView.setVisibility(CardView.GONE);
        }
    }

    private void initView(View view) {
        mLCustomeRadioGroup0 = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup0);
        tvUnreadMessage = (TextView) view.findViewById(R.id.tv_unread_message);
        mCardView = (CardView) view.findViewById(R.id.cardview);
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        mPageIndicatorView = (PageIndicatorView) view.findViewById(R.id.indicator);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void addAreaMenus() {
        mRadioButtons = new RadioButton[areas.size()];
        LCustomeRadioGroup.LayoutParams params = new LCustomeRadioGroup.LayoutParams(LCustomeRadioGroup.LayoutParams.MATCH_PARENT, LCustomeRadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < areas.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity().getApplicationContext());
            radioButton.setId(i);
            radioButton.setTag(areas.get(areas.size() - 1 - i).getAreaId());
            radioButton.setBackgroundResource(R.drawable.area_menu_selector_gray);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setText(areas.get(areas.size() - 1 - i).getAreaName());
            radioButton.setTextSize(18);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setPadding(4, 12, 4, 12);
            mLCustomeRadioGroup0.addView(radioButton, params);
            mRadioButtons[i] = radioButton;
        }
        if (mRadioButtons.length > 0)
            mRadioButtons[0].setChecked(true);
        setRadioButtonTextColor(0);
    }

    private void setListener() {
        tvUnreadMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomeOrderMessageDialog dialog = new CustomeOrderMessageDialog(getContext());
                dialog.setOnOrderMessageClearListener(new CustomeOrderMessageDialog.OnOrderMessageClearListener() {
                    @Override
                    public void onClearAllOrderMessage() {
                        setOrderMessage();
                    }
                });
            }
        });

        //不同的区域切换
        mLCustomeRadioGroup0.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                setRadioButtonTextColor(checkedId);
                new GetTablesAsyncTask().execute();
            }
        });

        mTablesAdapter.setOnRecyclerViewItemClickListener(new TablesAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(TableEntity tableEntity) {
                showDialog(tableEntity);
            }
        });

        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                new GetTablesAsyncTask().execute();
            }
        });
    }

    private void setRadioButtonTextColor(int index) {
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i == index) {
                mRadioButtons[i].setTextColor(getResources().getColor(R.color.theme_0_red_0));
            } else {
                mRadioButtons[i].setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    //刷新桌位
    private void refreshTableStatus() {
        setOrderMessage();
        String areaId = null;
        int checkId = mLCustomeRadioGroup0.getCheckedRadioButtonId();
        if (mRadioButtons.length >= 0 && checkId >= 0) {
            areaId = (String) mRadioButtons[checkId].getTag();
        }

        int[] statusCounts = DBHelper.getInstance(getActivity().getApplicationContext()).getTableStatusCount(areaId);
        RadioButton radioButton0 = (RadioButton) mLCustomeRadioGroup.findViewById(R.id.radiobutton_0);
        radioButton0.setText("全部(" + statusCounts[0] + ")");
        RadioButton radioButton1 = (RadioButton) mLCustomeRadioGroup.findViewById(R.id.radiobutton_1);
        radioButton1.setText("空闲(" + statusCounts[1] + ")");
        RadioButton radioButton2 = (RadioButton) mLCustomeRadioGroup.findViewById(R.id.radiobutton_2);
        radioButton2.setText("使用(" + statusCounts[2] + ")");
        RadioButton radioButton3 = (RadioButton) mLCustomeRadioGroup.findViewById(R.id.radiobutton_3);
        radioButton3.setText("预定(" + statusCounts[3] + ")");
        RadioButton radioButton4 = (RadioButton) mLCustomeRadioGroup.findViewById(R.id.radiobutton_4);
        radioButton4.setText("已结账(" + statusCounts[4] + ")");
    }

    @Override
    public void onPageChange(int index) {
        mPageIndicatorView.setSelectedPage(index);
    }

    //点击桌位时弹出对话框
    public void showDialog(TableEntity tableEntity) {
        if (dialog == null) {
            dialog = new CustomeDialog(getActivity(), tableEntity);
            dialog.setListener(this);
        } else {
            dialog.dismiss();
            dialog = null;
        }
    }

    //**********************************对话框中的监听回调函数
    @Override
    public void openTable(final TableEntity tableEntity, String orderId) {//开台
        dialog.dismiss();
        dialog = null;
        final CustomeOpenDialog openDialog = new CustomeOpenDialog(getActivity(), tableEntity);
        openDialog.setOpenTableListener(new CustomeOpenDialog.OpenTableListener() {
            @Override
            public void onOpenClick() {
                openDialog.dismiss();
                //插入一条账单数据
                int guestCount = openDialog.getMealsNum().isEmpty() ? tableEntity.getTableSeat() : Integer.valueOf(openDialog.getMealsNum());
                String orderId = DBHelper.getInstance(getActivity().getApplicationContext()).insertTableOrder(getActivity().getApplicationContext(), tableEntity, guestCount, openDialog.getRemarkValue());
                //修改桌位状态
                DBHelper.getInstance(getActivity().getApplicationContext()).replaceTableStatus(tableEntity.getTableId(), 1);
                mMainFragmentListener.openTable(tableEntity.getTableId(), orderId);
            }

            @Override
            public void onCancleClick() {
                openDialog.dismiss();
            }
        });
    }

    @Override
    public void scheduleTable(TableEntity tableEntity) {//预定
        mMainFragmentListener.openSchedule(tableEntity);
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void quickOpenTable(TableEntity tableEntity, String orderId) {//快速开台
        //插入一条账单数据
        int guestCount = tableEntity.getTableSeat();
        String mOrderId = DBHelper.getInstance(getActivity().getApplicationContext()).insertTableOrder(getActivity().getApplicationContext(), tableEntity, guestCount, null);
        //修改桌位状态
        tableEntity.setTableStatus(1);
        mTablesAdapter.updateItem(tableEntity);
        refreshTableStatus();
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.openTable(tableEntity.getTableId(), mOrderId);
    }

    @Override
    public void orderDish(TableEntity tableEntity, String orderId) {//点餐
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.openTable(tableEntity.getTableId(), orderId);
        mMainFragmentListener.syncDataLater(orderId);
    }

    @Override
    public void jointTable(TableEntity tableEntity, String orderId) {//合台
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.joinTable(tableEntity.getTableId(), orderId);
        mMainFragmentListener.syncDataLater(orderId);
    }

    @Override
    public void cancleJoinTable(TableEntity tableEntity, String orderId) {//取消合台
        dialog.dismiss();
        dialog = null;
        DBHelper.getInstance(getActivity().getApplicationContext()).cancleJoinTable(tableEntity.getTableId());
        refreshTableStatus();
        new GetTablesAsyncTask().execute();
        mMainFragmentListener.unlockTable(tableEntity);
    }

    @Override
    public void andTable(TableEntity tableEntity, String orderId) {//并单
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void cancleJoinOrder(TableEntity tableEntity, String orderId) {
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void openAgain(TableEntity tableEntity, String orderId) {//再开一单
        orderId = DBHelper.getInstance(getActivity().getApplicationContext()).insertOrderAgain(getActivity().getApplicationContext(), tableEntity);
        mMainFragmentListener.openTable(tableEntity.getTableId(), orderId);
        mMainFragmentListener.syncDataLater(orderId);
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void replaceTable(TableEntity tableEntity, String orderId) {//更换桌位
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.changeTable(tableEntity.getTableId(), orderId);
        mMainFragmentListener.syncDataLater(orderId);
    }

    @Override
    public void presentDish(final TableEntity tableEntity, final String orderId) {//赠菜
        dialog.dismiss();
        dialog = null;
        if (orderId != null) {
            if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasOrderedDish(orderId)) {
                //没有已下单商品
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasUnOrderedDish(orderId)) {
                //没有未下单商品
                mMainFragmentListener.presentDish(orderId, tableEntity.getTableId(), 2);
                mMainFragmentListener.syncDataLater(orderId);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("有未下单商品，是否继续赠菜？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续赠菜", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        mMainFragmentListener.presentDish(orderId, tableEntity.getTableId(), 2);
                        mMainFragmentListener.syncDataLater(orderId);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后再来", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public void retreatDish(final TableEntity tableEntity, final String orderId) {//退菜
        dialog.dismiss();
        dialog = null;
        if (orderId != null) {
            if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasOrderedDish(orderId)) {
                //没有已下单商品
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasUnOrderedDish(orderId)) {
                //没有未下单商品
                mMainFragmentListener.retreatDish(orderId, tableEntity.getTableId(), 1);
                mMainFragmentListener.syncDataLater(orderId);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("有未下单商品，是否继续退菜？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续退菜", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        mMainFragmentListener.retreatDish(orderId, tableEntity.getTableId(), 1);
                        mMainFragmentListener.syncDataLater(orderId);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后再来", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public void remindDish(final TableEntity tableEntity, final String orderId) {//催菜
        dialog.dismiss();
        dialog = null;
        if (orderId != null) {
            if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasOrderedDish(orderId)) {
                //没有已下单商品
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasUnOrderedDish(orderId)) {
                //没有未下单商品
                mMainFragmentListener.remindDish(orderId, tableEntity.getTableId(), 3);
                mMainFragmentListener.syncDataLater(orderId);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("有未下单商品，是否继续催菜？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续催菜", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        mMainFragmentListener.remindDish(orderId, tableEntity.getTableId(), 3);
                        mMainFragmentListener.syncDataLater(orderId);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后再来", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public void cancleTable(TableEntity tableEntity, String orderId) {//取消开台
        dialog.dismiss();
        dialog = null;
        int ableCancle = DBHelper.getInstance(getActivity().getApplicationContext()).cancleTable(tableEntity,orderId);
        switch (ableCancle) {
            case 0:
                CustomMethod.showMessage(getContext(), "已有下单的商品，无法取消开台！");
                break;
            case 1:
                refreshTableStatus();
                if(DBHelper.getInstance(getContext().getApplicationContext()).queryFirstOrder(tableEntity.getTableId(),0,0) != null){
                    tableEntity.setTableStatus(1);
                }else{
                    tableEntity.setTableStatus(0);
                }
                mTablesAdapter.updateItem(tableEntity);
                break;
            case 2:
                CustomMethod.showMessage(getContext(), "取消开台失败，请重新尝试！");
                break;
            case 3:
                CustomMethod.showMessage(getContext(), "微信点餐已支付，无法取消开台！");
                break;
            case 4:
                wxOrderCancle(tableEntity, orderId);
                break;
        }
    }

    //微信点餐取消
    private void wxOrderCancle(final TableEntity tableEntity, final String orderId) {
        showLoadingAnim("正在取消微信订单...");
        String partnerCode = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerCode",null);
        long ts = System.currentTimeMillis();
        String data = orderId;
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("id", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.WXORDER_CANCLE), orderId, map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信点餐取消：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(getContext()).confirmWXCancleTable(tableEntity,orderId);
                        refreshTableStatus();
                        if(DBHelper.getInstance(getContext().getApplicationContext()).queryFirstOrder(tableEntity.getTableId(),0,0) != null){
                            tableEntity.setTableStatus(1);
                        }else{
                            tableEntity.setTableStatus(0);
                        }
                        mTablesAdapter.updateItem(tableEntity);
                    } else {
                        String message = publicModule.getMessage() == null ? "微信订单取消开台失败，请重新尝试":publicModule.getMessage();
                        CustomMethod.showMessage(getContext(),message);
                    }
                } catch (Exception e) {
                    CustomMethod.showMessage(getContext(),"微信订单取消开台失败，请重新尝试");
                }
                hideLoadingAnim();
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"微信订单取消开台失败，请重新尝试");
            }
        });
    }

    @Override
    public void cashier(final TableEntity tableEntity, final String orderId) {//收银系统
        dialog.dismiss();
        dialog = null;
        if (orderId != null) {
            if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasOrderedDish(orderId)) {
                //没有已下单商品
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasUnOrderedDish(orderId)) {
                //没有未下单商品
                mMainFragmentListener.cashier(orderId, tableEntity.getTableId(), 0);
                mMainFragmentListener.syncDataLater(orderId);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("有未下单商品，是否继续结账？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续结账", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        mMainFragmentListener.cashier(orderId, tableEntity.getTableId(), 0);
                        mMainFragmentListener.syncDataLater(orderId);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后再来", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public void scheduleRecord(TableEntity tableEntity) {//预定记录
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.openScheduleHistory(tableEntity);
    }

    @Override
    public void clear(TableEntity tableEntity, String orderId) {//清台
        DBHelper.getInstance(getActivity().getApplicationContext()).updateTableStatus(tableEntity.getTableId());
        refreshTableStatus();
        tableEntity.setTableStatus(0);
        mTablesAdapter.updateItem(tableEntity);
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void cancle(TableEntity tableEntity) {//取消
        dialog.dismiss();
        dialog = null;
        mMainFragmentListener.unlockTable(tableEntity);
    }
}
