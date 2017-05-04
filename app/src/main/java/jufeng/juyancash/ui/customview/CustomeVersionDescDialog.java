package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.DialogScheduleHistoryAdapter;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeVersionDescDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private DialogScheduleHistoryAdapter adapter;
    private OnScheduleHistoryListener mOnScheduleHistoryListener;
    private Button btnOpen,btnCancle,btnCancleSchedule;
    private ScheduleEntity mScheduleEntity;

    public CustomeVersionDescDialog(Context context, String tableId){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_schedule_history);
        initView(window);
        setAdapter(tableId);
        setListener();
    }

    private void initView(Window window){
        btnCancle = (Button) window.findViewById(R.id.btn_cancle);
        btnOpen = (Button) window.findViewById(R.id.btn_open);
        btnCancleSchedule = (Button) window.findViewById(R.id.btn_cancle_schedule);
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
    }

    private void setAdapter(String tableId){
        adapter = new DialogScheduleHistoryAdapter(mContext,tableId);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener(){
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnScheduleHistoryListener.onScheduleHistoryCancle();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mScheduleEntity != null){
                    mOnScheduleHistoryListener.onScheduleHistoryOpen(mScheduleEntity);
                }else{
                    CustomMethod.showMessage(mContext,"请选择预订单");
                }
            }
        });

        btnCancleSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mScheduleEntity != null){
                    mOnScheduleHistoryListener.onScheduleCancle(mScheduleEntity);
                }else{
                    CustomMethod.showMessage(mContext,"请选择预订单");
                }
            }
        });

        adapter.setOnDialogScheduleHistoryClickListener(new DialogScheduleHistoryAdapter.OnDialogScheduleHistoryClickListener() {
            @Override
            public void onDialogScheduleHistoryClick(ScheduleEntity scheduleEntity) {
                mScheduleEntity = scheduleEntity;
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnScheduleHistoryListener(OnScheduleHistoryListener listener){
        mOnScheduleHistoryListener = listener;
    }

    public interface OnScheduleHistoryListener{
        void onScheduleHistoryCancle();
        void onScheduleCancle(ScheduleEntity scheduleEntity);
        void onScheduleHistoryOpen(ScheduleEntity scheduleEntity);
    }
}
