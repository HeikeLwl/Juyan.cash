package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import jufeng.juyancash.adapter.DishMenuAdapter;
import jufeng.juyancash.bean.DishBean;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.eventbus.OnNormalDishClickEvent;
import jufeng.juyancash.eventbus.OnTaocanClickEvent;
import jufeng.juyancash.eventbus.SearchDishClickEvent;
import jufeng.juyancash.eventbus.SnackAddNewDishEvent;
import jufeng.juyancash.eventbus.SnackRefreshDishMenuItemEvent;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.ConfigDialogFragment;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/21 0021.
 */
public class LDishListFragment extends BaseFragment {
    private CardView mCardView;
    private FloatingActionButton mFabSearchDish;
    private RecyclerView mRecyclerView, mRecyclerView1,mRecyclerView2;
    private DishListAdapter adapter0;
    private DishMenuAdapter adapter1;
    private DishChildMenuAdapter adapter2;
    private MainFragmentListener mOnDishListClickListener;
    private String orderId;
    private String typeId;
    private int type;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String orderId1 = msg.getData().getString("orderId");
                    orderId = orderId1;
                    adapter0.updateData(typeId,orderId,type);
                    break;
                case 3://刷新菜品列表
                    String dishId = msg.getData().getString("dishId");
                    int type = msg.getData().getInt("type");
                    adapter0.changeItem(dishId,type);
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshDishList(OnNormalDishClickEvent event){
        if(event != null && event.getOrderDishEntity() != null) {
            adapter0.changeItem(event.getOrderDishEntity().getDishId(), 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setDishListHandler(mHandler);
        try {
            mOnDishListClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_dish_list, container, false);
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

    public void setNewParam(String orderId){
        this.orderId = orderId;
    }

    //商品列表
    private void setAdapter0() {
        if(adapter2.getSelectedTypeId() != null){
            typeId = adapter2.getSelectedTypeId();
            type = adapter1.getType();
            adapter0 = new DishListAdapter(getActivity().getApplicationContext(), adapter2.getSelectedTypeId(), orderId, adapter1.getType());
        }else{
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
        adapter1 = new DishMenuAdapter(getActivity().getApplicationContext());
        mRecyclerView1.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView1.setAdapter(adapter1);
    }

    private void setAdapter2(){
        adapter2 = new DishChildMenuAdapter(getActivity().getApplicationContext(),adapter1.getSelectedTypeId(),adapter1.getType());
        mRecyclerView2.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView2.setAdapter(adapter2);
        if(DBHelper.getInstance(getActivity().getApplicationContext()).isHasChild(adapter1.getSelectedTypeId(),adapter1.getType())){
            mCardView.setVisibility(CardView.VISIBLE);
        }else{
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
            public void onDishClicked(View view,DishBean dishBean) {
                if (dishBean.isHasConfig()) {
                    ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId",orderId);
                    bundle.putString("orderDishId", null);
                    bundle.putString("dishId", dishBean.getDishEntity().getDishId());
                    configPopupWindow.setArguments(bundle);
                    configPopupWindow.show(getFragmentManager(), "config_dialog_fragment");
//                    mOnDishListClickListener.configDish(dishBean.getDishEntity().getDishId());
                } else {
//                    OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).insertNewDish(orderId, dishBean.getDishEntity().getDishId());
//                    mOnDishListClickListener.onDishListItemClick(orderDishEntity);
//                    adapter0.changeItem(orderDishEntity.getDishId(),0);
                    EventBus.getDefault().post(new SnackAddNewDishEvent(dishBean.getDishEntity().getDishId()));
                }
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onTaocanClicked(TaocanEntity taocanEntity, String typeId) {
                EventBus.getDefault().post(new OnTaocanClickEvent(taocanEntity.getTaocanId(),orderId));
//                mOnDishListClickListener.selectTaocan(taocanEntity.getTaocanId(),orderId);
            }

            @Override
            public void onDishChing() {
                CustomMethod.showMessage(getContext(),"该商品已估清");
            }
        });

        adapter1.setOnDishMenuClickListener(new DishMenuAdapter.OnDishMenuClickListener() {
            @Override
            public void onDishMenuClicked(String typeId, int type) {
                updaAdapter2(typeId,type);
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

    private void updaAdapter2(String typeId,int type){
        if(DBHelper.getInstance(getActivity().getApplicationContext()).isHasChild(typeId,type)){
            mCardView.setVisibility(CardView.VISIBLE);
        }else{
            mCardView.setVisibility(CardView.GONE);
        }
        adapter2.updateData(typeId,type);
    }

    private void updateAdapter0(String mtypeId, int type) {
        if(adapter2.getSelectedTypeId() != null){
            this.typeId = adapter2.getSelectedTypeId();
            this.type = adapter2.getType();
            adapter0.updateData(typeId, orderId, type);
        }else{
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
