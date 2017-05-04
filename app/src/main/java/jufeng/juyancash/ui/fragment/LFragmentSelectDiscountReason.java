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

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectDiscountReasonAdapter;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSelectDiscountReason extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle;
    private SelectDiscountReasonAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private MainFragmentListener mOnSelectReasonListener;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnSelectReasonListener = (MainFragmentListener) context;
        }catch (ClassCastException e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_discount_reason,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
        mSomeDiscountGoodsEntities = getArguments().getParcelableArrayList("someDiscountGoods");
    }

    public void setNewParam(DiscountHistoryEntity discountHistoryEntity,ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities){
        this.mDiscountHistoryEntity = discountHistoryEntity;
        this.mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
    }

    private void setAdapter(){
        adapter = new SelectDiscountReasonAdapter(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        adapter.setOnSelectDiscountReasonItemClickListener(new SelectDiscountReasonAdapter.OnSelectDiscountReasonItemClickListener() {
            @Override
            public void onSelectDiscountReasonItemClick(String reasonName) {
                mDiscountHistoryEntity.setDiscountReason(reasonName);
                mOnSelectReasonListener.onSelectReason(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectReasonListener.onSelectReason(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });
    }
}
