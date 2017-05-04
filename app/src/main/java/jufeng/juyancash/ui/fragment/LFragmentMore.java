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
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentMore extends BaseFragment {
    private final int FRAGMENT_UPLOAD = 5;
    private final int FRAGMENT_ABOUT = 4;
    private final int FRAGMENT_TEC = 3;
    private final int FRAGMENT_UPDATE = 2;
    private final int FRAGMENT_VIP = 1;
    private final int FRAGMENT_SETCASHIER = 0;
    private LFragmentAbout mLFragmentAbout;
    private LFragmentTec mLFragmentTec;
    private LFragmentSetCashier mLFragmentSetCashier;
    private LFragmentUpdate mLFragmentUpdate;
    private LFragmentUpload mLFragmentUpload;
    private LFragmentVip mLFragmentVip;
    private LCustomeRadioGroup mLCustomeRadioGroup;
//    private Button btnLogout;
    private MainFragmentListener mOnMoreListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnMoreListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_more, container, false);
        initView(mView);
        setRightFragment(FRAGMENT_SETCASHIER);
        setListener();
        return mView;
    }

    private void setRightFragment(int tag){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (tag){
            case FRAGMENT_SETCASHIER:
                if(mLFragmentSetCashier == null){
                    mLFragmentSetCashier = new LFragmentSetCashier();
                    ft.add(R.id.containerright,mLFragmentSetCashier);
                }else{
                    ft.show(mLFragmentSetCashier);
                }
                break;
            case FRAGMENT_VIP:
                if(mLFragmentVip == null){
                    mLFragmentVip = new LFragmentVip();
                    ft.add(R.id.containerright,mLFragmentVip);
                }else{
                    mLFragmentVip.setNewParam();
                    ft.show(mLFragmentVip);
                }
                break;
            case FRAGMENT_UPDATE:
                if(mLFragmentUpdate == null){
                    mLFragmentUpdate = new LFragmentUpdate();
                    ft.add(R.id.containerright,mLFragmentUpdate);
                }else{
                    ft.show(mLFragmentUpdate);
                }
                break;
            case FRAGMENT_TEC:
                if(mLFragmentTec == null){
                    mLFragmentTec = new LFragmentTec();
                    ft.add(R.id.containerright,mLFragmentTec);
                }else{
                    ft.show(mLFragmentTec);
                }
                break;
            case FRAGMENT_ABOUT:
                if(mLFragmentAbout == null){
                    mLFragmentAbout = new LFragmentAbout();
                    ft.add(R.id.containerright,mLFragmentAbout);
                }else{
                    ft.show(mLFragmentAbout);
                }
                break;
            case FRAGMENT_UPLOAD:
                if(mLFragmentUpload == null){
                    mLFragmentUpload = new LFragmentUpload();
                    ft.add(R.id.containerright,mLFragmentUpload);
                }else{
                    mLFragmentUpload.setNewParam();
                    ft.show(mLFragmentUpload);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft){
        if(mLFragmentUpload != null){
            ft.hide(mLFragmentUpload);
        }
        if(mLFragmentVip != null){
            ft.hide(mLFragmentVip);
        }
        if(mLFragmentSetCashier != null){
            ft.hide(mLFragmentSetCashier);
        }
        if(mLFragmentUpdate != null){
            ft.hide(mLFragmentUpdate);
        }
        if(mLFragmentTec != null){
            ft.hide(mLFragmentTec);
        }
        if(mLFragmentAbout != null){
            ft.hide(mLFragmentAbout);
        }
    }

    private void initView(View view) {
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
//        btnLogout = (Button) view.findViewById(R.id.btn_logout);
    }

    private void setListener() {
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_0:
                        setRightFragment(FRAGMENT_SETCASHIER);
                        break;
                    case R.id.radiobutton_1:
                        setRightFragment(FRAGMENT_VIP);
                        break;
                    case R.id.radiobutton_2:
                        setRightFragment(FRAGMENT_UPDATE);
                        break;
                    case R.id.radiobutton_3:
                        setRightFragment(FRAGMENT_TEC);
                        break;
                    case R.id.radiobutton_4:
                        setRightFragment(FRAGMENT_ABOUT);
                        break;
                    case R.id.radiobutton_5:
                        setRightFragment(FRAGMENT_UPLOAD);
                        break;
                }
            }
        });

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final CustomeExitDialog dialog = new CustomeExitDialog(getActivity(), "注销账号", "确定要注销吗？");
//                dialog.setExitDialogClickListener(new CustomeExitDialog.OnExitDialogClickListener() {
//                    @Override
//                    public void onCancle() {
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onConfirm() {
//                        dialog.dismiss();
//                        if (mOnMoreListener != null) {
//                            mOnMoreListener.onLogOut();
//                        }
//                    }
//                });
//            }
//        });
    }
}
