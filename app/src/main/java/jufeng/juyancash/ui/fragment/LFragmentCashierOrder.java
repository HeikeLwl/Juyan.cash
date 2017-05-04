package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectedDishAdapter;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.eventbus.CashierDishListRefreshEvent;
import jufeng.juyancash.eventbus.OnTaocanDetailClickEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DetailDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.ActivityIntentUtil;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/21 0021.
 */
public class LFragmentCashierOrder extends BaseFragment implements View.OnClickListener, SelectedDishAdapter.OnSelectedDishClickListener {
    private Button btnRetreatDish, btnPresentDish, btnPrintBill, btnRemindDish;
    private TextView tvSeatNumber, tvSeatCount, tvCreateTime, tvDishCount, tvBillMoney;
    private TextView tvOrderNumber;
    private RecyclerView mRecyclerView;
    private MainFragmentListener mOnCashierOrderClickListener;
    private SelectedDishAdapter adapter;
    private String tableId, orderId;
    private TextView tvChangeOrder;
    private boolean isOpenJoinOrder;
    private LinearLayout joinLayout;
    private TextView tvOrderName;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.updateData(orderId, 1, isOpenJoinOrder);
                    OrderEntity orderEntity = DBHelper.getInstance(getActivity()).getOneOrderEntity(orderId);
                    if (orderEntity != null) {
                        refreshBill(orderEntity);
                    }
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(CashierDishListRefreshEvent event) {
        if (event != null)
            mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mActivity = (MainActivity) activity;
        mActivity.setCashierOrderHandler(mHandler);
        try {
            mOnCashierOrderClickListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_cashier_order_layout, container, false);
        initView(mView);
        initData();
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
        tvSeatCount = (TextView) view.findViewById(R.id.tv_seat_count);
        tvCreateTime = (TextView) view.findViewById(R.id.tv_create_time);
        tvDishCount = (TextView) view.findViewById(R.id.tv_dish_count);
        tvBillMoney = (TextView) view.findViewById(R.id.tv_bill_money);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        btnRetreatDish = (Button) view.findViewById(R.id.btn_retreat_dish);
        btnPresentDish = (Button) view.findViewById(R.id.btn_present_dish);
        btnPrintBill = (Button) view.findViewById(R.id.btn_print_bill);
        btnRemindDish = (Button) view.findViewById(R.id.btn_remind_dish);
        tvChangeOrder = (TextView) view.findViewById(R.id.tv_change_order);
        joinLayout = (LinearLayout) view.findViewById(R.id.layout_join);
        tvOrderName = (TextView) view.findViewById(R.id.tv_order_name);
        tableId = getArguments().getString("tableId");
        orderId = getArguments().getString("orderId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
    }

    public void setNewParam(String tableId, String orderId, boolean isOpenJoinOrder) {
        this.tableId = tableId;
        this.orderId = orderId;
        this.isOpenJoinOrder = isOpenJoinOrder;

        initData();
        if (adapter != null) {
            adapter.updateData(this.orderId, 1, this.isOpenJoinOrder);
        }
    }

    private void initData() {
        if (tableId != null) {
            OrderEntity orderEntity = DBHelper.getInstance(getActivity()).getOneOrderEntity(orderId);
            if (orderEntity != null) {
                tvChangeOrder.setVisibility(TextView.GONE);
                showOrderHead(orderEntity);
                refreshBill(orderEntity);
            }
        }
    }

    //控制显示子单的信息
    private void showOrderHead(OrderEntity orderEntity) {
        if (orderEntity != null) {
            joinLayout.setVisibility(LinearLayout.VISIBLE);
            TableEntity tableEntity = DBHelper.getInstance(getActivity()).queryOneTableData(orderEntity.getTableId());
            tvSeatNumber.setText("桌位：" + tableEntity.getTableName());
            tvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
            tvSeatCount.setText("人数：" + orderEntity.getOrderGuests());
            tvCreateTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
            tvOrderName.setText("帐单");
        } else {
            joinLayout.setVisibility(LinearLayout.GONE);
            tvOrderName.setText("总账单");
        }
    }

    private void setAdapter() {
        adapter = new SelectedDishAdapter(getActivity(), orderId, 1, isOpenJoinOrder);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener() {
        adapter.setOnSelectedDishClickListener(this);
        btnRetreatDish.setOnClickListener(this);
        btnPresentDish.setOnClickListener(this);
        btnPrintBill.setOnClickListener(this);
        btnRemindDish.setOnClickListener(this);
        tvChangeOrder.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOnCashierOrderClickListener.onCashierOrderScroll(dy);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retreat_dish:
                mOnCashierOrderClickListener.retreatDish(orderId);
                break;
            case R.id.btn_present_dish:
                mOnCashierOrderClickListener.presentDish(orderId);
                break;
            case R.id.btn_print_bill:
                mOnCashierOrderClickListener.printKHL(orderId);
                break;
            case R.id.btn_remind_dish:
                mOnCashierOrderClickListener.remindDish(orderId);
                break;
            case R.id.tv_change_order:
                tvChangeOrder.setVisibility(TextView.GONE);
                mOnCashierOrderClickListener.changeJoinOrder();
                break;
        }
    }

    //刷新账单
    private void refreshBill(OrderEntity orderEntity) {
        //菜品数量
        double dishCount = 0;
        //账单金额
        double billMoney = 0;
        dishCount = DBHelper.getInstance(getActivity()).getDishCountByOrderId(orderEntity.getOrderId());
        billMoney = DBHelper.getInstance(getActivity()).getBillMoneyByOrderId(orderEntity.getOrderId(), 1);
        tvDishCount.setText("共" + AmountUtils.multiply("" + dishCount, "1") + "项");
        tvBillMoney.setText("合计：￥" + billMoney);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedDishClicked(OrderDishEntity orderDishEntity) {
//        mOnCashierOrderClickListener.cashierDishDetail(orderDishEntity.getOrderDishId(), orderId, tableId);
        if (orderDishEntity != null && orderDishEntity.getIsOrdered() != 0 && orderDishEntity.getIsOrdered() != -1) {
            if(orderDishEntity.getType() == 0){
                DetailDialogFragment dialogFragment = new DetailDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("orderDishId", orderDishEntity.getOrderDishId());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dishdetaildialog");
            }else{
                EventBus.getDefault().post(new OnTaocanDetailClickEvent(orderDishEntity,orderDishEntity.getDishId(),orderId, ActivityIntentUtil.FRAGMENT_CASHIER,tableId));
            }
        }
    }

    @Override
    public void onReduce(OrderDishEntity orderDishEntity) {
    }

    @Override
    public void onAdd(OrderDishEntity orderDishEntity) {
    }

    @Override
    public void changeChildOrder(String orderId) {
        mOnCashierOrderClickListener.changeChildOrder(orderId);
    }
}
