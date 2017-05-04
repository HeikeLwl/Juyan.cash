package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import jufeng.juyancash.R;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeExitDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private OnExitDialogClickListener mOnExitDialogClickListener;
    private TextView tvTitle,tvContent;
    private Button btnCancle, btnConfirm;

    public CustomeExitDialog(Context context,String title,String content){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_exit);
        initView(window);
        initData(title,content);
    }

    public CustomeExitDialog(Context context,String title,String content,String cancleStr,String confirmStr,int type){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_exit);
        initView(window);
        initData(title,content,cancleStr,confirmStr,type);
    }

    private void initView(Window window){
        tvTitle = (TextView) window.findViewById(R.id.tv_title);
        tvContent = (TextView) window.findViewById(R.id.tv_content);
        btnCancle = (Button) window.findViewById(R.id.btn_back_home);
        btnConfirm = (Button) window.findViewById(R.id.btn_goto_cashier);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnExitDialogClickListener.onCancle();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnExitDialogClickListener.onConfirm();
            }
        });
    }

    private void initData(String title,String content){
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    private void initData(String title,String content,String cancleStr,String confirmStr,int type){
        tvTitle.setText(title);
        tvContent.setText(content);
        btnCancle.setText(cancleStr);
        btnConfirm.setText(confirmStr);
        switch (type){
            case 0:
                btnCancle.setVisibility(Button.GONE);
                btnConfirm.setBackgroundResource(R.drawable.exit_btn_selector);
                break;
        }
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setExitDialogClickListener(OnExitDialogClickListener listener){
        mOnExitDialogClickListener = listener;
    }

    public interface OnExitDialogClickListener{
        void onCancle();
        void onConfirm();
    }
}
