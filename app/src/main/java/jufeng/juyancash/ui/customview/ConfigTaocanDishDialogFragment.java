package jufeng.juyancash.ui.customview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import jufeng.juyancash.adapter.MarkAdapter;
import jufeng.juyancash.adapter.PracticeAdapter;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.eventbus.SnackTaocanChangeDishEvent;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/4/13.
 */

public class ConfigTaocanDishDialogFragment extends DialogFragment {
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvNote;
    private LinearLayout layoutZF, layoutMark;
    private RecyclerView mZfRecyclerview, mMarkRecyclerview;
    private PracticeAdapter mPracticeAdapter;
    private MarkAdapter mMarkAdapter;
    private Button btnConfirm;
    private String practiceId;
    private OrderTaocanGroupDishEntity mOrderTaocanGroupDishEntity;
    private OrderDishEntity mOrderDishEntity;
    private List<String> allMarks;
    private List<String> mMarkLists;
    private List<String> mCustomMarks;

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
        dialog.setContentView(R.layout.layout_taocan_dish_config_edit);
        dialog.setCanceledOnTouchOutside(true); //外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM | Gravity.RIGHT; //紧贴底部
        lp.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.68); //宽度持平
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
        layoutZF = (LinearLayout) view.findViewById(R.id.layout_zf);
        layoutMark = (LinearLayout) view.findViewById(R.id.layout_mark);
        mZfRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_zf);
        mMarkRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_mark);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
    }

    private void initData() {
        allMarks = new ArrayList<>();
        allMarks.addAll(DBHelper.getInstance(getContext()).getAllMarks());
        mMarkLists = new ArrayList<>();
        mCustomMarks = new ArrayList<>();
        practiceId = null;
        this.mOrderDishEntity = getArguments().getParcelable("orderDishEntity");
        mOrderTaocanGroupDishEntity = getArguments().getParcelable("orderTaocanGroupDishEntity");
        String dishId = mOrderTaocanGroupDishEntity.getDishId();
        DishEntity dishEntity = DBHelper.getInstance(getActivity().getApplicationContext()).queryOneDishEntity(dishId);
        if (dishEntity != null) {
            tvPrice.setText("加价￥" + mOrderTaocanGroupDishEntity.getExtraPrice() + "元");
            tvName.setText(dishEntity.getDishName());
            practiceId = mOrderTaocanGroupDishEntity.getPracticeId();
            if (DBHelper.getInstance(getActivity().getApplicationContext()).isDishHasZuofa(dishId)) {
                layoutZF.setVisibility(LinearLayout.VISIBLE);
            } else {
                layoutZF.setVisibility(LinearLayout.GONE);
            }
        }
        try {
            mCustomMarks.addAll(Arrays.asList(mOrderTaocanGroupDishEntity.getRemark().split("`")));
            for (String mark :
                    allMarks) {
                if (mCustomMarks.contains(mark)) {
                    mCustomMarks.remove(mark);
                    mMarkLists.add(mark);
                }
            }
        } catch (Exception e) {

        }
        setZFAdapter();
        setMarkAdapter();
        setCustomMarks();
    }

    //设置做法
    private void setZFAdapter() {
        mPracticeAdapter = new PracticeAdapter(getContext(), mOrderTaocanGroupDishEntity.getDishId(), practiceId);
        mZfRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mZfRecyclerview.setItemAnimator(null);
        mZfRecyclerview.setHasFixedSize(true);
        mZfRecyclerview.setAdapter(mPracticeAdapter);
        if (mPracticeAdapter.getItemCount() <= 0) {
            layoutZF.setVisibility(LinearLayout.GONE);
        } else {
            layoutZF.setVisibility(LinearLayout.VISIBLE);
        }
    }

    //设置备注
    private void setMarkAdapter() {
        mMarkAdapter = new MarkAdapter(getContext(), mMarkLists);
        mMarkRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mMarkRecyclerview.setItemAnimator(null);
        mMarkRecyclerview.setHasFixedSize(true);
        mMarkRecyclerview.setAdapter(mMarkAdapter);
        if (mMarkAdapter.getItemCount() <= 0) {
            layoutMark.setVisibility(LinearLayout.GONE);
        } else {
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

    private void setListener() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                practiceId = mPracticeAdapter.getSelectedPractices();
                mOrderTaocanGroupDishEntity.setPracticeId(practiceId);
                DishPracticeEntity dishPracticeEntity = DBHelper.getInstance(getActivity().getApplicationContext()).getDishPracticeById(practiceId);
                if (dishPracticeEntity != null) {
                    mOrderTaocanGroupDishEntity.setPracticeName(DBHelper.getInstance(getActivity().getApplicationContext()).getPracticeName(dishPracticeEntity.getPracticeId()));
                } else {
                    mOrderTaocanGroupDishEntity.setPracticeName(null);
                }
                String marks = mMarkAdapter.getSelectedMarks();
                if (marks.length() > 0) {
                    if (!TextUtils.isEmpty(tvNote.getText().toString())) {
                        marks += "`" + tvNote.getText().toString();
                    }
                }else{
                    if (!TextUtils.isEmpty(tvNote.getText().toString())) {
                        marks += tvNote.getText().toString();
                    }else{
                        marks = null;
                    }
                }
                mOrderTaocanGroupDishEntity.setRemark(marks);
                DBHelper.getInstance(getContext().getApplicationContext()).changeTaocanGroupDish(mOrderTaocanGroupDishEntity);
                EventBus.getDefault().post(new SnackTaocanChangeDishEvent(mOrderTaocanGroupDishEntity));
                dismiss();
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
