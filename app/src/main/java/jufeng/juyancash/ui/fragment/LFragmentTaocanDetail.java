package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.TaocanListAdapter;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.eventbus.SnackDeleteTaocanGroupDishEvent;
import jufeng.juyancash.myinterface.SelectTaocanFragmentListener;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.ActivityIntentUtil;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentTaocanDetail extends BaseFragment {
    private RecyclerView mRecyclerView1;
    private TaocanListAdapter adapter1;
    private EditText etNote;
    private SelectTaocanFragmentListener mOnTaocanItemClickListener;
    private OrderDishEntity mOrderDishEntity;
    private String activityTag;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://刷新套餐

                    break;
                case 1://数量增加
                    adapter1.changeAddItem((OrderTaocanGroupDishEntity) msg.obj);
                    break;
                case 2://减少
                    adapter1.changeReduceItem((OrderTaocanGroupDishEntity) msg.obj);
                    break;
                case 3://删除菜品
                    adapter1.deleteItem((OrderTaocanGroupDishEntity) msg.obj);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_taocan_detail, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        etNote = (EditText) view.findViewById(R.id.et_note);
        mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        activityTag = getArguments().getString("activity");
    }

    public void setNewMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public void setOnTaocanItemClickListener(SelectTaocanFragmentListener listener) {
        if (mOnTaocanItemClickListener == null) {
            mOnTaocanItemClickListener = listener;
        }
    }

    public void setNewParam(OrderDishEntity orderDishEntity, String activityTag) {
        this.mOrderDishEntity = orderDishEntity;
        this.activityTag = activityTag;

        setAdapter();
    }

    private void setAdapter() {
        if (adapter1 == null) {
            if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                adapter1 = new TaocanListAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 0);
            } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                adapter1 = new TaocanListAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 1);
            }
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
        } else {
            if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                adapter1.updateData(mOrderDishEntity, 0);
            } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                adapter1.updateData(mOrderDishEntity, 1);
            }
        }
    }

    private void setListener() {
        adapter1.setOnTaocanClickListener(new TaocanListAdapter.OnTaoCanListItemClickListener() {
            @Override
            public void onDishClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
                if (mOnTaocanItemClickListener != null)
                    mOnTaocanItemClickListener.onTaocanItemClick(orderTaocanGroupDishEntity);
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
