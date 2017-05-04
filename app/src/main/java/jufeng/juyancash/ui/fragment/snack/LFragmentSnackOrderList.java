package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SnackOrderListAdapter;
import jufeng.juyancash.bean.SnackOrderItemClickEvent;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.eventbus.PrintCaiwulianEvent;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackChangeTableCodeSuccessEvent;
import jufeng.juyancash.eventbus.SnackOrderListRefreshEvent;
import jufeng.juyancash.eventbus.SnackOrderMoneyChangedEvent;
import jufeng.juyancash.eventbus.SnackReturnOrderEvent;
import jufeng.juyancash.eventbus.SnackUnreadMessageRefreshEvent;
import jufeng.juyancash.myinterface.OnRecyclerViewItemClickListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/3/20.
 */

public class LFragmentSnackOrderList extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private SnackOrderListAdapter mAdapter;
    private Unbinder mUnbinder;
    private String mOrderId;

    public static LFragmentSnackOrderList newInstance(String orderId) {
        LFragmentSnackOrderList fragment = new LFragmentSnackOrderList();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack_order_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initData();
        setAdapter();
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public void initData(){
        mOrderId = getArguments().getString("orderId");
    }

    public void setAdapter() {
        mAdapter = new SnackOrderListAdapter(getContext(),mOrderId);
        mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setItemAnimator(null);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.addOnItemTouchListener(new OnRecyclerViewItemClickListener(mRecyclerview) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                OrderEntity orderEntity = (OrderEntity) holder.itemView.getTag();
                if (orderEntity != null) {
                    EventBus.getDefault().post(new SnackOrderItemClickEvent(orderEntity.getOrderId()));
                    mAdapter.notifyItem(orderEntity);
                }
            }

            @Override
            public void onLongPress(RecyclerView.ViewHolder holder, int position) {
                super.onLongPress(holder, position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                OrderEntity orderEntity = (OrderEntity) viewHolder.itemView.getTag();
                boolean isHasOrderedDish = DBHelper.getInstance(getContext()).isHasSnackOrderedDish(orderEntity.getOrderId());
                boolean isPayOver = DBHelper.getInstance(getContext()).isOrderPayOver(orderEntity.getOrderId());
                if(isHasOrderedDish && isPayOver){
                    //该笔订单已结账
                    final int dragFlags;
                    final int swipeFlags;
                    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                        swipeFlags = 0;
                    } else {
                        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    }
                    return makeMovementFlags(dragFlags, swipeFlags);
                }else{
                    return 0;
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                OrderEntity orderEntity = (OrderEntity) viewHolder.itemView.getTag();
                boolean result0 = DBHelper.getInstance(getContext()).cashierOver(orderEntity.getOrderId());
                if(result0) {
                    DBHelper.getInstance(getContext()).insertUploadData(orderEntity.getOrderId(), orderEntity.getOrderId(), 7);
                    EventBus.getDefault().post(new PrintCaiwulianEvent(orderEntity.getOrderId()));
                    boolean result = mAdapter.deleteItem(orderEntity);
                    if (result) {
                        EventBus.getDefault().post(new SnackOrderItemClickEvent(null));
                        EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
                    }
                }else{
                    CustomMethod.showMessage(getContext(), "结账失败，请重新尝试");
                    mAdapter.updateData();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerview);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshList(SnackOrderListRefreshEvent event){
        if(event != null) {
            mAdapter.updateData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelOrderSuccess(SnackCancelOrderSuccessEvent event){
        if(event != null){
            mAdapter.updateData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeTableCodeSuccess(SnackChangeTableCodeSuccessEvent event){
        if(event != null){
            mAdapter.updateData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderMoneyChange(SnackOrderMoneyChangedEvent event){
        if(event != null){
            mAdapter.updateData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReturnOrder(SnackReturnOrderEvent event) {
        if (event != null) {
            mAdapter.updateData();
        }
    }
}
