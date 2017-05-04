package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.DishChildMenuAdapter;
import jufeng.juyancash.adapter.DishListAdapter;
import jufeng.juyancash.adapter.SnackDishMenuAdapter;
import jufeng.juyancash.bean.DishBean;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.eventbus.SearchDishClickEvent;
import jufeng.juyancash.eventbus.SnackAddNewDishEvent;
import jufeng.juyancash.eventbus.SnackRefreshDishMenuItemEvent;
import jufeng.juyancash.eventbus.SnackTaocanClickEvent;
import jufeng.juyancash.ui.customview.ConfigDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/3/20.
 */

public class LFragmentSnackDishMenu extends BaseFragment {
    private CardView mCardView;
    private FloatingActionButton mFabSearchDish;
    private RecyclerView mRecyclerView, mRecyclerView1, mRecyclerView2;
    private DishListAdapter adapter0;
    private SnackDishMenuAdapter adapter1;
    private DishChildMenuAdapter adapter2;
    private String orderId;
    private String typeId;
    private int type;

    public static LFragmentSnackDishMenu newInstance(String orderId) {
        LFragmentSnackDishMenu fragment = new LFragmentSnackDishMenu();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_snack_dish_list, container, false);
        initView(mView);
        setAdapter1();
        setAdapter2();
        setAdapter0();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mCardView = (CardView) view.findViewById(R.id.cardview);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        mRecyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerview2);
        mFabSearchDish = (FloatingActionButton) view.findViewById(R.id.fab_search_dish);
        orderId = getArguments().getString("orderId");
    }

    //商品列表
    private void setAdapter0() {
        if (adapter2.getSelectedTypeId() != null) {
            typeId = adapter2.getSelectedTypeId();
            type = adapter1.getType();
            adapter0 = new DishListAdapter(getActivity().getApplicationContext(), adapter2.getSelectedTypeId(), orderId, adapter1.getType());
        } else {
            typeId = adapter1.getSelectedTypeId();
            type = adapter1.getType();
            adapter0 = new DishListAdapter(getActivity().getApplicationContext(), adapter1.getSelectedTypeId(), orderId, adapter1.getType());
        }
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter0);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    //商品分类列表
    private void setAdapter1() {
        adapter1 = new SnackDishMenuAdapter(getActivity().getApplicationContext());
        mRecyclerView1.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView1.setAdapter(adapter1);
    }

    //二级分类
    private void setAdapter2() {
        adapter2 = new DishChildMenuAdapter(getActivity().getApplicationContext(), adapter1.getSelectedTypeId(), adapter1.getType());
        mRecyclerView2.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView2.setAdapter(adapter2);
        if (DBHelper.getInstance(getActivity().getApplicationContext()).isHasChild(adapter1.getSelectedTypeId(), adapter1.getType())) {
            mCardView.setVisibility(CardView.VISIBLE);
        } else {
            mCardView.setVisibility(CardView.GONE);
        }
    }

    private void setListener() {
        mFabSearchDish.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SearchDishClickEvent(orderId));
            }
        });

        adapter0.setOnDishClickListener(new DishListAdapter.OnDishClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onDishClicked(View view, DishBean dishBean) {
                Log.d("###", "点击菜品");
                if (dishBean.isHasConfig()) {
                    ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId",orderId);
                    bundle.putString("orderDishId", null);
                    bundle.putString("dishId", dishBean.getDishEntity().getDishId());
                    configPopupWindow.setArguments(bundle);
                    configPopupWindow.show(getFragmentManager(), "config_dialog_fragment");
//                    EventBus.getDefault().post(new SnackConfigDishEvent(dishBean.getDishEntity().getDishId(),null));
                } else {
                    EventBus.getDefault().post(new SnackAddNewDishEvent(dishBean.getDishEntity().getDishId()));
                }
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onTaocanClicked(TaocanEntity taocanEntity, String typeId) {
                EventBus.getDefault().post(new SnackTaocanClickEvent(taocanEntity, typeId));
            }

            @Override
            public void onDishChing() {
                CustomMethod.showMessage(getContext(), "该商品已估清");
            }
        });

        adapter1.setOnDishMenuClickListener(new SnackDishMenuAdapter.OnDishMenuClickListener() {
            @Override
            public void onDishMenuClicked(String typeId, int type) {
                updaAdapter2(typeId, type);
                updateAdapter0(typeId, type);
            }
        });

        adapter2.setOnChildDishMenuClickListener(new DishChildMenuAdapter.OnChildDishMenuClickListener() {
            @Override
            public void onChildDishMenuClicked(String typeId, int type) {
                updateAdapter0(typeId, type);
            }
        });
    }

    private void updaAdapter2(String typeId, int type) {
        if (DBHelper.getInstance(getActivity().getApplicationContext()).isHasChild(typeId, type)) {
            mCardView.setVisibility(CardView.VISIBLE);
        } else {
            mCardView.setVisibility(CardView.GONE);
        }
        adapter2.updateData(typeId, type);
    }

    private void updateAdapter0(String mtypeId, int type) {
        if (adapter2.getSelectedTypeId() != null) {
            this.typeId = adapter2.getSelectedTypeId();
            this.type = adapter2.getType();
            adapter0.updateData(typeId, orderId, type);
        } else {
            this.type = type;
            this.typeId = mtypeId;
            adapter0.updateData(typeId, orderId, type);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishRefresh(SnackRefreshDishMenuItemEvent event) {
        if (event != null) {
            adapter0.changeItem(event.getDishId(),type);
        }
    }
}
