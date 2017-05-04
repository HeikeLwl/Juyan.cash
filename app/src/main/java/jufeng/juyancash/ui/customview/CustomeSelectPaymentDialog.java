package jufeng.juyancash.ui.customview;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectPaymentAdapter;
import jufeng.juyancash.dao.Payment;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeSelectPaymentDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private SelectPaymentAdapter mAdapter;
    private OnPaymentSelectedListener mOnPaymentSelectedListener;

    public CustomeSelectPaymentDialog(Context context,int type){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_select_payment);
        initView(window);
        setAdapter(type);
    }

    private void initView(Window window){
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
    }

    private void setAdapter(int type){
        mAdapter = new SelectPaymentAdapter(mContext,type);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnPaymentClickListener(new SelectPaymentAdapter.OnPaymentClickListener() {
            @Override
            public void onPaymentItemClick(Payment payment) {
                mOnPaymentSelectedListener.onPaymentSelected(payment);
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnPaymentSelectedListener(OnPaymentSelectedListener listener){
        mOnPaymentSelectedListener = listener;
    }

    public interface OnPaymentSelectedListener{
        void onPaymentSelected(Payment payment);
    }
}
