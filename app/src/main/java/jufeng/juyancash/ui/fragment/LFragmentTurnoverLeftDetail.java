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
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderDishAdapter;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentTurnoverLeftDetail extends BaseFragment {
    private TextView tvOrderNumber, tvTableNumber, tvMealCount, tvDate, tvTotal, tvBillMoney, tvPayMode, tvSerialNumber, tvCashierName, tvWaiterName, tvIsOpenInvoice, tvCloseTime;
    private RecyclerView mRecyclerView;
    private OrderDishAdapter adapter;
    private OrderEntity mOrderEntity;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mOrderEntity = (OrderEntity) msg.getData().getParcelable("orderEntity");
                    initData(mOrderEntity);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setTurnoverLeftDetailHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover_left_detail, container, false);
        initView(mView);
        setAdapter();
        initData(mOrderEntity);
        return mView;
    }

    private void initView(View view) {
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvTableNumber = (TextView) view.findViewById(R.id.tv_table_number);
        tvMealCount = (TextView) view.findViewById(R.id.tv_meal_count);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        tvBillMoney = (TextView) view.findViewById(R.id.tv_bill_money);
        tvPayMode = (TextView) view.findViewById(R.id.tv_paymode);
        tvSerialNumber = (TextView) view.findViewById(R.id.tv_serial_number);
        tvCashierName = (TextView) view.findViewById(R.id.tv_cashier);
        tvWaiterName = (TextView) view.findViewById(R.id.tv_waiter);
        tvIsOpenInvoice = (TextView) view.findViewById(R.id.tv_invoice);
        tvCloseTime = (TextView) view.findViewById(R.id.tv_close_time);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void initData(OrderEntity orderEntity) {
        mOrderEntity = orderEntity;
        if (mOrderEntity == null) {
            tvOrderNumber.setText("NO.");
            tvTableNumber.setText("座号：");
            tvMealCount.setText("人数：");
            tvDate.setText("");
            tvTotal.setText("共   项");
            tvBillMoney.setText("合计：");
            tvPayMode.setText("");
            tvSerialNumber.setText("");
            tvCashierName.setText("");
            tvWaiterName.setText("");
            tvIsOpenInvoice.setText("");
            tvCloseTime.setText("");
            adapter.updateData(null,1,false);
        } else {
            tvOrderNumber.setText("NO." + mOrderEntity.getOrderNumber1());
            tvTableNumber.setText("座号：" + DBHelper.getInstance(getActivity().getApplicationContext()).getTableNameByTableId(mOrderEntity.getTableId()));
            tvMealCount.setText(mOrderEntity.getOrderGuests() == null ? "人数：>=1":"人数："+mOrderEntity.getOrderGuests());
            tvDate.setText(CustomMethod.parseTime(mOrderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
            tvTotal.setText("共" + DBHelper.getInstance(getActivity().getApplicationContext()).getOrderedDishCountByOrderId(mOrderEntity.getOrderId()) + "项");
            tvBillMoney.setText("合计：" + AmountUtils.multiply(""+DBHelper.getInstance(getActivity().getApplicationContext()).getBillMoneyByOrderId(mOrderEntity.getOrderId(), 1),"1"));
            tvPayMode.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPayModeStrByOrderId(mOrderEntity.getOrderId()));
            tvSerialNumber.setText(mOrderEntity.getSerialNumber());
            tvCashierName.setText(mOrderEntity.getCashierId() != null ? DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(mOrderEntity.getCashierId()) : "");
            tvWaiterName.setText(mOrderEntity.getWaiterId() != null ? DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(mOrderEntity.getWaiterId()) : "");
            tvIsOpenInvoice.setText("");
            tvCloseTime.setText(CustomMethod.parseTime(mOrderEntity.getCloseTime(), "yyyy-MM-dd HH:mm"));
            adapter.updateData(mOrderEntity.getOrderId(),1,false);
        }
    }

    private void setAdapter() {
        adapter = new OrderDishAdapter(getActivity().getApplicationContext(), null, 1,false);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }
}
