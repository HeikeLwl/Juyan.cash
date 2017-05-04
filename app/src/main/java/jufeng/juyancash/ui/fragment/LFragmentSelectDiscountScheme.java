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

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectDiscountSchemeAdapter;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class LFragmentSelectDiscountScheme extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvCancle;
    private SelectDiscountSchemeAdapter adapter;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private MainFragmentListener mOnSelectedScheme;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnSelectedScheme = (MainFragmentListener) context;
        }catch (ClassCastException e){

        }
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

    public void setNewParam(DiscountHistoryEntity discountHistoryEntity){
        this.mDiscountHistoryEntity = discountHistoryEntity;
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
            @Override
            public void onSelectDiscountSchemeItemClick(String discountId) {
                DiscountEntity discountEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getDiscountEntity(discountId);
                if(CustomMethod.isInPeriod(discountEntity)) {
                    mDiscountHistoryEntity.setDiscountId(discountId);
                    mOnSelectedScheme.selectedScheme(mDiscountHistoryEntity);
                }else{
                    CustomMethod.showMessage(getContext(),"当前时间不在该打折方案的有效期内，请重新选择打折方案");
                }
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectedScheme.selectedScheme(mDiscountHistoryEntity);
            }
        });
    }
}
