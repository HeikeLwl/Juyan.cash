package jufeng.juyancash.ui.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import jufeng.juyancash.R;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeConfirmDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private OnConfirmDialogClickListener mOnConfirmDialogClickListener;
    private ImageButton ibCancle;
    private Button btnBackHome,btnGotoCashier;

    public CustomeConfirmDialog(Context context){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_confirm);
        initView(window);
    }

    private void initView(Window window){
        ibCancle = (ImageButton) window.findViewById(R.id.ib_cancle);
        btnBackHome = (Button) window.findViewById(R.id.btn_back_home);
        btnGotoCashier = (Button) window.findViewById(R.id.btn_goto_cashier);

        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfirmDialogClickListener.cancle();
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfirmDialogClickListener.backHome();
            }
        });

        btnGotoCashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConfirmDialogClickListener.goToCashier();
            }
        });
    }

    public void dismiss(){
        mAlertDialog.dismiss();
    }

    public void setConfirmDialogClickListener(OnConfirmDialogClickListener listener){
        mOnConfirmDialogClickListener = listener;
    }

    public interface OnConfirmDialogClickListener{
        void backHome();
        void goToCashier();
        void cancle();
    }
}
