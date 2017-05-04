package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectTableListAdapter;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.DifferentDisplay;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomeSelectOrderDialog;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/1/17.
 */

public class LFragmentSelectTables extends BaseFragment {
    private LCustomeRadioGroup mLCustomeRadioGroup0;
    private Button btnCancle, btnConfirm;
    private RecyclerView tablesRecyclerView;//桌位展示的recyclerView
    private ArrayList<AreaEntity> areas;
    private RadioButton[] mRadioButtons;
    private SelectTableListAdapter adapter;
    private TextView tvTitle;
    public static final int OPERATION_TYPE_CHANGE_TABLE = 0;
    public static final int OPERATION_TYPE_JOIN_TABLE = 1;
    public static final int OPERATION_TYPE_JOIN_ORDER = 2;
    public static final int OPERATION_TYPE_SELECT_TABLE = 3;
    public static final int OPERATION_TYPE_CONFIRM_TABLE = 4;
    private int operationType;
    private String tableId;
    private String orderId;
    private ArrayList<TableEntity> selectedTables;
    private ArrayList<OrderEntity> selectedOrderes;
    private ScheduleEntity mScheduleEntity;
    private MainFragmentListener mMainFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMainFragmentListener = (MainFragmentListener) context;
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_tables, container, false);
        initView(view);
        addAreaMenus();
        setAdapter();
        setListener();
        sendDifferBroadcast(0, null, false);
        return view;
    }

    private void initView(View view) {
        mLCustomeRadioGroup0 = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup0);
        tablesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        operationType = getArguments().getInt("type", 0);
        mScheduleEntity = getArguments().getParcelable("scheduleEntity");
        tableId = getArguments().getString("tableId");
        orderId = getArguments().getString("orderId");
        selectedTables = new ArrayList<>();
        selectedOrderes = new ArrayList<>();

        initData();
    }

    public void setNewParam(int type, ScheduleEntity scheduleEntity, String tableId, String orderId) {
        this.operationType = type;
        this.mScheduleEntity = scheduleEntity;
        this.tableId = tableId;
        this.orderId = orderId;
        this.selectedTables.clear();

        initData();
        setAdapter();
        sendDifferBroadcast(0, null, false);
    }

    private void initData() {
        TableEntity tableEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneTableData(tableId);
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        switch (operationType) {
            case OPERATION_TYPE_CONFIRM_TABLE:
            case OPERATION_TYPE_SELECT_TABLE:
                //选择预定桌位
                tvTitle.setText(Html.fromHtml("<html><font color=\"red\">选择预定桌位</font></html>"));
                break;
            case OPERATION_TYPE_CHANGE_TABLE:
                //更换桌位
                if (tableEntity != null && orderEntity != null) {
                    tvTitle.setText(Html.fromHtml("<html><font color=\"black\">更换桌位：</font>" +
                            "<font color=\"red\">" + tableEntity.getTableName() + "(" + tableEntity.getTableCode() + ")NO." + orderEntity.getOrderNumber1() + "</font></html>"));
                }
                break;
            case OPERATION_TYPE_JOIN_ORDER:
                //并单
                if (tableEntity != null && orderEntity != null) {
                    tvTitle.setText(Html.fromHtml("<html><font color=\"black\">并单：</font>" +
                            "<font color=\"red\">" + tableEntity.getTableName() + "(" + tableEntity.getTableCode() + ")NO." + orderEntity.getOrderNumber1() + "</font></html>"));
                }
                break;
            case OPERATION_TYPE_JOIN_TABLE:
                //合台
                if (tableEntity != null) {
                    tvTitle.setText(Html.fromHtml("<html><font color=\"black\">合台：</font>" +
                            "<font color=\"red\">" + tableEntity.getTableName() + "(" + tableEntity.getTableCode() + ")" + "</font></html>"));
                }
                break;
        }
    }

    private void addAreaMenus() {
        areas = new ArrayList<>();
        areas.addAll(DBHelper.getInstance(getContext().getApplicationContext()).queryAreaData());
        mRadioButtons = new RadioButton[areas.size()];
        if (areas.size() > 0) {
            LCustomeRadioGroup.LayoutParams params = new LCustomeRadioGroup.LayoutParams(LCustomeRadioGroup.LayoutParams.MATCH_PARENT, LCustomeRadioGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < areas.size(); i++) {
                RadioButton radioButton = new RadioButton(getContext().getApplicationContext());
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
        } else {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setTitle("提示");
            dialog.setMessage("暂无桌位，请先添加区域和桌位");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });
            dialog.show();
        }
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

    private void setAdapter() {
        if(mRadioButtons.length > 0) {
            if (adapter == null) {
                switch (operationType) {
                    case OPERATION_TYPE_CHANGE_TABLE:
                        adapter = new SelectTableListAdapter(getContext().getApplicationContext(), (String) mRadioButtons[0].getTag(), 0, 0, selectedTables, tableId);
                        break;
                    case OPERATION_TYPE_JOIN_TABLE:
                        adapter = new SelectTableListAdapter(getContext().getApplicationContext(), (String) mRadioButtons[0].getTag(), 1, 1, selectedTables, tableId);
                        break;
                    case OPERATION_TYPE_JOIN_ORDER:
                        adapter = new SelectTableListAdapter(getContext().getApplicationContext(), (String) mRadioButtons[0].getTag(), 1, 1, selectedTables, null);
                        break;
                    case OPERATION_TYPE_CONFIRM_TABLE:
                    case OPERATION_TYPE_SELECT_TABLE:
                        adapter = new SelectTableListAdapter(getContext().getApplicationContext(), (String) mRadioButtons[0].getTag(), -1, 0, selectedTables, null);
                        break;
                    default:
                        adapter = new SelectTableListAdapter(getContext().getApplicationContext(), (String) mRadioButtons[0].getTag(), -1, 0, selectedTables, null);
                        break;
                }
                WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getContext().getApplicationContext(), 7);
                tablesRecyclerView.setLayoutManager(gridLayoutManager);
                tablesRecyclerView.setAdapter(adapter);
                tablesRecyclerView.setFocusable(false);

                adapter.setOnTableClickListener(new SelectTableListAdapter.OnTableClickListener() {
                    @Override
                    public void onTableClick(int selectMode, final TableEntity tableEntity) {
                        switch (operationType) {
                            case OPERATION_TYPE_CHANGE_TABLE:
                            case OPERATION_TYPE_SELECT_TABLE:
                            case OPERATION_TYPE_CONFIRM_TABLE:
                                //更换桌位
                                selectTable(tableEntity);
                                break;
                            case OPERATION_TYPE_JOIN_TABLE:
                                //合台
                                joinTable(tableEntity);
                                break;
                            case OPERATION_TYPE_JOIN_ORDER:
                                //并单
                                final CustomeSelectOrderDialog selectOrderDialog = new CustomeSelectOrderDialog(getContext(), tableEntity.getTableId(), selectedOrderes, orderId);
                                selectOrderDialog.setOnOrderSelectedListener(new CustomeSelectOrderDialog.OnOrderSelectedListener() {
                                    @Override
                                    public void onCancleClick() {
                                        selectOrderDialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClick(ArrayList<OrderEntity> selectOrderEntities) {
                                        selectOrderDialog.dismiss();
                                        selectedOrderes.clear();
                                        selectedOrderes.addAll(selectOrderEntities);
                                        joinOrder(tableEntity);
                                        adapter.updateData(selectedTables);
                                    }
                                });
                                break;
                        }
                    }
                });
            } else {
                RadioButton radioButton = (RadioButton) mLCustomeRadioGroup0.findViewById(mLCustomeRadioGroup0.getCheckedRadioButtonId());
                String areaId;
                if(radioButton != null) {
                    areaId = (String) radioButton.getTag();
                }else{
                    areaId = (String) mRadioButtons[0].getTag();
                }
                switch (operationType) {
                    case OPERATION_TYPE_CHANGE_TABLE:
                        adapter.updateData(areaId, 0, 0, selectedTables, tableId);
                        break;
                    case OPERATION_TYPE_JOIN_TABLE:
                        adapter.updateData(areaId, 1, 1, selectedTables, tableId);
                        break;
                    case OPERATION_TYPE_JOIN_ORDER:
                        adapter.updateData(areaId, 1, 1, selectedTables, null);
                        break;
                    case OPERATION_TYPE_CONFIRM_TABLE:
                    case OPERATION_TYPE_SELECT_TABLE:
                        adapter.updateData(areaId, -1, 0, selectedTables, null);
                        break;
                    default:
                        adapter.updateData(areaId, -1, 0, selectedTables, null);
                        break;
                }
            }
        }
    }

    private void setListener() {
        mLCustomeRadioGroup0.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                try {
                    setRadioButtonTextColor(checkedId);
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    switch (operationType) {
                        case OPERATION_TYPE_CHANGE_TABLE:
                        case OPERATION_TYPE_JOIN_TABLE:
                            adapter.changeAreaData((String) radioButton.getTag(), tableId);
                            break;
                        case OPERATION_TYPE_JOIN_ORDER:
                        case OPERATION_TYPE_SELECT_TABLE:
                        case OPERATION_TYPE_CONFIRM_TABLE:
                            adapter.changeAreaData((String) radioButton.getTag(), null);
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainFragmentListener != null) {
                    mMainFragmentListener.selectTableCancel();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTables.size() > 0) {
                    switch (operationType) {
                        case OPERATION_TYPE_CHANGE_TABLE:
                            //更换桌位
                            if (tableId != null && selectedTables.size() > 0) {
                                DBHelper.getInstance(getContext().getApplicationContext()).changeTable(tableId, selectedTables.get(0).getTableId(), orderId);
//                                Intent intent = new Intent(getContext(),MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
                                if (mMainFragmentListener != null) {
                                    mMainFragmentListener.selectedTables(OPERATION_TYPE_CHANGE_TABLE, OPERATION_TYPE_CHANGE_TABLE, null);
                                }
                            }
                            break;
                        case OPERATION_TYPE_JOIN_TABLE:
                            //合台
                            if (tableId != null && selectedTables.size() > 0) {
                                DBHelper.getInstance(getContext().getApplicationContext()).joinTable(tableId, selectedTables);
//                                Intent intent = new Intent(getContext(),MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
                                if (mMainFragmentListener != null) {
                                    mMainFragmentListener.selectedTables(OPERATION_TYPE_JOIN_TABLE, OPERATION_TYPE_JOIN_TABLE, null);
                                }
                            } else {
                                CustomMethod.showMessage(getContext(), "请选择桌位");
                            }
                            break;
                        case OPERATION_TYPE_JOIN_ORDER:
//                            //并单
//                            if (orderId != null && tableId != null && selectedOrderes.size() > 0) {
//                                DBHelper.getInstance(getContext().getApplicationContext()).joinOrder(tableId, orderId, selectedOrderes);
////                                Intent intent = new Intent(getContext(),MainActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
////                                startActivity(intent);
//                                if(mMainFragmentListener != null){
//                                    mMainFragmentListener.selectedTables(OPERATION_TYPE_JOIN_TABLE,OPERATION_TYPE_JOIN_TABLE,null);
//                                }
//                            }else {
//                                CustomMethod.showMessage(getContext(),"请选择账单");
//                            }
                            break;
                        case OPERATION_TYPE_SELECT_TABLE:
                            if (selectedTables.size() > 0) {
//                                Intent intent = new Intent(getContext(),MainActivity.class);
//                                intent.putExtra("tableEntity",selectedTables.get(0));
//                                intent.putExtra("type",1);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
                                if (mMainFragmentListener != null) {
                                    mMainFragmentListener.selectedTables(OPERATION_TYPE_SELECT_TABLE, 1, selectedTables.get(0));
                                }
                            } else {
                                CustomMethod.showMessage(getContext(), "请选择桌位");
                            }
                            break;
                        case OPERATION_TYPE_CONFIRM_TABLE:
                            if (mScheduleEntity != null && selectedTables.size() > 0) {
                                DBHelper.getInstance(getContext()).insertOneSchedule(getContext(), mScheduleEntity, selectedTables.get(0));
                                if (mScheduleEntity.getScheduleFrom() == 1) {
                                    DBHelper.getInstance(getContext()).insertUploadData(mScheduleEntity.getScheduleId(), selectedTables.get(0).getTableName(), 10);
                                }
                                if (mMainFragmentListener != null) {
                                    mMainFragmentListener.selectedTables(OPERATION_TYPE_CONFIRM_TABLE, 2, selectedTables.get(0));
                                }

                            } else {
                                CustomMethod.showMessage(getContext(), "请选择桌位");
                            }
                            break;
                    }
                } else {
                    CustomMethod.showMessage(getContext(), "请选择桌位");
                }
            }
        });
    }

    //并单
    private void joinOrder(TableEntity tableEntity) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).queryOrderData(tableEntity.getTableId()));
        boolean isExist = false;
        for (OrderEntity orderEntity :
                orderEntities) {
            for (OrderEntity orderEntity1 :
                    selectedOrderes) {
                if (orderEntity.getOrderId().equals(orderEntity1.getOrderId())) {
                    //已存在
                    isExist = true;
                    break;
                }
            }
            if (isExist) {
                break;
            }
        }

        boolean isAdd = true;
        for (int i = 0; i < selectedTables.size(); i++) {
            if (selectedTables.get(i).getTableId().equals(tableEntity.getTableId())) {
                //已经存在则取消
                isAdd = false;
                if (!isExist) {
                    selectedTables.remove(i);
                }
                break;
            }
        }
        if (isAdd && isExist) {
            selectedTables.add(tableEntity);
        }
        adapter.updateData(selectedTables);
    }

    //合台
    private void joinTable(TableEntity tableEntity) {
        boolean isAdd = true;
        for (int i = 0; i < selectedTables.size(); i++) {
            if (selectedTables.get(i).getTableId().equals(tableEntity.getTableId())) {
                //已经存在则取消
                isAdd = false;
                selectedTables.remove(i);
                break;
            }
        }
        if (isAdd) {
            selectedTables.add(tableEntity);
        }
        adapter.updateData(selectedTables);
    }

    //更换桌位
    private void selectTable(TableEntity tableEntity) {
        selectedTables.clear();
        selectedTables.add(tableEntity);
        adapter.updateData(selectedTables);
    }

    //客显界面
    private void sendDifferBroadcast(int type, String orderId, boolean isOpenJoinOrder) {
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type", type);
        intent.putExtra("orderId", orderId);
        intent.putExtra("isOpenJoinOrder", isOpenJoinOrder);
        getContext().sendBroadcast(intent);
    }
}
