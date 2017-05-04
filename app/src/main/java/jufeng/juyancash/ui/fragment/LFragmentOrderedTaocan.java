package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectedTaocanAdapter;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupEntity;
import jufeng.juyancash.eventbus.SnackDeleteTaocanGroupDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanChangeDishEvent;
import jufeng.juyancash.myinterface.SelectTaocanFragmentListener;
import jufeng.juyancash.ui.customview.ConfigTaocanDishDialogFragment;
import jufeng.juyancash.ui.customview.DetailTaocanDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.util.ActivityIntentUtil;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentOrderedTaocan extends BaseFragment {
    private TextView tvName, tvPrice;
    private RecyclerView mRecyclerView0;
    private SelectedTaocanAdapter adapter0;
    private Button btnCancle, btnDelete, btnConfirm;
    private SelectTaocanFragmentListener mOnOrderedTaocanClickListener;
    private OrderDishEntity mOrderDishEntity;
    private String activityTag;
    private int isDelete;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                        adapter0.updateData(mOrderDishEntity,0);
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
                    } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                        adapter0.updateData(mOrderDishEntity,1);
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getOrderedTaocanPrice(mOrderDishEntity) + "元");
                    }
                    break;
                case 1://加菜
                    if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                        adapter0.addItem((OrderTaocanGroupDishEntity) msg.obj);
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
                    }
                    break;
                case 2:
                    if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
                    }
                    break;
                case 3://删除
                    if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                        adapter0.deleteItem((OrderTaocanGroupDishEntity) msg.obj);
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
                    }
                    break;
                case 4://修改
                    if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                        adapter0.changeItem((OrderTaocanGroupDishEntity) msg.obj);
                        tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
                    }
                    break;
            }
        }
    };

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        LFragmentSelectTaocan selectTaoCanActivity = (LFragmentSelectTaocan) context;
////        selectTaoCanActivity.setOrderedTaocanHandler(mHandler);
//        try{
//            mOnOrderedTaocanClickListener = (OnOrderedTaocanClickListener) context;
//        }catch (ClassCastException e){
//            e.printStackTrace();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_taocan_list, container, false);
        initView(mView);
        setAdapter();
        setListener();
        return mView;
    }

    public void setNewParam(OrderDishEntity orderDishEntity,String activityTag,int isDelete){
        this.mOrderDishEntity = orderDishEntity;
        this.activityTag = activityTag;
        this.isDelete = isDelete;

        initData();
        setAdapter();
    }

    public void setListener(SelectTaocanFragmentListener listener) {
        if (mOnOrderedTaocanClickListener == null)
            this.mOnOrderedTaocanClickListener = listener;
    }

    public void setNewMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    private void initView(View view) {
        mRecyclerView0 = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        tvName = (TextView) view.findViewById(R.id.tv_taocan_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_total_price);
        mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        activityTag = getArguments().getString("activity");
        isDelete = getArguments().getInt("isDelete",0);
        initData();
    }

    private void initData(){
        if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
            btnCancle.setVisibility(Button.VISIBLE);
            if (isDelete == 1) {
                btnDelete.setVisibility(Button.VISIBLE);
            } else {
                btnDelete.setVisibility(Button.GONE);
            }
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
            btnDelete.setVisibility(Button.GONE);
            btnCancle.setVisibility(Button.GONE);
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getOrderedTaocanPrice(mOrderDishEntity) + "元");
        }
        tvName.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanNameById(mOrderDishEntity.getDishId()));
    }

    private void setAdapter() {
        if(adapter0 == null) {
            if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                adapter0 = new SelectedTaocanAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 0);
            } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                adapter0 = new SelectedTaocanAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 1);
            }
            mRecyclerView0.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView0.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView0.setAdapter(adapter0);
            mRecyclerView0.setItemAnimator(null);
        }else{
            if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
                adapter0.updateData(mOrderDishEntity,0);
            } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                adapter0.updateData(mOrderDishEntity,1);
            }
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = (OrderTaocanGroupDishEntity) viewHolder.itemView.getTag();
                if(orderTaocanGroupDishEntity == null){
                    return 0;
                }
                TaocanGroupEntity taocanGroupEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanGroupById(orderTaocanGroupDishEntity.getTaocanGroupId());
                if(taocanGroupEntity == null){
                    return 0;
                }
                if(orderTaocanGroupDishEntity.getStatus() != null && orderTaocanGroupDishEntity.getStatus() != 1 && taocanGroupEntity.getSelectMode() == 0){
                    //未打印并且是用户自选商品允许删除
                    //该菜品未下单
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
                OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = (OrderTaocanGroupDishEntity) viewHolder.itemView.getTag();
                DBHelper.getInstance(getContext().getApplicationContext()).deleteTaocanGroupDish(orderTaocanGroupDishEntity);
                adapter0.deleteItem(orderTaocanGroupDishEntity);
                EventBus.getDefault().post(new SnackDeleteTaocanGroupDishEvent(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId()));
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView0);
    }

    private void setListener() {
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOrderedTaocanClickListener != null)
                    mOnOrderedTaocanClickListener.onTaocanCanle();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOrderedTaocanClickListener != null)
                    mOnOrderedTaocanClickListener.onTaocanDelete();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOrderedTaocanClickListener != null)
                    mOnOrderedTaocanClickListener.onTaocanConfirm();
            }
        });

        adapter0.setOnSelectedTaocanItemClickListener(new SelectedTaocanAdapter.OnSelectedTaocanItemClickListener() {
            @Override
            public void onTaocanItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
//                if (mOnOrderedTaocanClickListener != null)
//                    mOnOrderedTaocanClickListener.onTaocanDishItemClick(orderTaocanGroupDishEntity);
                if(orderTaocanGroupDishEntity.getStatus() != null && orderTaocanGroupDishEntity.getStatus() == 1){
                    //该商品已下单并且已打印
                    DetailTaocanDialogFragment detailTaocanDialogFragment = new DetailTaocanDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderDishEntity",mOrderDishEntity);
                    bundle.putParcelable("orderTaocanGroupDishEntity",orderTaocanGroupDishEntity);
                    detailTaocanDialogFragment.setArguments(bundle);
                    detailTaocanDialogFragment.show(getFragmentManager(),"detailTaocanDialogFragment");
                }else{
                    ConfigTaocanDishDialogFragment configTaocanDishDialogFragment = new ConfigTaocanDishDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderDishEntity",mOrderDishEntity);
                    bundle.putParcelable("orderTaocanGroupDishEntity",orderTaocanGroupDishEntity);
                    configTaocanDishDialogFragment.setArguments(bundle);
                    configTaocanDishDialogFragment.show(getFragmentManager(),"configTaocanDishDialogFragment");
                }
            }

            @Override
            public void onTaocanAddClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
                if (mOnOrderedTaocanClickListener != null)
                    mOnOrderedTaocanClickListener.onTaocanDishAdd(orderTaocanGroupDishEntity);
            }

            @Override
            public void onTaocanReduceClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
                if (mOnOrderedTaocanClickListener != null)
                    mOnOrderedTaocanClickListener.onTaocanDishReduce(orderTaocanGroupDishEntity);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeDish(SnackTaocanChangeDishEvent event) {
        if (event != null && event.getOrderTaocanGroupDishEntity() != null) {
            adapter0.changeItem(event.getOrderTaocanGroupDishEntity());
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        }
    }
}
