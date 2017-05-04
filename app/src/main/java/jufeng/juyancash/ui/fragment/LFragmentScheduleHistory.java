package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentScheduleHistory extends BaseFragment {
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private LFragmentScheduleCheck mLFragmentScheduleCheck;
    private LFragmentScheduleEffect mLFragmentScheduleEffect;
    private LFragmentScheduleCancle mLFragmentScheduleCancle;
    private LFragmentSchedulePerfect mLFragmentSchedulePerfect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule_history,container,false);
        initView(mView);
        setListener();
        return mView;
    }

    public void setNewParam(){
        if(mLCustomeRadioGroup != null) {
            switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radiobutton_1:
                    setFragment(0);
                    break;
                case R.id.radiobutton_2:
                    setFragment(1);
                    break;
                case R.id.radiobutton_3:
                    setFragment(2);
                    break;
                case R.id.radiobutton_4:
                    setFragment(3);
                    break;
            }
        }
    }

    private void initView(View mView){
        mLCustomeRadioGroup = (LCustomeRadioGroup) mView.findViewById(R.id.lcustomeradiogroup);
        setFragment(0);
    }

    private void setListener(){
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.radiobutton_1:
                        setFragment(0);
                        break;
                    case R.id.radiobutton_2:
                        setFragment(1);
                        break;
                    case R.id.radiobutton_3:
                        setFragment(2);
                        break;
                    case R.id.radiobutton_4:
                        setFragment(3);
                        break;
                }
            }
        });
    }

    private void setFragment(int tag){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch(tag){
            case 0:
                if(mLFragmentScheduleCheck == null){
                    mLFragmentScheduleCheck = new LFragmentScheduleCheck();
                    ft.add(R.id.container,mLFragmentScheduleCheck);
                }else{
                    mLFragmentScheduleCheck.setNewParam();
                    ft.show(mLFragmentScheduleCheck);
                }
                break;
            case 1:
                if(mLFragmentScheduleEffect == null){
                    mLFragmentScheduleEffect = new LFragmentScheduleEffect();
                    ft.add(R.id.container,mLFragmentScheduleEffect);
                }else{
                    mLFragmentScheduleEffect.setNewParam();
                    ft.show(mLFragmentScheduleEffect);
                }
                break;
            case 2:
                if(mLFragmentScheduleCancle == null){
                    mLFragmentScheduleCancle = new LFragmentScheduleCancle();
                    ft.add(R.id.container,mLFragmentScheduleCancle);
                }else{
                    mLFragmentScheduleCancle.setNewParam();
                    ft.show(mLFragmentScheduleCancle);
                }
                break;
            case 3:
                if(mLFragmentSchedulePerfect == null){
                    mLFragmentSchedulePerfect = new LFragmentSchedulePerfect();
                    ft.add(R.id.container,mLFragmentSchedulePerfect);
                }else{
                    mLFragmentSchedulePerfect.setNewParam();
                    ft.show(mLFragmentSchedulePerfect);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction trans){
        if(mLFragmentScheduleCheck != null){
            trans.hide(mLFragmentScheduleCheck);
        }
        if(mLFragmentScheduleEffect != null){
            trans.hide(mLFragmentScheduleEffect);
        }
        if(mLFragmentScheduleCancle != null){
            trans.hide(mLFragmentScheduleCancle);
        }
        if(mLFragmentSchedulePerfect != null){
            trans.hide(mLFragmentSchedulePerfect);
        }
    }
}
