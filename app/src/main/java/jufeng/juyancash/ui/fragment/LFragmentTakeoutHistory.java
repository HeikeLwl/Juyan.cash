package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.myinterface.MainFragmentListener;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentTakeoutHistory extends BaseFragment {
    private LFragmentTakeoutHistoryLeft mLFragmentTakeoutHistoryLeft;
    private LFragmentTakeoutHistoryRight mLFragmentTakeoutHistoryRight;
    private MainFragmentListener mMainFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(mMainFragmentListener != null && !hidden){
            mMainFragmentListener.onChangeTakeoutStatusCount();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_takeout_history,container,false);
        addFragment();
        return mView;
    }

    private void addFragment(){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(mLFragmentTakeoutHistoryLeft == null){
            mLFragmentTakeoutHistoryLeft = new LFragmentTakeoutHistoryLeft();
            ft.add(R.id.containerleft,mLFragmentTakeoutHistoryLeft);
        }

        if(mLFragmentTakeoutHistoryRight == null){
            mLFragmentTakeoutHistoryRight = new LFragmentTakeoutHistoryRight();
            ft.add(R.id.containerright,mLFragmentTakeoutHistoryRight);
        }
        ft.commit();
    }
}
