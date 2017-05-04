package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.DishSpecifyEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.SpecifyEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeRadioGroup1;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/22 0022.
 */
public class LFragmentDishConfigEdit extends BaseFragment {
    private EditText mEtCount;
    private TextView tvName;
    private TextView tvPrice;
    private LinearLayout layoutGG, layoutZF;
    private CustomeRadioGroup1 mRadioGroup0, mRadioGroup1;
    private TextView etNote;
    private Button btnCancle, btnConfirm, btnDelete;
    private MainFragmentListener mOnDishDetailEditClickListener;
    private String dishId;
    private String specifyId;
    private String practiceId;
    private String orderDishId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDishDetailEditClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_dish_config_edit, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        specifyId = null;
        practiceId = null;
        tvName = (TextView) view.findViewById(R.id.tv_dish_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
        layoutGG = (LinearLayout) view.findViewById(R.id.layout_guige);
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zuofa);
        mRadioGroup0 = (CustomeRadioGroup1) view.findViewById(R.id.rg_guige);
        mRadioGroup1 = (CustomeRadioGroup1) view.findViewById(R.id.rg_zuofa);
        etNote = (TextView) view.findViewById(R.id.et_dish_note);
        btnCancle = (Button) view.findViewById(R.id.btn_cancle);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mEtCount = (EditText) view.findViewById(R.id.et_count);

        dishId = getArguments().getString("dishId");
        orderDishId = getArguments().getString("orderDishId");
    }

    public void setNewParam(String dishId,String orderDishId){
        this.dishId = dishId;
        this.orderDishId = orderDishId;

        initData();
    }

    private void initData(){
        mRadioGroup0.clearCheck();
        mRadioGroup1.clearCheck();
        mRadioGroup0.removeAllViews();
        mRadioGroup1.removeAllViews();
        specifyId = null;
        practiceId = null;
        DishEntity dishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneDishEntity(dishId);
        if (dishEntity != null) {
            tvPrice.setText("原价：￥" + dishEntity.getDishPrice());
            tvName.setText(dishEntity.getDishName());
            String specifyId = null, practiceId = null;
            if (orderDishId == null) {//配置菜品
                btnDelete.setVisibility(Button.GONE);
                etNote.setText("");
                mEtCount.setText("1");
            } else {//编辑菜品
                OrderDishEntity orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneOrderDishEntity(orderDishId);
                btnDelete.setVisibility(Button.VISIBLE);
                if (orderDishEntity != null) {
                    mEtCount.setText(""+orderDishEntity.getDishCount());
                    practiceId = orderDishEntity.getPracticeId();
                    specifyId = orderDishEntity.getSpecifyId();
                    if (orderDishEntity.getDishNote() != null)
                        etNote.setText(orderDishEntity.getDishNote());
                    else
                        etNote.setText("");
                }
            }

            if (DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasGuige(dishId) && DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasZuofa(dishId)) {
                layoutZF.setVisibility(LinearLayout.VISIBLE);
                layoutGG.setVisibility(LinearLayout.VISIBLE);
                setGuiGe(dishEntity.getDishId(), specifyId);
                setZuoFa(dishEntity.getDishId(), practiceId);
            } else if (DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasGuige(dishId)) {
                layoutGG.setVisibility(LinearLayout.VISIBLE);
                layoutZF.setVisibility(LinearLayout.GONE);
                setGuiGe(dishEntity.getDishId(), specifyId);
            } else if (DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasZuofa(dishId)) {
                layoutGG.setVisibility(LinearLayout.GONE);
                layoutZF.setVisibility(LinearLayout.VISIBLE);
                setZuoFa(dishEntity.getDishId(), practiceId);
            } else {
                layoutGG.setVisibility(LinearLayout.GONE);
                layoutZF.setVisibility(LinearLayout.GONE);
            }
        }
    }

    //设置规格
    private void setGuiGe(String dishId, String checkId) {
        DishSpecifyEntity dishSpecifyEntity;
        DishEntity dishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneDishEntity(dishId);
        ArrayList<DishSpecifyEntity> dishSpecifyEntities = DBHelper.getInstance(getActivity().getApplicationContext()).queryAllSpecify(dishId);
        for (int i = 0; i < dishSpecifyEntities.size(); i++) {
            dishSpecifyEntity = dishSpecifyEntities.get(i);
            SpecifyEntity specifyEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getSpecifyeById(dishSpecifyEntity.getSpecifyId());
            RadioButton radioButton = new RadioButton(getActivity().getApplicationContext());
            radioButton.setTextColor(getResources().getColor(R.color.dark));
            if (dishSpecifyEntity.getCustomPrice() == 0 && dishSpecifyEntity.getDefaultPrice() == 0) {
                radioButton.setText(Html.fromHtml("<font color=\"black\">" + DBHelper.getInstance(getActivity().getApplicationContext()).getSpecifyName(dishSpecifyEntity.getSpecifyId()) + "</font><font color=\"red\" size=\"8\">/" + specifyEntity.getPriceMultiple() * dishEntity.getDishPrice() + "元</font>"));
            } else {
                radioButton.setText(Html.fromHtml("<font color=\"black\">" + DBHelper.getInstance(getActivity().getApplicationContext()).getSpecifyName(dishSpecifyEntity.getSpecifyId()) + "</font><font color=\"red\" size=\"8\">/" + dishSpecifyEntity.getCustomPrice() + "元</font>"));
            }
            radioButton.setTextColor(getContext().getResources().getColor(R.color.dark));
            radioButton.setId(i);
            radioButton.setTextSize(16);
            radioButton.setButtonDrawable(getContext().getResources().getDrawable(R.drawable.custom_radiobutton));
            radioButton.setTag(dishSpecifyEntity.getDishSpecifyId());
            if (dishSpecifyEntity.getDishSpecifyId().equals(checkId)) {
                specifyId = checkId;
            }
            radioButton.setChecked(dishSpecifyEntity.getDishSpecifyId().equals(checkId));
            radioButton.setPadding(6, 6, 6, 6);
            mRadioGroup0.addView(radioButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mRadioGroup0.getChildCount() > 0 && mRadioGroup0.getCheckedRadioButtonId() < 0) {
            //默认选择第一个
            RadioButton radioButton = (RadioButton) mRadioGroup0.findViewWithTag(dishSpecifyEntities.get(0).getDishSpecifyId());
            radioButton.setChecked(true);
            specifyId = dishSpecifyEntities.get(0).getDishSpecifyId();
        }
    }

    //设置做法
    private void setZuoFa(String dishId, String checkId) {
        DishPracticeEntity dishPracticeEntity;
        ArrayList<DishPracticeEntity> dishPracticeEntities = DBHelper.getInstance(getActivity().getApplicationContext()).queryAllPractice(dishId);
        for (int i = 0; i < dishPracticeEntities.size(); i++) {
            dishPracticeEntity = dishPracticeEntities.get(i);
            RadioButton radioButton = new RadioButton(getActivity().getApplicationContext());
            radioButton.setTextColor(getResources().getColor(R.color.dark));
            //获取做法详情
            switch (dishPracticeEntity.getIncreaseMode()) {
                case 0://不加价
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
                case 1://一次性加价
                    radioButton.setText(Html.fromHtml("<font color=\"black\">" + DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()) + "</font><font color=\"red\" size=\"8\">+" + dishPracticeEntity.getIncreaseValue() + "元/一次性加价" + "</font>"));
                    break;
                case 2://每购买单位加价
                    radioButton.setText(Html.fromHtml("<font color=\"black\">" + DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()) + "</font><font color=\"red\" size=\"8\">+" + dishPracticeEntity.getIncreaseValue() + "元/每购买单位" + "</font>"));
                    break;
                case 3:
                    radioButton.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                    break;
            }
            radioButton.setId(i);
            radioButton.setTextSize(16);
            radioButton.setButtonDrawable(getContext().getResources().getDrawable(R.drawable.custom_radiobutton));
            radioButton.setTag(dishPracticeEntity.getDishPracticeId());
            if (dishPracticeEntity.getDishPracticeId().equals(checkId)) {
                practiceId = checkId;
            }
            radioButton.setChecked(dishPracticeEntity.getDishPracticeId().equals(checkId));
            radioButton.setPadding(6, 6, 6, 6);
            mRadioGroup1.addView(radioButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mRadioGroup1.getChildCount() > 0 && mRadioGroup1.getCheckedRadioButtonId() < 0) {
            //默认选择第一个
            RadioButton radioButton = (RadioButton) mRadioGroup1.findViewWithTag(dishPracticeEntities.get(0).getDishPracticeId());
            radioButton.setChecked(true);
            practiceId = dishPracticeEntities.get(0).getDishPracticeId();
        }
    }

    private void setListener() {
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDishDetailEditClickListener.onCancle(dishId);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDishDetailEditClickListener.onDelete(orderDishId);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtCount.getText() != null && !mEtCount.getText().toString().isEmpty()){
                    mOnDishDetailEditClickListener.onConfirm(dishId, specifyId, practiceId, orderDishId, etNote.getText().toString(), AmountUtils.multiply1(mEtCount.getText().toString(),"1"));
                }else{
                    CustomMethod.showMessage(getContext(),"请输入菜品数量");
                }
            }
        });
        mRadioGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    specifyId = (String) mRadioGroup0.findViewById(checkedId).getTag();
                }catch (Exception e){

                }
            }
        });
        mRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    practiceId = (String) mRadioGroup1.findViewById(checkedId).getTag();
                }catch (Exception e){
                }
            }
        });
        etNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMethod.addNote(getContext(), etNote);
            }
        });

        mEtCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mEtCount.setText("");
                }
            }
        });
    }
}
