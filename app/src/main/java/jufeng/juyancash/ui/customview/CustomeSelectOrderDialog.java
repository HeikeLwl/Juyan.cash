package jufeng.juyancash.ui.customview;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectOrderAdapter;
import jufeng.juyancash.dao.OrderEntity;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeSelectOrderDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private SelectOrderAdapter mAdapter;
    private OnOrderSelectedListener mOnOrderSelectedListener;
    private ImageButton ibCancle,ibConfirm;

    public CustomeSelectOrderDialog(Context context, String tableId, ArrayList<OrderEntity> selectOrderes,String orderId){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_select_order);
        initView(window);
        setAdapter(tableId,selectOrderes,orderId);
        setListener();
    }

    private void initView(Window window){
        ibCancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        ibConfirm = (ImageButton) window.findViewById(R.id.ib_confirm);
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
    }

    private void setAdapter(String tableId,ArrayList<OrderEntity> orderEntities,String orderId){
        mAdapter = new SelectOrderAdapter(mContext.getApplicationContext(),tableId,orderEntities,orderId);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener(){
        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOrderSelectedListener.onCancleClick();
            }
        });

        ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOrderSelectedListener.onConfirmClick(mAdapter.getSelectOrderes());
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setOnOrderSelectedListener(OnOrderSelectedListener listener){
        mOnOrderSelectedListener = listener;
    }

    public interface OnOrderSelectedListener{
        void onCancleClick();
        void onConfirmClick(ArrayList<OrderEntity> selectOrderEntities);
    }
}
