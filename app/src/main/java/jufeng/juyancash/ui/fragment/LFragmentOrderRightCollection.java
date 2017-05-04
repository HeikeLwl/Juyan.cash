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
import jufeng.juyancash.adapter.OrderColletionAdapter;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderRightCollection extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OrderColletionAdapter adapter;
    private MainFragmentListener mOnOrderRightCollectionClickListener;
    private int employId;
    private int shift;
    private String paymodeId;
    private String payModeName;
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
                    payModeName = msg.getData().getString("payModeName");
                    date = msg.getData().getInt("date",-1);
                    type = msg.getData().getString("type");
//                    adapter.updateData(employId,shift,paymodeId,payModeName,date,type);
                    break;
                case 1:
//                    adapter.updateData(employId,shift,paymodeId,payModeName,date,type);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setOrderRightCollectionHandler(mHandler);
        try {
            mOnOrderRightCollectionClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_right_collection, container, false);
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
        payModeName = getArguments().getString("payModeName");
        date = getArguments().getInt("date");
        type = getArguments().getString("type");
    }

    @Override
    public void onResume() {
        super.onResume();
//        adapter.updateData(employId,shift,paymodeId,payModeName,date,type);
        mOnOrderRightCollectionClickListener.nothing();
    }

    private void setAdapter() {
//        adapter = new OrderColletionAdapter(getActivity().getApplicationContext(),employId,shift,paymodeId,payModeName,date,type);
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener() {
//        adapter.setOnOrderColletionItemClickListener(new OrderColletionAdapter.OnOrderColletionItemClickListener() {
//            @Override
//            public void onNothingClick() {
//                mOnOrderRightCollectionClickListener.nothing();
//            }
//
//            @Override
//            public void onOrderCashierClick(String cashierId, int shift, String payModeId,String payModeName, int date,String type) {
//                mOnOrderRightCollectionClickListener.orderCashier(cashierId, shift, payModeId,payModeName, date,type);
//            }
//
//            @Override
//            public void onOrderCollectionClick(int cashier, int shift, String payModeId,String payModeName, int date,String type) {
//                mOnOrderRightCollectionClickListener.orderCollection(cashier, shift, payModeId,payModeName, date,type);
//            }
//
//            @Override
//            public void onTypeCollectionClick(int cashier, int shift, String payModeId,String payModeName, int date,String type) {
//                mOnOrderRightCollectionClickListener.typeCollection(cashier, shift, payModeId,payModeName, date,type);
//            }
//        });
    }
}
