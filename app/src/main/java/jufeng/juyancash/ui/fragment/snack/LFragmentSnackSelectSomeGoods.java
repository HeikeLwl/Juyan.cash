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
import jufeng.juyancash.adapter.SelectDiscountGoodsAdapter;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.eventbus.SnackSelectSomeGoodsConfirmEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;

/**
 * Created by Administrator102 on 2016/8/31.
 */
public class LFragmentSnackSelectSomeGoods extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle, tvConfirm;
    private SelectDiscountGoodsAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;

    public static LFragmentSnackSelectSomeGoods newInstance(DiscountHistoryEntity discountHistoryEntity,ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities){
        LFragmentSnackSelectSomeGoods fragment = new LFragmentSnackSelectSomeGoods();
        Bundle bundle = new Bundle();
        bundle.putParcelable("discountHistory",discountHistoryEntity);
        bundle.putParcelableArrayList("someDiscountGoods",someDiscountGoodsEntities);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_discount_goods, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
        mSomeDiscountGoodsEntities = getArguments().getParcelableArrayList("someDiscountGoods");
    }

    private void setAdapter() {
        adapter = new SelectDiscountGoodsAdapter(getActivity().getApplicationContext(), mSomeDiscountGoodsEntities, mDiscountHistoryEntity.getOrderId(), mDiscountHistoryEntity, false);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                mSomeDiscountGoodsEntities.clear();
                mSomeDiscountGoodsEntities.addAll(adapter.getSomeDiscountGoods());
                EventBus.getDefault().post(new SnackSelectSomeGoodsConfirmEvent(mDiscountHistoryEntity, mSomeDiscountGoodsEntities));
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackSelectSomeGoodsConfirmEvent(mDiscountHistoryEntity, mSomeDiscountGoodsEntities));
            }
        });
    }
}
