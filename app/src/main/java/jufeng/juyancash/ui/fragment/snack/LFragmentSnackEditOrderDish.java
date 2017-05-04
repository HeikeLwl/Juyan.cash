package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SnackEditDishAdapter;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.eventbus.SnackEditDishConfirmEvent;
import jufeng.juyancash.eventbus.SnackEditOrderDishBackEvent;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/1.
 */
public class LFragmentSnackEditOrderDish extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
//    @BindView(R.id.tv_serial_number)
//    TextView mTvSerialNumber;
    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.tv_edit_all)
    TextView mTvEditAll;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    private SnackEditDishAdapter adapter;
    private String orderId;
    private int mType;
    private Unbinder mUnbinder;

    public static LFragmentSnackEditOrderDish newInstance(int type, String orderId) {
        LFragmentSnackEditOrderDish fragment = new LFragmentSnackEditOrderDish();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("orderId", orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_snack_edit_dish_layout, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        initData();
        setAdapter();
        return mView;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    private void initData() {
        orderId = getArguments().getString("orderId");
        mType = getArguments().getInt("type");
        OrderEntity orderEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getOneOrderEntity(orderId);
        if (orderEntity != null) {
            mTvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
//            mTvSerialNumber.setText(orderEntity.getTableId() == null ? "" : orderEntity.getTableId());
            switch (mType) {
                case 0://退菜
                    mTvTitle.setText("退菜");
                    mTvConfirm.setText("退菜");
                    mTvEditAll.setText("整单退菜");
                    break;
                case 1://赠菜
                    mTvTitle.setText("赠菜");
                    mTvConfirm.setText("赠菜");
                    mTvEditAll.setText("整单赠菜");
                    break;
                case 2://催菜
                    mTvTitle.setText("催菜");
                    mTvConfirm.setText("催菜");
                    mTvEditAll.setText("整单催菜");
                    break;
                default:
                    mTvTitle.setText("");
                    mTvConfirm.setText("");
                    mTvEditAll.setText("");
                    break;
            }
        } else {
            mTvTitle.setText("");
            mTvConfirm.setText("");
            mTvEditAll.setText("");
            mTvOrderNumber.setText("");
//            mTvSerialNumber.setText("");
        }
    }

    private void setAdapter() {
        adapter = new SnackEditDishAdapter(getActivity(), orderId, mType);
        mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.setItemAnimator(null);
        mRecyclerview.setHasFixedSize(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @OnClick({R.id.tv_back, R.id.tv_edit_all, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                EventBus.getDefault().post(new SnackEditOrderDishBackEvent(orderId));
                break;
            case R.id.tv_edit_all:
                adapter.setReteatAll();
                if (adapter.getRetreatDishes().size() > 0) {
                    EventBus.getDefault().post(new SnackEditDishConfirmEvent(orderId, adapter.getRetreatDishes(), mType));
                } else {
                    CustomMethod.showMessage(getContext(), "请先选择需菜品！");
                }
                break;
            case R.id.tv_confirm:
                if (adapter.getRetreatDishes().size() > 0) {
                    EventBus.getDefault().post(new SnackEditDishConfirmEvent(orderId, adapter.getRetreatDishes(), mType));
                } else {
                    CustomMethod.showMessage(getContext(), "请先选择菜品！");
                }
                break;
        }
    }
}
