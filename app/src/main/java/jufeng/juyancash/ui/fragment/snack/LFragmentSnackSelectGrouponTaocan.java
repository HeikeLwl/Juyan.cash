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

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectGrouponTaocanAdapter;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.eventbus.SnackSelectGrouponTaocanConfrimEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSnackSelectGrouponTaocan extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvBillType;
    private TextView tvCancle;
    private SelectGrouponTaocanAdapter adapter;
    private GrouponEntity mGrouponEntity;
    private GrouponTaocanEntity mGrouponTaocanEntity;
    private PayModeEntity mPayModeEntity;
    private String orderId;

    public static LFragmentSnackSelectGrouponTaocan newInstance(String orderId, GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity) {
        LFragmentSnackSelectGrouponTaocan fragment = new LFragmentSnackSelectGrouponTaocan();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("grouponEntity", grouponEntity);
        bundle.putParcelable("grouponTaocan", grouponTaocanEntity);
        bundle.putParcelable("payment", payModeEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_billperson, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvBillType = (TextView) view.findViewById(R.id.tv_bill_type);
        orderId = getArguments().getString("orderId");
        mGrouponEntity = getArguments().getParcelable("grouponEntity");
        mGrouponTaocanEntity = getArguments().getParcelable("grouponTaocan");
        mPayModeEntity = getArguments().getParcelable("payment");
        tvBillType.setText("选择团购套餐");
    }

    public void setNewParam(String orderId, GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payment) {
        this.orderId = orderId;
        this.mGrouponEntity = grouponEntity;
        this.mGrouponTaocanEntity = grouponTaocanEntity;
        this.mPayModeEntity = payment;

        tvBillType.setText("选择团购套餐");
        if (adapter != null && mGrouponEntity != null) {
            adapter.updateData(mGrouponEntity.getGrouponId());
        }
    }

    private void setAdapter() {
        adapter = new SelectGrouponTaocanAdapter(getActivity().getApplicationContext(), mGrouponEntity.getGrouponId());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener() {
        adapter.setOnSelectGrouponTaocanItemClickListener(new SelectGrouponTaocanAdapter.OnSelectGrouponTaocanItemClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSelectGrouponTaocanItemClick(GrouponTaocanEntity grouponTaocanEntity) {
                EventBus.getDefault().post(new SnackSelectGrouponTaocanConfrimEvent(orderId, mGrouponEntity,grouponTaocanEntity, mPayModeEntity));
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackSelectGrouponTaocanConfrimEvent(orderId, mGrouponEntity,mGrouponTaocanEntity, mPayModeEntity));
            }
        });
    }
}
