package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderCollectionLeftAdapter;
import jufeng.juyancash.adapter.OrderTypeCollectionAdapter;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderLeftCollection extends BaseFragment {
    private TextView tvName;
    private LinearLayout layoutTypeCollection;
    private RecyclerView mRecyclerView;
    private TextView tvTotal, tvMoney;
    private OrderTypeCollectionAdapter mTypeAdapter;
    private ListView mListView;
    private OrderCollectionLeftAdapter mCollectionAdapter;
    private Button btnPrint;
    private int currentType;
    private String cashierName;
    private String isShift;
    private String payModeName = "";
    private int dateType;
    private String areaTypeName;
    private MainFragmentListener mMainFragmentListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    currentType = -1;
                    cashierName = "全部";
                    isShift = "全部";
                    payModeName = "全部";
                    dateType = -1;
                    areaTypeName = "全部";
                    btnPrint.setVisibility(Button.GONE);
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.GONE);
                    tvName.setVisibility(TextView.GONE);
                    break;
                case 0://账单汇总
                    currentType = 0;
                    btnPrint.setVisibility(Button.VISIBLE);
                    tvName.setVisibility(TextView.VISIBLE);
                    tvName.setText("帐单汇总");
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.VISIBLE);
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        int cashier = bundle.getInt("cashier");
                        int shift = bundle.getInt("shift");
                        String payModeId = bundle.getString("payModeId");
                        int date = bundle.getInt("date");
                        String type = bundle.getString("type");
                        payModeName = bundle.getString("payModeName");
                        String cashierId = null;
                        if(cashier == 1){
                            //当前收银员
                            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                            cashierId = spf.getString("employeeId",null);
                            cashierName = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(cashierId);
                        }else{
                            cashierName = "全部";
                        }
                        isShift = shift == -1?"全部":(shift == 0?"未交接":"已交接");
                        dateType = date;
                        areaTypeName = type;
                        mCollectionAdapter = new OrderCollectionLeftAdapter(getActivity().getApplicationContext(),0,cashierId,shift,payModeId,date,type);
                        mListView.setAdapter(mCollectionAdapter);
                    }
                    break;
                case 1://分类汇总
                    currentType = 1;
                    btnPrint.setVisibility(Button.VISIBLE);
                    layoutTypeCollection.setVisibility(RecyclerView.VISIBLE);
                    mListView.setVisibility(ListView.GONE);
                    Bundle bundle1 = msg.getData();
                    tvName.setVisibility(TextView.VISIBLE);
                    tvName.setText("分类汇总");
                    if(bundle1 != null){
                        int cashier = bundle1.getInt("cashier");
                        int shift = bundle1.getInt("shift");
                        String payModeId = bundle1.getString("payModeId");
                        payModeName = bundle1.getString("payModeName");
                        int date = bundle1.getInt("date");
                        String type = bundle1.getString("type");
                        String cashierId = null;
                        if(cashier == 1){
                            //当前收银员
                            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                            cashierId = spf.getString("employeeId",null);
                            cashierName = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(cashierId);
                        }else{
                            cashierName = "全部";
                        }
                        isShift = shift == -1?"全部":(shift == 0?"未交接":"已交接");
                        dateType = date;
                        areaTypeName = type;
                        mTypeAdapter.updateData(cashierId,shift,payModeId,date,type);
                        tvTotal.setText(String.valueOf(DBHelper.getInstance(getActivity().getApplicationContext()).getTypeCollectionDishTypeTotal(cashierId,shift,payModeId,date,type)));
                        tvMoney.setText("￥"+String.valueOf(DBHelper.getInstance(getActivity().getApplicationContext()).getTypeCollectionDishTypeTotalMoney(cashierId,shift,payModeId,date,type)));
                    }
                    break;
                case 2://收银员
                    currentType = 2;
                    btnPrint.setVisibility(Button.VISIBLE);
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.VISIBLE);
                    Bundle bundle2 = msg.getData();
                    if (bundle2 != null) {
                        String cashierId = bundle2.getString("employeeId");
                        int shift = bundle2.getInt("shift");
                        String payModeId = bundle2.getString("payModeId");
                        payModeName = bundle2.getString("payModeName");
                        int date = bundle2.getInt("date");
                        String type = bundle2.getString("type");
                        cashierName = DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(cashierId);
                        isShift = shift == -1?"全部":(shift == 0?"未交接":"已交接");
                        dateType = date;
                        areaTypeName = type;
                        tvName.setVisibility(TextView.VISIBLE);
                        tvName.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(cashierId));
                        mCollectionAdapter = new OrderCollectionLeftAdapter(getActivity().getApplicationContext(),1,cashierId,shift,payModeId,date,type);
                        mListView.setAdapter(mCollectionAdapter);
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setOrderLeftCollectionHandler(mHandler);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroy() {
        this.mMainFragmentListener = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_left_collection, container, false);
        initView(mView);
        setListener();
        setAdapter();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        mListView = (ListView) view.findViewById(R.id.listview);
        layoutTypeCollection = (LinearLayout) view.findViewById(R.id.layout_type_collection);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
        currentType = -1;
        btnPrint.setVisibility(Button.GONE);
    }

    private void setListener() {
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainFragmentListener != null){
                    switch (currentType){
                        case 0:
                            mMainFragmentListener.printOrderCollection(cashierName,isShift,payModeName,dateType,areaTypeName,mCollectionAdapter.getOrderDetialCollection(),mCollectionAdapter.getPayModeEntity());
                            break;
                        case 1:
                            mMainFragmentListener.printTypeCollection(cashierName,isShift,payModeName,dateType,areaTypeName,mTypeAdapter.getDishTypeCollectionItemBean());
                            break;
                        case 2:
                            mMainFragmentListener.printCashierCollection(cashierName,isShift,payModeName,dateType,areaTypeName,mCollectionAdapter.getOrderDetialCollection(),mCollectionAdapter.getPayModeEntity());
                            break;
                    }
                }
            }
        });
    }

    private void setAdapter(){
        mTypeAdapter = new OrderTypeCollectionAdapter(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mTypeAdapter);
    }
}
