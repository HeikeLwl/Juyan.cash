package jufeng.juyancash.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.eventbus.LoadingDialogEvent;
import jufeng.juyancash.eventbus.ShowResultDialogEvent;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.util.CustomMethod;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by 15157_000 on 2016/6/14 0014.
 */
public class BaseActivity extends SupportActivity {
    public CustomLoadingProgress mLoadingProgress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoadingDialog(LoadingDialogEvent event){
        if(event != null){
            if(event.isShow()){
                showLoadingAnim(event.getMessage());
            }else{
                hideLoadingAnim();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShowResultDialog(ShowResultDialogEvent event){
        if(event != null){
            CustomMethod.showMessage(this,event.getTitle(),event.getMessage());
        }
    }

    public void showLoadingAnim(String message) {
        if(mLoadingProgress == null) {
            mLoadingProgress = new CustomLoadingProgress(this);
        }
        mLoadingProgress.setMessage(message);
        mLoadingProgress.show();
    }

    public void hideLoadingAnim() {
        if (mLoadingProgress != null) {
            mLoadingProgress.cancel();
        }
    }
}
