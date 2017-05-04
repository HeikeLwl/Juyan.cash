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
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentArrange extends BaseFragment {
    private LFragmentArrangeSort mLFragmentArrangeSort;
    private LFragmentArrangeHistory mLFragmentArrangeHistory;
    private static final int ARRANGESORT = 0;
    private static final int ARRANGEHISTORY = 1;
    private LCustomeRadioGroup mLCustomeRadioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_arrange,container,false);
        initView(mView);
        setListener();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View view){
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        setFragment(0);
    }

    private void setListener(){
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId,Bundle bundle) {
                if (group.getCheckedRadioButtonId() == R.id.radiobutton_2){
                    setFragment(ARRANGESORT);
                }else if(group.getCheckedRadioButtonId() == R.id.radiobutton_1){
                    setFragment(ARRANGEHISTORY);
                }
            }
        });
    }

    private void setFragment(int tag){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (tag) {
            case 0:
                if (mLFragmentArrangeSort == null) {
                    mLFragmentArrangeSort = new LFragmentArrangeSort();
                    fragmentTransaction.add(R.id.container, mLFragmentArrangeSort);
                } else {
                    fragmentTransaction.show(mLFragmentArrangeSort);
                }
                break;
            case 1:
                if (mLFragmentArrangeHistory == null) {
                    mLFragmentArrangeHistory = new LFragmentArrangeHistory();
                    fragmentTransaction.add(R.id.container, mLFragmentArrangeHistory);
                } else {
                    fragmentTransaction.show(mLFragmentArrangeHistory);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if(mLFragmentArrangeSort != null){
            transaction.hide(mLFragmentArrangeSort);
//            mLFragmentArrangeSort = null;
        }

        if(mLFragmentArrangeHistory != null){
            transaction.hide(mLFragmentArrangeHistory);
//            mLFragmentArrangeHistory = null;
        }
    }
}
