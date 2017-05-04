package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.MyViewPagerAdapter;
import jufeng.juyancash.adapter.TablesAdapter;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeDialog;
import jufeng.juyancash.ui.customview.CustomeOpenDialog;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.PageIndicatorView;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.FilterArrayList;
import jufeng.juyancash.util.IObjectFilter;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2017/1/19.
 */

public class LFragmentTables extends BaseFragment implements CustomeDialog.OnDialogItemClickListener {
    private static final int PAGE_SIZE = 24;
    private String areaId;
    private CustomeDialog dialog;
    private MainFragmentListener mMainFragmentListener;
    private LCustomeRadioGroup mLCustomeRadioGroup;//选择不同状态的桌位
    private MyViewPagerAdapter adapter;
    private ViewPager viewPager;
    private PageIndicatorView mPageIndicatorView;
    private int tableStatus;
    private List<TablesAdapter> mAppAdapters;
    private FilterArrayList<TableEntity> mTableEntities;
    private List<RecyclerView> mGridViews;
    private List<List<TableEntity>> mTableLists;
    IObjectFilter filter = new IObjectFilter() {
        public boolean filter(Object object) {
            TableEntity tableEntity = (TableEntity) object;
            if (tableStatus == 2) {
                //预定
                return DBHelper.getInstance(getActivity().getApplicationContext()).queryOrderData(tableEntity.getTableId(), 0, 2).size() > 0;
            } else {
                return tableEntity.getTableStatus() == tableStatus;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshTableStatus();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMainFragmentListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tables, container, false);
        initView(view);
        initData();
        setViewPagerAdapter();
        setListener();
        refreshTableStatus();
        return view;
    }

    private void initView(View view) {
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        viewPager = (ViewPager) view.findViewById(R.id.myviewpager);
        mPageIndicatorView = (PageIndicatorView) view.findViewById(R.id.indicator);
    }

    private void initData() {
        mTableLists = new ArrayList<>();
        mTableEntities = new FilterArrayList<>();
        mAppAdapters = new ArrayList<>();
        mGridViews = new ArrayList<>();
        tableStatus = -1;
        areaId = getArguments().getString("areaId", null);
        int pageCount = initTableData();
        getGridViewes1(pageCount);
    }

    private int initTableData() {
        mTableEntities.clear();
        mTableLists.clear();
        mTableEntities.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryTableData(areaId));
        if (tableStatus == -1) {
            mTableEntities.setFilter(null);
        } else {
            mTableEntities.setFilter(filter);
        }
        int pageCount = mTableEntities.size() % PAGE_SIZE == 0 ? mTableEntities.size() / PAGE_SIZE : mTableEntities.size() / PAGE_SIZE + 1;
        for (int i = 0; i < pageCount; i++) {
            final ArrayList<TableEntity> tableEntities = new ArrayList<>();
            int count = mTableEntities.size() - i * PAGE_SIZE > PAGE_SIZE ? PAGE_SIZE : mTableEntities.size() - i * PAGE_SIZE;
            for (int j = i * PAGE_SIZE; j < i * PAGE_SIZE + count; j++) {
                tableEntities.add(mTableEntities.get(j));
            }
            mTableLists.add(tableEntities);
        }
        return pageCount;
    }

    private void getGridViewes1(int pageCount) {
        mAppAdapters.clear();
        mGridViews.clear();
        if(pageCount == 0){
            RecyclerView mRecyclerView = new RecyclerView(getContext().getApplicationContext());
            TablesAdapter appAdapter = new TablesAdapter(getActivity().getApplicationContext(), new ArrayList<TableEntity>());
            WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(appAdapter);
            mRecyclerView.setFocusable(false);
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setHasFixedSize(true);
            appAdapter.setOnRecyclerViewItemClickListener(new TablesAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(TableEntity tableEntity) {
                    showDialog(tableEntity);
                }
            });
            mAppAdapters.add(appAdapter);
            mGridViews.add(mRecyclerView);
        }else {
            for (int i = 0; i < pageCount; i++) {
                final ArrayList<TableEntity> tableEntities = new ArrayList<>();
                tableEntities.addAll(mTableLists.get(i));
                RecyclerView mRecyclerView = new RecyclerView(getContext().getApplicationContext());
                TablesAdapter appAdapter = new TablesAdapter(getActivity().getApplicationContext(), mTableLists.get(i));
                WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                mRecyclerView.setAdapter(appAdapter);
                mRecyclerView.setFocusable(false);
                mRecyclerView.setItemAnimator(null);
                mRecyclerView.setHasFixedSize(true);
                appAdapter.setOnRecyclerViewItemClickListener(new TablesAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(TableEntity tableEntity) {
                        showDialog(tableEntity);
                    }
                });
                mAppAdapters.add(appAdapter);
                mGridViews.add(mRecyclerView);
            }
        }
        mPageIndicatorView.initIndicator(mGridViews.size());
        mPageIndicatorView.setSelectedPage(0);
    }

    private void setViewPagerAdapter() {
        adapter = new MyViewPagerAdapter(getActivity().getApplicationContext(), mGridViews);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPageIndicatorView.setSelectedPage(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setListener() {
        //不同的桌位状态切换
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (checkedId) {
                    case R.id.radiobutton_0:
                        tableStatus = -1;
                        break;
                    case R.id.radiobutton_1:
                        tableStatus = 0;
                        break;
                    case R.id.radiobutton_2:
                        tableStatus = 1;
                        break;
                    case R.id.radiobutton_3:
                        tableStatus = 2;
                        break;
                    case R.id.radiobutton_4:
                        tableStatus = 3;
                        break;
                }
                int pageCount = initTableData();
                getGridViewes1(pageCount);
                adapter.updateData(mGridViews);
            }
        });
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

    //刷新桌位
    private void refreshTableStatus() {
        if (mMainFragmentListener != null) {
            mMainFragmentListener.changeOrderMessageCount();
        }
        try {
            initTableData();
            mAppAdapters.get(viewPager.getCurrentItem()).update(mTableLists.get(viewPager.getCurrentItem()));
        } catch (Exception e) {
            e.printStackTrace();
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
        DBHelper.getInstance(getActivity().getApplicationContext()).replaceTableStatus(tableEntity.getTableId(), 1);
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
    public void cancleJoinTable(TableEntity tableEntity, String orderId) {
        dialog.dismiss();
        dialog = null;
        DBHelper.getInstance(getActivity().getApplicationContext()).cancleJoinTable(tableEntity.getTableId());
        refreshTableStatus();
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
