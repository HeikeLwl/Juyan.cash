package jufeng.juyancash.ui.fragment;

import android.content.Context;
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
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;

/**
 * Created by 15157_000 on 2016/6/22 0022.
 */
public class LFragmentDishDetailEdit extends BaseFragment{
    private TextView tvName;
    private TextView tvPrice;
    private LinearLayout layoutGG,layoutZF;
    private TextView tvSpecify,tvPractice;
    private TextView tvMark;
    private Button btnCancle;
    private MainFragmentListener mOnDishDetailEditClickListener;
    private String orderDishId;
    private int type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnDishDetailEditClickListener = (MainFragmentListener)context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_dish_detail_edit,container,false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_dish_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
        layoutGG = (LinearLayout) view.findViewById(R.id.layout_guige);
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zuofa);
        tvSpecify = (TextView) view.findViewById(R.id.tv_dish_specify);
        tvPractice = (TextView) view.findViewById(R.id.tv_dish_practice);
        tvMark = (TextView) view.findViewById(R.id.tv_dish_mark);
        btnCancle = (Button) view.findViewById(R.id.btn_close);
        type = getArguments().getInt("type");
        orderDishId = getArguments().getString("orderDishId");
    }

    public void setNewParam(int type,String orderDishId){
        this.type = type;
        this.orderDishId = orderDishId;

        initData();
    }

    private void initData(){
        OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext().getApplicationContext()).queryOneOrderDishEntity(orderDishId);
        if(orderDishEntity != null) {
            tvPrice.setText("￥" +orderDishEntity.getDishPrice() + "/份");
            tvName.setText(orderDishEntity.getDishName());
            String mark = orderDishEntity.getDishNote();
            if(mark != null){
                tvMark.setText(mark);
            }
            String specifyName = orderDishEntity.getDishSpecify();
            String practiceName = orderDishEntity.getDishPractice();
            String extras = "";
            if(specifyName != null && !specifyName.isEmpty()){
                layoutGG.setVisibility(LinearLayout.VISIBLE);
                tvSpecify.setText(specifyName);
            }else{
                layoutGG.setVisibility(LinearLayout.GONE);
            }
            if(practiceName != null && !practiceName.isEmpty()){
                layoutZF.setVisibility(LinearLayout.VISIBLE);
                tvPractice.setText(practiceName);
            }else{
                layoutZF.setVisibility(LinearLayout.GONE);
            }
        }
    }

    private void setListener(){
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDishDetailEditClickListener.onClose(type,orderDishId);
            }
        });
    }
}
