package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectDiscountReasonAdapter;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.eventbus.SnackSelectDiscountReasonConfirmEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSnackSelectDiscountReason extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle;
    private SelectDiscountReasonAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;
    private int mType;

    public static LFragmentSnackSelectDiscountReason newInstance(int type,DiscountHistoryEntity discountHistoryEntity,ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities){
        LFragmentSnackSelectDiscountReason fragment = new LFragmentSnackSelectDiscountReason();
        Bundle  bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putParcelable("discountHistory",discountHistoryEntity);
        bundle.putParcelableArrayList("someDiscountGoods",someDiscountGoodsEntities);
        fragment.setArguments(bundle);
        return fragment;
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
        this.mType = getArguments().getInt("type",0);
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
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSelectDiscountReasonItemClick(String reasonName) {
                mDiscountHistoryEntity.setDiscountReason(reasonName);
                EventBus.getDefault().post(new SnackSelectDiscountReasonConfirmEvent(mType,mDiscountHistoryEntity,mSomeDiscountGoodsEntities));
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackSelectDiscountReasonConfirmEvent(mType,mDiscountHistoryEntity,mSomeDiscountGoodsEntities));
            }
        });
    }
}
