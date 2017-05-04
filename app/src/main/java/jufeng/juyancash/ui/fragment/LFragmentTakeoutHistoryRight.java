package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.TakeoutAdapter;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;

import static jufeng.juyancash.R.id.radiobutton_1;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentTakeoutHistoryRight extends BaseFragment {
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private RecyclerView mRecyclerView;
    private TakeoutAdapter adapter;
    private TextView[] mTextViews;
    private int[] textviewIds = {R.id.tv_1, R.id.tv_5, R.id.tv_2, R.id.tv_3};
    private MainFragmentListener mOnTakeOutHistoryRightClickListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int status = 0;
            switch (msg.what) {
                case 0:
                    switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                        case radiobutton_1:
                            status = 0;
                            break;
                        case R.id.radiobutton_2:
                            status = 1;
                            break;
                        case R.id.radiobutton_3:
                            status = 2;
                            break;
                        case R.id.radiobutton_4:
                            status = 3;
                            break;
                        case R.id.radiobutton_5:
                            status = 4;
                            break;
                        case R.id.radiobutton_6:
                            status = 5;
                            break;
                    }
                    TakeOutOrderEntity takeOutOrderEntity = msg.getData().getParcelable("takeOutOrderEntity");
                    if (takeOutOrderEntity != null && adapter.getSelectedTakeOutOrder() != null && takeOutOrderEntity.getOrderId().equals(adapter.getSelectedTakeOutOrder().getOrderId())) {
                        //当前订单被选中
                        adapter.updateData(status);
                    } else {
                        adapter.updateData1(status);
                    }
                    mOnTakeOutHistoryRightClickListener.onTakeOutOrderClick(adapter.getSelectedTakeOutOrder(), status);
                    break;
                case 1://修改外卖状态数
                    setRadioButtonText();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setTakeOutHistoryHandler(mHandler);
        try {
            mOnTakeOutHistoryRightClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_takeout_history_right, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mTextViews = new TextView[textviewIds.length];
        for (int i = 0; i < textviewIds.length; i++) {
            TextView textView = (TextView) mLCustomeRadioGroup.findViewById(textviewIds[i]);
            mTextViews[i] = textView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
            case radiobutton_1:
                adapter.updateData(0);
                break;
            case R.id.radiobutton_2:
                adapter.updateData(1);
                break;
            case R.id.radiobutton_3:
                adapter.updateData(2);
                break;
            case R.id.radiobutton_4:
                adapter.updateData(3);
                break;
            case R.id.radiobutton_5:
                adapter.updateData(4);
                break;
            case R.id.radiobutton_6:
                adapter.updateData(5);
                break;
        }
        mOnTakeOutHistoryRightClickListener.onTakeOutOrderClick(null, 0);
        setRadioButtonText();
    }

    private void setRadioButtonText() {
        int[] counts = DBHelper.getInstance(getContext().getApplicationContext()).getTakeoutCounts();
        for (int i = 0; i < textviewIds.length; i++) {
            if (counts[i] > 0) {
                mTextViews[i].setVisibility(TextView.VISIBLE);
                mTextViews[i].setText(counts[i] + "");
            } else {
                mTextViews[i].setVisibility(TextView.GONE);
                mTextViews[i].setText("");
            }
        }
    }

    private void setAdapter() {
        adapter = new TakeoutAdapter(getActivity().getApplicationContext(), 0);
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
        adapter.setOnTakeOutItemClickListener(new TakeoutAdapter.OnTakeOutItemClickListener() {
            @Override
            public void onTakeOutItemClick(TakeOutOrderEntity takeOutOrderEntity, int type) {
                mOnTakeOutHistoryRightClickListener.onTakeOutOrderClick(takeOutOrderEntity, type);
            }
        });
    }

    private void setListener() {
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (group.getCheckedRadioButtonId()) {
                    case radiobutton_1:
                        //控制按钮显示
                        adapter.updateData(0);
                        break;
                    case R.id.radiobutton_2:
                        //控制按钮显示
                        adapter.updateData(1);
                        break;
                    case R.id.radiobutton_3:
                        //控制按钮显示
                        adapter.updateData(2);
                        break;
                    case R.id.radiobutton_4:
                        //控制按钮显示
                        adapter.updateData(3);
                        break;
                    case R.id.radiobutton_5:
                        adapter.updateData(4);
                        break;
                    case R.id.radiobutton_6:
                        adapter.updateData(5);
                        break;
                }
                mOnTakeOutHistoryRightClickListener.onTakeOutOrderClick(null, 0);
            }
        });
    }
}
