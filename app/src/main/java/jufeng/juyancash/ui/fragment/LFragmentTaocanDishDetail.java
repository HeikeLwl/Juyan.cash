package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.myinterface.SelectTaocanFragmentListener;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentTaocanDishDetail extends BaseFragment {
    private TextView tvName;
    private TextView tvPrice;
    private LinearLayout layoutZF;
    private TextView tvNote;
    private TextView tvZFName;
    private Button btnClose;
    private SelectTaocanFragmentListener mOnTaocanDishDetailEditClickListener;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_taocan_dish_detail_edit, container, false);
        initView(mView);
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_dish_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zuofa);
        tvZFName = (TextView) view.findViewById(R.id.tv_zf_name);
        btnClose = (Button) view.findViewById(R.id.btn_close);
        tvNote = (TextView) view.findViewById(R.id.tv_dish_note);
        mOrderTaocanGroupDishEntity = getArguments().getParcelable("orderTaocanGroupDishEntity");
        initData();
    }

    public void setNewParam(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        this.mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;
        initData();
    }

    public void setListener(SelectTaocanFragmentListener listener) {
        if (mOnTaocanDishDetailEditClickListener == null) {
            mOnTaocanDishDetailEditClickListener = listener;
        }
    }

    private void initData() {
        String dishId = mOrderTaocanGroupDishEntity.getDishId();
        DishEntity dishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneDishEntity(dishId);
        if (dishEntity != null) {
            tvPrice.setText("加价￥" + mOrderTaocanGroupDishEntity.getExtraPrice() + "元");
            tvName.setText(dishEntity.getDishName());
            String practiceName = mOrderTaocanGroupDishEntity.getPracticeName();
            if (practiceName == null || practiceName.isEmpty()) {
                layoutZF.setVisibility(LinearLayout.GONE);
            } else {
                layoutZF.setVisibility(LinearLayout.VISIBLE);
                tvZFName.setText(practiceName);
            }
            String dishNote = mOrderTaocanGroupDishEntity.getRemark();
            if (dishNote == null || dishNote.isEmpty()) {
                tvNote.setText("无");
            } else {
                tvNote.setText(dishNote);
            }
        }
    }

    private void setListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTaocanDishDetailEditClickListener != null)
                    mOnTaocanDishDetailEditClickListener.onTaocanDetailClose();
            }
        });
    }
}
