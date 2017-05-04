package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.eventbus.OnHasConfigDishClickEvent;
import jufeng.juyancash.eventbus.OnNormalDishClickEvent;
import jufeng.juyancash.eventbus.OnTaocanClickEvent;
import jufeng.juyancash.eventbus.SearchDishBackEvent;
import jufeng.juyancash.eventbus.SearchDishClickEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.DifferentDisplay;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.ActivityIntentUtil;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/11/16.
 */

public class LFragmentOrderDish extends BaseFragment {
    private LOrderDishFragment mOrderDishFragment;
    private LDishListFragment mDishListFragment;
    private LFragmentDishDetailEdit mLFragmentDishDetailEdit;
    private LFragmentDishConfigEdit mLFragmentDishConfigEdit;
    private LFragmentSearchDish mLFragmentSearchDish;
    private final int SHOW_DISH_LIST_FRAGMENT = 0;
    private final int SHOW_DISH_CONFIG_EDIT_FRAGMENT = 1;
    private final int SHOW_DISH_DETAIL_EDIT_FRAGMENT = 2;
    private final int SHOW_DISH_EDIT_EDIT_FRAGMENT = 3;
    private final int SEARCH_DISH_FRAGMENT = 4;
    private String mOrderId,mTableId;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mOnDishListItemClick();
                    break;
                case 1:
                    String dishId = msg.getData().getString("dishId");
                    mConfigDish(dishId);
                    break;
                case 2:
                    String taocanId = msg.getData().getString("taocanId");
                    String orderId = msg.getData().getString("orderId");
                    mSelectTaocan(taocanId,orderId);
                    break;
                case 3:
                    mOnCancle();
                    break;
                case 4:
                    mOnDelete();
                    break;
                case 5:
                    mOnConfirm();
                    break;
                case 6:
                    mOnClose();
                    break;
                case 7:
                    mAddDish();
                    break;
                case 8:
                    mReduceDish();
                    break;
                case 9:
                    mDishDetail(msg.getData().getString("orderDishId"));
                    break;
                case 10:
                    String tableId = msg.getData().getString("tableId");
                    String orderId1 = msg.getData().getString("orderId");
                    if(orderId1 != null && mOrderId != null && mOrderId.equals(orderId1)){
                        Log.d("###", "同一笔单");
                    }else{
                        //切单
                        mOrderId = orderId1;
                        mTableId = tableId;
                        setRightFragment(SHOW_DISH_LIST_FRAGMENT,mOrderId,true);
                    }
                    break;
            }
        }
    };
    private MainFragmentListener mMainFragmentListener;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanClick(OnTaocanClickEvent event){
        if(event != null && event.getOrderId() != null && event.getTaocanId() != null){
            mSelectTaocan(event.getTaocanId(),event.getOrderId());
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventTaocanDetailClick(OnTaocanDetailClickEvent event){
//        if(event != null) {
//            if (mMainFragmentListener != null) {
//                mMainFragmentListener.onTaocanDishDetail(event.getOrderDishEntity(), ActivityIntentUtil.FRAGMENT_CASHIER, event.getTaocanId(), event.getOrderId(), mTableId);
//            }
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishConfig(OnHasConfigDishClickEvent event){
        if(event != null&& event.getDishId() != null){
            mConfigDish(event.getDishId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNormalDish(OnNormalDishClickEvent event){
        if(event != null && event.getOrderDishEntity() != null){
            mOnDishListItemClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setOrderDishHandler(mHandler);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_order_dish, container, false);
        mOrderId = getArguments().getString("orderId");
        mTableId = getArguments().getString("tableId");
        setLeftFragment();
        setRightFragment(SHOW_DISH_LIST_FRAGMENT, mOrderId,false);
        sendDifferBroadcast(1,mOrderId,false);
        return mView;
    }

    private void setLeftFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        if (mOrderDishFragment == null) {
            mOrderDishFragment = new LOrderDishFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tableId", mTableId);
            bundle.putString("orderId", mOrderId);
            mOrderDishFragment.setArguments(bundle);
            trans.add(R.id.containerleft, mOrderDishFragment);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("tableId", mTableId);
            bundle.putString("orderId", mOrderId);
            mOrderDishFragment.setArguments(bundle);
        }
        trans.commitAllowingStateLoss();
    }

    private void setRightFragment(int tag, String id,boolean changeOrder) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideRightFragment(trans,changeOrder);
        switch (tag) {
            case SHOW_DISH_LIST_FRAGMENT://显示商品列表
                if (mDishListFragment == null) {
                    mDishListFragment = new LDishListFragment();
                    Bundle b = new Bundle();
                    b.putString("orderId", id);
                    mDishListFragment.setArguments(b);
                    trans.add(R.id.containerright, mDishListFragment);
                } else {
                    trans.show(mDishListFragment);
                }
                break;
            case SHOW_DISH_CONFIG_EDIT_FRAGMENT://配置商品规格和做法
                if (mLFragmentDishConfigEdit == null) {
                    mLFragmentDishConfigEdit = new LFragmentDishConfigEdit();
                    Bundle b = new Bundle();
                    b.putString("dishId", id);
                    mLFragmentDishConfigEdit.setArguments(b);
                    trans.add(R.id.containerright, mLFragmentDishConfigEdit);
                } else {
                    mLFragmentDishConfigEdit.setNewParam(id,null);
                    trans.show(mLFragmentDishConfigEdit);
                }
                break;
            case SHOW_DISH_DETAIL_EDIT_FRAGMENT://显示已下单商品详情
                if (mLFragmentDishDetailEdit == null) {
                    mLFragmentDishDetailEdit = new LFragmentDishDetailEdit();
                    Bundle b = new Bundle();
                    b.putString("orderDishId", id);
                    b.putInt("type",0);
                    mLFragmentDishDetailEdit.setArguments(b);
                    trans.add(R.id.containerright, mLFragmentDishDetailEdit);
                } else {
                    mLFragmentDishDetailEdit.setNewParam(0,id);
                    trans.show(mLFragmentDishDetailEdit);
                }
                break;
            case SHOW_DISH_EDIT_EDIT_FRAGMENT://编辑商品规格和做法
                if (mLFragmentDishConfigEdit == null) {
                    mLFragmentDishConfigEdit = new LFragmentDishConfigEdit();
                    Bundle b = new Bundle();
                    b.putString("dishId", DBHelper.getInstance(getContext().getApplicationContext()).queryOneOrderDishEntity(id).getDishId());
                    b.putString("orderDishId", id);
                    mLFragmentDishConfigEdit.setArguments(b);
                    trans.add(R.id.containerright, mLFragmentDishConfigEdit);
                } else {
                    mLFragmentDishConfigEdit.setNewParam(DBHelper.getInstance(getContext().getApplicationContext()).queryOneOrderDishEntity(id).getDishId(),id);
                    trans.show(mLFragmentDishConfigEdit);
                }
                break;
            case SEARCH_DISH_FRAGMENT:
                if (mLFragmentSearchDish == null) {
                    mLFragmentSearchDish = new LFragmentSearchDish();
                    Bundle b = new Bundle();
                    b.putString("orderId", id);
                    mLFragmentSearchDish.setArguments(b);
                    trans.add(R.id.containerright, mLFragmentSearchDish);
                } else {
                    mLFragmentSearchDish.setNewParam(id);
                    trans.show(mLFragmentSearchDish);
                }
                break;

        }
        trans.commitAllowingStateLoss();
    }

    private void hideRightFragment(FragmentTransaction trans,boolean changeOrder) {
        if (mDishListFragment != null) {
            trans.hide(mDishListFragment);
        }
        if (mLFragmentDishDetailEdit != null) {
            trans.hide(mLFragmentDishDetailEdit);
        }
        if (mLFragmentDishConfigEdit != null) {
            trans.hide(mLFragmentDishConfigEdit);
        }
        if(mLFragmentSearchDish != null){
            trans.hide(mLFragmentSearchDish);
        }
    }

    public void mOnDishListItemClick() {
        sendDifferBroadcast(1,mOrderId,false);
    }

    //配置菜品
    public void mConfigDish(String dishId) {
        setRightFragment(SHOW_DISH_CONFIG_EDIT_FRAGMENT, dishId,false);
    }

    //选择套餐
    public void mSelectTaocan(String taocanId, String orderId) {
        if(mMainFragmentListener != null){
            mMainFragmentListener.onTaocanDishDetail(null,ActivityIntentUtil.FRAGMENT_ORDERDISH,taocanId,orderId,mTableId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    //**********************************************************已点菜品界面的回调
    //客单Fragment增加菜品数量
    public void mAddDish() {
        sendDifferBroadcast(1,mOrderId,false);
    }

    //客单fragment减少菜品数量
    public void mReduceDish() {
        sendDifferBroadcast(1,mOrderId,false);
    }

    //菜品详情
    public void mDishDetail(String orderDishId) {
        OrderDishEntity orderDishEntity = DBHelper.getInstance(getContext().getApplicationContext()).queryOneOrderDishEntity(orderDishId);
        if (orderDishEntity != null) {
            if(orderDishEntity .getIsFromWX() != null && orderDishEntity.getIsFromWX() == 1 && orderDishEntity.getIsOrdered() == 0){
                //微信点的菜品，并且未下单
                CustomMethod.showMessage(getContext(),"微信菜品需要下单后才能查看详情");
            }else {
                if (orderDishEntity.getType() == 0) {//非套餐
                    if (orderDishEntity.getIsOrdered() == 1) {
                        setRightFragment(SHOW_DISH_DETAIL_EDIT_FRAGMENT, orderDishId, false);
                    } else {
                        setRightFragment(SHOW_DISH_EDIT_EDIT_FRAGMENT, orderDishId, false);
                    }
                } else {//套餐
                    if (mMainFragmentListener != null) {
                        mMainFragmentListener.onTaocanDishDetail(orderDishEntity, ActivityIntentUtil.FRAGMENT_ORDERDISH, orderDishEntity.getDishId(), mOrderId, mTableId);
                    }
                }
            }
        }
    }

    //**********************************************************菜品编辑界面的回调
    //关闭商品详情页
    public void mOnClose() {
        setRightFragment(SHOW_DISH_LIST_FRAGMENT, mOrderId,false);
    }

    //关闭菜品配置界面
    public void mOnCancle() {
        setRightFragment(SHOW_DISH_LIST_FRAGMENT, mOrderId,false);
    }

    //删除菜品
    public void mOnDelete() {
        setRightFragment(SHOW_DISH_LIST_FRAGMENT, mOrderId,false);
        sendDifferBroadcast(1,mOrderId,false);
    }

    //配置菜品完成
    public void mOnConfirm() {
        setRightFragment(SHOW_DISH_LIST_FRAGMENT, mOrderId,false);
        sendDifferBroadcast(1,mOrderId,false);
    }

    //客显界面
    private void sendDifferBroadcast(int type,String orderId,boolean isOpenJoinOrder){
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type",type);
        intent.putExtra("orderId",orderId);
        intent.putExtra("isOpenJoinOrder",isOpenJoinOrder);
        getContext().sendBroadcast(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchDishClick(SearchDishClickEvent event){
        if(event != null){
            setRightFragment(SEARCH_DISH_FRAGMENT,mOrderId,false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchDishBack(SearchDishBackEvent event){
        if(event != null){
            setRightFragment(SHOW_DISH_LIST_FRAGMENT,mOrderId,false);
        }
    }
}
