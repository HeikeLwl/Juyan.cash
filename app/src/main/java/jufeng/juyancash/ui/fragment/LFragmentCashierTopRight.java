package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.eventbus.CashierTopRightRefreshEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class LFragmentCashierTopRight extends BaseFragment {
    private Button btnCashier;
    private TextView tvReceivableMoney;
    private String orderId;
    private Button btnML;
    private boolean isOpenJoinOrder;
    private MainFragmentListener mOnTopRightClickListner;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    tvReceivableMoney.setText("￥" + AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(CashierTopRightRefreshEvent event) {
        if (event != null) {
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mActivity = (MainActivity) activity;
        mActivity.setCashierTopRightHandler(mHandler);
        try {
            mOnTopRightClickListner = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
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
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String orderId, boolean isOpenJoinOrder) {
        this.orderId = orderId;
        this.isOpenJoinOrder = isOpenJoinOrder;

        initData();
    }

    private void initData() {
        tvReceivableMoney.setText("￥" + AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
    }

    private void setListener() {
        btnCashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTopRightClickListner.onContinuCashier(orderId, isOpenJoinOrder);
            }
        });
        btnML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double money = 0;
                //子单
                try {
                    String money0 = AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId));
                    money = AmountUtils.multiply1(money0, "1.0");
                } catch (Exception e) {
                    e.printStackTrace();
                    money = 0;
                }
                mOnTopRightClickListner.onMLClick(orderId, money);
            }
        });
    }
}
