package jufeng.juyancash.ui.fragment.snack;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.SnackSelectedDishAdapter;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.eventbus.OnTableCodeItemClickEvent;
import jufeng.juyancash.eventbus.SnackAddDishEvent;
import jufeng.juyancash.eventbus.SnackAddNewDishEvent;
import jufeng.juyancash.eventbus.SnackCallOrderEvent;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackCashierEvent;
import jufeng.juyancash.eventbus.SnackChangeTableCodeSuccessEvent;
import jufeng.juyancash.eventbus.SnackCheckAllOrderEvent;
import jufeng.juyancash.eventbus.SnackDishConfigConfirmEvent;
import jufeng.juyancash.eventbus.SnackDishConfigEvent;
import jufeng.juyancash.eventbus.SnackDishDetailEvent;
import jufeng.juyancash.eventbus.SnackEditDishEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailRefreshEvent;
import jufeng.juyancash.eventbus.SnackOrderMoneyChangedEvent;
import jufeng.juyancash.eventbus.SnackPrintKHLEvent;
import jufeng.juyancash.eventbus.SnackPrintKitchenEvent;
import jufeng.juyancash.eventbus.SnackRefreshDishMenuItemEvent;
import jufeng.juyancash.eventbus.SnackTaocanDetailEvent;
import jufeng.juyancash.eventbus.SnackUnreadMessageRefreshEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by Administrator102 on 2017/3/20.
 */

public class LFragmentSnackOrderDetail extends BaseFragment implements SnackSelectedDishAdapter.OnSelectedDishClickListener {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    //    @BindView(R.id.tv_serial_number)
//    TextView mTvSerialNumber;
    @BindView(R.id.tv_dish_count)
    TextView mTvDishCount;
    @BindView(R.id.tv_total_money)
    TextView mTvTotalMoney;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.ib_cancel_order)
    ImageButton mIbCancelOrder;
    @BindView(R.id.tv_cashier)
    TextView mTvCashier;
    @BindView(R.id.tv_print_kitchen)
    TextView mTvPrintKitchen;
    @BindView(R.id.tv_print_order)
    TextView mTvPrintOrder;
    @BindView(R.id.tv_retreat_dish)
    TextView mTvRetreatDish;
    @BindView(R.id.tv_present_dish)
    TextView mTvPresentDish;
    @BindView(R.id.tv_remind_dish)
    TextView mTvRemindDish;
    @BindView(R.id.tv_add_dish)
    TextView mTvAddDish;
    @BindView(R.id.tv_call_order)
    TextView mTvCallOrder;
    @BindView(R.id.tv_find_order)
    TextView mTvFindOrder;
    private String mOrderId;
    private Unbinder mUnbinder;
    private SnackSelectedDishAdapter mAdapter;

    public static LFragmentSnackOrderDetail newInstance(String orderId) {
        LFragmentSnackOrderDetail fragment = new LFragmentSnackOrderDetail();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        if(args != null) {
            String orderId = args.getString("orderId");
            if(orderId == null && mOrderId == null){
                return;
            }
            if(mOrderId != null && orderId != null && mOrderId.equals(orderId)){
                return;
            }
            this.mOrderId = orderId;
            Log.d("###", "切换订单："+args);
            initData();
            setAdapter();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            mOrderId = bundle.getString("orderId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack_order_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        initData();
        setAdapter();
    }

    @Override
    public void onDestroyView() {
        mRecyclerview.setAdapter(null);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @OnClick({R.id.tv_cashier, R.id.tv_print_kitchen, R.id.tv_print_order, R.id.tv_retreat_dish, R.id.tv_present_dish, R.id.tv_remind_dish, R.id.ib_cancel_order, R.id.tv_find_order, R.id.tv_add_dish, R.id.tv_call_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cashier:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                EventBus.getDefault().post(new SnackCashierEvent(mOrderId));
                OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
                if (orderEntity != null) {
                    if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                        DBHelper.getInstance(getContext()).deleteWxOrderMessage(mOrderId, 1);
                    }
                    DBHelper.getInstance(getContext()).deleteWxOrderMessage(mOrderId, 2);
                    EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
                }
                break;
            case R.id.tv_print_kitchen:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择订单后落单厨打");
                    return;
                }
                if (!DBHelper.getInstance(getContext()).isHasUnPrintedDish(mOrderId)) {
                    CustomMethod.showMessage(getContext(), "该订单没有未打印菜品");
                    return;
                }
                OrderEntity orderEntity1 = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
                if (orderEntity1 == null) {
                    return;
                }
                EventBus.getDefault().post(new SnackPrintKitchenEvent(mOrderId));
                break;
            case R.id.tv_print_order:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                EventBus.getDefault().post(new SnackPrintKHLEvent(mOrderId));
                break;
            case R.id.tv_retreat_dish:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasPrintedDish(mOrderId)) {
                    //没有已下单商品
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    EventBus.getDefault().post(new SnackEditDishEvent(mOrderId, 0));
                }
                break;
            case R.id.tv_present_dish:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasPrintedDish(mOrderId)) {
                    //没有已下单商品
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    //没有未下单商品
                    EventBus.getDefault().post(new SnackEditDishEvent(mOrderId, 1));
                }
                break;
            case R.id.tv_remind_dish:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                if (!DBHelper.getInstance(getActivity().getApplicationContext()).isHasPrintedDish(mOrderId)) {
                    //没有已下单商品
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("没有已下单商品，请先点餐并落单厨打！");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    //没有未下单商品
                    EventBus.getDefault().post(new SnackEditDishEvent(mOrderId, 2));
                }
                break;
            case R.id.ib_cancel_order:
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要结算的订单！");
                    return;
                }
                int ableCancle = DBHelper.getInstance(getActivity().getApplicationContext()).cancleSnackOrder(mOrderId);
                switch (ableCancle) {
                    case 0:
                        CustomMethod.showMessage(getContext(), "已有打印的商品，无法取消订单！");
                        break;
                    case 1:
                        //取消成功
                        EventBus.getDefault().post(new SnackCancelOrderSuccessEvent());
                        break;
                    case 2:
                        CustomMethod.showMessage(getContext(), "取消订单失败，请重新尝试！");
                        break;
                    case 3:
                        CustomMethod.showMessage(getContext(), "该笔订单已有支付，无法取消订单！");
                        break;
                    case 4:
                        //微信订单取消
                        wxOrderCancle(mOrderId);
                        break;
                }
                break;
            case R.id.tv_call_order:
                //叫号
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择订单后再进行叫号操作");
                    break;
                }
                EventBus.getDefault().post(new SnackCallOrderEvent(mOrderId));
                break;
            case R.id.tv_find_order:
                //找单
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择订单后再进行找单操作");
                    break;
                }
                EventBus.getDefault().post(new SnackCheckAllOrderEvent(mOrderId));
                break;
            case R.id.tv_add_dish:
                //加菜
                if (mOrderId == null) {
                    CustomMethod.showMessage(getContext(), "请选择订单后再进行加菜操作");
                    break;
                }
                EventBus.getDefault().post(new SnackAddDishEvent(mOrderId));
                break;
        }
    }

    //微信点餐取消
    private void wxOrderCancle(final String orderId) {
        showLoadingAnim("正在取消微信订单...");
        String partnerCode = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", null);
        long ts = System.currentTimeMillis();
        String data = orderId;
        String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("id", data);
        map.put("ts", String.valueOf(ts));
        map.put("sign", sign);
        VolleyRequest.RequestPost(getContext(), getContext().getResources().getString(R.string.WXORDER_CANCLE), orderId, map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "微信点餐取消：" + arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0 || publicModule.getCode() == -1) {
                        DBHelper.getInstance(getContext()).confirmWXCancleTable(orderId);
                        EventBus.getDefault().post(new SnackCancelOrderSuccessEvent());
                    } else {
                        String message = publicModule.getMessage() == null ? "微信订单取消失败，请重新尝试" : publicModule.getMessage();
                        CustomMethod.showMessage(getContext(), message);
                    }
                } catch (Exception e) {
                    CustomMethod.showMessage(getContext(), "微信订单取消失败，请重新尝试");
                }
                hideLoadingAnim();
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(), "微信订单取消失败，请重新尝试");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initData() {
        if (mOrderId != null) {
            double count = DBHelper.getInstance(getContext()).getDishCountByOrderId(mOrderId);
            double totalMoney = DBHelper.getInstance(getContext()).getBillMoneyByOrderId(mOrderId);
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
            if (orderEntity != null) {
                mTvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
                mTvDishCount.setText(AmountUtils.multiply("" + count, "1"));
                mTvTotalMoney.setText(AmountUtils.multiply("" + totalMoney, "1"));
                EventBus.getDefault().post(new SnackOrderDetailChangeEvent(mOrderId, 1));
            } else {
                mTvOrderNumber.setText("");
                mTvDishCount.setText("0");
                mTvTotalMoney.setText("0.00");
            }
            mIbCancelOrder.setVisibility(ImageButton.VISIBLE);
        } else {
            mTvOrderNumber.setText("");
            mTvDishCount.setText("0");
            mTvTotalMoney.setText("0.00");
            mIbCancelOrder.setVisibility(ImageButton.GONE);
        }
    }

    private void setAdapter() {
        if(mAdapter == null) {
            mAdapter = new SnackSelectedDishAdapter(getActivity().getApplicationContext(), mOrderId);
            mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setItemAnimator(null);
            mRecyclerview.setHasFixedSize(true);
            mAdapter.setOnSelectedDishClickListener(this);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    OrderDishEntity orderDishEntity = (OrderDishEntity) viewHolder.itemView.getTag();
                    if (orderDishEntity != null && orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 1) {
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
                    if (orderDishEntity.getType() == 0) {
                        DBHelper.getInstance(getContext()).deleteOrderDishById(orderDishEntity.getOrderDishId());
                    } else {
                        DBHelper.getInstance(getContext()).deleteTaocan(orderDishEntity);
                    }
                    mAdapter.deleteItem(orderDishEntity.getOrderDishId());
                    refreshBill();
                    EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
                }
            });
            itemTouchHelper.attachToRecyclerView(mRecyclerview);
        }else{
            mAdapter.updateData(mOrderId);
        }
    }

    //刷新账单
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void refreshBill() {
        //菜品数量
        double dishCount = mOrderId == null ? 0 : DBHelper.getInstance(getContext()).getDishCountByOrderId(mOrderId);
        //账单金额
        double billMoney = mOrderId == null ? 0.0 : DBHelper.getInstance(getContext()).getBillMoneyByOrderId(mOrderId);
        mTvDishCount.setText(AmountUtils.multiply("" + dishCount, "1"));
        mTvTotalMoney.setText(AmountUtils.multiply("" + billMoney, "1"));
        EventBus.getDefault().post(new SnackOrderMoneyChangedEvent(mOrderId));
        EventBus.getDefault().post(new SnackOrderDetailChangeEvent(mOrderId, 1));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onSelectedDishClicked(OrderDishEntity orderDishEntity) {
        if (orderDishEntity != null) {
            if (orderDishEntity.getIsFromWX() != null && orderDishEntity.getIsFromWX() == 1 && orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 0) {
                //微信点的菜品，并且未下单
                CustomMethod.showMessage(getContext(), "微信菜品需要下单后才能查看详情");
            } else {
                if (orderDishEntity.getType() == 0) {//非套餐
                    if (orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 1) {
                        EventBus.getDefault().post(new SnackDishDetailEvent(orderDishEntity.getOrderDishId()));
                    } else {
                        EventBus.getDefault().post(new SnackDishConfigEvent(orderDishEntity.getOrderDishId(), orderDishEntity.getDishId()));
                    }
                } else {//套餐
                    EventBus.getDefault().post(new SnackTaocanDetailEvent(orderDishEntity));
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onReduce(OrderDishEntity orderDishEntity) {
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onAdd(OrderDishEntity orderDishEntity) {
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddNewDish(SnackAddNewDishEvent event) {
        if (event != null && mOrderId != null && event.getDishId() != null) {
            OrderDishEntity orderDishEntity = DBHelper.getInstance(getContext()).insertSnackNewDish(mOrderId, event.getDishId());
            if (orderDishEntity != null) {
                mAdapter.addItem(orderDishEntity);
                refreshBill();
            }
            try {
                mRecyclerview.scrollToPosition(0);
            } catch (Exception e) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishConfigConfirm(SnackDishConfigConfirmEvent event) {
        if (event == null || mOrderId == null) {
            return;
        }
        OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).insertSnackNewDish(mOrderId, event.getDishId(), event.getPracticeId(), event.getSpecifyId(), event.getOrderDishId(), event.getNote(), event.getCount(), event.getDishSelectedMaterialEntities());
        if (event.getOrderDishId() != null) {
            //修改菜品
            mAdapter.changeItem(orderDishEntity);
        } else {
            //添加新的菜品
            try {
                mRecyclerview.scrollToPosition(0);
            } catch (Exception e) {

            }
            mAdapter.addItem(orderDishEntity);
        }
        refreshBill();
        EventBus.getDefault().post(new SnackRefreshDishMenuItemEvent(orderDishEntity.getDishId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresh(SnackOrderDetailRefreshEvent event) {
        if (event != null && mOrderId != null) {
            refreshBill();
            mAdapter.updateData(mOrderId);
//            if (mTvSerialNumber.getText() == null || mTvSerialNumber.getText().toString().isEmpty()) {
//                OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
//                if (orderEntity != null && orderEntity.getTableId() != null) {
//                    mTvSerialNumber.setText(orderEntity.getTableId());
//                }
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTableCodeItemClick(OnTableCodeItemClickEvent event) {
        if (event != null && event.getTableCode() != null) {
            if (mOrderId == null) {
                return;
            }
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
            if (orderEntity != null && orderEntity.getSerialNumber() != null && orderEntity.getTableId() != null && !orderEntity.getTableId().isEmpty()) {
                //当前订单已经分配了牌号并且已经落单厨打，不允许更换牌号
                CustomMethod.showMessage(getContext(), "该订单已经分配牌号并且落单厨打，不允许更换牌号！");
            } else if (orderEntity != null) {
                DBHelper.getInstance(getContext()).changeTableCode(mOrderId, event.getTableCode());
//                mTvSerialNumber.setText(event.getTableCode());
                EventBus.getDefault().post(new SnackChangeTableCodeSuccessEvent());
            }
        }
    }
}
