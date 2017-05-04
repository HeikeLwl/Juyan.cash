package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectReturnOrderReasonAdapter;
import jufeng.juyancash.dao.SpecialEntity;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeSelectReturnOrderReasonDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private SelectReturnOrderReasonAdapter mAdapter;
    private OnReturnOrderReasonSelectedListener mOnReturnOrderReasonSelectedListener;
    private ImageButton ibCancle,ibConfirm;

    public CustomeSelectReturnOrderReasonDialog(Context context){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_select_return_order_reason);
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
        mAdapter = new SelectReturnOrderReasonAdapter(mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener(){
        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReturnOrderReasonSelectedListener.onCancleClick();
            }
        });

        ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReturnOrderReasonSelectedListener.onConfirmClick(mAdapter.getSelectSpecial());
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnReturnOrderReasonSelectedListener(OnReturnOrderReasonSelectedListener listener){
        mOnReturnOrderReasonSelectedListener = listener;
    }

    public interface OnReturnOrderReasonSelectedListener{
        void onCancleClick();
        void onConfirmClick(SpecialEntity specialEntity);
    }
}
