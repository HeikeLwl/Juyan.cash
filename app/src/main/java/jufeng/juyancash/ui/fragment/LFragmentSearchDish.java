package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SearchDishAdapter;
import jufeng.juyancash.bean.DishBean;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.eventbus.OnTaocanClickEvent;
import jufeng.juyancash.eventbus.SearchDishBackEvent;
import jufeng.juyancash.eventbus.SearchDishUpdateEvent;
import jufeng.juyancash.eventbus.SnackAddNewDishEvent;
import jufeng.juyancash.eventbus.SnackRefreshDishMenuItemEvent;
import jufeng.juyancash.ui.customview.ClearEditText;
import jufeng.juyancash.ui.customview.ConfigDialogFragment;
import jufeng.juyancash.ui.customview.KeyboardUtil1;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentSearchDish extends BaseFragment {
    private ClearEditText etIndex;
    private RecyclerView mRecyclerView1;
    private SearchDishAdapter rightAdapter;
    private KeyboardUtil1 mKeyboardUtil1;
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private TextView tvBack;
    private String mOrderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search_dish, container, false);
        initView(mView);
        setRightAdapter();
        setListener();
        return mView;
    }

    private void initView(View view) {
        etIndex = (ClearEditText) view.findViewById(R.id.et_search);
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        mKeyboardUtil1 = new KeyboardUtil1(view, getContext(), etIndex, 2, R.id.ching_keyboard);
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        tvBack = (TextView) view.findViewById(R.id.tv_back);
        CustomMethod.setMyInputType(etIndex, getActivity());
        etIndex.requestFocus();
        mKeyboardUtil1.showKeyboard();

        mOrderId = getArguments().getString("orderId");
    }

    private void setListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SearchDishBackEvent());
            }
        });

        etIndex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void afterTextChanged(Editable s) {
                if(etIndex.getText() == null)
                    return;
                String matchStr = etIndex.getText().toString();
                int type = 0;
                switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_normal_dish:
                        type = 0;
                        break;
                    case R.id.rb_taocan:
                        type = 1;
                        break;
                }
                EventBus.getDefault().post(new SearchDishUpdateEvent(matchStr, type, mOrderId));
            }
        });

        rightAdapter.setOnSearchDishClickListener(new SearchDishAdapter.OnSearchDishClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onNormalDishClick(DishBean dishBean) {
                if (dishBean != null && dishBean.isHasConfig()) {
                    ConfigDialogFragment configPopupWindow = new ConfigDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", mOrderId);
                    bundle.putString("orderDishId",null);
                    bundle.putString("dishId",dishBean.getDishEntity().getDishId());
                    configPopupWindow.setArguments(bundle);
                    configPopupWindow.show(getFragmentManager(),"config_dialog_fragment");
//                    EventBus.getDefault().post(new OnHasConfigDishClickEvent(dishBean.getDishEntity().getDishId()));
                } else {
                    EventBus.getDefault().post(new SnackAddNewDishEvent(dishBean.getDishEntity().getDishId()));
//                    OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).insertNewDish(mOrderId, dishBean.getDishEntity().getDishId());
//                    EventBus.getDefault().post(new OnNormalDishClickEvent(orderDishEntity));
//                    if(etIndex.getText() == null)
//                        return;
//                    String matchStr = etIndex.getText().toString();
//                    int type = 0;
//                    switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
//                        case R.id.rb_normal_dish:
//                            type = 0;
//                            break;
//                        case R.id.rb_taocan:
//                            type = 1;
//                            break;
//                    }
//                    rightAdapter.updateData(new SearchDishUpdateEvent(matchStr, type, mOrderId));
                }
            }

            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onTaocanClick(TaocanEntity taocanEntity) {
                EventBus.getDefault().post(new OnTaocanClickEvent(taocanEntity.getTaocanId(),mOrderId));
            }

            @Override
            public void onDishChing() {
                CustomMethod.showMessage(getContext(),"该商品已估清");
            }
        });
    }

    private void setRightAdapter() {
        rightAdapter = new SearchDishAdapter(getActivity().getApplicationContext(), 0);
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView1.setLayoutManager(gridLayoutManager);
        mRecyclerView1.setAdapter(rightAdapter);
        mRecyclerView1.setFocusable(false);
        mRecyclerView1.setItemAnimator(null);
        mRecyclerView1.setHasFixedSize(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setNewParam(String orderId) {
        this.mOrderId = orderId;
        etIndex.setText("");
        mLCustomeRadioGroup.check(R.id.rb_normal_dish);
        EventBus.getDefault().post(new SearchDishUpdateEvent("", 0, mOrderId));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchDishUpdate(SearchDishUpdateEvent event) {
        if(event != null){
            rightAdapter.updateData(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDishRefresh(SnackRefreshDishMenuItemEvent event) {
        if (event != null) {
            int type = 0;
            switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_normal_dish:
                    type = 0;
                    break;
                case R.id.rb_taocan:
                    type = 1;
                    break;
            }
            rightAdapter.changeItem(event.getDishId(),type);
        }
    }
}
