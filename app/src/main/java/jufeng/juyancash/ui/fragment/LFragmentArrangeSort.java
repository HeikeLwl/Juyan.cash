package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.ArrangeSortAdapter;
import jufeng.juyancash.bean.TableModel;
import jufeng.juyancash.dao.ArrangeEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentArrangeSort extends BaseFragment {
    private RecyclerView mRecyclerView;
    private ArrangeSortAdapter adapter;
    private Button btnCall,btnConfirm;
    private ArrayList<TableModel> mTableModels;
    private RadioButton[] mRadioButtons;
    private MainFragmentListener mMainFragmentListener;
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.updateData(0, (ArrangeEntity) btnCall.getTag());
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setArrangeSortHandler(mHandler);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_arrange_sort,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(0, (ArrangeEntity) btnCall.getTag());
        addAreaMenus();
    }

    /**
     * 添加桌位类型统计
     */
    private void addAreaMenus() {
        if(mLCustomeRadioGroup.getChildCount() > 0){
            mLCustomeRadioGroup.removeAllViews();
        }
        mTableModels = DBHelper.getInstance(getActivity()).getTableStatusData();
        mRadioButtons = new RadioButton[mTableModels.size()];
        for (int i = 0; i < mTableModels.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setBackgroundResource(R.drawable.red_table_seat_background);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setText(mTableModels.get(i).getSeatCount()+"座("+mTableModels.get(i).getTableCount()+"个)");
            radioButton.setTextSize(16);
            radioButton.setTextColor(getResources().getColor(R.color.white));
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setPadding(4, 8, 4, 8);
            radioButton.setClickable(false);
            LCustomeRadioGroup.LayoutParams params = new LCustomeRadioGroup.LayoutParams(LCustomeRadioGroup.LayoutParams.WRAP_CONTENT, LCustomeRadioGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 6;
            params.rightMargin = 6;
            params.bottomMargin = 4;
            params.topMargin = 4;
            params.width = 100;
            mLCustomeRadioGroup.addView(radioButton, params);
            mRadioButtons[i] = radioButton;
        }
        if (mRadioButtons.length > 0)
            mRadioButtons[0].setChecked(true);
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCall = (Button) view.findViewById(R.id.btn_call);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
    }

    private void setAdapter(){
        adapter = new ArrangeSortAdapter(getActivity(),0);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        adapter.setOnArrangeEntityClickListener(new ArrangeSortAdapter.OnArrangeEntityClickListener() {
            @Override
            public void onArrangeEntityClick(ArrangeEntity arrangeEntity) {
                btnCall.setTag(arrangeEntity);
                btnConfirm.setTag(arrangeEntity);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrangeEntity arrangeEntity = (ArrangeEntity) v.getTag();
                if(arrangeEntity != null) {
                    DBHelper.getInstance(getActivity()).insertUploadData(arrangeEntity.getArrangeId(),arrangeEntity.getArrangeId(),5);
                    DBHelper.getInstance(getActivity()).callArrange(arrangeEntity);
                    adapter.updateData(0,arrangeEntity);
                    if(mMainFragmentListener != null){
                        mMainFragmentListener.onArraySortCall(arrangeEntity.getArrangeNumber()+"号，请您就餐");
                    }
//                    CustomMethod.xfYunCall(getActivity(),arrangeEntity.getArrangeNumber()+"号，请您就餐","xiaoyan");
                }else{
                    CustomMethod.showMessage(getContext(),"请选择排号记录！");
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrangeEntity arrangeEntity = (ArrangeEntity) v.getTag();
                if(arrangeEntity != null){
                    DBHelper.getInstance(getActivity()).insertUploadData(arrangeEntity.getArrangeId(),arrangeEntity.getArrangeId(),6);
                    DBHelper.getInstance(getActivity()).confirmArrange(arrangeEntity);
                    adapter.updateData(0,arrangeEntity);
                    sendMainBroad();
                }else{
                    CustomMethod.showMessage(getContext(),"请选择排号记录！");
                }
            }
        });
    }

    private void sendMainBroad(){
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type",22);
        getActivity().sendBroadcast(intent);
    }
}
