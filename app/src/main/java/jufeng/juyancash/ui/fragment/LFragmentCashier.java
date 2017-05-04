package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.ui.activity.DifferentDisplay;
import jufeng.juyancash.ui.activity.MainActivity;

/**
 * Created by Administrator102 on 2016/11/16.
 */

public class LFragmentCashier extends BaseFragment {
    private LFragmentCashierOrder mLFragmentCashierOrder;
    private LCashierFragment mCashierFragment;
    private LFragmentDishDetailEdit mLFragmentDishDetailEdit;
    private LFragmentRetreatDish mLFragmentRetreatDish;
    private LFragmentRetreatOrder mLFragmentRetreatOrder;
    private LFragmentPresentDish mLFragmentPresentDish;
    private LFragmentPresentOrder mLFragmentPresentOrder;
    private LFragmentRemindDish mLFragmentRemindDish;
    private String tableId, orderId;
    private final int SHOW_CASHIER = 0;
    private final int SHOW_DISH_DETAIL_EDIT_FRAGMENT = 1;
    private boolean isOpenJoinOrder;
    private boolean isJoinOrder;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String orderDishId = msg.getData().getString("orderDishId");
                    setRightFragment(SHOW_DISH_DETAIL_EDIT_FRAGMENT, orderDishId, false);
                    break;
                case 1:
                    setLeftFragment(1, tableId, orderId);
                    break;
                case 2:
                    setLeftFragment(2, tableId, orderId);
                    break;
                case 3:
                    setLeftFragment(3, tableId, orderId);
                    break;
                case 4:
                    isOpenJoinOrder = true;
                    setLeftFragment(0, tableId, orderId);
                    setRightFragment(SHOW_CASHIER, orderId, true);
                    break;
                case 5:
                    String orderId1 = msg.getData().getString("orderId");
                    isOpenJoinOrder = false;
                    setLeftFragment(0, tableId, orderId1);
                    setRightFragment(SHOW_CASHIER, orderId1, true);
                    break;
                case 6:
                    setRightFragment(SHOW_CASHIER, orderId, false);
                    break;
                case 7:
                    String orderId2 = msg.getData().getString("orderId");
                    setLeftFragment(0, tableId, orderId2);
                    break;
                case 8:
                    String orderId3 = msg.getData().getString("orderId");
                    ArrayList<OrderDishEntity> orderDishEntities = msg.getData().getParcelableArrayList("orderDishEntities");
                    setRetreatOrderFragment(orderDishEntities, orderId3);
                    break;
                case 9:
                    String orderId4 = msg.getData().getString("orderId");
                    ArrayList<OrderDishEntity> orderDishEntities1 = msg.getData().getParcelableArrayList("orderDishEntities");
                    setRetreatOrderFragment(orderDishEntities1, orderId4);
                    break;
                case 10:
                    String orderId5 = msg.getData().getString("orderId");
                    setLeftFragment(1, tableId, orderId5);
                    break;
                case 11:
                    String orderId6 = msg.getData().getString("orderId");
                    setLeftFragment(0, tableId, orderId6);
                    break;
                case 12:
                    String orderId7 = msg.getData().getString("orderId");
                    setLeftFragment(0, tableId, orderId7);
                    break;
                case 13:
                    String orderId8 = msg.getData().getString("orderId");
                    ArrayList<OrderDishEntity> orderDishEntities2 = msg.getData().getParcelableArrayList("orderDishEntities");
                    setPresentOrderFragment(orderDishEntities2, orderId8);
                    break;
                case 14:
                    String orderId9 = msg.getData().getString("orderId");
                    setLeftFragment(2, tableId, orderId9);
                    break;
                case 15:
                    String orderId10 = msg.getData().getString("orderId");
                    setLeftFragment(0, tableId, orderId10);
                    break;
                case 16:
                    String orderId11 = msg.getData().getString("orderId");
                    setLeftFragment(0, tableId, orderId11);
                    break;
                case 17://切单
                    tableId = msg.getData().getString("tableId");
                    orderId = msg.getData().getString("orderId");
                    int type = msg.getData().getInt("tag");
                    OrderEntity orderEntity = DBHelper.getInstance(getContext().getApplicationContext()).getOneOrderEntity(orderId);
                    if (orderEntity != null) {
                        DBHelper.getInstance(getContext().getApplicationContext()).dealWithVoucher(orderEntity, 1);
                    }
                    isOpenJoinOrder = false;
                    isJoinOrder = false;
                    setLeftFragment(type, tableId, orderId);
                    setRightFragment(SHOW_CASHIER, orderId, true);
                    sendDifferBroadcast(2, orderId, isOpenJoinOrder);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setCashierMainHandler(mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cashier, container, false);
        tableId = getArguments().getString("tableId");
        orderId = getArguments().getString("orderId");
        int type = getArguments().getInt("tag", 0);
        isOpenJoinOrder = false;
        isJoinOrder = false;
        setLeftFragment(type, tableId, orderId);
        setRightFragment(SHOW_CASHIER, orderId, false);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
        return view;
    }

    private void setLeftFragment(int tag, String tableId, String orderId) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideLeftFragment(trans);
        switch (tag) {
            case 0:
                if (mLFragmentCashierOrder == null) {
                    mLFragmentCashierOrder = new LFragmentCashierOrder();
                    Bundle b = new Bundle();
                    b.putString("tableId", tableId);
                    b.putString("orderId", orderId);
                    b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
                    b.putBoolean("isJoinOrder", isJoinOrder);
                    mLFragmentCashierOrder.setArguments(b);
                    trans.add(R.id.containerleft, mLFragmentCashierOrder);
                } else {
                    mLFragmentCashierOrder.setNewParam(tableId, orderId, isOpenJoinOrder);
                    trans.show(mLFragmentCashierOrder);
                }
                break;
            case 1:
                if (mLFragmentRetreatDish == null) {
                    mLFragmentRetreatDish = new LFragmentRetreatDish();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
                    mLFragmentRetreatDish.setArguments(b);
                    trans.add(R.id.containerleft, mLFragmentRetreatDish);
                } else {
                    mLFragmentRetreatDish.setNewParam(orderId, isOpenJoinOrder);
                    trans.show(mLFragmentRetreatDish);
                }
                break;
            case 2:
                if (mLFragmentPresentDish == null) {
                    mLFragmentPresentDish = new LFragmentPresentDish();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
                    mLFragmentPresentDish.setArguments(b);
                    trans.add(R.id.containerleft, mLFragmentPresentDish);
                } else {
                    mLFragmentPresentDish.setNewParam(orderId, isOpenJoinOrder);
                    trans.show(mLFragmentPresentDish);
                }
                break;
            case 3:
                if (mLFragmentRemindDish == null) {
                    mLFragmentRemindDish = new LFragmentRemindDish();
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
                    mLFragmentRemindDish.setArguments(b);
                    trans.add(R.id.containerleft, mLFragmentRemindDish);
                } else {
                    mLFragmentRemindDish.setNewParam(orderId, isOpenJoinOrder);
                    trans.show(mLFragmentRemindDish);
                }
                break;
        }
        trans.commitAllowingStateLoss();
    }

    private void setRetreatOrderFragment(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideLeftFragment(trans);
        if (mLFragmentRetreatOrder == null) {
            mLFragmentRetreatOrder = new LFragmentRetreatOrder();
            Bundle b = new Bundle();
            b.putSerializable("retreatDishes", orderDishEntities);
            b.putString("orderId", orderId);
            b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
            mLFragmentRetreatOrder.setArguments(b);
            trans.add(R.id.containerleft, mLFragmentRetreatOrder);
        } else {
            mLFragmentRetreatOrder.setNewParam(orderId, orderDishEntities);
            trans.show(mLFragmentRetreatOrder);
        }
        trans.commitAllowingStateLoss();
    }

    private void setPresentOrderFragment(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideLeftFragment(trans);
        if (mLFragmentPresentOrder == null) {
            mLFragmentPresentOrder = new LFragmentPresentOrder();
            Bundle b = new Bundle();
            b.putSerializable("retreatDishes", orderDishEntities);
            b.putString("orderId", orderId);
            b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
            mLFragmentPresentOrder.setArguments(b);
            trans.add(R.id.containerleft, mLFragmentPresentOrder);
        } else {
            mLFragmentPresentOrder.setNewParam(orderId, orderDishEntities);
            trans.show(mLFragmentPresentOrder);
        }
        trans.commitAllowingStateLoss();
    }

    private void setRightFragment(int tag, String id, boolean isChange) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideRightFragment(trans, isChange);
        switch (tag) {
            case SHOW_CASHIER:
                if (mCashierFragment == null) {
                    mCashierFragment = new LCashierFragment();
                    Bundle b = new Bundle();
                    b.putString("orderId", id);
                    b.putString("tableId", tableId);
                    b.putBoolean("isOpenJoinOrder", isOpenJoinOrder);
                    mCashierFragment.setArguments(b);
                    trans.add(R.id.containerright, mCashierFragment);
                } else {
                    if (isChange) {
                        mCashierFragment.setNewParam(id, tableId, isOpenJoinOrder);
                    }
                    trans.show(mCashierFragment);
                }
                break;
            case SHOW_DISH_DETAIL_EDIT_FRAGMENT:
                if (mLFragmentDishDetailEdit == null) {
                    mLFragmentDishDetailEdit = new LFragmentDishDetailEdit();
                    Bundle b = new Bundle();
                    b.putString("orderDishId", id);
                    b.putInt("type", 1);
                    mLFragmentDishDetailEdit.setArguments(b);
                    trans.add(R.id.containerright, mLFragmentDishDetailEdit);
                } else {
                    mLFragmentDishDetailEdit.setNewParam(1, id);
                    trans.show(mLFragmentDishDetailEdit);
                }
                break;
        }
        trans.commit();
    }

    private void hideLeftFragment(FragmentTransaction trans) {
        if (mLFragmentCashierOrder != null) {
            trans.hide(mLFragmentCashierOrder);
        }
        if (mLFragmentRetreatDish != null) {
            trans.hide(mLFragmentRetreatDish);
        }
        if (mLFragmentRetreatOrder != null) {
            trans.hide(mLFragmentRetreatOrder);
        }
        if (mLFragmentPresentDish != null) {
            trans.hide(mLFragmentPresentDish);
        }
        if (mLFragmentPresentOrder != null) {
            trans.hide(mLFragmentPresentOrder);
        }
        if (mLFragmentRemindDish != null) {
            trans.hide(mLFragmentRemindDish);
        }

    }

    private void hideRightFragment(FragmentTransaction trans, boolean isChange) {
        if (mCashierFragment != null) {
            trans.hide(mCashierFragment);
        }
        if (mLFragmentDishDetailEdit != null) {
            trans.hide(mLFragmentDishDetailEdit);
        }
    }

    //客显界面
    private void sendDifferBroadcast(int type, String orderId, boolean isOpenJoinOrder) {
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type", type);
        intent.putExtra("orderId", orderId);
        intent.putExtra("isOpenJoinOrder", isOpenJoinOrder);
        getContext().sendBroadcast(intent);
    }
}
