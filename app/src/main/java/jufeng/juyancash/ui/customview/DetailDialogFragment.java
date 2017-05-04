package jufeng.juyancash.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liangfeizc.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.DishSelectedMaterialEntity;
import jufeng.juyancash.dao.DishSpecifyEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.SpecifyEntity;


/**
 * Created by Administrator102 on 2017/4/13.
 */

public class DetailDialogFragment extends DialogFragment {
    private EditText mEtCount;
    private TextView tvName;
    private TextView tvPrice;
    private LinearLayout layoutGG, layoutZF,layoutMark,layoutMaterial;
    private FlowLayout mZfFlowLayout, mMarkFlowLayout, mGGFlowLayout,mMaterialLayout;
    private ImageButton ibClose;
    private String specifyId;
    private String practiceIdLists;
    private String orderDishId;
    private List<String> mMarkLists;
    private OrderDishEntity orderDishEntity;
    private ArrayList<DishSelectedMaterialEntity> mDishSelectedMaterialEntities;

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
        dialog.setContentView(R.layout.layout_dish_detail_edit);
        dialog.setCanceledOnTouchOutside(true); //外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.LEFT; //紧贴底部
        lp.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.32); //宽度持平
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        mGGFlowLayout = (FlowLayout) view.findViewById(R.id.flow_gg);
        mZfFlowLayout = (FlowLayout) view.findViewById(R.id.flow_zf);
        mMarkFlowLayout = (FlowLayout) view.findViewById(R.id.flow_mark);
        mMaterialLayout = (FlowLayout) view.findViewById(R.id.flow_material);
        ibClose = (ImageButton) view.findViewById(R.id.ib_close);
        mEtCount = (EditText) view.findViewById(R.id.et_count);
        mEtCount.setEnabled(false);
    }

    private void initData() {
        this.orderDishId = getArguments().getString("orderDishId");
        orderDishEntity = DBHelper.getInstance(getActivity().getApplicationContext().getApplicationContext()).queryOneOrderDishEntity(orderDishId);
        mMarkLists = new ArrayList<>();
        specifyId = null;
        practiceIdLists = null;
        mDishSelectedMaterialEntities = new ArrayList<>();

        if (orderDishEntity != null) {
            tvPrice.setText("金额：￥" + orderDishEntity.getDishPrice());
            tvName.setText(orderDishEntity.getDishName());
            mEtCount.setText("" + orderDishEntity.getDishCount());
            practiceIdLists = orderDishEntity.getPracticeId();

            specifyId = orderDishEntity.getSpecifyId();
            mDishSelectedMaterialEntities.addAll(DBHelper.getInstance(getContext()).getSelectedDishMaterial(orderDishId));
            try {
                mMarkLists.addAll(Arrays.asList(orderDishEntity.getDishNote().split("`")));
            } catch (Exception e) {

            }
        }
        setGGAdapter();
        setZFAdapter();
        setMarkAdapter();
        setMaterialAdapter();
    }

    //设置规格
    private void setGGAdapter() {
        if (specifyId == null) {
            layoutGG.setVisibility(LinearLayout.GONE);
            return;
        }
        DishSpecifyEntity dishSpecifyEntity = DBHelper.getInstance(getContext()).getDishSpecifyById(specifyId);
        if (dishSpecifyEntity == null) {
            layoutGG.setVisibility(LinearLayout.GONE);
            return;
        }
        SpecifyEntity specifyEntity = DBHelper.getInstance(getContext()).getSpecifyeById(dishSpecifyEntity.getSpecifyId());
        if (specifyEntity == null) {
            layoutGG.setVisibility(LinearLayout.GONE);
            return;
        }
        DishEntity dishEntity = DBHelper.getInstance(getContext()).queryOneDishEntity(orderDishEntity.getDishId());
        if (dishEntity == null) {
            layoutGG.setVisibility(LinearLayout.GONE);
            return;
        }
        layoutGG.setVisibility(LinearLayout.VISIBLE);
        TextView tv = new TextView(getContext());
        tv.setTextColor(getContext().getResources().getColor(R.color.white));
        tv.setTextSize(14);
        tv.setGravity(Gravity.CENTER);
        tv.setMinWidth(80);
        tv.setPadding(8, 6, 8, 6);
        tv.setBackground(getContext().getResources().getDrawable(R.drawable.blue_layout));
        if (dishSpecifyEntity.getCustomPrice() == 0 && dishSpecifyEntity.getDefaultPrice() == 0) {
            tv.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(getContext()).getSpecifyName(dishSpecifyEntity.getSpecifyId()) + "</font><font color=\"red\">/" + specifyEntity.getPriceMultiple() * dishEntity.getDishPrice() + "元</font>"));
        } else {
            tv.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(getContext()).getSpecifyName(dishSpecifyEntity.getSpecifyId()) + "</font><font color=\"red\">/" + dishSpecifyEntity.getCustomPrice() + "元</font>"));
        }
        mGGFlowLayout.addView(tv);
    }

    //设置做法
    private void setZFAdapter() {
        if (practiceIdLists == null) {
            layoutZF.setVisibility(LinearLayout.GONE);
            return;
        }
        DishPracticeEntity dishPracticeEntity;
        dishPracticeEntity = DBHelper.getInstance(getContext()).getDishPracticeById(practiceIdLists);
        if (dishPracticeEntity == null) {
            layoutZF.setVisibility(LinearLayout.GONE);
            return;
        }
        layoutZF.setVisibility(LinearLayout.VISIBLE);
        TextView tv = new TextView(getContext());
        tv.setTextColor(getContext().getResources().getColor(R.color.white));
        tv.setTextSize(14);
        tv.setMinWidth(80);
        tv.setPadding(8, 6, 8, 6);
        tv.setGravity(Gravity.CENTER);
        tv.setBackground(getContext().getResources().getDrawable(R.drawable.blue_layout));
        switch (dishPracticeEntity.getIncreaseMode()) {
            case 0://不加价
                tv.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(getContext()).getPracticeName(dishPracticeEntity.getPracticeId()) + "</font>"));
                break;
            case 1://一次性加价
                tv.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(getContext()).getPracticeName(dishPracticeEntity.getPracticeId()) + "</font><font color=\"red\">+" + dishPracticeEntity.getIncreaseValue() + "元" + "</font>"));
                break;
            case 2://每购买单位加价
                tv.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(getContext()).getPracticeName(dishPracticeEntity.getPracticeId()) + "</font><font color=\"red\">+" + dishPracticeEntity.getIncreaseValue() + "元" + "</font>"));
                break;
            case 3:
                tv.setText(DBHelper.getInstance(getContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                break;
            default:
                tv.setText(DBHelper.getInstance(getContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                break;
        }
        mZfFlowLayout.addView(tv);
    }

    //设置规格
    private void setMarkAdapter() {
        if (mMarkLists.size() == 0) {
            layoutMark.setVisibility(LinearLayout.GONE);
            return;
        }
        layoutMark.setVisibility(LinearLayout.VISIBLE);
        for (String mark :
                mMarkLists) {
            TextView tv = new TextView(getContext());
            tv.setTextColor(getContext().getResources().getColor(R.color.white));
            tv.setTextSize(14);
            tv.setMinWidth(80);
            tv.setPadding(8, 6, 8, 6);
            tv.setGravity(Gravity.CENTER);
            tv.setBackground(getContext().getResources().getDrawable(R.drawable.blue_layout));
            tv.setText(mark);
            mMarkFlowLayout.addView(tv);
        }
    }

    //设置规格
    private void setMaterialAdapter() {
        if (mDishSelectedMaterialEntities.size() == 0) {
            layoutMaterial.setVisibility(LinearLayout.GONE);
            return;
        }
        layoutMaterial.setVisibility(LinearLayout.VISIBLE);
        for (DishSelectedMaterialEntity dishSelectedMaterialEntity :
                mDishSelectedMaterialEntities) {
            TextView tv = new TextView(getContext());
            tv.setTextColor(getContext().getResources().getColor(R.color.white));
            tv.setTextSize(14);
            tv.setMinWidth(80);
            tv.setPadding(8, 6, 8, 6);
            tv.setGravity(Gravity.CENTER);
            tv.setBackground(getContext().getResources().getDrawable(R.drawable.blue_layout));
            tv.setText(dishSelectedMaterialEntity.getMaterialName()+" X "+dishSelectedMaterialEntity.getSelectedCount());
            mMaterialLayout.addView(tv);
        }
    }

    private void setListener() {
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
