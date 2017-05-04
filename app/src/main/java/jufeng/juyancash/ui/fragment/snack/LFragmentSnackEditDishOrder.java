package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.RetreatOrderAdapter;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.SpecialEntity;
import jufeng.juyancash.eventbus.SnackEditDishOrderBackEvent;
import jufeng.juyancash.eventbus.SnackEditDishOrderConfirmEvent;
import jufeng.juyancash.eventbus.SnackRemindPrintEvent;
import jufeng.juyancash.eventbus.SnackRetreatPrintEvent;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeRadioGroup1;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

import static jufeng.juyancash.util.CustomMethod.showToast;

/**
 * Created by Administrator102 on 2016/8/4.
 */
public class LFragmentSnackEditDishOrder extends BaseFragment {
    @BindView(R.id.tv_reason_title)
    TextView mTvReasonTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
//    @BindView(R.id.tv_serial_number)
//    TextView mTvSerialNumber;
    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.crg_retreat)
    CustomeRadioGroup1 mCrgRetreat;
    @BindView(R.id.tv_more)
    TextView mmTvMore;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private RetreatOrderAdapter adapter;
    private String orderId;
    private ArrayList<OrderDishEntity> presentDishes;
    private int mType;
    private Unbinder mUnbinder;

    public static LFragmentSnackEditDishOrder newInstance(String orderId, ArrayList<OrderDishEntity> orderDishEntities, int type) {
        LFragmentSnackEditDishOrder fragment = new LFragmentSnackEditDishOrder();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelableArrayList("editDishes", orderDishEntities);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_snack_edit_order_layout, container, false);
        mUnbinder = ButterKnife.bind(this,mView);
        initData();
        setSpecial(mType);
        setAdapter();
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    private void initData() {
        orderId = getArguments().getString("orderId");
        presentDishes = getArguments().getParcelableArrayList("editDishes");
        this.mType = getArguments().getInt("type");
        OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(orderId);
        if (orderEntity != null) {
            mTvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
//            mTvSerialNumber.setText(orderEntity.getTableId() == null ? "" : orderEntity.getTableId());
            switch (mType) {
                case 0://退菜
                    mTvTitle.setText("退菜单");
                    mTvConfirm.setText("确认退菜");
                    mTvReasonTitle.setText("退菜原因");
                    break;
                case 1://赠菜
                    mTvTitle.setText("赠菜单");
                    mTvConfirm.setText("确认赠菜");
                    mTvReasonTitle.setText("赠菜原因");
                    break;
                case 2://催菜
                    mTvTitle.setText("催菜单");
                    mTvConfirm.setText("确认催菜");
                    mTvReasonTitle.setText("催菜原因");
                    break;
                default:
                    mTvTitle.setText("");
                    mTvConfirm.setText("");
                    break;
            }
        } else {
            mTvTitle.setText("");
            mTvConfirm.setText("");
            mTvOrderNumber.setText("");
        }
    }

    private void setAdapter() {
        adapter = new RetreatOrderAdapter(getActivity().getApplicationContext(), presentDishes);
        mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerview.setAdapter(adapter);
    }

    private void setSpecial(int type) {
        ArrayList<SpecialEntity> specialEntities = DBHelper.getInstance(getActivity().getApplicationContext()).queryAllSpecialByType(type);
        for (int i = 0; i < specialEntities.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(specialEntities.get(i).getSpecialName());
            radioButton.setTextColor(getResources().getColor(R.color.dark));
            radioButton.setId(i);
            radioButton.setTag(specialEntities.get(i).getSpecialId());
            mCrgRetreat.addView(radioButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @OnClick({R.id.tv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                EventBus.getDefault().post(new SnackEditDishOrderBackEvent(mType,orderId,presentDishes));
                break;
            case R.id.tv_confirm:
                if(mType == 2){
                    //催菜
                    confirm("");
                    return;
                }
                EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
                if(employeeEntity == null){
                    CustomMethod.showMessage(getContext(),"操作失败，请重新尝试！");
                    return;
                }
                if(isHasPermision(employeeEntity)){
                    confirm(employeeEntity.getEmployeeName());
                }else{
                    final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", mType+3);
                    dialog.setArguments(bundle);
                    dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if(employeeEntity == null){
                                dialog.dismiss();
                                CustomMethod.showMessage(getContext(),"操作失败，请稍后重试！");
                                return;
                            }
                            if(isHasPermision(employeeEntity)){
                                //该员工可以退菜，需要判断赠菜是否会不满足优惠券的使用
                                boolean canUseCoupon = DBHelper.getInstance(getContext().getApplicationContext()).canUseCouponPresent(orderId, presentDishes);
                                if (canUseCoupon) {
                                    //赠菜后订单金额仍然满足优惠券使用条件
                                    confirm(employeeEntity.getEmployeeName());
                                    dialog.dismiss();
                                } else {
                                    //赠菜后订单金额无法满足优惠券使用条件
                                    dialog.dismiss();
                                    CustomMethod.showMessage(getContext(), "赠菜后订单金额将不满足优惠券使用条件，请先取消优惠券的使用！");
                                }
                            }else{
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "");
                }
                break;
        }
    }

    private boolean isHasPermision(EmployeeEntity employeeEntity){
        boolean result = false;
        switch (mType){
            case 0://退菜
                result = employeeEntity.getAuthRetreat() == 1;
                break;
            case 1://赠菜
                result = employeeEntity.getAuthPresent() == 1;
                break;
            case 2://催菜
                result = true;
                break;
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void confirm(String employeeName){
        switch (mType){
            case 0:
                retreatDish(employeeName);
                break;
            case 1:
                presentDish();
                break;
            case 2:
                printRemindKitchen();
                break;
        }
        EventBus.getDefault().post(new SnackEditDishOrderConfirmEvent(mType,orderId,presentDishes));
    }

    private void retreatDish(String employeeName) {
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        boolean canUseCoupon = DBHelper.getInstance(getContext().getApplicationContext()).canUseCouponRetreat(orderId,presentDishes);
        if(canUseCoupon) {
            //退菜后优惠券仍然可以继续使用
            String reason = "无";
            if (mCrgRetreat.getCheckedRadioButtonId() >= 0) {
                RadioButton rb = (RadioButton) mCrgRetreat.findViewById(mCrgRetreat.getCheckedRadioButtonId());
                if (rb != null) {
                    reason = rb.getText().toString();
                }
            } else {
                reason = mmTvMore.getText() == null || mmTvMore.getText().toString().isEmpty() ? "无" : mmTvMore.getText().toString();
            }
            printRetreatKitchen(employeeName, reason);
            DBHelper.getInstance(getActivity().getApplicationContext()).retreatOrderDish(orderId, presentDishes);
            DBHelper.getInstance(getContext()).dealWithVoucher(orderEntity, 1);
        }else{
            //退菜后订单金额将不满足优惠券使用条件
            CustomMethod.showMessage(getContext(),"退菜后订单金额将不满足优惠券使用条件，请先取消优惠券的使用！");
        }
    }

    private void presentDish(){
        OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
        boolean canUseCoupon = DBHelper.getInstance(getContext().getApplicationContext()).canUseCouponPresent(orderId,presentDishes);
        if(canUseCoupon){
            //赠菜后订单金额仍然满足优惠券使用条件
            DBHelper.getInstance(getActivity().getApplicationContext()).presentOrderDish(orderId, presentDishes);
            DBHelper.getInstance(getContext()).dealWithVoucher(orderEntity, 1);
        }else{
            //赠菜后订单金额无法满足优惠券使用条件
            CustomMethod.showMessage(getContext(),"赠菜后订单金额将不满足优惠券使用条件，请先取消优惠券的使用！");
        }
    }


    //落单厨打
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void printRetreatKitchen(String employeeName, String reason) {
        if (orderId != null) {
            if (presentDishes.size() > 0) {
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getContext().getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            presentDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        EventBus.getDefault().post(new SnackRetreatPrintEvent(printDishBeenes1, orderId, printKitchen, employeeName, reason));
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity.getIsPrintDish() == 1) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            presentDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        EventBus.getDefault().post(new SnackRetreatPrintEvent(printDishBeenes1, orderId, null, employeeName, reason));
                    }
                }
            }
        } else {
            showToast(getActivity().getApplicationContext(), "退菜打印失败");
        }
    }

    //落单厨打
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void printRemindKitchen() {
        if (orderId != null) {
            if(presentDishes.size() > 0){
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getContext().getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            presentDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if(dishEntity != null){
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity,unitName,0,0,new int[]{100,100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if(orderDishEntity.getType() == 1){
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if(dishEntity1 != null){
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish,printDishBean,unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printKitchen, printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if(printDishBeenes1.size() > 0) {
                        EventBus.getDefault().post(new SnackRemindPrintEvent(printDishBeenes1,orderId,printKitchen,null));
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getContext().getApplicationContext()).getPrintCashierEntity();
                if(printCashierEntity.getIsPrintDish() == 1){
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            presentDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if(dishEntity != null){
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity,unitName,0,0,new int[]{100,100});
                        if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if(orderDishEntity.getType() == 1){
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getContext().getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if(dishEntity1 != null){
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish,printDishBean,unitName1);
                                    if (DBHelper.getInstance(getContext().getApplicationContext()).isPrint(printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if(printDishBeenes1.size() > 0) {
                        EventBus.getDefault().post(new SnackRemindPrintEvent(printDishBeenes1,orderId,null,null));
                    }
                }
            }
        } else {
            showToast(getActivity().getApplicationContext(), "催菜打印失败");
        }
    }
}
