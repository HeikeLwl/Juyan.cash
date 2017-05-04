package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RemindDialogAdapter;
import jufeng.juyancash.bean.RemindBean;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.dao.WXMessageEntity;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeRemindDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private TextView tvTableName, tvOrderNumber;
    private RemindDialogAdapter mAdapter;
    private TextView tvCancle,tvConfirm,tvReminded;
    private WXMessageEntity mWXMessageEntity;
    private RemindDialogClickListener mRemindDialogClickListener;
    private RemindBean remindBean;

    public CustomeRemindDialog(Context context, WXMessageEntity wxMessageEntity) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        this.mWXMessageEntity = wxMessageEntity;
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_remind_layout);
        initView(window);
        setAdapter();
        setListener();
    }

    private void initView(Window window) {
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
        tvTableName = (TextView) window.findViewById(R.id.tv_table_name);
        tvOrderNumber = (TextView) window.findViewById(R.id.tv_order_number);
        tvCancle = (TextView) window.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) window.findViewById(R.id.tv_remind);
        tvReminded = (TextView) window.findViewById(R.id.tv_reminded);
    }

    private void setAdapter() {
        try {
            remindBean = JSON.parseObject(mWXMessageEntity.getWxContent(), RemindBean.class);
            OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(remindBean.getOrderId());
            if(orderEntity.getStoreVersion() == 0){
                TableEntity tableEntity = DBHelper.getInstance(mContext).queryOneTableData(orderEntity.getTableId());
                tvTableName.setText(tableEntity.getTableName() + "(" + tableEntity.getTableCode() + ")");
            }else if(orderEntity.getStoreVersion() == 1){
                tvTableName.setText("无");
            }
            tvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
            mAdapter = new RemindDialogAdapter(mContext.getApplicationContext(), remindBean);
        }catch (Exception e){
            tvTableName.setText("无");
            tvOrderNumber.setText("无");
            mAdapter = null;
        }
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener(){
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mAdapter != null && remindBean != null) {
                        mRemindDialogClickListener.onRemindConfirm(remindBean.getOrderId(), mAdapter.getOrderDishEntities());
                    }
                }catch (Exception e){

                }
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindDialogClickListener.onRemindCancle();
            }
        });

        tvReminded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindDialogClickListener.onRemindedClick();
            }
        });
    }

    public void setRemindDialogClickListener(RemindDialogClickListener listener){
        this.mRemindDialogClickListener = listener;
    }

    public  interface RemindDialogClickListener{
        void onRemindCancle();
        void onRemindConfirm(String orderId,ArrayList<OrderDishEntity> orderDishEntities);
        void onRemindedClick();
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }
}
