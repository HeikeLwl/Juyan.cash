package jufeng.juyancash.ui.activity;

import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.DiffSelectedDishAdapter;
import jufeng.juyancash.dao.CashierDisplayEntity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.AmountUtils;


/**
 * Created by Administrator102 on 2016/9/23.
 */
public class DifferentDisplay extends Presentation {
    private int mType;
    private LinearLayout mLinearLayout;
    private TextView tvTitle;
    private RecyclerView mRecyclerView;
    private CashierDisplayEntity mCashierDisplayEntity;
    private String mOrderId;
    private DiffSelectedDishAdapter adapter;
    private Context mContext;
    private boolean mIsOpenJoinOrder;
    private TextView tvCount,tvMoney;
    public static final String ACTION_INTENT_DIFF = "jufeng.juyancash.intent.different";
    private DifferentBroadCast mDifferentBroadCast;
    private WebView mWebView;

    public DifferentDisplay(Context outerContext, Display display, int type, String orderId,boolean isOpenJoinOrder) {
        super(outerContext, display);
        this.mContext = outerContext;
        this.mType = type;
        this.mOrderId = orderId;
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        mCashierDisplayEntity = DBHelper.getInstance(mContext).getCashierDisplay();
        if(mCashierDisplayEntity == null){
            mCashierDisplayEntity = new CashierDisplayEntity();
            mCashierDisplayEntity.setIsCheckoutDisplay(1);
            mCashierDisplayEntity.setIsOrderDisplay(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diffrentdisplay_basket);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        setRightLayout(0);
        setAdapter();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        mContext.unregisterReceiver(mDifferentBroadCast);
        super.onStop();
    }

    private void registerBroadcast(){
        mDifferentBroadCast = new DifferentBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_DIFF);
        mContext.registerReceiver(mDifferentBroadCast,filter);
    }

    class DifferentBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_INTENT_DIFF)){
                mType = intent.getIntExtra("type",0);
                mOrderId = intent.getStringExtra("orderId");
                mIsOpenJoinOrder = intent.getBooleanExtra("isOpenJoinOrder",false);
                updateData(intent.getIntExtra("dy",0));
            }
        }
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        tvCount = (TextView) findViewById(R.id.tv_dish_count);
        tvMoney = (TextView) findViewById(R.id.tv_bill_money);
    }

    private void setAdapter(){
        adapter = new DiffSelectedDishAdapter(mContext.getApplicationContext(), null, 0,false);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setWebView(){
        try {
            mWebView.clearCache(true);
            String partner_code = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerCode",null);
            if(partner_code != null){
                mWebView.loadUrl(mContext.getResources().getString(R.string.AD_LOCATION)+partner_code);
            }else{
                mWebView.loadUrl(mContext.getResources().getString(R.string.AD_LOCATION));
            }
            WebSettings webSettings = mWebView.getSettings();
            mWebView.setSaveEnabled(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
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

                @Override
                public void onPageFinished(WebView webView, String s) {
                    Log.d("###", "onPageFinished: ");
                    mWebView.setVisibility(WebView.VISIBLE);
                    super.onPageFinished(webView, s);
                    mWebView.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                }

                @Override
                public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                    Log.d("###", "onReceivedHttpError: ");
                    mWebView.setVisibility(WebView.GONE);
                    super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                }

                @Override
                public void onReceivedError(WebView webView, int i, String s, String s1) {
                    Log.d("###", "onReceivedError: ");
                    mWebView.setVisibility(WebView.GONE);
                    super.onReceivedError(webView, i, s, s1);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setRightLayout(int dy) {
        if (mType == 1 && mCashierDisplayEntity.getIsOrderDisplay() == 1) {//显示点菜单
            mLinearLayout.setVisibility(LinearLayout.VISIBLE);
            tvTitle.setText("点菜单");
            //菜品数量
            double dishCount = DBHelper.getInstance(mContext).getDishCountByOrderId(mOrderId);
            //账单金额
            double billMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(mOrderId);
            tvCount.setText("共"+ AmountUtils.multiply(""+dishCount,"1")+"项");
            tvMoney.setText("合计：￥"+billMoney);
        } else if (mType == 2 && mCashierDisplayEntity.getIsOrderDisplay() == 1) {//显示结账单
            mLinearLayout.setVisibility(LinearLayout.VISIBLE);
            tvTitle.setText("结账单");
            //菜品数量
            double dishCount = 0;
            //账单金额
            double billMoney = 0;
            if(mIsOpenJoinOrder) {
                dishCount = DBHelper.getInstance(mContext).getJoinOrderDishCount(mOrderId);
                billMoney = DBHelper.getInstance(mContext).getJoinOrderBillMoney(mOrderId);
            }else{
                dishCount = DBHelper.getInstance(mContext).getDishCountByOrderId(mOrderId);
                billMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(mOrderId, 1);
            }
            tvCount.setText("共" + AmountUtils.multiply(""+dishCount,"1") + "项");
            tvMoney.setText("合计：￥" + billMoney);
        } else if(mType == 4){
            mRecyclerView.scrollBy(0,dy);
        }else{//显示空白
            mLinearLayout.setVisibility(LinearLayout.GONE);
        }
    }

    public void updateData(int dy){
        setRightLayout(dy);
        if (adapter != null && mCashierDisplayEntity != null && mCashierDisplayEntity.getIsOrderDisplay() == 1 && mType == 1) {//显示点菜单
            adapter.updateData(mOrderId,0,false);
        } else if (adapter != null && mCashierDisplayEntity != null && mCashierDisplayEntity.getIsOrderDisplay() == 1 && mType == 2) {//显示结账单
            adapter.updateData(mOrderId,1,mIsOpenJoinOrder);
        }else if(adapter != null && mType == 0){//
            adapter.updateData(null,0,false);
        }else if(mType == 3){
            mCashierDisplayEntity = DBHelper.getInstance(mContext).getCashierDisplay();
            if(mCashierDisplayEntity == null){
                mCashierDisplayEntity = new CashierDisplayEntity();
                mCashierDisplayEntity.setIsCheckoutDisplay(1);
                mCashierDisplayEntity.setIsOrderDisplay(1);
            }
            setWebView();
        }
    }
}