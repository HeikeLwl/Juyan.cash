package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;


/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentMessage extends BaseFragment {
    private SystemNotifyFragment mSystemNotifyFragment;
    private StoreNofityFragment mStoreNofityFragment;
    private WXNotifyFragment mWXNotifyFragment;
    private FloatingActionButton fabClear;
    private final int FRAGMENT_WX = 0;
    private final int FRAGMENT_STORE = 1;
    private final int FRAGMENT_SYSTEM = 2;
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private MainFragmentListener mOnMessageClearClickListener;
    private TextView mTextView1,mTextView2,mTextView3;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    changeStatusCount();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setAllMessageHandler(mHandler);
        try{
            mOnMessageClearClickListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            changeStatusCount();
        }
    }

    private void changeStatusCount(){
        int[] counts = DBHelper.getInstance(getContext().getApplicationContext()).getUnreadMessageCounts();
        if(counts[0] > 0){
            mTextView1.setVisibility(TextView.VISIBLE);
            mTextView1.setText(counts[0]+"");
        }else{
            mTextView1.setVisibility(TextView.GONE);
        }
        if(counts[1] > 0){
            mTextView2.setVisibility(TextView.VISIBLE);
            mTextView2.setText(counts[1]+"");
        }else{
            mTextView2.setVisibility(TextView.GONE);
        }
        if(counts[2] > 0){
            mTextView3.setVisibility(TextView.VISIBLE);
            mTextView3.setText(counts[2]+"");
        }else{
            mTextView3.setVisibility(TextView.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_message, container, false);
        initView(mView);
        setRightFragment(FRAGMENT_WX);
        setListener();
        changeStatusCount();
        return mView;
    }

    private void setRightFragment(int tag){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (tag){
            case FRAGMENT_WX:
                if(mWXNotifyFragment == null){
                    mWXNotifyFragment = new WXNotifyFragment();
                    ft.add(R.id.containerright,mWXNotifyFragment);
                }else{
                    ft.show(mWXNotifyFragment);
                }
                break;
            case FRAGMENT_STORE:
                if(mStoreNofityFragment == null){
                    mStoreNofityFragment = new StoreNofityFragment();
                    ft.add(R.id.containerright,mStoreNofityFragment);
                }else{
                    ft.show(mStoreNofityFragment);
                }
                break;
            case FRAGMENT_SYSTEM:
                if(mSystemNotifyFragment == null){
                    mSystemNotifyFragment = new SystemNotifyFragment();
                    ft.add(R.id.containerright,mSystemNotifyFragment);
                }else{
                    ft.show(mSystemNotifyFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft){
        if(mWXNotifyFragment != null){
            ft.hide(mWXNotifyFragment);
        }
        if(mStoreNofityFragment != null){
            ft.hide(mStoreNofityFragment);
        }
        if(mSystemNotifyFragment != null){
            ft.hide(mSystemNotifyFragment);
        }
    }

    private void initView(View view) {
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        fabClear = (FloatingActionButton) view.findViewById(R.id.fab_clear);
        mTextView1 = (TextView) view.findViewById(R.id.tv_1);
        mTextView2 = (TextView) view.findViewById(R.id.tv_2);
        mTextView3 = (TextView) view.findViewById(R.id.tv_3);
    }

    private void setListener(){
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.radiobutton_0:
                        setRightFragment(FRAGMENT_WX);
                        break;
                    case R.id.radiobutton_1:
                        setRightFragment(FRAGMENT_STORE);
                        break;
                    case R.id.radiobutton_2:
                        setRightFragment(FRAGMENT_SYSTEM);
                        break;
                }
            }
        });

        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                switch (mLCustomeRadioGroup.getCheckedRadioButtonId()){
                    case R.id.radiobutton_0:
                        content = "确认清空所有已读微信消息吗？";
                        showDialog(content,0);
                        break;
                    case R.id.radiobutton_1:
                        content = "确认清空所有已读店家通知吗？";
                        showDialog(content,1);
                        break;
                    case R.id.radiobutton_2:
                        content = "确认清空所有已读系统通知吗？";
                        showDialog(content,2);
                        break;
                }
            }
        });
    }

    //显示清空消息提示对话框
    private void showDialog(String content, final int type){
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("清空已读消息");
        alertDialog.setMessage(content);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "清空已读", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type){
                    case 0:
                        DBHelper.getInstance(getContext().getApplicationContext()).clearWXMessage();
                        mOnMessageClearClickListener.onClearClick(0);
                        break;
                    case 1:
                        DBHelper.getInstance(getContext().getApplicationContext()).clearStoreMessage();
                        mOnMessageClearClickListener.onClearClick(1);
                        break;
                    case 2:
                        DBHelper.getInstance(getContext().getApplicationContext()).clearSystemMessage();
                        mOnMessageClearClickListener.onClearClick(2);
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
