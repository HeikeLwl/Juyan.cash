package jufeng.juyancash.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.DiscountSpinnerAdapter;
import jufeng.juyancash.adapter.MarkAdapter;
import jufeng.juyancash.adapter.PracticeAdapter;
import jufeng.juyancash.adapter.SelectMaterialAdapter;
import jufeng.juyancash.adapter.SpecifyAdapter;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.eventbus.SnackDishConfigConfirmEvent;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/4/13.
 */

public class ConfigDialogFragment extends DialogFragment implements CashierKeyboardUtil2.OnCashierKeyBoardClickListener {
    private EditText mEtCount;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvNote;
    private LinearLayout layoutGG, layoutZF,layoutMark,layoutMaterial,layoutDiscount;
    private RecyclerView mZfRecyclerview,mMarkRecyclerview,mGGRecyclerview,mMaterialRecyclerView;
    private AppCompatSpinner mAppCompatSpinner;
    private SpecifyAdapter mSpecifyAdapter;
    private PracticeAdapter mPracticeAdapter;
    private MarkAdapter mMarkAdapter;
    private SelectMaterialAdapter mMaterialAdapter;
    private ImageButton ibConfirm;
    private String dishId;
    private String specifyId;
    private String practiceId;
    private String orderDishId;
    private CashierKeyboardUtil2 mKeyboardUtil1;
    private List<String> allMarks;
    private List<String>  mMarkLists;
    private List<String> mCustomMarks;
    private String orderId;
    private ArrayList<DiscountEntity> mDiscountEntities;

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置Content前设定
        dialog.setContentView(R.layout.layout_dish_config_edit);
        dialog.setCanceledOnTouchOutside(true); //外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM|Gravity.RIGHT; //紧贴底部
        lp.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.68); //宽度持平
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(window);
        initData();
        setListener();
        return dialog;
    }

    private void initView(Window view) {
        tvName = (TextView) view.findViewById(R.id.tv_dish_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
        layoutGG = (LinearLayout) view.findViewById(R.id.layout_gg);
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zf);
        layoutMark = (LinearLayout) view.findViewById(R.id.layout_mark);
        layoutMaterial = (LinearLayout) view.findViewById(R.id.layout_material);
        layoutDiscount = (LinearLayout) view.findViewById(R.id.layout_discount);
        mAppCompatSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_discount);
        mGGRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_gg);
        mZfRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_zf);
        mMarkRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_mark);
        mMaterialRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_material);
        ibConfirm = (ImageButton) view.findViewById(R.id.ib_confirm);
        mEtCount = (EditText) view.findViewById(R.id.et_count);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
        mKeyboardUtil1 = new CashierKeyboardUtil2(getContext(), view, mEtCount);
        mKeyboardUtil1.showKeyboard();
        CustomMethod.setMyInputType1(mEtCount,getActivity());
        mEtCount.requestFocus();
        mEtCount.setEnabled(false);
    }

    @Override
    public void onCashierKeyClick(int keyCode) {
    }

    private void initData() {
        allMarks = new ArrayList<>();
        allMarks.addAll(DBHelper.getInstance(getContext()).getAllMarks());
        this.orderDishId = getArguments().getString("orderDishId");
        this.dishId = getArguments().getString("dishId");
        this.orderId = getArguments().getString("orderId");
        mMarkLists = new ArrayList<>();
        mCustomMarks = new ArrayList<>();
//        mDiscountEntities = new ArrayList<>();
//        mDiscountEntities.addAll(DBHelper.getInstance(getContext()).getAllDiscountEntities(1));
//        DiscountEntity discountEntity = new DiscountEntity();
//        discountEntity.setDiscountName("不使用促销方案");
//        mDiscountEntities.add(0,discountEntity);
        specifyId = null;
        practiceId = null;
        DishEntity dishEntity = DBHelper.getInstance(getContext()).queryOneDishEntity(dishId);
        if (dishEntity != null) {
            tvPrice.setText("金额：￥" + dishEntity.getDishPrice());
            tvName.setText(dishEntity.getDishName());
            if (orderDishId == null) {//配置菜品
                mEtCount.setText("1");
            } else {//编辑菜品
                OrderDishEntity orderDishEntity = DBHelper.getInstance(getContext()).queryOneOrderDishEntity(orderDishId);
                if (orderDishEntity != null) {
                    mEtCount.setText("" + orderDishEntity.getDishCount());
                    practiceId = orderDishEntity.getPracticeId();
                    specifyId = orderDishEntity.getSpecifyId();
                    try {
                        mCustomMarks.addAll(Arrays.asList(orderDishEntity.getDishNote().split("`")));
                        for (String mark :
                                allMarks) {
                            if (mCustomMarks.contains(mark)) {
                                mCustomMarks.remove(mark);
                                mMarkLists.add(mark);
                            }
                        }
                    }catch (Exception e){

                    }
                }
            }
            setGGAdapter();
            setZFAdapter();
            setMarkAdapter();
            setMaterialAdapter();
//            setDiscount();
            setCustomMarks();
        }
    }

    //设置规格
    private void setGGAdapter(){
        mSpecifyAdapter = new SpecifyAdapter(getContext(),dishId,specifyId);
        mGGRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGGRecyclerview.setItemAnimator(null);
        mGGRecyclerview.setHasFixedSize(true);
        mGGRecyclerview.setAdapter(mSpecifyAdapter);
        if(mSpecifyAdapter.getItemCount() <= 0){
            layoutGG.setVisibility(LinearLayout.GONE);
        }else{
            layoutGG.setVisibility(LinearLayout.VISIBLE);
        }
    }

    //设置做法
    private void setZFAdapter(){
        mPracticeAdapter = new PracticeAdapter(getContext(),dishId, practiceId);
        mZfRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mZfRecyclerview.setItemAnimator(null);
        mZfRecyclerview.setHasFixedSize(true);
        mZfRecyclerview.setAdapter(mPracticeAdapter);
        if(mPracticeAdapter.getItemCount() <= 0){
            layoutZF.setVisibility(LinearLayout.GONE);
        }else{
            layoutZF.setVisibility(LinearLayout.VISIBLE);
        }
    }

    //设置备注
    private void setMarkAdapter(){
        mMarkAdapter = new MarkAdapter(getContext(), mMarkLists);
        mMarkRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mMarkRecyclerview.setItemAnimator(null);
        mMarkRecyclerview.setHasFixedSize(true);
        mMarkRecyclerview.setAdapter(mMarkAdapter);
        if(mMarkAdapter.getItemCount() <= 0){
            layoutMark.setVisibility(LinearLayout.GONE);
        }else{
            layoutMark.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void setCustomMarks() {
        StringBuilder sb = new StringBuilder();
        for (String mark :
                mCustomMarks) {
            sb.append(mark);
            sb.append("、");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            tvNote.setText(sb.toString());
        }
    }

    //设置加料
    private void setMaterialAdapter(){
        mMaterialAdapter = new SelectMaterialAdapter(getContext(),orderDishId,dishId,orderId);
        mMaterialRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMaterialRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mMaterialRecyclerView.setItemAnimator(null);
        mMaterialRecyclerView.setHasFixedSize(true);
        mMaterialRecyclerView.setAdapter(mMaterialAdapter);
        if(mMaterialAdapter.getItemCount() <= 0){
            layoutMaterial.setVisibility(LinearLayout.GONE);
        }else{
            layoutMaterial.setVisibility(LinearLayout.VISIBLE);
        }
    }

    //设置促销方案
    private void setDiscount(){
        if(mDiscountEntities.size() > 0){
            layoutDiscount.setVisibility(LinearLayout.VISIBLE);
        }else{
            layoutDiscount.setVisibility(LinearLayout.GONE);
        }
        mAppCompatSpinner.setAdapter(new DiscountSpinnerAdapter(getActivity().getApplicationContext(), mDiscountEntities));
    }

    private void setListener() {
//        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if(mAppCompatSpinner.getTag() != null) {
//                    if (position == 0) {
//
//                    } else {
//
//                    }
//                }else{
//                    mAppCompatSpinner.setTag(position);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        mKeyboardUtil1.setCashierKeyBoardClickListener(this);
        ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                String specify = mSpecifyAdapter.getselectedSpecifyId();
                String practices = mPracticeAdapter.getSelectedPractices();
                String marks = mMarkAdapter.getSelectedMarks();
                if (marks.length() > 0) {
                    if (!TextUtils.isEmpty(tvNote.getText().toString())) {
                        marks += "`" + tvNote.getText().toString();
                    }
                }else{
                    if (!TextUtils.isEmpty(tvNote.getText().toString())) {
                        marks += tvNote.getText().toString();
                    }
                }
                if(TextUtils.isEmpty(practices)){
                    practices = null;
                }
                if(TextUtils.isEmpty(marks)){
                    marks = null;
                }
                if (mEtCount.getText() != null && !mEtCount.getText().toString().isEmpty()) {
                    EventBus.getDefault().post(new SnackDishConfigConfirmEvent(orderDishId, dishId, specify, practices, marks, AmountUtils.multiply1(mEtCount.getText().toString(), "1"),mMaterialAdapter.getDishSelectedMaterialEntities()));
                    dismiss();
                } else {
                    Snackbar.make(mEtCount, "请输入菜品数量", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        tvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomMethod.addNote(getContext(), tvNote);
            }
        });
    }
}
