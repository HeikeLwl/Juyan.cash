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
public class LFragmentTurnoverRightCollection extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OrderColletionAdapter adapter;
    private MainFragmentListener mOnTurnoverRightCollectionClickListener;
    private int employId;
    private String type;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    employId = msg.getData().getInt("employId",0);
                    type = msg.getData().getString("type");
//                    adapter.updateData(employId,0,null,"全部",-1,type);
                    break;
                case 1:
//                    adapter.updateData(employId,0,null,"全部",-1,type);
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
//        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setTurnoverRightCollectionHandler(mHandler);
        try {
            mOnTurnoverRightCollectionClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover_right_collection, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        employId = getArguments().getInt("employeeId");
        type = getArguments().getString("type");
    }

    @Override
    public void onResume() {
        super.onResume();
//        adapter.updateData(employId,0,null,"全部",-1,type);
        mOnTurnoverRightCollectionClickListener.turnoverNothing();
    }

    private void setAdapter() {
//        adapter = new OrderColletionAdapter(getActivity().getApplicationContext(),employId,0,null,"全部",-1,type);
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusable(false);
    }

    private void setListener() {
//        adapter.setOnOrderColletionItemClickListener(new OrderColletionAdapter.OnOrderColletionItemClickListener() {
//            @Override
//            public void onNothingClick() {
//                mOnTurnoverRightCollectionClickListener.turnoverNothing();
//            }
//
//            @Override
//            public void onOrderCashierClick(String cashierId, int shift, String payModeId,String payModeName, int date,String type) {
//                mOnTurnoverRightCollectionClickListener.turnoverCashier(cashierId, shift, payModeId, date,type);
//            }
//
//            @Override
//            public void onOrderCollectionClick(int cashier, int shift, String payModeId,String payModeName, int date,String type) {
//                mOnTurnoverRightCollectionClickListener.turnoverCollection(cashier, shift, payModeId, date,type);
//            }
//
//            @Override
//            public void onTypeCollectionClick(int cashier, int shift, String payModeId, String payModeName,int date,String type) {
//                mOnTurnoverRightCollectionClickListener.turnoverTypeCollection(cashier, shift, payModeId, date,type);
//            }
//        });
    }
}
