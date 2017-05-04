package jufeng.juyancash.ui.fragment.snack;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.bean.SnackOrderItemClickEvent;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.eventbus.LoadingDialogEvent;
import jufeng.juyancash.eventbus.PrintHCLEvent;
import jufeng.juyancash.eventbus.PrintKitchenEvent;
import jufeng.juyancash.eventbus.PrintLabelEvent;
import jufeng.juyancash.eventbus.PrintXFDLEvent;
import jufeng.juyancash.eventbus.RefreshStockEvent;
import jufeng.juyancash.eventbus.SnackCallOrderEvent;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackCashierOverEvent;
import jufeng.juyancash.eventbus.SnackConfigDishEvent;
import jufeng.juyancash.eventbus.SnackDishConfigEvent;
import jufeng.juyancash.eventbus.SnackDishDetailEvent;
import jufeng.juyancash.eventbus.SnackNewOrderEvent;
import jufeng.juyancash.eventbus.SnackOpenOrderedTaocanEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailRefreshEvent;
import jufeng.juyancash.eventbus.SnackOrderListRefreshEvent;
import jufeng.juyancash.eventbus.SnackPrintKitchenEvent;
import jufeng.juyancash.eventbus.SnackTaocanClickEvent;
import jufeng.juyancash.eventbus.SnackUnreadMessageRefreshEvent;
import jufeng.juyancash.eventbus.TableCodeCallEvent;
import jufeng.juyancash.ui.activity.DifferentDisplay;
import jufeng.juyancash.ui.customview.ConfigDialogFragment;
import jufeng.juyancash.ui.customview.CustomeSnackOrderMessageDialog;
import jufeng.juyancash.ui.customview.DetailDialogFragment;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/3/20.
 */

public class LFragmentSnack extends BaseFragment {
    @BindView(R.id.tv_new_order)
    public TextView tvNewOrder;
    @BindView(R.id.tv_message_count)
    public TextView tvUnreadMessage;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroyView();
    }

//    //打开订单列表页
//    private void openOrderList(final String orderId){
//        LFragmentSnackOrderList fragmentSnackOrderList = findChildFragment(LFragmentSnackOrderList.class);
//        if(fragmentSnackOrderList == null){
//            popToChild(LFragmentSnackOrderList.class, false, new Runnable() {
//                @Override
//                public void run() {
//                    start(LFragmentSnackOrderList.newInstance(orderId));
//                }
//            });
//        }else{
//            Bundle newBundle = new Bundle();
//            newBundle.putString("orderId", orderId);
//            fragmentSnackOrderList.putNewBundle(newBundle);
//            start(fragmentSnackOrderList,SupportFragment.SINGLETASK);
//        }
//    }
//
//    //打开菜单页
//    private void openDishMenu(final String orderId){
//        LFragmentSnackDishMenu fragmentSnackDishMenu = findChildFragment(LFragmentSnackDishMenu.class);
//        if(fragmentSnackDishMenu == null){
//            popToChild(LFragmentSnackOrderList.class, false, new Runnable() {
//                @Override
//                public void run() {
//                    start(LFragmentSnackDishMenu.newInstance(orderId));
//                }
//            });
//        }else{
//            Bundle newBundle = new Bundle();
//            newBundle.putString("orderId",orderId);
//            fragmentSnackDishMenu.putNewBundle(newBundle);
//            start(fragmentSnackDishMenu,SupportFragment.SINGLETASK);
//        }
//    }
//
//    //打开搜索菜品页
//    private void openSearchDish(final String orderId){
//        LFragmentSnackSearchDish fragment = findChildFragment(LFragmentSnackSearchDish.class);
//        if(fragment == null){
//            popToChild(LFragmentSnackOrderList.class, false, new Runnable() {
//                @Override
//                public void run() {
//                    start(LFragmentSnackSearchDish.newInstance(orderId));
//                }
//            });
//        }else{
//            Bundle newBundle = new Bundle();
//            newBundle.putString("orderId",orderId);
//            fragment.putNewBundle(newBundle);
//            start(fragment,SupportFragment.SINGLETASK);
//        }
//    }
//
//    //打开套餐详情页
//    private void openTaocanDetail(final OrderDishEntity orderDishEntity){
//        LFragmentSnackTaocanDetail fragment = findChildFragment(LFragmentSnackTaocanDetail.class);
//        if(fragment == null){
//            popToChild(LFragmentSnackOrderList.class, false, new Runnable() {
//                @Override
//                public void run() {
//                    start(LFragmentSnackTaocanDetail.newInstance(orderDishEntity));
//                }
//            });
//        }else{
//            Bundle newBundle = new Bundle();
//            newBundle.putParcelable("orderDishEntity",orderDishEntity);
//            fragment.putNewBundle(newBundle);
//            start(fragment,SupportFragment.SINGLETASK);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCallOrder(SnackCallOrderEvent event) {
        if (event == null || event.getOrderId() == null) {
            return;
        }
        try {
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(event.getOrderId());
            if (!TextUtils.isEmpty(orderEntity.getSerialNumber())) {
                if (!TextUtils.isEmpty(orderEntity.getTableId())) {
                    EventBus.getDefault().post(new TableCodeCallEvent(orderEntity.getTableId()));
                } else if (!TextUtils.isEmpty(orderEntity.getOrderNumber1() + "")) {
                    EventBus.getDefault().post(new TableCodeCallEvent(orderEntity.getOrderNumber1() + ""));
                }
            } else {
                CustomMethod.showMessage(getContext(), "请先下单后才能叫号！");
            }
        } catch (Exception e) {

        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    @OnClick(R.id.tv_add_table_code)
//    public void onClick() {
//        hideSoftKeyboard(mEtTableCode);
//        if (mEtTableCode.getText() != null && !mEtTableCode.getText().toString().isEmpty()) {
//            boolean result = DBHelper.getInstance(getContext()).addCustomTableCode(mEtTableCode.getText().toString());
//            if (!result) {
//                CustomMethod.showMessage(getContext(), "该牌号已经存在，请重新添加");
//                return;
//            }
//            EventBus.getDefault().post(new AddTableCodeSuccessEvent(mEtTableCode.getText().toString()));
//        } else {
//            CustomMethod.showMessage(getContext(), "请填写需要自定义的牌号后，再进行添加操作！");
//            return;
//        }
//    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.container_center, LFragmentSnackRight.newInstance());
            loadRootFragment(R.id.container_right, LFragmentSnackLeft.newInstance());
            tvUnreadMessage.setTag(null);
            setTvUnreadMessage();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUnreadMessageRefresh(SnackUnreadMessageRefreshEvent event) {
        if (event != null) {
            setTvUnreadMessage();
        }
    }

    public void setTvUnreadMessage() {
        int count = DBHelper.getInstance(getContext()).getAllWxOrderMessageCount();
        if (count > 0) {
            tvUnreadMessage.setText(count + "条消息");
        } else {
            tvUnreadMessage.setText("暂无消息");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @OnClick(R.id.tv_new_order)
    public void onNewOrderClick() {
        String orderId = DBHelper.getInstance(getContext()).createSnackOrder();
        tvUnreadMessage.setTag(orderId);
        EventBus.getDefault().post(new SnackNewOrderEvent(orderId));
//        replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(orderId), false);
//        replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
    }

    @OnClick(R.id.tv_message_count)
    public void onUnreadMessageClick() {
        final CustomeSnackOrderMessageDialog dialog = new CustomeSnackOrderMessageDialog(getContext());
        dialog.setOnOrderMessageClearListener(new CustomeSnackOrderMessageDialog.OnOrderMessageClearListener() {
            @Override
            public void onClearAllOrderMessage() {
                setTvUnreadMessage();
            }
        });
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSearchDishBack(SearchDishBackEvent event) {
//        replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSnackDishDetail(SnackDishDetailEvent event) {
        if (event != null && event.getOrderDishId() != null) {
            DetailDialogFragment dialogFragment = new DetailDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("orderDishId",event.getOrderDishId());
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getFragmentManager(),"dishdetaildialog");
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishDetailEdit.newInstance(event.getOrderDishId()), false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishConfig(SnackDishConfigEvent event) {
        if (event != null && event.getOrderDishId() != null && event.getDishId() != null) {
            ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", (String) tvUnreadMessage.getTag());
            bundle.putString("orderDishId",event.getOrderDishId());
            bundle.putString("dishId",event.getDishId());
            configPopupWindow.setArguments(bundle);
            configPopupWindow.show(getFragmentManager(),"config_dialog_fragment");
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventTaocanDetail(SnackTaocanDetailEvent event) {
//        if (event != null && event.getOrderDishEntity() != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(event.getOrderDishEntity()), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderedTaocan.newInstance(event.getOrderDishEntity(), 1), false);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderDetailChange(SnackOrderDetailChangeEvent event) {
        if (event != null) {
            if (event.getOrderId() != null) {
                sendDifferBroadcast(event.getType(), event.getOrderId());
            } else {
                sendDifferBroadcast(0, null);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventConfigDish(SnackConfigDishEvent event) {
        if (event != null) {
            ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", (String) tvUnreadMessage.getTag());
            bundle.putString("orderDishId",event.getOrderDishId());
            bundle.putString("dishId",event.getDishId());
            configPopupWindow.setArguments(bundle);
            configPopupWindow.show(getFragmentManager(),"config_dialog_fragment");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanClick(SnackTaocanClickEvent event) {
        if (event != null && tvUnreadMessage.getTag() != null) {
            OrderDishEntity mOrderDishEntity = new OrderDishEntity();
            mOrderDishEntity.setOrderDishId(UUID.randomUUID().toString());
            mOrderDishEntity.setOrderId((String) tvUnreadMessage.getTag());
            mOrderDishEntity.setDishId(event.getTaocanEntity().getTaocanId());
            mOrderDishEntity.setIsOrdered(1);
            mOrderDishEntity.setIsPrint(0);
            mOrderDishEntity.setDishCount(1.0);
            mOrderDishEntity.setDishPrice(event.getTaocanEntity().getTaocanPrice());
            mOrderDishEntity.setDishName(event.getTaocanEntity().getTaocanName());
            mOrderDishEntity.setIsAbleDiscount(event.getTaocanEntity().getIsAbleDiscount());
            mOrderDishEntity.setOrderedTime(System.currentTimeMillis());
            mOrderDishEntity.setIsFromWX(0);
            mOrderDishEntity.setType(1);//设置类型为套餐
            //将套餐中必须商品加入数据库
            DBHelper.getInstance(getContext().getApplicationContext()).insertSnackDefaultTaocanDish((String) tvUnreadMessage.getTag(), mOrderDishEntity.getOrderDishId(), event.getTaocanEntity().getTaocanId(), -1);
            EventBus.getDefault().post(new SnackOpenOrderedTaocanEvent(mOrderDishEntity,0));
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(mOrderDishEntity), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderedTaocan.newInstance(mOrderDishEntity, 0), false);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventDishConfigCancel(SnackDishConfigCancelEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
//        }
//    }

    //客显界面
    private void sendDifferBroadcast(int type, String orderId) {
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type", type);
        intent.putExtra("orderId", orderId);
        intent.putExtra("isOpenJoinOrder", false);
        getContext().sendBroadcast(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSnackOrderItemClick(SnackOrderItemClickEvent event) {
        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(event.getOrderId()), false);
            tvUnreadMessage.setTag(event.getOrderId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPrintKitchen(final SnackPrintKitchenEvent event) {
        if (event != null && event.getOrderId() != null) {
            new PrintKitchenAsyncTask(event.getOrderId()).execute();
        }
    }

    class PrintKitchenAsyncTask extends AsyncTask<String, Integer, Object> {
        private String mOrderId;

        public PrintKitchenAsyncTask(String orderId) {
            this.mOrderId = orderId;
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        @Override
        protected void onPreExecute() {
            EventBus.getDefault().post(new LoadingDialogEvent(true, "打印中..."));
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            printKitchen(mOrderId);
            return null;
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            EventBus.getDefault().post(new LoadingDialogEvent(false, ""));
            EventBus.getDefault().post(new SnackOrderListRefreshEvent(mOrderId));
            EventBus.getDefault().post(new SnackOrderDetailRefreshEvent());
        }
    }

    //落单厨打
    @Subscribe(threadMode = ThreadMode.ASYNC)
    private void printKitchen(String mOrderId) {
        if (mOrderId != null) {
            boolean isAddDish = DBHelper.getInstance(getContext().getApplicationContext()).isHasPrintedDish(mOrderId);
            ArrayList<PrintDishBean> printDishBeenes = new ArrayList<>();
            printDishBeenes.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).SnackOrderDish(mOrderId));
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
                        EventBus.getDefault().post(new PrintKitchenEvent(printDishBeenes1, mOrderId, printKitchen, isAddDish));
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
                        EventBus.getDefault().post(new PrintKitchenEvent(printDishBeenes1, mOrderId, null, isAddDish));
                    }
                }
            }

            if (printDishBeenes.size() > 0) {
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity != null && printCashierEntity.getIsPrintVoucher() == 1) {
                    if (printCashierEntity.getVoucherType() == 1 && printCashierEntity.getVoucherIp() != null && printCashierEntity.getVoucherIp().length() > 0) {//网口打印
                        EventBus.getDefault().post(new PrintXFDLEvent(printDishBeenes, mOrderId, printCashierEntity.getVoucherIp()));
                    } else if (printCashierEntity.getVoucherType() == 0) {//usb打印
                        EventBus.getDefault().post(new PrintXFDLEvent(printDishBeenes, mOrderId, null));
                    }
                }

                if (printCashierEntity != null && printCashierEntity.getIsPrintHuacai() == 1) {
                    if (printCashierEntity.getHuacaiType() == 1 && printCashierEntity.getHuacaiIp() != null && printCashierEntity.getHuacaiIp().length() > 0) {
                        //网口打印
                        EventBus.getDefault().post(new PrintHCLEvent(printDishBeenes, mOrderId, printCashierEntity.getHuacaiIp()));
                    } else if (printCashierEntity.getHuacaiType() == 0) {//usb打印
                        EventBus.getDefault().post(new PrintHCLEvent(printDishBeenes, mOrderId, null));
                    }
                }
            }
            EventBus.getDefault().post(new RefreshStockEvent());
            OrderEntity orderEntity = DBHelper.getInstance(getContext()).getOneOrderEntity(mOrderId);
            if (orderEntity != null && orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
            }
            //标签打印
            int storeVersion = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion",0);
            if(storeVersion == 2){
                //奶茶版
                EventBus.getDefault().post(new PrintLabelEvent(printDishBeenes,orderEntity.getOrderId(),null));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelOrderSuccess(SnackCancelOrderSuccessEvent event) {
        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center,LFragmentSnackOrderList.newInstance(null),false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(null), false);
            tvUnreadMessage.setTag(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCashierOver(SnackCashierOverEvent event) {
        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackOrderList.newInstance(null), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(null), false);
            tvUnreadMessage.setTag(null);
            setTvUnreadMessage();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventCashierCancel(SnackCashierCancelEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackOrderList.newInstance((String) tvUnreadMessage.getTag()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventEditDish(SnackEditDishEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackEditOrderDish.newInstance(event.getType(), event.getOrderId()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventEditBack(SnackEditOrderDishBackEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(event.getOrderId()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventEditConfirm(SnackEditDishConfirmEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackEditDishOrder.newInstance(event.getOrderId(), event.getOrderDishEntities(), event.getType()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventEditOrderBack(SnackEditDishOrderBackEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackEditOrderDish.newInstance(event.getType(), event.getOrderId()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventEditOrderConfirm(SnackEditDishOrderConfirmEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance(event.getOrderId()), false);
//            if (event.getType() != 2) {
//                EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
//                EventBus.getDefault().post(new SnackOpenTopRightEvent());
//                EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
//                EventBus.getDefault().post(new SnackOrderListRefreshEvent(event.getOrderId()));
//            }
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventTaocanCancel(SnackTaocanCancelEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance((String) tvUnreadMessage.getTag()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventTaocanDelete(SnackTaocanDeleteEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance((String) tvUnreadMessage.getTag()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventTaocanConfirm(SnackTaocanConfirmEvent event) {
//        if (event != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackDishMenu.newInstance((String) tvUnreadMessage.getTag()), false);
//            replaceLoadRootFragment(R.id.container_right, LFragmentSnackOrderDetail.newInstance((String) tvUnreadMessage.getTag()), false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSnackOrderedTaocanItemClick(SnackTaocanOrderedDishClickEvent event) {
//        if (event != null && event.getOrderTaocanGroupDishEntity() != null) {
//            if (event.getOrderTaocanGroupDishEntity().getStatus() == 1) {
//                replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDishDetail.newInstance(event.getOrderDishEntity(), event.getOrderTaocanGroupDishEntity()), false);
//            } else {
//                replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDishConfig.newInstance(event.getOrderDishEntity(), event.getOrderTaocanGroupDishEntity()), false);
//            }
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSnackTaocanDishConfigCancel(SnackTaocanDishConfigCancelEvent event) {
//        if (event != null && event.getOrderDishEntity() != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(event.getOrderDishEntity()), false);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSnackTaocanDishConfigDelete(SnackTaocanDishConfigDeleteEvent event) {
//        if (event != null && event.getOrderDishEntity() != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(event.getOrderDishEntity()), false);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSnackTaocanDishConfigConfirm(SnackTaocanDishConfigConfirmEvent event) {
//        if (event != null && event.getOrderDishEntity() != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(event.getOrderDishEntity()), false);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventSnackTaocanDishDetailCloseEvent(SnackTaocanDishDetailCloseEvent event) {
//        if (event != null && event.getOrderDishEntity() != null) {
//            replaceLoadRootFragment(R.id.container_center, LFragmentSnackTaocanDetail.newInstance(event.getOrderDishEntity()), false);
//        }
//    }
}
