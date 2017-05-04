package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderRightStatisticAdapter;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderRightStatistic extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OrderRightStatisticAdapter adapter;
    private MainFragmentListener mOnOrderRightStatisticClickListener;
    private int employId;
    private int shift;
    private String paymodeId;
    private int date;
    private String type;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    employId = msg.getData().getInt("employeeId",0);
                    shift = msg.getData().getInt("shift",-1);
                    paymodeId = msg.getData().getString("payModeId");
                    date = msg.getData().getInt("date",-1);
                    type = msg.getData().getString("type");
                    adapter.updateData();
                    break;
                case 1:
                    adapter.updateData();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setOrderRightStatisticHandler(mHandler);
        try{
            mOnOrderRightStatisticClickListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_right_statistic, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        employId = getArguments().getInt("employeeId");
        shift = getArguments().getInt("shift");
        paymodeId = getArguments().getString("payModeId");
        date = getArguments().getInt("date");
        type = getArguments().getString("type");
    }

    private void setAdapter() {
        adapter = new OrderRightStatisticAdapter(getActivity().getApplicationContext());
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 7);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        adapter.setOnOrderRightStatisticItemClickListener(new OrderRightStatisticAdapter.OnOrderRightStatisticItemClickListener() {
            @Override
            public void onOrderRightStatisticItemClick(DishTypeModel dishTypeModel) {
                mOnOrderRightStatisticClickListener.onOrderRightStatisticClick(dishTypeModel,shift,date);
            }
        });
    }
}
