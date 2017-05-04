package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.TaocanListAdapter;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.eventbus.SnackDeleteTaocanGroupDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanAddDishEvent;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentSnackTaocanDetail extends BaseFragment {
    private RecyclerView mRecyclerView1;
    private TaocanListAdapter adapter1;
    private EditText etNote;
    private OrderDishEntity mOrderDishEntity;

    public static LFragmentSnackTaocanDetail newInstance(OrderDishEntity orderDishEntity){
        LFragmentSnackTaocanDetail fragment = new LFragmentSnackTaocanDetail();
        Bundle bundle = new Bundle();
        bundle.putParcelable("orderDishEntity",orderDishEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_taocan_detail, container, false);
        initView(mView);
        initData();
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        etNote = (EditText) view.findViewById(R.id.et_note);
    }

    private void initData(){
        mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        if(mOrderDishEntity == null){

        }
    }

    private void setAdapter() {
        adapter1 = new TaocanListAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 0);
        WrapContentGridLayoutManager mLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 5);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(mLayoutManager);
        mRecyclerView1.setAdapter(adapter1);
        mRecyclerView1.setItemAnimator(null);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter1.getItemViewType(position) == TaocanListAdapter.LISTHEADER) {
                    return 5;
                } else if (adapter1.getItemViewType(position) == TaocanListAdapter.LISTITEM) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    private void setListener() {
        adapter1.setOnTaocanClickListener(new TaocanListAdapter.OnTaoCanListItemClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onDishClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
                EventBus.getDefault().post(new SnackTaocanAddDishEvent(orderTaocanGroupDishEntity));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeleteTaocanDish(SnackDeleteTaocanGroupDishEvent event){
        if(event != null && event.getOrderedTaocanGroupDishId() != null){
            adapter1.deleteItem(event.getOrderedTaocanGroupDishId());
        }
    }
}
