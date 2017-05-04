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
import jufeng.juyancash.adapter.SelectDiscountGoodsAdapter;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by Administrator102 on 2016/8/31.
 */
public class LFragmentSelectSomeGoods extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle,tvConfirm;
    private SelectDiscountGoodsAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;
    private MainFragmentListener mOnSelectSomeDiscountGoods;
    private boolean isOpenJoinOrder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnSelectSomeDiscountGoods = (MainFragmentListener) context;
        }catch (ClassCastException e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_discount_goods,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
        mSomeDiscountGoodsEntities = getArguments().getParcelableArrayList("someDiscountGoods");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(DiscountHistoryEntity discountHistoryEntity,ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities,boolean isOpenJoinOrder){
        this.mDiscountHistoryEntity = discountHistoryEntity;
        this.mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
        this.isOpenJoinOrder = isOpenJoinOrder;

        if(adapter != null){
            adapter.updateData(mSomeDiscountGoodsEntities,this.mDiscountHistoryEntity.getOrderId(),mDiscountHistoryEntity,this.isOpenJoinOrder);
        }
    }

    private void setAdapter(){
        adapter = new SelectDiscountGoodsAdapter(getActivity().getApplicationContext(),mSomeDiscountGoodsEntities,mDiscountHistoryEntity.getOrderId(),mDiscountHistoryEntity,isOpenJoinOrder);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSomeDiscountGoodsEntities.clear();
                mSomeDiscountGoodsEntities.addAll(adapter.getSomeDiscountGoods());
                mOnSelectSomeDiscountGoods.onSelectSomeDiscountGoods(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectSomeDiscountGoods.onSelectSomeDiscountGoods(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });
    }
}
