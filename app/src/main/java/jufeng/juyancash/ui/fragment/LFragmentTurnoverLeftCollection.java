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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderCollectionLeftAdapter;
import jufeng.juyancash.adapter.OrderTypeCollectionAdapter;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentTurnoverLeftCollection extends BaseFragment {
    private TextView tvName;
    private LinearLayout layoutTypeCollection;
    private RecyclerView mRecyclerView;
    private TextView tvTotal, tvMoney;
    private OrderTypeCollectionAdapter mTypeAdapter;
    private ListView mListView;
    private OrderCollectionLeftAdapter mCollectionAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.GONE);
                    tvName.setVisibility(TextView.GONE);
                    break;
                case 0://账单汇总
                    tvName.setVisibility(TextView.VISIBLE);
                    tvName.setText("帐单汇总");
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.VISIBLE);
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        int cashier = bundle.getInt("cashier");
                        String type = bundle.getString("type");
                        String cashierId = null;
                        if(cashier == 1){
                            //当前收银员
                            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                            cashierId = spf.getString("employeeId",null);
                        }
                        mCollectionAdapter = new OrderCollectionLeftAdapter(getActivity().getApplicationContext(),0,cashierId,0,null,-1,type);
                        mListView.setAdapter(mCollectionAdapter);
                    }
                    break;
                case 1://分类汇总
                    layoutTypeCollection.setVisibility(RecyclerView.VISIBLE);
                    mListView.setVisibility(ListView.GONE);
                    Bundle bundle1 = msg.getData();
                    tvName.setVisibility(TextView.VISIBLE);
                    tvName.setText("分类汇总");
                    if(bundle1 != null){
                        int cashier = bundle1.getInt("cashier");
                        String type = bundle1.getString("type");
                        String cashierId = null;
                        if(cashier == 1){
                            //当前收银员
                            SharedPreferences spf = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                            cashierId = spf.getString("employeeId",null);
                        }
                        mTypeAdapter.updateData(cashierId,0,null,-1,type);
                        tvTotal.setText(String.valueOf(DBHelper.getInstance(getActivity().getApplicationContext()).getTypeCollectionDishTypeTotal(cashierId,0,null,-1,type)));
                        tvMoney.setText(String.valueOf(DBHelper.getInstance(getActivity().getApplicationContext()).getTypeCollectionDishTypeTotalMoney(cashierId,0,null,-1,type)));
                    }
                    break;
                case 2://收银员
                    layoutTypeCollection.setVisibility(RecyclerView.GONE);
                    mListView.setVisibility(ListView.VISIBLE);
                    Bundle bundle2 = msg.getData();
                    if (bundle2 != null) {
                        String cashierId = bundle2.getString("employeeId");
                        String type = bundle2.getString("type");
                        tvName.setVisibility(TextView.VISIBLE);
                        tvName.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(cashierId));
                        mCollectionAdapter = new OrderCollectionLeftAdapter(getActivity().getApplicationContext(),1,cashierId,0,null,-1,type);
                        mListView.setAdapter(mCollectionAdapter);
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setTurnoverLeftCollectionHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover_left_collection, container, false);
        initView(mView);
        setListener();
        setAdapter();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        mListView = (ListView) view.findViewById(R.id.listview);
        layoutTypeCollection = (LinearLayout) view.findViewById(R.id.layout_type_collection);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        tvMoney = (TextView) view.findViewById(R.id.tv_money);
    }

    private void setListener() {

    }

    private void setAdapter(){
        mTypeAdapter = new OrderTypeCollectionAdapter(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mTypeAdapter);
    }
}
