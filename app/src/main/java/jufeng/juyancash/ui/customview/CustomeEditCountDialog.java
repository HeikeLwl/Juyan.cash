package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jufeng.juyancash.R;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeEditCountDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private OnEditCountDialogClickListener mOnExitDialogClickListener;
    private TextView tvTitle;
    private EditText etCount;
    private Button btnCancle, btnConfirm;

    public CustomeEditCountDialog(Context context, String title, String content) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_edit_count);
        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        initView(window);
        initData(title, content);
    }

    private void initView(Window window) {
        tvTitle = (TextView) window.findViewById(R.id.tv_title);
        etCount = (EditText) window.findViewById(R.id.et_count);
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
                if (etCount.getText() != null && !etCount.getText().toString().isEmpty()) {
                    mOnExitDialogClickListener.onConfirm(etCount.getText().toString());
                } else {
                    Snackbar.make(etCount, "请输入数量", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData(String title, String content) {
        tvTitle.setText(title);
        etCount.setText(content);
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }

    public void setExitDialogClickListener(OnEditCountDialogClickListener listener) {
        mOnExitDialogClickListener = listener;
    }

    public interface OnEditCountDialogClickListener {
        void onCancle();

        void onConfirm(String count);
    }
}
