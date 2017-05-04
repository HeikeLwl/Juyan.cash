package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectedDishAdapter;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.eventbus.OnNormalDishClickEvent;
import jufeng.juyancash.eventbus.OnTaocanDetailClickEvent;
import jufeng.juyancash.eventbus.SnackAddNewDishEvent;
import jufeng.juyancash.eventbus.SnackDishConfigConfirmEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackRefreshDishMenuItemEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.ConfigDialogFragment;
import jufeng.juyancash.ui.customview.DetailDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.ActivityIntentUtil;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/21 0021.
 */
public class LOrderDishFragment extends BaseFragment implements View.OnClickListener, SelectedDishAdapter.OnSelectedDishClickListener {
    private Button btnKichen, btnCasher, btnConfirm;
    private TextView tvSeatNumber, tvSeatCount, tvCreateTime, tvDishCount, tvBillMoney, tvOrderNumber;
    private RecyclerView mRecyclerView;
    private MainFragmentListener mOnOrderDishClickListener;
    private SelectedDishAdapter adapter;
    private String tableId, orderId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //新添加的菜品
                    OrderDishEntity orderDishEntity = msg.getData().getParcelable("orderDishEntity");
                    adapter.addItem(orderDishEntity);
                    mRecyclerView.scrollToPosition(0);
                    refreshBill();
                    break;
                case 1:
                    //修改的菜品
                    String dishId1 = msg.getData().getString("dishId");
                    String practiceId = msg.getData().getString("practiceId");
                    String specifyId = msg.getData().getString("specifyId");
                    String orderDishId = msg.getData().getString("orderDishId");
                    String note = msg.getData().getString("note");
                    double count = msg.getData().getDouble("count");
                    OrderDishEntity orderDishEntity1 = DBHelper.getInstance(getActivity().getApplicationContext()).insertNewDish(orderId, dishId1, practiceId, specifyId, orderDishId, note, count, null);
                    if (orderDishId == null) {
                        adapter.addItem(orderDishEntity1);
                        mRecyclerView.scrollToPosition(0);
                    } else {
                        adapter.changeItem(orderDishEntity1);
                    }
                    refreshBill();
                    break;
                case 2://删除菜品
                    String orderDishId1 = msg.getData().getString("orderDishId");
                    DBHelper.getInstance(getActivity().getApplicationContext()).deleteOrderDishById(orderDishId1);
                    adapter.deleteItem(orderDishId1);
                    refreshBill();
                    break;
                case 3:
                    String orderId1 = msg.getData().getString("orderId");
                    tableId = msg.getData().getString("tableId");
                    orderId = orderId1;
                    adapter.updateData(orderId);
                    refreshData();
                    refreshBill();
                    break;
                case 4://修改套餐
                    OrderDishEntity orderDishEntity2 = msg.getData().getParcelable("orderDishEntity");
                    adapter.changeItem(orderDishEntity2);
                    refreshBill();
                    break;
                case 5://删除套餐
                    OrderDishEntity orderDishEntity3 = msg.getData().getParcelable("orderDishEntity");
                    adapter.deleteItem(orderDishEntity3.getOrderDishId());
                    refreshBill();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setOrderHandler(mHandler);
        try {
            mOnOrderDishClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNormalDish(OnNormalDishClickEvent event) {
        if (event != null && event.getOrderDishEntity() != null) {
            adapter.addItem(event.getOrderDishEntity());
            mRecyclerView.scrollToPosition(0);
            refreshBill();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_bill_layout, container, false);
        initView(mView);
        refreshData();
        refreshBill();
        setAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvSeatNumber = (TextView) view.findViewById(R.id.tv_seat_number);
        tvSeatCount = (TextView) view.findViewById(R.id.tv_seat_count);
        tvCreateTime = (TextView) view.findViewById(R.id.tv_create_time);
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvDishCount = (TextView) view.findViewById(R.id.tv_dish_count);
        tvBillMoney = (TextView) view.findViewById(R.id.tv_bill_money);
        btnKichen = (Button) view.findViewById(R.id.btn_kitchen);
        btnCasher = (Button) view.findViewById(R.id.btn_casher);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        tableId = getArguments().getString("tableId");
        orderId = getArguments().getString("orderId");
    }

    private void refreshData() {
        if (tableId != null) {
            TableEntity tableEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneTableData(tableId);
            tvSeatNumber.setText("座号：" + tableEntity.getTableName());
            OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(orderId);
            if (orderEntity != null) {
                tvOrderNumber.setText("单号：" + orderEntity.getOrderNumber1());
                tvSeatCount.setText("人数：" + orderEntity.getOrderGuests());
                Date date = new Date(orderEntity.getOpenTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tvCreateTime.setText(sdf.format(date));
            }
        }
    }

    private void setAdapter() {
        adapter = new SelectedDishAdapter(getActivity().getApplicationContext(), orderId);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setListener() {
        adapter.setOnSelectedDishClickListener(this);
        btnKichen.setOnClickListener(this);
        btnCasher.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("###", "onScrolled: " + dx + "," + dy);
                mOnOrderDishClickListener.onOrderDishScroll(dy);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                OrderDishEntity orderDishEntity = (OrderDishEntity) viewHolder.itemView.getTag();
                if (orderDishEntity != null && orderDishEntity.getIsOrdered() != 0) {
                    return 0;
                } else {
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
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                OrderDishEntity orderDishEntity = (OrderDishEntity) viewHolder.itemView.getTag();
                DBHelper.getInstance(getActivity().getApplicationContext()).deleteOrderDishById(orderDishEntity.getOrderDishId());
                adapter.deleteItem(orderDishEntity.getOrderDishId());
                refreshBill();
                EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    //落单厨打
    private void printKitchen() {
        if (orderId != null) {
            boolean isAddDish = DBHelper.getInstance(getContext().getApplicationContext()).isHasOrderedDish(orderId);
            ArrayList<PrintDishBean> printDishBeenes = DBHelper.getInstance(getActivity().getApplicationContext()).orderDish(orderId);
            if (printDishBeenes.size() > 0) {
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getContext().getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (PrintDishBean printDishBean :
                            printDishBeenes) {
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        mOnOrderDishClickListener.printKitchen(printDishBeenes1, orderId, printKitchen, isAddDish);
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity != null && printCashierEntity.getIsPrintDish() == 1) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (PrintDishBean printDishBean :
                            printDishBeenes) {
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        mOnOrderDishClickListener.printKitchen(printDishBeenes1, orderId, null, isAddDish);
                    }
                }

                adapter.updateData(orderId);
            }

            if (printDishBeenes.size() > 0) {
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity != null && printCashierEntity.getIsPrintVoucher() == 1) {
                    if (printCashierEntity.getVoucherType() == 1 && printCashierEntity.getVoucherIp() != null && printCashierEntity.getVoucherIp().length() > 0) {//网口打印
                        mOnOrderDishClickListener.printXFDL(printDishBeenes, orderId, printCashierEntity.getVoucherIp());
                    } else if (printCashierEntity.getVoucherType() == 0) {//usb打印
                        mOnOrderDishClickListener.printXFDL(printDishBeenes, orderId, null);
                    }
                }

                if (printCashierEntity != null && printCashierEntity.getIsPrintHuacai() == 1) {
                    if (printCashierEntity.getHuacaiType() == 1 && printCashierEntity.getHuacaiIp() != null && printCashierEntity.getHuacaiIp().length() > 0) {
                        //网口打印
                        mOnOrderDishClickListener.printHCL(printDishBeenes, orderId, printCashierEntity.getHuacaiIp());
                    } else if (printCashierEntity.getHuacaiType() == 0) {//usb打印
                        mOnOrderDishClickListener.printHCL(printDishBeenes, orderId, null);
                    }
                }
            }
            mOnOrderDishClickListener.refreshStock();
        }
    }

    //前台打印
    private void printCasher() {
        mOnOrderDishClickListener.printOrder(orderId, 0);
    }

    //确认
    private void onConfirm() {
        mOnOrderDishClickListener.onOrderDishConfirm();
    }

    //刷新账单
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void refreshBill() {
        //菜品数量
        double dishCount = DBHelper.getInstance(getActivity().getApplicationContext()).getDishCountByOrderId(orderId);
        //账单金额
        double billMoney = DBHelper.getInstance(getActivity().getApplicationContext()).getBillMoneyByOrderId(orderId);
        tvDishCount.setText("共" + AmountUtils.multiply("" + dishCount, "1") + "项");
        tvBillMoney.setText("合计：￥" + billMoney);
        EventBus.getDefault().post(new SnackOrderDetailChangeEvent(orderId, 1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kitchen:
                if (orderId == null) {
                    CustomMethod.showMessage(getContext(), "落单厨打失败");
                    return;
                }
                if (!DBHelper.getInstance(getContext()).isHasUnOrderedDish(orderId)) {
                    CustomMethod.showMessage(getContext(), "没有未下单菜品");
                    return;
                }
                new MyAsyncTask().execute();
                break;
            case R.id.btn_casher:
                if (orderId == null) {
                    CustomMethod.showMessage(getContext(), "前台打印失败");
                    return;
                }
                OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(orderId);
                if (orderEntity == null) {
                    CustomMethod.showMessage(getContext(), "前台打印失败");
                    return;
                }
                if (orderEntity.getSerialNumber() == null) {
                    CustomMethod.showMessage(getContext(), "请先落单厨打，再进行前台打印");
                    return;
                }
                printCasher();
                break;
            case R.id.btn_confirm:
                onConfirm();
                break;
        }
    }

    @Override
    public void onSelectedDishClicked(OrderDishEntity orderDishEntity) {
//        mOnOrderDishClickListener.dishDetail(orderDishEntity.getOrderDishId());
        if (orderDishEntity != null) {
            if (orderDishEntity.getIsFromWX() != null && orderDishEntity.getIsFromWX() == 1 && orderDishEntity.getIsOrdered() == 0) {
                //微信点的菜品，并且未下单
                CustomMethod.showMessage(getContext(), "微信菜品需要下单后才能查看详情");
            } else {
                if (orderDishEntity.getType() == 0) {//非套餐
                    if (orderDishEntity.getIsOrdered() == 0) {
                        ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", orderId);
                        bundle.putString("orderDishId", orderDishEntity.getOrderDishId());
                        bundle.putString("dishId", orderDishEntity.getDishId());
                        configPopupWindow.setArguments(bundle);
                        configPopupWindow.show(getFragmentManager(), "config_dialog_fragment");
//                        EventBus.getDefault().post(new SnackDishDetailEvent(orderDishEntity.getOrderDishId()));
                    } else if (orderDishEntity.getIsOrdered() == 1 || orderDishEntity.getIsOrdered() == -2) {
                        DetailDialogFragment dialogFragment = new DetailDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("orderDishId", orderDishEntity.getOrderDishId());
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getFragmentManager(), "dishdetaildialog");
//                        EventBus.getDefault().post(new SnackDishConfigEvent(orderDishEntity.getOrderDishId(), orderDishEntity.getDishId()));
                    }
                } else {//套餐
                    EventBus.getDefault().post(new OnTaocanDetailClickEvent(orderDishEntity, orderDishEntity.getDishId(), orderId, ActivityIntentUtil.FRAGMENT_ORDERDISH, tableId));
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onReduce(OrderDishEntity orderDishEntity) {
//        mOnOrderDishClickListener.reduceDish(orderDishEntity);
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onAdd(OrderDishEntity orderDishEntity) {
//        mOnOrderDishClickListener.addDish(orderDishEntity);
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }

    @Override
    public void changeChildOrder(String orderId) {

    }

    class MyAsyncTask extends AsyncTask<String, Integer, Object> {
        @Override
        protected void onPreExecute() {
            showLoadingAnim("打印中。。。");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            printKitchen();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            hideLoadingAnim();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddNewDish(SnackAddNewDishEvent event) {
        if (event != null && orderId != null && event.getDishId() != null) {
            OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).insertNewDish(orderId, event.getDishId());
            adapter.addItem(orderDishEntity);
            refreshBill();
            try {
                mRecyclerView.scrollToPosition(0);
            } catch (Exception e) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishConfigConfirm(SnackDishConfigConfirmEvent event) {
        if (event == null || orderId == null) {
            return;
        }
        OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).insertNewDish(orderId, event.getDishId(), event.getPracticeId(), event.getSpecifyId(), event.getOrderDishId(), event.getNote(), event.getCount(), event.getDishSelectedMaterialEntities());
        if (event.getOrderDishId() != null) {
            //修改菜品
            adapter.changeItem(orderDishEntity);
        } else {
            //添加新的菜品
            try {
                mRecyclerView.scrollToPosition(0);
            } catch (Exception e) {

            }
            adapter.addItem(orderDishEntity);
        }
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }
}
