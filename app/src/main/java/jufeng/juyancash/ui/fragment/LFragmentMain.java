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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.eventbus.OnEventSelectMenuEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.fragment.snack.LFragmentSnack;

/**
 * Created by Administrator102 on 2016/11/16.
 */

public class LFragmentMain extends BaseFragment {
    private LFragmentMainMenu mLFragmentMainMenu;
    private LFragmentSnack mLFragmentSnack1;
    private LFragmentForHere mLFragmentForHere;
    private LFragmentArrange mLFragmentArrange;
    private LFragmentSchedule mLFragmentSchedule;
    private LFragmentTakeoutHistory mLFragmentTakeoutHistory;
    private LFragmentChing mLFragmentChing;
    private LFragmentOrder mLFragmentOrder;
    private LFragmentMessage mLFragmentMessage;
    private LFragmentTurnover mLFragmentTurnover;
    private LFragmentKichen mLFragmentKichen;
    private LFragmentMore mLFragmentMore;
    private MainFragmentListener mMainFragmentListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Bundle bundle = msg.getData();
                    int checked = (int) msg.obj;
                    setFragment(checked,bundle);
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            if(mMainFragmentListener != null){
                mMainFragmentListener.refreshTable();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setMainHandler(mHandler);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSelectMenu(OnEventSelectMenuEvent event){
        if(event != null){
            setFragment(event.getIndex(),event.getBundle());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        setMainMenuFragment();
        setFragment(0, null);
        return view;
    }

    private void setMainMenuFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        if (mLFragmentMainMenu == null) {
            mLFragmentMainMenu = new LFragmentMainMenu();
            trans.add(R.id.containerleft, mLFragmentMainMenu);
        }
        trans.commit();
    }

    private void setFragment(int tag, Bundle bundle) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans);
        switch (tag) {
            case 0:
                int storeversion = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE).getInt("storeversion",0);
                if(storeversion == 0){
                    if (mLFragmentForHere == null) {
                        mLFragmentForHere = new LFragmentForHere();
                        mLFragmentForHere.setArguments(bundle);
                        trans.add(R.id.containerright, mLFragmentForHere);
                    } else {
                        trans.show(mLFragmentForHere);
                    }
                }else if(storeversion == 1){
                    if (mLFragmentSnack1 == null) {
                        mLFragmentSnack1 = new LFragmentSnack();
                        mLFragmentSnack1.setArguments(bundle);
                        trans.add(R.id.containerright, mLFragmentSnack1);
                    } else {
                        trans.show(mLFragmentSnack1);
                    }
                }else if(storeversion == 2){
                    if (mLFragmentSnack1 == null) {
                        mLFragmentSnack1 = new LFragmentSnack();
                        mLFragmentSnack1.setArguments(bundle);
                        trans.add(R.id.containerright, mLFragmentSnack1);
                    } else {
                        trans.show(mLFragmentSnack1);
                    }
                }

                break;
            case 1:
                if (mLFragmentSchedule == null) {
                    mLFragmentSchedule = new LFragmentSchedule();
                    mLFragmentSchedule.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentSchedule);
                } else {
                    mLFragmentSchedule.setNewParam(bundle);
                    trans.show(mLFragmentSchedule);
                }
                break;
            case 2:
                if (mLFragmentArrange == null) {
                    mLFragmentArrange = new LFragmentArrange();
                    mLFragmentArrange.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentArrange);
                } else {
                    trans.show(mLFragmentArrange);
                }
                break;
            case 3:
                if (mLFragmentTakeoutHistory == null) {
                    mLFragmentTakeoutHistory = new LFragmentTakeoutHistory();
                    mLFragmentTakeoutHistory.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentTakeoutHistory);
                } else {
                    trans.show(mLFragmentTakeoutHistory);
                }
                break;
            case 4:
                if (mLFragmentChing == null) {
                    mLFragmentChing = new LFragmentChing();
                    mLFragmentChing.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentChing);
                } else {
                    trans.show(mLFragmentChing);
                }
                break;
            case 5:
                if (mLFragmentOrder == null) {
                    mLFragmentOrder = new LFragmentOrder();
                    mLFragmentOrder.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentOrder);
                } else {
                    trans.show(mLFragmentOrder);
                }
                break;
            case 6:
                if (mLFragmentMessage == null) {
                    mLFragmentMessage = new LFragmentMessage();
                    mLFragmentMessage.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentMessage);
                } else {
                    trans.show(mLFragmentMessage);
                }
                break;
            case 7:
                if (mLFragmentTurnover == null) {
                    mLFragmentTurnover = new LFragmentTurnover();
                    mLFragmentTurnover.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentTurnover);
                } else {
                    trans.show(mLFragmentTurnover);
                }
                break;
            case 8:
                if (mLFragmentKichen == null) {
                    mLFragmentKichen = new LFragmentKichen();
                    mLFragmentKichen.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentKichen);
                } else {
                    trans.show(mLFragmentKichen);
                }
                break;
            case 9:
                if (mLFragmentMore == null) {
                    mLFragmentMore = new LFragmentMore();
                    mLFragmentMore.setArguments(bundle);
                    trans.add(R.id.containerright, mLFragmentMore);
                } else {
                    trans.show(mLFragmentMore);
                }
                break;

            default:
                break;
        }

        trans.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction trans) {
        // TODO Auto-generated method stub
        if (mLFragmentSnack1 != null) {
            trans.hide(mLFragmentSnack1);
        }
        if (mLFragmentForHere != null) {
            trans.hide(mLFragmentForHere);
        }
        if (mLFragmentSchedule != null) {
            trans.hide(mLFragmentSchedule);
//            mLFragmentSchedule = null;
        }
        if (mLFragmentArrange != null) {
            trans.hide(mLFragmentArrange);
        }
        if (mLFragmentTakeoutHistory != null) {
            trans.hide(mLFragmentTakeoutHistory);
        }
        if (mLFragmentChing != null) {
            trans.hide(mLFragmentChing);
        }
        if (mLFragmentOrder != null) {
            trans.hide(mLFragmentOrder);
            mLFragmentOrder = null;
        }
        if (mLFragmentMessage != null) {
            trans.hide(mLFragmentMessage);
        }
        if (mLFragmentTurnover != null) {
            trans.hide(mLFragmentTurnover);
            mLFragmentTurnover = null;
        }
        if (mLFragmentKichen != null) {
            trans.hide(mLFragmentKichen);
        }
        if (mLFragmentMore != null) {
            trans.hide(mLFragmentMore);
        }
    }
}
