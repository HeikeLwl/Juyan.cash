package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.myinterface.SelectTaocanFragmentListener;
import jufeng.juyancash.ui.activity.DifferentDisplay;
import jufeng.juyancash.util.ActivityIntentUtil;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/2/11.
 */

public class LFragmentSelectTaocan extends BaseFragment implements SelectTaocanFragmentListener{
    private LFragmentOrderedTaocan mLFragmentOrderedTaocan;
    private LFragmentTaocanDetail mLFragmentTaocanDetail;
    private LFragmentTaocanDishConfig mLFragmentTaocanDishConfig;
    private LFragmentTaocanDishDetail mLFragmentTaocanDishDetail;
    private final int TAOCAN_DETAIL = 0;
    private final int TAOCAN_DISH_CONFIG = 1;
    private final int TAOCAN_DISH_DETAIL = 2;
    private OrderDishEntity mOrderDishEntity;
    private String activityTag;
    private ArrayList<OrderTaocanGroupDishEntity> mOrderTaocanGroupDishEntities;//已点套餐内商品的原数据
    private int isDelete;
    private boolean b;
    private String taocanId;
    private String orderId;
    private MainFragmentListener mMainFragmentListener;
    private String tableId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_select_taocan, container, false);
        mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        activityTag = getArguments().getString("activity");
        taocanId = getArguments().getString("taocanId");
        orderId = getArguments().getString("orderId");
        tableId = getArguments().getString("tableId");
        initData();
        setLeftFragment();
        setRightFragment(TAOCAN_DETAIL, null,false);
        sendDifferBroadcast(0,null,false);
        return mView;
    }

    public void setNewParam(OrderDishEntity orderDishEntity,String activityTag,String taocanId,String orderId,String tableId){
        this.mOrderDishEntity = orderDishEntity;
        this.activityTag = activityTag;
        this.taocanId = taocanId;
        this.orderId = orderId;
        this.tableId = tableId;

        initData();
        setLeftFragment();
        setRightFragment(TAOCAN_DETAIL,null,true);
        sendDifferBroadcast(0,null,false);
    }

    private void initData() {
        isDelete = 1;
        mOrderTaocanGroupDishEntities = new ArrayList<>();
        if (mOrderDishEntity == null) {
            b = false;
            TaocanEntity taocanEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryTaocanById(taocanId);
            mOrderDishEntity = new OrderDishEntity();
            mOrderDishEntity.setOrderDishId(UUID.randomUUID().toString());
            mOrderDishEntity.setOrderId(orderId);
            mOrderDishEntity.setDishId(taocanId);
            mOrderDishEntity.setIsOrdered(0);
            mOrderDishEntity.setDishCount(1.0);
            mOrderDishEntity.setDishPrice(taocanEntity.getTaocanPrice());
            mOrderDishEntity.setDishName(taocanEntity.getTaocanName());
            mOrderDishEntity.setIsAbleDiscount(taocanEntity.getIsAbleDiscount());
            mOrderDishEntity.setOrderedTime(System.currentTimeMillis());
            mOrderDishEntity.setIsFromWX(0);
            mOrderDishEntity.setType(1);//设置类型为套餐
            isDelete = 0;
            //将套餐中必须商品加入数据库
            DBHelper.getInstance(getContext().getApplicationContext()).insertDefaultTaocanDish(orderId,mOrderDishEntity.getOrderDishId(), taocanId, -1);
        } else {
            b = true;
            mOrderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(mOrderDishEntity));
        }
    }

    private void setLeftFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mLFragmentOrderedTaocan == null) {
            mLFragmentOrderedTaocan = new LFragmentOrderedTaocan();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", mOrderDishEntity);
            bundle.putString("activity",activityTag);
            bundle.putInt("isDelete", isDelete);
            mLFragmentOrderedTaocan.setArguments(bundle);
            mLFragmentOrderedTaocan.setListener(this);
            ft.add(R.id.containerleft, mLFragmentOrderedTaocan);
        } else {
            mLFragmentOrderedTaocan.setNewParam(mOrderDishEntity,activityTag,isDelete);
            mLFragmentOrderedTaocan.setListener(this);
            ft.show(mLFragmentOrderedTaocan);
        }
        ft.commit();
    }

    private void setRightFragment(int tag, OrderTaocanGroupDishEntity orderTaocanGroupDishEntity,boolean isChanged) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideRightFragment(ft);
        switch (tag) {
            case TAOCAN_DETAIL://套餐详情
                if (mLFragmentTaocanDetail == null) {
                    mLFragmentTaocanDetail = new LFragmentTaocanDetail();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderDishEntity", mOrderDishEntity);
                    bundle.putString("activity",activityTag);
                    mLFragmentTaocanDetail.setArguments(bundle);
                    mLFragmentTaocanDetail.setOnTaocanItemClickListener(this);
                    ft.add(R.id.containerright, mLFragmentTaocanDetail);
                } else {
                    mLFragmentTaocanDetail.setOnTaocanItemClickListener(this);
                    if(isChanged) {
                        mLFragmentTaocanDetail.setNewParam(mOrderDishEntity, activityTag);
                    }
                    ft.show(mLFragmentTaocanDetail);
                }
                break;
            case TAOCAN_DISH_CONFIG://套餐内商品配置
                if (mLFragmentTaocanDishConfig == null) {
                    mLFragmentTaocanDishConfig = new LFragmentTaocanDishConfig();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderTaocanGroupDishEntity", orderTaocanGroupDishEntity);
                    mLFragmentTaocanDishConfig.setArguments(bundle);
                    mLFragmentTaocanDishConfig.setOnTaocanDishDetailEditClickListener(this);
                    ft.add(R.id.containerright, mLFragmentTaocanDishConfig);
                } else {
                    mLFragmentTaocanDishConfig.setOnTaocanDishDetailEditClickListener(this);
                    mLFragmentTaocanDishConfig.setNewParam(orderTaocanGroupDishEntity);
                    ft.show(mLFragmentTaocanDishConfig);
                }
                break;
            case TAOCAN_DISH_DETAIL://套餐内下单商品详情
                if (mLFragmentTaocanDishDetail == null) {
                    mLFragmentTaocanDishDetail = new LFragmentTaocanDishDetail();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderTaocanGroupDishEntity", orderTaocanGroupDishEntity);
                    mLFragmentTaocanDishDetail.setArguments(bundle);
                    mLFragmentTaocanDishDetail.setListener(this);
                    ft.add(R.id.containerright, mLFragmentTaocanDishDetail);
                } else {
                    mLFragmentTaocanDishDetail.setNewParam(orderTaocanGroupDishEntity);
                    mLFragmentTaocanDishDetail.setListener(this);
                    ft.show(mLFragmentTaocanDishDetail);
                }
                break;
        }
        ft.commit();
    }

    private void hideRightFragment(FragmentTransaction ft) {
        if (mLFragmentTaocanDetail != null) {
            ft.hide(mLFragmentTaocanDetail);
        }
        if (mLFragmentTaocanDishConfig != null) {
            ft.hide(mLFragmentTaocanDishConfig);
        }
        if (mLFragmentTaocanDishDetail != null) {
            ft.hide(mLFragmentTaocanDishDetail);
        }
    }

    @Override
    public void onTaocanCanle() {
        DBHelper.getInstance(getContext()).cancleTaocanEdit(mOrderDishEntity, mOrderTaocanGroupDishEntities);
        if(mMainFragmentListener != null){
            mMainFragmentListener.onTaocanCancle(tableId,orderId,activityTag);
        }
    }

    @Override
    public void onTaocanDelete() {
        if (mOrderDishEntity.getIsOrdered() == 1) {
            CustomMethod.showMessage(getContext(),"已有下单商品无法删除");
        } else {
            DBHelper.getInstance(getContext().getApplicationContext()).deleteTaocan(mOrderDishEntity);
            if(mMainFragmentListener != null){
                mMainFragmentListener.onTaocanDelete(tableId,orderId,mOrderDishEntity,activityTag);
            }
        }
    }

    @Override
    public void onTaocanConfirm() {
        if(DBHelper.getInstance(getContext().getApplicationContext()).getOrderedTaocanDish(mOrderDishEntity).size() > 0) {
            DBHelper.getInstance(getContext().getApplicationContext()).confirmTaocanEdit(mOrderDishEntity);
            if(activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
                if(mMainFragmentListener != null){
                    mMainFragmentListener.onTaocanChangeConfirm(tableId,orderId,mOrderDishEntity,activityTag);
                }
            }else if(activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)){
                if(b){
                    //修改
                    if(mMainFragmentListener != null){
                        mMainFragmentListener.onTaocanChangeConfirm(tableId,orderId,mOrderDishEntity,activityTag);
                    }
                }else{
                    //添加
                    if(mMainFragmentListener != null){
                        mMainFragmentListener.onTaocanAddConfirm(tableId,orderId,mOrderDishEntity,activityTag);
                    }
                }
            }
        }else{
            CustomMethod.showMessage(getContext(),"未选商品，无法执行此操作");
        }
    }

    @Override
    public void onTaocanItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        //加菜
        Message msg = new Message();
        msg.what = 1;
        msg.obj = orderTaocanGroupDishEntity;
        if(mLFragmentOrderedTaocan != null){
            mLFragmentOrderedTaocan.setNewMessage(msg);
        }
    }

    @Override
    public void onTaocanDishItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        if (orderTaocanGroupDishEntity.getStatus() == 1) {
            //已下单
            setRightFragment(TAOCAN_DISH_DETAIL, orderTaocanGroupDishEntity,false);
        } else {
            //未下单
            setRightFragment(TAOCAN_DISH_CONFIG, orderTaocanGroupDishEntity,false);
        }
    }

    @Override
    public void onTaocanDishAdd(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        if(mLFragmentOrderedTaocan != null){
            Message msg = new Message();
            msg.what = 2;
            mLFragmentOrderedTaocan.setNewMessage(msg);
        }
        if(mLFragmentTaocanDetail != null){
            Message msg = new Message();
            msg.what = 1;
            msg.obj = orderTaocanGroupDishEntity;
            mLFragmentTaocanDetail.setNewMessage(msg);
        }
    }

    @Override
    public void onTaocanDishReduce(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        if(mLFragmentOrderedTaocan != null){
            Message msg = new Message();
            msg.what = 2;
            mLFragmentOrderedTaocan.setNewMessage(msg);
        }
        if(mLFragmentTaocanDetail != null){
            Message msg = new Message();
            msg.what = 2;
            msg.obj = orderTaocanGroupDishEntity;
            mLFragmentTaocanDetail.setNewMessage(msg);
        }
    }

    @Override
    public void onTaocanDishCancle(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        setRightFragment(TAOCAN_DETAIL, null,false);
    }

    @Override
    public void onTaocanDishDelete(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        DBHelper.getInstance(getContext().getApplicationContext()).deleteTaocanGroupDish(orderTaocanGroupDishEntity);
        setRightFragment(TAOCAN_DETAIL, orderTaocanGroupDishEntity,false);
        if(mLFragmentOrderedTaocan != null) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = orderTaocanGroupDishEntity;
            mLFragmentOrderedTaocan.setNewMessage(msg);
        }

        if(mLFragmentTaocanDetail != null) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = orderTaocanGroupDishEntity;
            mLFragmentTaocanDetail.setNewMessage(msg);
        }
    }

    @Override
    public void onTaocanDishConfirm(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        DBHelper.getInstance(getContext().getApplicationContext()).changeTaocanGroupDish(orderTaocanGroupDishEntity);
        setRightFragment(TAOCAN_DETAIL, orderTaocanGroupDishEntity,false);
        if(mLFragmentOrderedTaocan != null) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = orderTaocanGroupDishEntity;
            mLFragmentOrderedTaocan.setNewMessage(msg);
        }
    }

    //************************************************************套餐内商品详情
    @Override
    public void onTaocanDetailClose() {
        setRightFragment(TAOCAN_DETAIL,null,false);
    }

    //客显界面
    private void sendDifferBroadcast(int type,String orderId,boolean isOpenJoinOrder){
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type",type);
        intent.putExtra("orderId",orderId);
        intent.putExtra("isOpenJoinOrder",isOpenJoinOrder);
        getContext().sendBroadcast(intent);
    }
}
