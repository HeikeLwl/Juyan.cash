package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.ScheduleHistoryAdapter;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class LFragmentScheduleCancle extends BaseFragment {
    private RecyclerView mRecyclerView;
    private ScheduleHistoryAdapter adapter;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.updateData(2);
                    break;
            }
        }
    };

    public void setNewParam(){
        adapter.updateData(2);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setScheduleCancleHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule_cancle,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void setAdapter(){
        adapter = new ScheduleHistoryAdapter(getActivity(),2);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        adapter.setOnScheduleHistoryClickListener(new ScheduleHistoryAdapter.OnScheduleHistoryClickListener() {
            @Override
            public void onScheduleHistoryClick(ScheduleEntity scheduleEntity) {

            }

            @Override
            public void onClickMark(String markStr) {
                CustomMethod.showMessage(getContext(),"预定备注信息",markStr);
            }
        });
    }
}
