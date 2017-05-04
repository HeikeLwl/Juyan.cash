package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
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

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.ScheduleHistoryAdapter;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class LFragmentScheduleCheck extends BaseFragment implements ScheduleHistoryAdapter.OnScheduleHistoryClickListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private Button btnRefuse;
    private Button btnPass;
    private ScheduleHistoryAdapter adapter;
    private ScheduleEntity mScheduleEntity;
    private MainFragmentListener mOnScheduleCheckListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.updateData(0);
                    break;
                case 1:
                    adapter.updateData(0);
                    Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
                    mIntent.putExtra("type", 1);
                    getActivity().sendBroadcast(mIntent);
                    break;
            }
        }
    };

    public void setNewParam(){
        adapter.updateData(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setScheduleCheckHandler(mHandler);
        try {
            mOnScheduleCheckListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule_check, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnRefuse = (Button) view.findViewById(R.id.btn_refuse_schedule);
        btnPass = (Button) view.findViewById(R.id.btn_pass_schedule);
    }

    private void setAdapter() {
        adapter = new ScheduleHistoryAdapter(getActivity(), 0);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener() {
        adapter.setOnScheduleHistoryClickListener(this);
        btnPass.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refuse_schedule:
                //拒绝预定
                if(mScheduleEntity != null) {
                    DBHelper.getInstance(getContext().getApplicationContext()).changeScheduleStatusById(mScheduleEntity.getScheduleId(), 2);
                    if(mScheduleEntity.getScheduleFrom() == 1) {
                        DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mScheduleEntity.getScheduleId(), mScheduleEntity.getScheduleId(), 9);
                    }
                    adapter.updateData(0);
                    if(mOnScheduleCheckListener != null){
                        mOnScheduleCheckListener.checkCancle();
                    }
                }else{

                }
                break;
            case R.id.btn_pass_schedule:
                //选择桌位
                if(mScheduleEntity != null) {
                    mOnScheduleCheckListener.scheduleConfirmTable(mScheduleEntity);
                }else{

                }
                break;
        }
    }

    @Override
    public void onScheduleHistoryClick(ScheduleEntity scheduleEntity) {
        if (scheduleEntity == null) {
            //当前没有选中项
            btnRefuse.setClickable(false);
            btnPass.setClickable(false);
        } else {
            btnRefuse.setClickable(true);
            btnPass.setClickable(true);
        }
        mScheduleEntity = scheduleEntity;
    }

    @Override
    public void onClickMark(String markStr) {
        CustomMethod.showMessage(getContext(),"预定备注信息",markStr);
    }
}
