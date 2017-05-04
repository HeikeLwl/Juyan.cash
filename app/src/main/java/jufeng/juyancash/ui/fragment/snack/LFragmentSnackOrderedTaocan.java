package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SnackSelectedTaocanAdapter;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupEntity;
import jufeng.juyancash.eventbus.SnackDeleteTaocanGroupDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanAddDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanCancelEvent;
import jufeng.juyancash.eventbus.SnackTaocanChangeDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanConfirmEvent;
import jufeng.juyancash.eventbus.SnackTaocanDeleteDishEvent;
import jufeng.juyancash.eventbus.SnackTaocanDeleteEvent;
import jufeng.juyancash.eventbus.SnackTaocanOrderRefreshEvent;
import jufeng.juyancash.ui.customview.ConfigTaocanDishDialogFragment;
import jufeng.juyancash.ui.customview.DetailTaocanDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentSnackOrderedTaocan extends BaseFragment {
    private TextView tvName, tvPrice;
    private RecyclerView mRecyclerView0;
    private SnackSelectedTaocanAdapter adapter0;
    private Button btnCancle, btnDelete, btnConfirm;
    private ArrayList<OrderTaocanGroupDishEntity> mOrderTaocanGroupDishEntities;//已点套餐内商品的原数据
    private OrderDishEntity mOrderDishEntity;
    private int mType;

    public static LFragmentSnackOrderedTaocan newInstance(OrderDishEntity orderDishEntity,int type){
        LFragmentSnackOrderedTaocan fragment = new LFragmentSnackOrderedTaocan();
        Bundle bundle = new Bundle();
        bundle.putParcelable("orderDishEntity",orderDishEntity);
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddDish(SnackTaocanAddDishEvent event) {
        if (event != null && event.getOrderTaocanGroupDishEntity() != null) {
            adapter0.addItem(event.getOrderTaocanGroupDishEntity());
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeleteDish(SnackTaocanDeleteDishEvent event) {
        if (event != null && event.getOrderTaocanGroupDishEntity() != null) {
            adapter0.deleteItem(event.getOrderTaocanGroupDishEntity());
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeDish(SnackTaocanChangeDishEvent event) {
        if (event != null && event.getOrderTaocanGroupDishEntity() != null) {
            adapter0.changeItem(event.getOrderTaocanGroupDishEntity());
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshDish(SnackTaocanOrderRefreshEvent event) {
        if (event != null && mOrderDishEntity != null) {
            adapter0.updateData(mOrderDishEntity, 0);
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_taocan_list, container, false);
        initView(mView);
        initData();
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView0 = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        tvName = (TextView) view.findViewById(R.id.tv_taocan_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_total_price);
    }

    private void initData() {
        mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        mType = getArguments().getInt("type", 0);
        if(mOrderDishEntity != null) {
            mOrderTaocanGroupDishEntities = new ArrayList<>();
            if (mType == 0) {
                //添加套餐
                btnCancle.setVisibility(Button.VISIBLE);
                btnDelete.setVisibility(Button.GONE);
            } else {
                //修改套餐
                mOrderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(mOrderDishEntity));
                btnCancle.setVisibility(Button.VISIBLE);
                if (mOrderDishEntity.getIsPrint() != null && mOrderDishEntity.getIsPrint() == 1) {
                    btnDelete.setVisibility(Button.GONE);
                } else {
                    btnDelete.setVisibility(Button.VISIBLE);
                }
            }
            tvPrice.setText("￥" + DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanPrice(mOrderDishEntity) + "元");
            tvName.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanNameById(mOrderDishEntity.getDishId()));
        }
    }

    private void setAdapter() {
        adapter0 = new SnackSelectedTaocanAdapter(getActivity().getApplicationContext(), mOrderDishEntity, 0);
        mRecyclerView0.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView0.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView0.setAdapter(adapter0);
        mRecyclerView0.setItemAnimator(null);

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
                if(orderTaocanGroupDishEntity.getIsPrint() != null && orderTaocanGroupDishEntity.getIsPrint() == 0 && taocanGroupEntity.getSelectMode() == 0){
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
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(getContext()).cancleTaocanEdit(mOrderDishEntity, mOrderTaocanGroupDishEntities);
                EventBus.getDefault().post(new SnackTaocanCancelEvent(mOrderDishEntity.getOrderId()));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                if (mOrderDishEntity.getIsOrdered() == 1) {
                    CustomMethod.showMessage(getContext(), "已有下单商品无法删除");
                } else {
                    DBHelper.getInstance(getContext().getApplicationContext()).deleteTaocan(mOrderDishEntity);
                    EventBus.getDefault().post(new SnackTaocanDeleteEvent(mOrderDishEntity.getOrderId()));
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                if (DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(mOrderDishEntity).size() > 0) {
                    DBHelper.getInstance(getContext()).confirmTaocanEdit(mOrderDishEntity);
                    EventBus.getDefault().post(new SnackTaocanConfirmEvent(mOrderDishEntity.getOrderId()));
                } else {
                    CustomMethod.showMessage(getContext(), "未选商品，无法执行此操作");
                }
            }
        });

        adapter0.setOnSelectedTaocanItemClickListener(new SnackSelectedTaocanAdapter.OnSelectedTaocanItemClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onTaocanItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
                Log.d("###", "套餐分组内商品："+orderTaocanGroupDishEntity.toString());
                if(orderTaocanGroupDishEntity.getIsPrint() != null && orderTaocanGroupDishEntity.getIsPrint() == 1 && orderTaocanGroupDishEntity.getStatus() == 1){
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

            }

            @Override
            public void onTaocanReduceClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {

            }
        });
    }
}
