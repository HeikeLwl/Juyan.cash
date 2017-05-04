package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomeOrderMessageDialog;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.util.FilterArrayList;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentForHere extends BaseFragment {
    private LCustomeRadioGroup mLCustomeRadioGroup0;
    private ArrayList<AreaEntity> areas;
    private RadioButton[] mRadioButtons;
    private TextView tvUnreadMessage;
    private CardView mCardView;
    private Map<String, FilterArrayList<TableEntity>> mAllTables;
    private Map<String, LFragmentTables> mLFragmentTablesMap;
    private Animation anim;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String areaId = (String) mRadioButtons[mLCustomeRadioGroup0.getCheckedRadioButtonId()].getTag();
                    setFragment(areaId);
                    setOrderMessage();
                    break;
                case 1:
                    setOrderMessage();
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            setOrderMessage();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setForHereHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_forhere, container, false);
        initView(mView);
        initDataes();
        addAreaMenus();
        setListener();
        if (mRadioButtons.length > 0) {
            setFragment((String) mRadioButtons[0].getTag());
        }
        return mView;
    }

    private void setFragment(String areaId) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        if (mLFragmentTablesMap.containsKey(areaId) && mLFragmentTablesMap.get(areaId) == null) {
            LFragmentTables lFragmentTables = new LFragmentTables();
            Bundle bundle = new Bundle();
            bundle.putString("areaId", areaId);
            lFragmentTables.setArguments(bundle);
            ft.add(R.id.container, lFragmentTables);
            mLFragmentTablesMap.put(areaId, lFragmentTables);
        } else if (mLFragmentTablesMap.containsKey(areaId)) {
            ft.show(mLFragmentTablesMap.get(areaId));
        }
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft){
        for (Map.Entry<String, LFragmentTables> entity :
                mLFragmentTablesMap.entrySet()) {
            if (entity.getValue() != null) {
                ft.hide(entity.getValue());
            }
        }
    }

    //获取区域和桌位数据
    private void initDataes() {
        anim = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.toast_anim);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        mAllTables = new HashMap<>();
        areas = new ArrayList<>();
        mLFragmentTablesMap = new HashMap<>();
        areas.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryAreaData());
        for (AreaEntity area :
                areas) {
            FilterArrayList<TableEntity> tableEntities = new FilterArrayList<>();
            tableEntities.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryTableData(area.getAreaId()));
            mAllTables.put(area.getAreaId(), tableEntities);
            if (!mLFragmentTablesMap.containsKey(area.getAreaId())) {
                mLFragmentTablesMap.put(area.getAreaId(), null);
            }
        }
        setOrderMessage();
    }

    private void setOrderMessage() {
        int unreadMessageCount = DBHelper.getInstance(getContext().getApplicationContext()).getAllWxOrderMessageCount();
        if (unreadMessageCount > 0) {
            mCardView.setVisibility(CardView.VISIBLE);
            tvUnreadMessage.setText(unreadMessageCount + "条订单消息");
            mCardView.setAnimation(anim);
            anim.start();
        } else {
            tvUnreadMessage.setText(unreadMessageCount + "条订单消息");
            anim.cancel();
            mCardView.clearAnimation();
            mCardView.setVisibility(CardView.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setOrderMessage();
    }

    private void initView(View view) {
        mLCustomeRadioGroup0 = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup0);
        tvUnreadMessage = (TextView) view.findViewById(R.id.tv_unread_message);
        mCardView = (CardView) view.findViewById(R.id.cardview);
    }

    private void addAreaMenus() {
        mRadioButtons = new RadioButton[areas.size()];
        LCustomeRadioGroup.LayoutParams params = new LCustomeRadioGroup.LayoutParams(LCustomeRadioGroup.LayoutParams.MATCH_PARENT, LCustomeRadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < areas.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity().getApplicationContext());
            radioButton.setId(i);
            radioButton.setTag(areas.get(areas.size() - 1 - i).getAreaId());
            radioButton.setBackgroundResource(R.drawable.area_menu_selector_gray);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setText(areas.get(areas.size() - 1 - i).getAreaName());
            radioButton.setTextSize(18);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setPadding(4, 12, 4, 12);
            mLCustomeRadioGroup0.addView(radioButton, params);
            mRadioButtons[i] = radioButton;
        }
        if (mRadioButtons.length > 0)
            mRadioButtons[0].setChecked(true);
        setRadioButtonTextColor(0);
    }

    private void setListener() {
        tvUnreadMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomeOrderMessageDialog dialog = new CustomeOrderMessageDialog(getContext());
                dialog.setOnOrderMessageClearListener(new CustomeOrderMessageDialog.OnOrderMessageClearListener() {
                    @Override
                    public void onClearAllOrderMessage() {
                        setOrderMessage();
                    }
                });
            }
        });

        //不同的区域切换
        mLCustomeRadioGroup0.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                setRadioButtonTextColor(checkedId);
                String areaId = (String) mRadioButtons[mLCustomeRadioGroup0.getCheckedRadioButtonId()].getTag();
                setFragment(areaId);
            }
        });
    }

    private void setRadioButtonTextColor(int index) {
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i == index) {
                mRadioButtons[i].setTextColor(getResources().getColor(R.color.theme_0_red_0));
            } else {
                mRadioButtons[i].setTextColor(getResources().getColor(R.color.white));
            }
        }
    }
}
