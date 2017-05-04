package jufeng.juyancash.ui.customview;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderTurnOverAdapter;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.ui.fragment.LFragmentTurnOverAccount;
import jufeng.juyancash.ui.fragment.LFragmentTurnOverQR;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeTurnoverDialog extends DialogFragment{
    private TextView tvTime, tvCashier, tvArea;
    private TextView tvChange;
    private ListView mListView;
    private OrderTurnOverAdapter mAdapter1;
    private ImageButton ibCancle;
    private String mTime;
    private int mCashier;
    private String mArea;
    private String mAreaId;
    private OnTurnOverListener mOnTurnOverListener;
    private LFragmentTurnOverQR mLFragmentTurnOverQR;
    private LFragmentTurnOverAccount mLFragmentTurnOverAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_turnover_layout, container);
        mTime = getArguments().getString("time");
        mCashier = getArguments().getInt("cashier");
        mArea = getArguments().getString("area");
        mAreaId = getArguments().getString("areaId");
        initView(view);
        setAdapter(mTime, mCashier, mArea, mAreaId);
        setFragment();
        setListener();
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvCashier = (TextView) view.findViewById(R.id.tv_cashier);
        tvArea = (TextView) view.findViewById(R.id.tv_area);
        ibCancle = (ImageButton) view.findViewById(R.id.ib_cancle);
        tvChange = (TextView) view.findViewById(R.id.tv_change);
        tvChange.setTag(true);
        tvChange.setText("使用员工账号授权>>>");
    }

    private void setFragment(){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        if((boolean)tvChange.getTag()){
            //显示二维码授权界面
            if(mLFragmentTurnOverQR == null){
                mLFragmentTurnOverQR = new LFragmentTurnOverQR();
                Bundle bundle = new Bundle();
                bundle.putLong("ts", CustomMethod.parseTime(mTime,"yyyy-MM-dd HH:mm"));
                mLFragmentTurnOverQR.setArguments(bundle);
                fragmentTransaction.add(R.id.turnover_right,mLFragmentTurnOverQR);
            }else{
                fragmentTransaction.show(mLFragmentTurnOverQR);
            }
            mLFragmentTurnOverQR.setOnQRTurnOverListener(new LFragmentTurnOverQR.OnQRTurnOverListener() {
                @Override
                public void onQRTurnOverSuccess(View view, EmployeeEntity employeeEntity) {
                    if(mOnTurnOverListener != null) {
                        mOnTurnOverListener.onTurnOverSuccess(tvChange,employeeEntity,mCashier,mAreaId);
                    }
                }
            });
        }else{
            //显示账号授权界面
            if(mLFragmentTurnOverAccount == null){
                mLFragmentTurnOverAccount = new LFragmentTurnOverAccount();
                fragmentTransaction.add(R.id.turnover_right,mLFragmentTurnOverAccount);
            }else{
                fragmentTransaction.show(mLFragmentTurnOverAccount);
            }
            mLFragmentTurnOverAccount.setOnTurnOverAccountListener(new LFragmentTurnOverAccount.OnTurnOverAccountListener() {
                @Override
                public void onAccountTurnOverClick(EmployeeEntity employeeEntity) {
                    if(mOnTurnOverListener != null) {
                        mOnTurnOverListener.onTurnOverSuccess(tvChange,employeeEntity,mCashier,mAreaId);
                    }
                }
            });
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction ft){
        if(mLFragmentTurnOverAccount != null){
            ft.hide(mLFragmentTurnOverAccount);
            mLFragmentTurnOverAccount = null;
        }
        if(mLFragmentTurnOverQR != null){
            ft.hide(mLFragmentTurnOverQR);
            mLFragmentTurnOverQR = null;
        }
    }

    private void setListener(){
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvChange.setTag(!((Boolean)v.getTag()));
                tvChange.setText((boolean)tvChange.getTag()?"使用员工账号授权方式>>>":"使用扫码授权方式>>>");
                setFragment();
            }
        });

        ibCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnTurnOverListener(OnTurnOverListener listener){
        this.mOnTurnOverListener = listener;
    }

    public interface OnTurnOverListener{
        void onTurnOverSuccess(View view, EmployeeEntity employeeEntity,int cashier,String areaId);
    }

    private void setAdapter(String time, int cashier, String area, String areaId) {
        tvTime.setText(time);
        tvArea.setText(area);
        String cashierId = null;
        if (cashier == 0) {
            //全部收银员
            tvCashier.setText("全部收银员");
        } else {
            //当前收银员
            tvCashier.setText("当前收银员");
            SharedPreferences spf = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
            cashierId = spf.getString("employeeId", null);
        }
        mAdapter1 = new OrderTurnOverAdapter(getContext().getApplicationContext(), cashierId, areaId);
        mListView.setAdapter(mAdapter1);
    }
}
