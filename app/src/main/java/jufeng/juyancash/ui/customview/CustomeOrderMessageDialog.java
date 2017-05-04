package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderMessageAdapter;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeOrderMessageDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private TextView tvConfirm,tvClearAll;
    private OrderMessageAdapter mAdapter;
    private OnOrderMessageClearListener mOnOrderMessageClearListener;

    public CustomeOrderMessageDialog(Context context){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_order_message);
        initView(window);
        setAdapter();
        setListner();
    }

    private void initView(Window window){
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
        tvClearAll = (TextView) window.findViewById(R.id.tv_clear_all);
        tvConfirm = (TextView) window.findViewById(R.id.tv_confirm);
    }

    private void setAdapter(){
        mAdapter = new OrderMessageAdapter(mContext.getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListner(){
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(mContext).deleteAllWxOrderMessage();
                dismiss();
                if(mOnOrderMessageClearListener != null){
                    mOnOrderMessageClearListener.onClearAllOrderMessage();
                }
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnOrderMessageClearListener(OnOrderMessageClearListener listener){
        this.mOnOrderMessageClearListener = listener;
    }

    public interface OnOrderMessageClearListener{
        void onClearAllOrderMessage();
    }
}
