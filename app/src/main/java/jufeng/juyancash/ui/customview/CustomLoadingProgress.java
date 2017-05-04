package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/7/11.
 */
public class CustomLoadingProgress {
    AlertDialog ad;
    ContentLoadingProgressBar mProgressBar;
    TextView tvMessage;

    public CustomLoadingProgress(Context context) {
        // TODO Auto-generated constructor stub
        ad = new AlertDialog.Builder(context).create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.loading_progress_layout);
        mProgressBar = (ContentLoadingProgressBar) window.findViewById(R.id.progress);
        mProgressBar.show();
        tvMessage = (TextView) window.findViewById(R.id.tv_message);
    }

    public void setMessage(String message) {
        tvMessage.setVisibility(TextView.VISIBLE);
        tvMessage.setText(message);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if (ad.isShowing())
            ad.dismiss();
    }

    public void toggle() {
        if (ad.isShowing()) {
            ad.cancel();
        } else {
            ad.show();
        }
    }

    public void show() {
        if (ad != null && !ad.isShowing())
            ad.show();
    }

    public void cancel(){
        if(ad != null && ad.isShowing()){
            ad.cancel();
        }
    }
}
