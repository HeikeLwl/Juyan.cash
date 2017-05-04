package jufeng.juyancash.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupEntity;
import jufeng.juyancash.myinterface.SelectTaocanFragmentListener;
import jufeng.juyancash.ui.customview.CustomeRadioGroup1;

/**
 * Created by Administrator102 on 2016/9/2.
 */
public class LFragmentTaocanDishConfig extends BaseFragment {
    private TextView tvName;
    private TextView tvPrice;
    private LinearLayout layoutZF;
    private CustomeRadioGroup1 mRadioGroup1;
    private Button btnCancle, btnConfirm, btnDelete;
    private SelectTaocanFragmentListener mOnTaocanDishDetailEditClickListener;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_taocan_dish_config_edit, container, false);
        initView(mView);
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_dish_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zuofa);
        mRadioGroup1 = (CustomeRadioGroup1) view.findViewById(R.id.rg_zuofa);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);

        mOrderTaocanGroupDishEntity = getArguments().getParcelable("orderTaocanGroupDishEntity");
        initData();
    }

    public void setOnTaocanDishDetailEditClickListener(SelectTaocanFragmentListener listener){
        if(mOnTaocanDishDetailEditClickListener == null) {
            this.mOnTaocanDishDetailEditClickListener = listener;
        }
    }

    public void setNewParam(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity){
        this.mOrderTaocanGroupDishEntity = orderTaocanGroupDishEntity;

        initData();
    }

    private void initData(){
        String dishId = mOrderTaocanGroupDishEntity.getDishId();
        DishEntity dishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneDishEntity(dishId);
        if (dishEntity != null) {
            tvPrice.setText("加价￥" + mOrderTaocanGroupDishEntity.getExtraPrice() + "元");
            tvName.setText(dishEntity.getDishName());
            TaocanGroupEntity taocanGroupEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getTaocanGroupById(mOrderTaocanGroupDishEntity.getTaocanGroupId());
            btnDelete.setVisibility(taocanGroupEntity.getSelectMode() == 0?Button.VISIBLE:Button.GONE);
            String practiceId = mOrderTaocanGroupDishEntity.getPracticeId();

            if (DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasZuofa(dishId)) {
                layoutZF.setVisibility(LinearLayout.VISIBLE);
                setZuoFa(dishEntity.getDishId(), practiceId);
            } else {
                layoutZF.setVisibility(LinearLayout.GONE);
            }
        }
    }

    //设置做法
    private void setZuoFa(String dishId, String checkId) {
        mRadioGroup1.removeAllViews();
        ArrayList<DishPracticeEntity> dishPracticeEntities = DBHelper.getInstance(getActivity().getApplicationContext()).queryAllPractice(dishId);
        for (int i = 0; i < dishPracticeEntities.size(); i++) {
            DishPracticeEntity dishPracticeEntity = dishPracticeEntities.get(i);
            RadioButton radioButton = new RadioButton(getActivity().getApplicationContext());
            //获取做法详情
            switch (dishPracticeEntity.getIncreaseMode()) {
                case 0://不加价
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
                case 1://一次性加价
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
                case 2:
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
                case 3:
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
            }
            radioButton.setTextColor(getContext().getResources().getColor(R.color.dark));
            radioButton.setId(i);
            radioButton.setTag(dishPracticeEntity.getDishPracticeId());
            radioButton.setChecked(dishPracticeEntity.getDishPracticeId().equals(checkId));
            mRadioGroup1.addView(radioButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setListener() {
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTaocanDishDetailEditClickListener.onTaocanDishCancle(mOrderTaocanGroupDishEntity);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTaocanDishDetailEditClickListener.onTaocanDishDelete(mOrderTaocanGroupDishEntity);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String practiceId = null;
                if(mRadioGroup1.getChildCount() > 0 && mRadioGroup1.getCheckedRadioButtonId() > 0){
                    practiceId = (String) (mRadioGroup1.findViewById(mRadioGroup1.getCheckedRadioButtonId())).getTag();
                }
                mOrderTaocanGroupDishEntity.setPracticeId(practiceId);
                DishPracticeEntity dishPracticeEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getDishPracticeById(practiceId);
                if(dishPracticeEntity != null) {
                    mOrderTaocanGroupDishEntity.setPracticeName(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                }else{
                    mOrderTaocanGroupDishEntity.setPracticeName(null);
                }
                mOnTaocanDishDetailEditClickListener.onTaocanDishConfirm(mOrderTaocanGroupDishEntity);
            }
        });

        mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
    }
}
