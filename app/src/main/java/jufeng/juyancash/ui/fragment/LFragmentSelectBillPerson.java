package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectBillPersonAdapter;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.BillPerson;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSelectBillPerson extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvBillType;
    private TextView tvCancle;
    private SelectBillPersonAdapter adapter;
    private MainFragmentListener mOnSelectBillPersonListener;
    private BillAccountHistoryEntity mBillAccountHistoryEntity;
    private int mType;
    private String orderId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnSelectBillPersonListener = (MainFragmentListener) context;
        }catch (ClassCastException e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_billperson,container,false);
        initView(mView);
        initData();
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvBillType = (TextView) view.findViewById(R.id.tv_bill_type);
        orderId = getArguments().getString("orderId");
        mBillAccountHistoryEntity = getArguments().getParcelable("billHistory");
        mType = getArguments().getInt("type", 0);
    }

    public void setNewParam(String param0,BillAccountHistoryEntity billAccountHistoryEntity,int type){
        this.orderId = param0;
        this.mBillAccountHistoryEntity = billAccountHistoryEntity;
        this.mType = type;

        initData();
        if(adapter != null){
            adapter.updateData(mType,mBillAccountHistoryEntity.getBillAccountId());
        }
    }

    private void initData(){
        switch (mType){
            case 0:
                tvBillType.setText("选择挂账单位");
                break;
            case 1:
                tvBillType.setText("选择挂账人");
                break;
            case 2:
                tvBillType.setText("选择签字人");
                break;
        }
    }

    private void setAdapter(){
        adapter = new SelectBillPersonAdapter(getActivity().getApplicationContext(),mType,mBillAccountHistoryEntity.getBillAccountId());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener(){
        adapter.setOnSelectBillPersonItemClickListener(new SelectBillPersonAdapter.OnSelectBillPersonItemClickListener() {
            @Override
            public void onSelectBillPersonItemClick(BillPerson billPerson) {
                switch (mType){
                    case 0://挂账单位
                        mBillAccountHistoryEntity.setBillAccountUnitId(billPerson.getPersonId());
                        mBillAccountHistoryEntity.setBillAccountUnitName(billPerson.getName());
                        break;
                    case 1://挂账人
                        mBillAccountHistoryEntity.setBillAccountPersonId(billPerson.getPersonId());
                        mBillAccountHistoryEntity.setBillAccountPersonName(billPerson.getName());
                        break;
                    case 2://签字人
                        mBillAccountHistoryEntity.setBillAccountSignId(billPerson.getPersonId());
                        mBillAccountHistoryEntity.setBillAccountSignName(billPerson.getName());
                        break;
                }
                mOnSelectBillPersonListener.onSelectBillPerson(orderId,mBillAccountHistoryEntity);
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectBillPersonListener.onSelectBillPerson(orderId,mBillAccountHistoryEntity);
            }
        });
    }
}
