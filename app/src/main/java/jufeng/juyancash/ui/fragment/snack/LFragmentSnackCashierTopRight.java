package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.eventbus.SnackCashierTopRightRefreshEvent;
import jufeng.juyancash.eventbus.SnackContinuCashierEvent;
import jufeng.juyancash.eventbus.SnackMlClickEvent;
import jufeng.juyancash.eventbus.SnackOrderMoneyChangedEvent;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class LFragmentSnackCashierTopRight extends BaseFragment {
    private Button btnCashier;
    private TextView tvReceivableMoney;
    private String orderId;
    private Button btnML;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    tvReceivableMoney.setText("￥" + AmountUtils.changeF2Y(DBHelper.getInstance(getContext()).getReceivableMoneyByOrderId(orderId)));
                    break;
            }
        }
    };

    public static LFragmentSnackCashierTopRight newInstance(String orderId){
        LFragmentSnackCashierTopRight fragment = new LFragmentSnackCashierTopRight();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_cashier_topright, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        btnCashier = (Button) view.findViewById(R.id.btn_cashier);
        btnML = (Button) view.findViewById(R.id.btn_moling);
        tvReceivableMoney = (TextView) view.findViewById(R.id.tv_receivable_money);
        orderId = getArguments().getString("orderId");
    }

    private void initData(){
            tvReceivableMoney.setText("￥" + AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
    }

    private void setListener() {
        btnCashier.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackContinuCashierEvent());
            }
        });
        btnML.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                double money = 0;
                try {
                    String money0 = AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId));
                    money = AmountUtils.multiply1(money0,"1.0");
                } catch (Exception e) {
                    e.printStackTrace();
                    money = 0;
                }
                EventBus.getDefault().post(new SnackMlClickEvent(money));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(SnackCashierTopRightRefreshEvent event){
        if(event != null){
            mHandler.sendEmptyMessage(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderMoneyChange(SnackOrderMoneyChangedEvent event){
        if(event != null){
            mHandler.sendEmptyMessage(0);
        }
    }
}
