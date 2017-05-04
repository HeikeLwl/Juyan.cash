package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectCancelMeituanOrderReasonAdapter;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeCancelMeituanOrderReasonDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private SelectCancelMeituanOrderReasonAdapter mAdapter;
    private OnMeituanCancelReasonSelectedListener mOnMeituanCancelReasonSelectedListener;
    private ImageButton ibCancle,ibConfirm;

    public CustomeCancelMeituanOrderReasonDialog(Context context){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_select_meituan_cancel_reason);
        initView(window);
        setAdapter();
        setListener();
    }

    private void initView(Window window){
        ibCancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        ibConfirm = (ImageButton) window.findViewById(R.id.ib_confirm);
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
    }

    private void setAdapter(){
        mAdapter = new SelectCancelMeituanOrderReasonAdapter(mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener(){
        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMeituanCancelReasonSelectedListener.onCancleClick();
            }
        });

        ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectReason() == -1){
                    Snackbar.make(ibConfirm,"请选择取消原因",Snackbar.LENGTH_SHORT).show();
                }else {
                    mOnMeituanCancelReasonSelectedListener.onConfirmClick(mAdapter.getSelectReason());
                }
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnReturnOrderReasonSelectedListener(OnMeituanCancelReasonSelectedListener listener){
        mOnMeituanCancelReasonSelectedListener = listener;
    }

    public interface OnMeituanCancelReasonSelectedListener{
        void onCancleClick();
        void onConfirmClick(int reasonCode);
    }
}
