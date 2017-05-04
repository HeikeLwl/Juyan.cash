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
public class LFragmentScheduleEffect extends BaseFragment implements View.OnClickListener,ScheduleHistoryAdapter.OnScheduleHistoryClickListener{
    private RecyclerView mRecyclerView;
    private Button btnCancle;
    private Button btnConfirm;
    private ScheduleHistoryAdapter adapter;
    private ScheduleEntity mScheduleEntity;
    private MainFragmentListener mOnScheduleEffectClickListener;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.updateData(1);
                    break;
            }
        }
    };

    public void setNewParam(){
        adapter.updateData(1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setScheduleEffectHandler(mHandler);
        try{
            mOnScheduleEffectClickListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule_effect,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle_order);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm_order);
    }

    private void setAdapter(){
        adapter = new ScheduleHistoryAdapter(getActivity(),1);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        btnCancle.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        adapter.setOnScheduleHistoryClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancle_order://取消预定
                //预定单状态改为2，并且桌位状态改为0
                if(mScheduleEntity != null) {
                    DBHelper.getInstance(getContext().getApplicationContext()).cancleScheduleTable(mScheduleEntity.getTableId(),mScheduleEntity.getOrderId());
                    DBHelper.getInstance(getActivity().getApplicationContext()).updateTableStatus(mScheduleEntity.getTableId(), 0);
                    if(mScheduleEntity.getScheduleFrom() == 1) {
                        DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mScheduleEntity.getScheduleId(), DBHelper.getInstance(getContext().getApplicationContext()).getTableNameByTableId(mScheduleEntity.getTableId()), 12);
                    }else{
                        String data = "{\"mobile\":\""+mScheduleEntity.getGuestPhone()+"\",\"eatTime\":\""+ CustomMethod.parseTime(mScheduleEntity.getMealTime(),"yyyy年MM月dd日 HH:mm")+"\",\"op\":\""+1+"\"}";
                        DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mScheduleEntity.getOrderId(),data,18);
                    }
                    adapter.updateData(1);
                    mOnScheduleEffectClickListener.onScheduleCancle();
                }
                break;
            case R.id.btn_confirm_order://到店确认
                //预订单状态改为3，并且桌位状态改为1
                if(mScheduleEntity != null){
                    DBHelper.getInstance(getActivity().getApplicationContext()).openScheduleTable(mScheduleEntity.getTableId(), mScheduleEntity.getOrderId());
                    if(mScheduleEntity.getScheduleFrom() == 1) {
                        DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mScheduleEntity.getScheduleId(), DBHelper.getInstance(getContext().getApplicationContext()).getTableNameByTableId(mScheduleEntity.getTableId()), 11);
                    }
                    adapter.updateData(1);
                    mOnScheduleEffectClickListener.onScheduleConfirm();
                }
                break;
        }
    }

    @Override
    public void onScheduleHistoryClick(ScheduleEntity scheduleEntity) {
        if(scheduleEntity == null){
            //当前没有选中项
            btnCancle.setClickable(false);
            btnConfirm.setClickable(false);
        }else{
            btnCancle.setClickable(true);
            btnConfirm.setClickable(true);
        }
        mScheduleEntity = scheduleEntity;
    }

    @Override
    public void onClickMark(String markStr) {
        CustomMethod.showMessage(getContext(),"预定备注信息",markStr);
    }
}
