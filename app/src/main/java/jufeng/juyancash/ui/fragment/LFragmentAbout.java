package jufeng.juyancash.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/10/5.
 */

public class LFragmentAbout extends BaseFragment {
    @BindView(R.id.webview)
    WebView mWebview;
    private String url = "http://www.jfeleven.com/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, mView);
        setWebView();
        return mView;
    }

    private void setWebView() {
        try {
            mWebview.clearCache(true);
            mWebview.loadUrl(url);
            WebSettings webSettings = mWebview.getSettings();
            webSettings.setNeedInitialFocus(false);
            webSettings.setSupportZoom(true);
            webSettings.setLoadWithOverviewMode(true);//适应屏幕
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
            webSettings.setLoadsImagesAutomatically(true);//自动加载图片
            mWebview.setSaveEnabled(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                    Log.d("###", "onPageStarted: ");
                    super.onPageStarted(webView, s, bitmap);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
