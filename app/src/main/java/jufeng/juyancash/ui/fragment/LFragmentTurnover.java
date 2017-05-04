package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderAdapter;
import jufeng.juyancash.adapter.OrderColletionAdapter;
import jufeng.juyancash.adapter.OrderSpinnerAdapter;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.OrderCollectionBean;
import jufeng.juyancash.bean.TurnoverBean;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.eventbus.TurnoverHistoryCloseDialogEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.LoginActivity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomeTurnoverDialog;
import jufeng.juyancash.ui.customview.CustomeTurnoverHistoryDialog;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentTurnover extends BaseFragment {
    private LFragmentTurnoverLeftCollection mLeftCollection;
    private LFragmentTurnoverLeftDetail mLeftDetail;
    private LCustomeRadioGroup mRadioGroup;
    private TextView tvTime;
    private Spinner mSpinner0,mSpinner1;
    private Button btnPrint,btnTurnOver,btnTurnoverHistory;
    private MainFragmentListener mOnTurnoverSpinnerChangeListener;
    private static ArrayList<AreaEntity> areaEntities;
    private static ArrayList<String> typeNames;
    private static ArrayList<String> cashieres;
    private static final int ORDERDETAIL = 0;
    private static final int ORDERCOLLECTION = 1;
    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what){
                case 0://二维码扫描成功，开始交接班
                    String mTime = tvTime.getText().toString();
                    TurnoverBean turnoverBean = msg.getData().getParcelable("turnoverBean");
                    if(mTime != null && !mTime.isEmpty()) {
                        long ts = CustomMethod.parseTime(mTime, "yyyy-MM-dd HH:mm");
                        if (turnoverBean.getTs().equals(String.valueOf(ts))) {
                            EmployeeEntity employee = DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeById(turnoverBean.getEmployeeId());
                            CustomMethod.showMessage(getContext(),"交接的员工："+employee.getEmployeeName());
                        }else{
                            CustomMethod.showMessage(getContext(),"交接班时间不一致");
                        }
                    }else{
                        CustomMethod.showMessage(getContext(),"请重新尝试！");
                    }
                    break;
            }
        }
    };

    static {
        cashieres = new ArrayList<>();
        cashieres.add("全部");
        cashieres.add("当前收银员");

        typeNames = new ArrayList<>();
        areaEntities = new ArrayList<>();
        typeNames.add("全部");
        typeNames.add("外卖单");
    }

    private TurnoverHistoryEntity mTurnoverHistoryEntity;

    private RecyclerView mRecyclerView;
    private int employId = 0;
    private int shift = 0;
    private String paymodeId = null;
    private int date = -1;
    private String type = "-1";
    //账单明细
    private OrderAdapter mOrderAdapter;
    private Map<Integer, List<OrderEntity>> mListMap;
    private TextView tvPageNum;
    private ImageButton ibPre,ibNext;
    private LinearLayout layoutPageNum;
    //账单汇总
    private OrderColletionAdapter mOrderColletionAdapter;
    private ArrayList<OrderCollectionBean> mOrderCollectionBeen;

    private CustomeTurnoverHistoryDialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setTurnoverHandler(mHandler);
        try {
            mOnTurnoverSpinnerChangeListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover,container,false);
        initView(mView);
        initView1(mView);
        initData();
        setListener1();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyAsyncTask().execute();
    }

    private void initView(View view){
        mRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        mSpinner0 = (Spinner) view.findViewById(R.id.spinner_cashier);
        mSpinner1 = (Spinner) view.findViewById(R.id.spinner_type);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        btnTurnOver = (Button) view.findViewById(R.id.btn_turnover);
        btnTurnoverHistory = (Button) view.findViewById(R.id.btn_turnover_history);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvTime.setText(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"));
    }

    class MyAsyncTask extends AsyncTask<String,Integer,Object> {

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在加载...");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            areaEntities.clear();
            areaEntities.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryAreaData());
            typeNames.clear();
            typeNames.add("全部");
            for (AreaEntity areaEntity :
                    areaEntities) {
                typeNames.add(areaEntity.getAreaName());
            }
            typeNames.add("外卖单");
            return  null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setSpinner();
            setListener();
            setFragment(0);
            hideLoadingAnim();
            new GetOrderDataAsyncTask().execute();
        }
    }

    private void setSpinner() {
        mSpinner0.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), cashieres));
        mSpinner1.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), typeNames));
    }

    private void setListener(){
        mRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId,Bundle bundle) {
                if (group.getCheckedRadioButtonId() == R.id.radiobutton_1){
                    changeRightAdapter(0);
                }else if(group.getCheckedRadioButtonId() == R.id.radiobutton_2){
                    changeRightAdapter(1);
                }
            }
        });

        mSpinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner0.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner0.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner1.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner1.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTurnoverHistoryEntity != null && mOnTurnoverSpinnerChangeListener != null){
                    mOnTurnoverSpinnerChangeListener.printTurnOverOrder(mTurnoverHistoryEntity);
                }
            }
        });

        btnTurnOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderType = "-1";
                if (mSpinner1.getSelectedItemPosition() == 0) {
                    //全部
                    orderType = "-1";
                } else if (mSpinner1.getSelectedItemPosition() == typeNames.size() - 1) {
                    //外卖
                    orderType = "0";
                } else {
                    orderType = areaEntities.get(mSpinner1.getSelectedItemPosition() - 1).getAreaId();
                }
                final CustomeTurnoverDialog dialog = new CustomeTurnoverDialog();
                Bundle bundle = new Bundle();
                bundle.putString("time",tvTime.getText().toString());
                bundle.putInt("cashier",mSpinner0.getSelectedItemPosition());
                bundle.putString("area",typeNames.get(mSpinner1.getSelectedItemPosition()));
                bundle.putString("areaId",orderType);
                dialog.setArguments(bundle);
                dialog.setOnTurnOverListener(new CustomeTurnoverDialog.OnTurnOverListener() {
                    @Override
                    public void onTurnOverSuccess(View view, EmployeeEntity employeeEntity,int cashier,String areaId) {
                        if(employeeEntity.getAuthCashier() == 1){
                            //有收银权限
                            if(mTurnoverHistoryEntity != null) {
                                mTurnoverHistoryEntity.setTurnoverState("已交接");
                                DBHelper.getInstance(getContext()).insertTurnoverHistory(mTurnoverHistoryEntity);
                                Log.d("###", "onTurnOverSuccess: "+mTurnoverHistoryEntity.toString());
                                mTurnoverHistoryEntity = null;
                            }
                            DBHelper.getInstance(getContext().getApplicationContext()).turnoverConfirm(getContext().getApplicationContext(),cashier,areaId);
                            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString("employeeName", employeeEntity.getLoginName());
                            editor.putString("employeeId", employeeEntity.getEmployeeId());
                                editor.putString("employeePsd", null);
                            editor.commit();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.putExtra("isUpdate",true);
                            startActivity(intent);
                            dialog.dismiss();
                        }else{
                            Snackbar.make(view,"该员工无收银权限",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show(getFragmentManager(),"");
            }
        });

        btnTurnoverHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog != null){
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new CustomeTurnoverHistoryDialog(getContext());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTurnoverHistoryCloseEvent(TurnoverHistoryCloseDialogEvent event){
        if(event != null){
            if(dialog != null){
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    private void onSpinnerChanged() {
        employId = mSpinner0.getSelectedItemPosition();
        if (mSpinner1.getSelectedItemPosition() == 0) {
            type = "-1";
        } else if (mSpinner1.getSelectedItemPosition() == typeNames.size() - 1) {
            type = "0";
        } else {
            type = areaEntities.get(mSpinner1.getSelectedItemPosition() - 1).getAreaId();
        }
        new GetOrderDataAsyncTask().execute();
    }
    
    class TurnOverPrintTask extends AsyncTask<String,Integer,Object>{
        private ArrayList<DishTypeCollectionItemBean> mDishTypeCollectionItemBean;
        private ArrayList<PayModeEntity> mPayModeEntities;
        private Map<String,String> map;
        String employeeId;
        String orderType;
        int type = 0;

        @Override
        protected void onPreExecute() {
            showLoadingAnim("交接班汇总中...");
            if(mTurnoverHistoryEntity == null) {
                mTurnoverHistoryEntity = new TurnoverHistoryEntity();
                mTurnoverHistoryEntity.setTurnoverHistoryId(UUID.randomUUID().toString());
            }
            mPayModeEntities = new ArrayList<>();
            map = new HashMap<>();
            mDishTypeCollectionItemBean = new ArrayList<>();

            employeeId = mSpinner0.getSelectedItemPosition() == 0?null:getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("employeeId",null);
            orderType = "-1";
            if (mSpinner1.getSelectedItemPosition() == 0) {
                orderType = "-1";
            } else if (mSpinner1.getSelectedItemPosition() == typeNames.size() - 1) {
                orderType = "0";
            } else {
                orderType = areaEntities.get(mSpinner1.getSelectedItemPosition() - 1).getAreaId();
            }
            super.onPreExecute();
        }

        public void initDetialCollection(){
            String[] discount = DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderDiscountMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType);
            map.put("cashieredCount",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getCashieredCount(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("cashieredMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getCashieredMoney(getContext().getApplicationContext(),employeeId,shift,null,-1, orderType)));
            map.put("unCashieredCount",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getUnCashierdOrderCount(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("unCashieredMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getUnCashieredMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("totalCount",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderCount(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("totalMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("receivableMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderReceivableMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("incomeMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderReceivedMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("mlMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderTreatmentMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("discountMoney",String.valueOf(discount[0]));
            map.put("bjlMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderMantissaMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("presentMoney",String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getAllOrderPresentMoney(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType)));
            map.put("couponMoney",String.valueOf(discount[1]));
        }

        @Override
        protected Object doInBackground(String... params) {
            initDetialCollection();
            mPayModeEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getAllPayMode(getContext().getApplicationContext(),employeeId, shift,null,-1, orderType));
            mDishTypeCollectionItemBean.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getTypeCollectionDishType(employeeId, shift,null,-1, orderType));
            mTurnoverHistoryEntity.setOrderTotalCount(map.get("totalCount"));
            mTurnoverHistoryEntity.setOrderedTotalCount(map.get("cashieredCount"));
            mTurnoverHistoryEntity.setUnOrderedTotalCount(map.get("unCashieredCount"));
            mTurnoverHistoryEntity.setOrderedTotalMoney(map.get("cashieredMoney"));
            mTurnoverHistoryEntity.setUnOrderedTotalMoney(map.get("unCashieredMoney"));
            mTurnoverHistoryEntity.setMoney0(map.get("receivableMoney"));
            mTurnoverHistoryEntity.setMoney1(map.get("incomeMoney"));
            mTurnoverHistoryEntity.setMoney2(map.get("presentMoney"));
            mTurnoverHistoryEntity.setMoney3(map.get("mlMoney"));
            mTurnoverHistoryEntity.setMoney4(map.get("discountMoney"));
            mTurnoverHistoryEntity.setMoney5(map.get("bjlMoney"));
            mTurnoverHistoryEntity.setMoney6(map.get("couponMoney"));
            StringBuilder sb = new StringBuilder();
            sb.append("");
            for (DishTypeCollectionItemBean itemBean:
                    mDishTypeCollectionItemBean) {
                sb.append(itemBean.getDishTypeName());
                sb.append("`");
                sb.append(AmountUtils.multiply(itemBean.getDishTypeCount(),1));
                sb.append("`");
                sb.append(AmountUtils.multiply("1",""+itemBean.getDishTypeMoney()));
                sb.append("`");
            }
            if(sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            mTurnoverHistoryEntity.setDishTypes(sb.toString());
            StringBuilder sb1 = new StringBuilder();
            sb1.append("");
            for (PayModeEntity payMode :
                    mPayModeEntities) {
                sb1.append(payMode.getPaymentName());
                sb1.append("`");
                sb1.append(AmountUtils.multiply(""+payMode.getPayMoney(),"1"));
                sb1.append("`");
            }
            if(sb1.length() > 0) {
                sb1.deleteCharAt(sb1.length() - 1);
            }
            mTurnoverHistoryEntity.setPayments(sb1.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String cashierName;
            if(employeeId == null){
                cashierName = "全部";
            }else{
                cashierName = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(employeeId);
            }
            String cashierId = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
            String employeeName = DBHelper.getInstance(getContext()).getEmployeeNameById(cashierId);
            mTurnoverHistoryEntity.setOperatorName(employeeName);
            mTurnoverHistoryEntity.setCreateTime(System.currentTimeMillis());
            mTurnoverHistoryEntity.setPrintTime("");
            TurnoverHistoryEntity lastTurnoverHistory = DBHelper.getInstance(getContext()).getLastTurnoverHistory();
            if(lastTurnoverHistory != null) {
                mTurnoverHistoryEntity.setTurnoverStartTime(lastTurnoverHistory.getTurnoverEndTime());
            }else{
                long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                mTurnoverHistoryEntity.setTurnoverStartTime(CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss"));
            }
            mTurnoverHistoryEntity.setTurnoverEndTime(tvTime.getText().toString());
            mTurnoverHistoryEntity.setStartTurnoverTime(tvTime.getText().toString());
            mTurnoverHistoryEntity.setCashierName(cashierName);
            mTurnoverHistoryEntity.setTurnoverState("未交接");
            mTurnoverHistoryEntity.setPayMentType("所有");
            mTurnoverHistoryEntity.setAreaType(typeNames.get(mSpinner1.getSelectedItemPosition()));
            Log.d("###", "doInBackground: "+mTurnoverHistoryEntity.toString());
//            mOnTurnoverSpinnerChangeListener.printTurnOverOrder(tvTime.getText().toString(),cashierName,-1,orderType,mPayModeEntities,mDishTypeCollectionItemBean,map);
            hideLoadingAnim();
        }
    }

    private void setFragment(int tag){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        Bundle bundle = new Bundle();
        bundle.putInt("employeeId", mSpinner0.getSelectedItemPosition());
        if (mSpinner1.getSelectedItemPosition() == 0) {
            bundle.putString("type", "-1");
        } else if (mSpinner1.getSelectedItemPosition() == typeNames.size() - 1) {
            bundle.putString("type", "0");
        } else {
            bundle.putString("type", areaEntities.get(mSpinner1.getSelectedItemPosition() - 1).getAreaId());
        }
        
        switch (tag) {
            case 0:
                if (mLeftDetail == null) {
                    mLeftDetail = new LFragmentTurnoverLeftDetail();
                    mLeftDetail.setArguments(bundle);
                    fragmentTransaction.add(R.id.containerleft, mLeftDetail);
                } else {
                    fragmentTransaction.show(mLeftDetail);
                }
                break;
            case 1:
                if (mLeftCollection == null) {
                    mLeftCollection = new LFragmentTurnoverLeftCollection();
                    mLeftCollection.setArguments(bundle);
                    fragmentTransaction.add(R.id.containerleft, mLeftCollection);
                } else {
                    fragmentTransaction.show(mLeftCollection);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction){
        if(mLeftDetail != null){
            transaction.hide(mLeftDetail);
        }
        if(mLeftCollection != null){
            transaction.hide(mLeftCollection);
        }
    }

    private void initView1(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvPageNum = (TextView) view.findViewById(R.id.tv_page_number);
        layoutPageNum = (LinearLayout) view.findViewById(R.id.layout_page_number);
        ibPre = (ImageButton) view.findViewById(R.id.ib_pre_page);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next_page);
    }

    private void initData() {
        mListMap = new HashMap<>();
        setTvPageNum(1);
        mOrderCollectionBeen = new ArrayList<>();
        setRecyclerView();
        initAdapter();
        mTurnoverHistoryEntity = null;
    }

    private void setTvPageNum(Integer pageNum) {
        tvPageNum.setText(String.valueOf(pageNum));
        tvPageNum.setTag(pageNum);
    }

    private void setRecyclerView() {
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void initAdapter(){
        mOrderAdapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<OrderEntity>());
        mOrderColletionAdapter = new OrderColletionAdapter(getActivity().getApplicationContext(),mOrderCollectionBeen);
    }

    //设置账单明细列表adapter
    private void setOrderDetail() {
        if (mOrderAdapter == null) {
            mOrderAdapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<>(mListMap.get(1)));
        }
        mRecyclerView.setAdapter(mOrderAdapter);
        updateOrderRightDetailAdapter();
    }

    //设置账单汇总列表adapter
    private void setOrderRightCollection() {
        if (mOrderColletionAdapter == null) {
            mOrderColletionAdapter = new OrderColletionAdapter(getActivity().getApplicationContext(),mOrderCollectionBeen);
        }
        mRecyclerView.setAdapter(mOrderColletionAdapter);
        updateOrderRightCollectionAdapter();
    }

    //更新账单明细列表adapter
    private void updateOrderRightDetailAdapter() {
        setTvPageNum(1);
        if (mOrderAdapter != null)
            mOrderAdapter.updateData(new ArrayList<>(mListMap.get(1)));
        mOnTurnoverSpinnerChangeListener.onTurnoverRightClick(null,employId,shift,paymodeId,date,type);
    }

    //更新账单汇总列表adapter
    private void updateOrderRightCollectionAdapter() {
        if(mOrderColletionAdapter != null){
            mOrderColletionAdapter.updateData(mOrderCollectionBeen);
        }
        mOnTurnoverSpinnerChangeListener.turnoverNothing();
    }

    //切换右边adapter
    private void changeRightAdapter(int type) {
        switch (type) {
            case 0://显示账单明细的列表
                layoutPageNum.setVisibility(LinearLayout.VISIBLE);
                setOrderDetail();
                setFragment(ORDERDETAIL);
                break;
            case 1://显示账单汇总的列表
                layoutPageNum.setVisibility(LinearLayout.VISIBLE);
                setOrderRightCollection();
                setFragment(ORDERCOLLECTION);
                break;
        }
    }

    //切换左边adapter
    private void changeLeftAdapter(int type) {
        switch (type) {
            case 0://显示账单明细空白页

                break;
            case 1://显示账单汇总空白页

                break;
            case 2://显示销售统计空白页

                break;
        }
    }

    private void setListener1(){
        ibPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvPageNum.getTag() != null){
                    int currentPage = (int) tvPageNum.getTag();
                    if(currentPage > 1 && mOrderAdapter != null){
                        mOrderAdapter.updateData(new ArrayList<>(mListMap.get(currentPage - 1)));
                        setTvPageNum(currentPage - 1);
                    }else{
                        CustomMethod.showMessage(getContext(),"已是第一页");
                    }
                }
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvPageNum.getTag() != null){
                    int currentPage = (int) tvPageNum.getTag();
                    if(currentPage < mListMap.size() && mOrderAdapter != null){
                        mOrderAdapter.updateData(new ArrayList<>(mListMap.get(currentPage + 1)));
                        setTvPageNum(currentPage + 1);
                    }else{
                        CustomMethod.showMessage(getContext(),"已是最后一页");
                    }
                }
            }
        });

        mOrderAdapter.setOnOrderItemClickListener(new OrderAdapter.OnOrderItemClickListener() {
            @Override
            public void onOrderItemClick(OrderEntity orderEntity) {
                mOnTurnoverSpinnerChangeListener.onTurnoverRightClick(orderEntity,employId,shift,paymodeId,date,type);
            }
        });
        mOrderColletionAdapter.setOnOrderColletionItemClickListener(new OrderColletionAdapter.OnOrderColletionItemClickListener() {
            @Override
            public void onNothingClick() {
                mOnTurnoverSpinnerChangeListener.turnoverNothing();
            }

            @Override
            public void onOrderCashierClick(String cashierId) {
                mOnTurnoverSpinnerChangeListener.turnoverCashier(cashierId, shift, paymodeId, date,type);
            }

            @Override
            public void onOrderCollectionClick() {
                mOnTurnoverSpinnerChangeListener.turnoverCollection(employId, shift, paymodeId, date,type);
            }

            @Override
            public void onTypeCollectionClick() {
                mOnTurnoverSpinnerChangeListener.turnoverTypeCollection(employId, shift, paymodeId, date,type);
            }
        });
    }

    class GetOrderDataAsyncTask extends AsyncTask<Object, Integer, Object> {

        public GetOrderDataAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在统计账单数据...");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            //获取账单明细数据
            ArrayList<OrderEntity> orderEntities = new ArrayList<>();
            if (employId == 0) {
                //全部收银员
                orderEntities.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(null, shift, paymodeId, date, type));
            } else if (employId == 1) {
                //当前收银员
                SharedPreferences spf = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                String cashierId = spf.getString("employeeId", null);
                orderEntities.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(cashierId, shift, paymodeId, date, type));
            }
            mListMap.clear();
            int length = orderEntities.size();
            int pageCount = length / 24 + 1;
            for (int i = 1; i <= pageCount; i++) {
                if (i * 24 < length) {
                    mListMap.put(i, orderEntities.subList((i - 1) * 24, i * 24));
                } else {//最后一页
                    mListMap.put(i, orderEntities.subList((i - 1) * 24, length));
                }
            }

            //获取账单汇总数据
            mOrderCollectionBeen.clear();
            ArrayList<String> mCashierIds = new ArrayList<>();
            if (employId == 0) {
                //全部收银员
                mCashierIds.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getCashierIds(null, shift, paymodeId, date, type));
            } else if (employId == 1) {
                //当前收银员
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                mCashierIds.add(sharedPreferences.getString("employeeId", null));
            }
            OrderCollectionBean orderCollectionBean = new OrderCollectionBean();
            orderCollectionBean.setCashierId(null);
            orderCollectionBean.setCashierName("账单汇总");
            if (employId == 1 && mCashierIds.size() > 0) {
                orderCollectionBean.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(mCashierIds.get(0), shift, paymodeId, date, type)));
            } else {
                orderCollectionBean.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(null, shift, paymodeId, date, type)));
            }
            mOrderCollectionBeen.add(orderCollectionBean);

            OrderCollectionBean orderCollectionBean1 = new OrderCollectionBean();
            orderCollectionBean1.setCashierId(null);
            orderCollectionBean1.setCashierName("分类汇总");
            if (employId == 1 && mCashierIds.size() > 0) {
                orderCollectionBean1.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(mCashierIds.get(0), shift, paymodeId, date, type)));
            } else {
                orderCollectionBean1.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(null, shift, paymodeId, date, type)));
            }
            mOrderCollectionBeen.add(orderCollectionBean1);
            for (String cashierId :
                    mCashierIds) {
                OrderCollectionBean orderCollectionBean2 = new OrderCollectionBean();
                orderCollectionBean2.setCashierName(DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(cashierId));
                orderCollectionBean2.setCashierId(cashierId);
                orderCollectionBean2.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(cashierId, shift, paymodeId, date, type)));
                mOrderCollectionBeen.add(orderCollectionBean2);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (mRadioGroup.getCheckedRadioButtonId() == R.id.radiobutton_1) {
                changeRightAdapter(0);
            } else if (mRadioGroup.getCheckedRadioButtonId() == R.id.radiobutton_2) {
                changeRightAdapter(1);
            } else if (mRadioGroup.getCheckedRadioButtonId() == R.id.radiobutton_3) {
                changeRightAdapter(2);
            }
            new TurnOverPrintTask().execute();
        }
    }
}
