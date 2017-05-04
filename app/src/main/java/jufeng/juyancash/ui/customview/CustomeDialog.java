package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.util.CustomMethod;


/**
 * Created by 15157_000 on 2016/6/18 0018.
 */
public class CustomeDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private TextView title;
    private TextView tableCount;
    private TextView tvMealTime;
    private ImageButton cancle;
//    private AppCompatSpinner mSpinner;//选择单号
    //空闲状态：0
    private Button btnOpen;//开台
    private Button btnSchedule;//预定
    private Button btnQuickOpen;//快速开台
    private Button btnScheduleHistory;//预定记录
    //使用中状态：1
    private Button btnOrder;//点餐
    private Button btnJoint;//合台
//    private Button btnAnd;//并台
    private Button btnOpenAgain;//再开一台
    private Button btnReplaceTable;//更换桌位
    private Button btnPresent;//赠菜
    private Button btnRetreat;//退菜
    private Button btnRemind;//催才
    private Button btnCancleTable;//取消开台
    private Button btnCashier;//收银系统
    private Button btnSchedule1;
    private Button btnScheduleHistory1;
    private TextView tvSpend;//消费金额
    private Button btnCancleJoinTable;//取消合台
//    private Button btnCancleJoinOrder;//取消并单
    private TextView tvOrderStatus;
    private FlowRadioGroup mRadioGroup;
    //已结账状态：3
    private Button btnClear;//清台
    private Button btnSchedule2;
    private Button btnScheduleHistory2;
    private OnDialogItemClickListener mOnDialogItemClickListener;
    private TableEntity mTableEntity;
    private String orderId;
    private ArrayList<OrderEntity> mOrderEntities;
    private Drawable mWeChatPayed,mWeChatUnPay,mStoreUnPay,mStorePay;

    public CustomeDialog(Context context, TableEntity tableEntity) {
        this.mContext = context;
        this.mTableEntity = tableEntity;
        this.orderId = null;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.setCancelable(false);
        Window window = mAlertDialog.getWindow();
        switch(tableEntity.getTableStatus()){
            case 0:
                window.setContentView(R.layout.dialog_layout_green);
                initView0(window);
                break;
            case 1:
                window.setContentView(R.layout.dialog_layout_red);
                initView1(window);
                break;
            case 3:
                window.setContentView(R.layout.dialog_layout_blue);
                initView3(window);
                break;
        }
        title.setText(mTableEntity.getTableName()+"("+mTableEntity.getTableCode()+")");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.cancle(mTableEntity);
            }
        });
    }

    private void initView0(Window window) {
//        mSpinner = (AppCompatSpinner) window.findViewById(R.id.spinner);
        title = (TextView) window.findViewById(R.id.title);
        tableCount = (TextView) window.findViewById(R.id.tv_table_seat_count);
        cancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        btnOpen = (Button) window.findViewById(R.id.btn_open);
        btnSchedule = (Button) window.findViewById(R.id.btn_schedule);
        btnQuickOpen = (Button) window.findViewById(R.id.btn_quickopen);
        btnScheduleHistory = (Button) window.findViewById(R.id.btn_schedule_history);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.openTable(mTableEntity,orderId);
            }
        });
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleTable(mTableEntity);
            }
        });
        btnQuickOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.quickOpenTable(mTableEntity,orderId);
            }
        });
        btnScheduleHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleRecord(mTableEntity);
            }
        });
        tableCount.setText("桌位："+mTableEntity.getTableSeat()+"座");
    }

    private void initView1(Window window) {
        mWeChatPayed = mContext.getResources().getDrawable(R.drawable.wechat_pay);
        mWeChatPayed.setBounds(0,0,mWeChatPayed.getMinimumWidth(),mWeChatPayed.getMinimumHeight());
        mWeChatUnPay = mContext.getResources().getDrawable(R.drawable.wechat_unpay);
        mWeChatUnPay.setBounds(0,0,mWeChatUnPay.getMinimumWidth(),mWeChatUnPay.getMinimumHeight());
        mStoreUnPay = mContext.getResources().getDrawable(R.drawable.store_unpay);
        mStoreUnPay.setBounds(0,0,mStoreUnPay.getMinimumWidth(),mStoreUnPay.getMinimumHeight());
        mStorePay = mContext.getResources().getDrawable(R.drawable.store_pay);
        mStorePay.setBounds(0,0,mStorePay.getMinimumWidth(),mStorePay.getMinimumHeight());
//        mSpinner = (AppCompatSpinner) window.findViewById(R.id.spinner);
        title = (TextView) window.findViewById(R.id.title);
        tableCount = (TextView) window.findViewById(R.id.tv_meal_count);
        tvMealTime = (TextView) window.findViewById(R.id.tv_meal_time);
        tvSpend = (TextView) window.findViewById(R.id.tv_table_spend);
        cancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        btnOrder = (Button) window.findViewById(R.id.btn_order);
        btnJoint = (Button) window.findViewById(R.id.btn_jointtable);
//        btnAnd = (Button) window.findViewById(R.id.btn_andtable);
        btnOpenAgain = (Button) window.findViewById(R.id.btn_openagain);
        btnReplaceTable = (Button) window.findViewById(R.id.btn_replace_table);
        btnPresent = (Button) window.findViewById(R.id.btn_present_dish);
        btnRetreat = (Button) window.findViewById(R.id.btn_retreat_dish);
        btnRemind = (Button) window.findViewById(R.id.btn_remind_dish);
        btnCancleTable = (Button) window.findViewById(R.id.btn_cancle_table);
        btnCashier = (Button) window.findViewById(R.id.btn_cashier);
        btnCancleJoinTable = (Button) window.findViewById(R.id.btn_cancle_join_table);
//        btnCancleJoinOrder = (Button) window.findViewById(R.id.btn_cancle_join_order);
        btnSchedule1 = (Button) window.findViewById(R.id.btn_schedule);
        btnScheduleHistory1 = (Button) window.findViewById(R.id.btn_schedule_history);
        mRadioGroup = (FlowRadioGroup) window.findViewById(R.id.radiogroup);
        tvOrderStatus = (TextView) window.findViewById(R.id.tv_order_status);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.orderDish(mTableEntity,orderId);
            }
        });
        btnJoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.jointTable(mTableEntity,orderId);
            }
        });
//        btnAnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnDialogItemClickListener.andTable(mTableEntity,orderId);
//            }
//        });
        btnOpenAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.openAgain(mTableEntity,orderId);
            }
        });
        btnReplaceTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.replaceTable(mTableEntity,orderId);
            }
        });
        btnPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.presentDish(mTableEntity,orderId);
            }
        });
        btnRetreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.retreatDish(mTableEntity,orderId);
            }
        });
        btnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.remindDish(mTableEntity,orderId);
            }
        });
        btnCancleTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.cancleTable(mTableEntity,orderId);
            }
        });
        btnCashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.cashier(mTableEntity,orderId);
            }
        });
        btnCancleJoinTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.cancleJoinTable(mTableEntity,orderId);
            }
        });
//        btnCancleJoinOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(orderId != null) {
//                    OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
//                    if (orderEntity != null && orderEntity.getIsJoinedOrder() == 1 && orderEntity.getJoinedOrderId() != null)
//                        mOnDialogItemClickListener.cancleJoinOrder(mTableEntity, orderEntity.getJoinedOrderId());
//                }
//            }
//        });

        btnSchedule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleTable(mTableEntity);
            }
        });
        btnScheduleHistory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleRecord(mTableEntity);
            }
        });

        //获取该桌位是否有合台
        btnCancleJoinTable.setVisibility(DBHelper.getInstance(mContext).isJoinTable(mTableEntity.getTableId()) ? Button.VISIBLE : Button.GONE);
        //获取开台的桌位账单
        mOrderEntities = DBHelper.getInstance(mContext).queryOrderData(mTableEntity.getTableId(),0,0);
        if(mOrderEntities.size() > 0) {
            orderId = mOrderEntities.get(0).getOrderId();
            setData(mOrderEntities.get(0));
            addOrderRadio(mOrderEntities);
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    OrderEntity orderEntity = mOrderEntities.get(group.getCheckedRadioButtonId());
                    orderId = orderEntity.getOrderId();
                    setData(orderEntity);
                }
            });
        }
    }
    
    public void setData(OrderEntity orderEntity){
        tableCount.setText("就餐人数：" + orderEntity.getOrderGuests() + "人");
        tvMealTime.setText("开台时间：" + CustomMethod.parseTime(orderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
        tvSpend.setText("消费金额：￥" + DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderEntity.getOrderId()));
//        initBtnVisibility(orderEntity);
        if(orderEntity.getIsUpload() == 1){
            //微信点餐
            if(!DBHelper.getInstance(mContext).isHasOrderedDish(orderEntity.getOrderId())){
                //没有已下单商品
                tvOrderStatus.setText("微信点餐未下单");
                tvOrderStatus.setCompoundDrawables(mWeChatUnPay,null,null,null);
            }else {
                //未支付
                tvOrderStatus.setText("微信点餐待结账");
                tvOrderStatus.setCompoundDrawables(mWeChatPayed,null,null,null);
            }
        }else{
            //到店点餐
            if(!DBHelper.getInstance(mContext).isHasOrderedDish(orderEntity.getOrderId())){
                //没有已下单商品
                tvOrderStatus.setText("到店点餐未下单");
                tvOrderStatus.setCompoundDrawables(mStoreUnPay,null,null,null);
            }else {
                //未支付
                tvOrderStatus.setText("到店点餐待结账");
                tvOrderStatus.setCompoundDrawables(mStorePay,null,null,null);
            }
        }
    }

    //添加账单radiobutton
    private void addOrderRadio(ArrayList<OrderEntity> orderEntities){
        for (int i = 0; i < orderEntities.size(); i++) {
            OrderEntity orderEntity = orderEntities.get(i);
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText("NO."+orderEntity.getOrderNumber1());
            radioButton.setTag(orderEntity.getOrderId());
            radioButton.setId(i);
            mRadioGroup.addView(radioButton);
        }
        if(mRadioGroup.getChildCount() > 0) {
            mRadioGroup.check(0);
        }
    }

//    //根据是否是并单设置按钮的可视化
//    public void initBtnVisibility(OrderEntity orderEntity){
//        if(orderEntity.getIsJoinedOrder() == 1 && orderEntity.getJoinedOrderId() != null){
//            btnCancleJoinOrder.setVisibility(Button.VISIBLE);
////            btnAnd.setVisibility(Button.GONE);
//        }else {
//            btnCancleJoinOrder.setVisibility(Button.GONE);
////            btnAnd.setVisibility(Button.VISIBLE);
//        }
//    }

    private void initView3(Window window){
        title = (TextView) window.findViewById(R.id.title);
        tableCount = (TextView) window.findViewById(R.id.tv_table_seat_count);
        cancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        btnClear = (Button) window.findViewById(R.id.btn_clear);
        btnSchedule2 = (Button) window.findViewById(R.id.btn_schedule);
        btnScheduleHistory2 = (Button) window.findViewById(R.id.btn_schedule_history);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.clear(mTableEntity,orderId);
            }
        });
        btnSchedule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleTable(mTableEntity);
            }
        });
        btnScheduleHistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.scheduleRecord(mTableEntity);
            }
        });
        tableCount.setText("桌位："+mTableEntity.getTableSeat()+"座");
    }

    //关闭对话框
    public void dismiss() {
        mAlertDialog.dismiss();
        mAlertDialog = null;
        orderId = null;
    }

    //设置点击事件监听器
    public void setListener(OnDialogItemClickListener listener){
        this.mOnDialogItemClickListener = listener;
    }

    public interface OnDialogItemClickListener{
        void openTable(TableEntity mTableEntity,String orderId);//空桌开台
        void scheduleTable(TableEntity mTableEntity);//预定
        void quickOpenTable(TableEntity mTableEntity,String orderId);//快速开台
        void orderDish(TableEntity mTableEntity,String orderId);//点餐
        void jointTable(TableEntity mTableEntity,String orderId);//合台
        void andTable(TableEntity mTableEntity,String orderId);//并台
        void openAgain(TableEntity mTableEntity,String orderId);//再开一单
        void replaceTable(TableEntity mTableEntity,String orderId);//更换桌位
        void presentDish(TableEntity mTableEntity,String orderId);//赠菜
        void retreatDish(TableEntity mTableEntity,String orderId);//退菜
        void remindDish(TableEntity mTableEntity,String orderId);//催菜
        void cancleTable(TableEntity mTableEntity,String orderId);//取消开台
        void cashier(TableEntity mTableEntity,String orderId);//收银系统
        void scheduleRecord(TableEntity mTableEntity);//预定记录
        void clear(TableEntity mTableEntity,String orderId);//清台
        void cancle(TableEntity tableEntity);//取消
        void cancleJoinTable(TableEntity tableEntity,String orderId);//取消合台
        void cancleJoinOrder(TableEntity tableEntity,String orderId);//取消并单
    }
}
