package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.ArrangeSortAdapter;
import jufeng.juyancash.dao.ArrangeEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentArrangeHistory extends BaseFragment {
    private RecyclerView mRecyclerView;
    private ArrangeSortAdapter adapter;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.updateData(1,null);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setArrangeHistoryHandler(mHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_arrange_history,container,false);
        initView(mView);
        setAdapter();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(1,null);
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void setAdapter(){
        adapter = new ArrangeSortAdapter(getActivity().getApplicationContext(),1);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnArrangeEntityClickListener(new ArrangeSortAdapter.OnArrangeEntityClickListener() {
            @Override
            public void onArrangeEntityClick(ArrangeEntity arrangeEntity) {

            }
        });
    }
}
