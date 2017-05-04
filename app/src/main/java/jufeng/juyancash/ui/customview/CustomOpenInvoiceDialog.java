package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;

/**
 * Created by Administrator102 on 2016/11/8.
 */

public class CustomOpenInvoiceDialog  {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private TextView tvTitle,tvOrderMoney;
    private EditText etInvoiceMoney;
    private Button btnOpen;
    private ImageButton ibCancle;
    private OpenInvoiceListener mOpenInvoiceListener;
    private OrderEntity mOrderEntity;

    public CustomOpenInvoiceDialog(Context context,String orderId){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_open_invoice);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initView(window,orderId);
        setListener();
    }

    private void initView(Window window, String orderId){
        mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        tvTitle = (TextView) window.findViewById(R.id.title);
        tvOrderMoney = (TextView) window.findViewById(R.id.tv_order_money);
        ibCancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        etInvoiceMoney = (EditText) window.findViewById(R.id.et_invoice_money);
        btnOpen = (Button) window.findViewById(R.id.btn_open);
        if(mOrderEntity != null) {
            double receivableMoney = DBHelper.getInstance(mContext).getReceivableMoney(orderId,1);
            tvTitle.setText("订单NO."+mOrderEntity.getOrderNumber1());
            tvOrderMoney.setText(String.valueOf(receivableMoney));
            etInvoiceMoney.setText(String.valueOf(receivableMoney));
        }
    }

    private void setListener(){
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOrderEntity != null && !etInvoiceMoney.getText().toString().isEmpty() && Float.valueOf(etInvoiceMoney.getText().toString()) <= Float.valueOf(tvOrderMoney.getText().toString())) {
                    mOpenInvoiceListener.onOpenClick(mOrderEntity, Float.valueOf(etInvoiceMoney.getText().toString()));
                }else{
                    Snackbar.make(etInvoiceMoney,"发票金额不正确",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenInvoiceListener.onCancleClick();
            }
        });
    }

    //设置开台点击事件
    public void setOpenInvoiceListener(OpenInvoiceListener listener){
        mOpenInvoiceListener = listener;
    }

    public interface OpenInvoiceListener{
        void onOpenClick(OrderEntity orderEntity,float money);
        void onCancleClick();
    }

    //关闭对话框
    public void dismiss() {
        mAlertDialog.dismiss();
        mAlertDialog = null;
    }
}
