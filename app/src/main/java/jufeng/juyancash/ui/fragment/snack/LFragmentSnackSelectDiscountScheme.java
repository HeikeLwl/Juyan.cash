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

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectDiscountSchemeAdapter;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.eventbus.SnackSelectSchemeConfirmEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSnackSelectDiscountScheme extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle;
    private SelectDiscountSchemeAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;

    public static LFragmentSnackSelectDiscountScheme newInstance(DiscountHistoryEntity discountHistoryEntity){
        LFragmentSnackSelectDiscountScheme fragment = new LFragmentSnackSelectDiscountScheme();
        Bundle bundle = new Bundle();
        bundle.putParcelable("discountHistory",discountHistoryEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_select_discount_scheme,container,false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
    }

    private void setAdapter(){
        adapter = new SelectDiscountSchemeAdapter(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener(){
        adapter.setOnSelectDiscountReasonItemClickListener(new SelectDiscountSchemeAdapter.OnSelectDiscountSchemeItemClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSelectDiscountSchemeItemClick(String discountId) {
                DiscountEntity discountEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getDiscountEntity(discountId);
                if(CustomMethod.isInPeriod(discountEntity)) {
                    mDiscountHistoryEntity.setDiscountId(discountId);
                    EventBus.getDefault().post(new SnackSelectSchemeConfirmEvent(mDiscountHistoryEntity));
                }else{
                    CustomMethod.showMessage(getContext(),"当前时间不在该打折方案的有效期内，请重新选择打折方案");
                }
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackSelectSchemeConfirmEvent(mDiscountHistoryEntity));
            }
        });
    }
}
