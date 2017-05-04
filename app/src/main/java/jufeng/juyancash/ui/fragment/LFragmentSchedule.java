package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jufeng.juyancash.R;
import jufeng.juyancash.ui.activity.MainActivity;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentSchedule extends BaseFragment {
    private LFragmentSchedulePhone mLFragmentSchedulePhone;
    private LFragmentScheduleHistory mLFragmentScheduleHistory;
    private static final int PHONESCHEDULE = 0;
    private static final int SCHEDULEHISTORY = 1;
    private TextView tvScheduleHistory,tvSchedulePhone;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://预定成功
                    changeTextView(0,null);
                    break;
                case 1:

                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity activity1 = (MainActivity) context;
        activity1.setScheduleHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule, container, false);
        initView(mView);
        setListener();
        initData();
        return mView;
    }

    public void setNewParam(Bundle bundle){
        if (bundle != null && bundle.containsKey("type") && bundle.getInt("type") == PHONESCHEDULE) {
            changeTextView(1,bundle);
        } else{
            changeTextView(0,bundle);
        }
    }

    private void initView(View view) {
        tvScheduleHistory = (TextView) view.findViewById(R.id.tv_schedule_history);
        tvSchedulePhone = (TextView) view.findViewById(R.id.tv_schedule_phone);
        changeTextView(0,null);
    }

    private void changeTextView(int type,Bundle bundle){
        if(type == 0){
            tvScheduleHistory.setBackgroundResource(R.color.activityBackground);
            tvSchedulePhone.setBackgroundResource(android.R.color.transparent);
            tvScheduleHistory.setTextColor(getContext().getResources().getColor(R.color.red));
            tvSchedulePhone.setTextColor(getContext().getResources().getColor(R.color.white));
            setFragment(SCHEDULEHISTORY, bundle);
        }else{
            tvScheduleHistory.setBackgroundResource(android.R.color.transparent);
            tvSchedulePhone.setBackgroundResource(R.color.activityBackground);
            tvScheduleHistory.setTextColor(getContext().getResources().getColor(R.color.white));
            tvSchedulePhone.setTextColor(getContext().getResources().getColor(R.color.red));
            setFragment(PHONESCHEDULE, bundle);
        }
    }

    private void setListener() {
        tvScheduleHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextView(0,null);
            }
        });
        tvSchedulePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextView(1,null);
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("type") && bundle.getInt("type") == PHONESCHEDULE) {
            changeTextView(1,bundle);
        } else {
            changeTextView(0,null);
        }
    }

    private void setFragment(int tag, Bundle bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (tag) {
            case 0:
                if (mLFragmentSchedulePhone == null) {
                    mLFragmentSchedulePhone = new LFragmentSchedulePhone();
                    mLFragmentSchedulePhone.setArguments(bundle);
                    fragmentTransaction.add(R.id.container, mLFragmentSchedulePhone);
                } else {
                    mLFragmentSchedulePhone.setNewParam(bundle);
                    fragmentTransaction.show(mLFragmentSchedulePhone);
                }
                break;
            case 1:
                if (mLFragmentScheduleHistory == null) {
                    mLFragmentScheduleHistory = new LFragmentScheduleHistory();
                    mLFragmentScheduleHistory.setArguments(bundle);
                    fragmentTransaction.add(R.id.container, mLFragmentScheduleHistory);
                } else {
                    mLFragmentScheduleHistory.setNewParam();
                    fragmentTransaction.show(mLFragmentScheduleHistory);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mLFragmentSchedulePhone != null) {
            transaction.hide(mLFragmentSchedulePhone);
//            mLFragmentSchedulePhone = null;
        }

        if (mLFragmentScheduleHistory != null) {
            transaction.hide(mLFragmentScheduleHistory);
//            mLFragmentScheduleHistory = null;
        }
    }
}
