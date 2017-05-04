package jufeng.juyancash.ui.fragment.snack;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.eventbus.SnackCashConfirmEvent;
import jufeng.juyancash.eventbus.SnackOpenCashBoxEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentSnackPayModeCash extends BaseFragment {
    private RelativeLayout layoutIncome, layoutChange;
    private TextView tvReceivable, tvChange;
    private EditText etIncome;
    private TextView tvCancle, tvConfirm, tvName;
    private ShopPaymentEntity payModeEntity;
    private String orderId;

    public static LFragmentSnackPayModeCash newInstance(String orderId,ShopPaymentEntity payModeEntity){
        LFragmentSnackPayModeCash fragment = new LFragmentSnackPayModeCash();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        bundle.putParcelable("payment",payModeEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_paymode_cash, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvReceivable = (TextView) view.findViewById(R.id.tv_receivable_money);
        etIncome = (EditText) view.findViewById(R.id.et_income);
        tvChange = (TextView) view.findViewById(R.id.tv_change);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        layoutIncome = (RelativeLayout) view.findViewById(R.id.layout_income);
        layoutChange = (RelativeLayout) view.findViewById(R.id.layout_change);

        orderId = getArguments().getString("orderId");
        payModeEntity = getArguments().getParcelable("payment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initData(){
        layoutIncome.setTag(1);
        layoutChange.setTag(0);
        setLayoutBackground();

        CustomMethod.setMyInputType1(etIncome,getActivity());
        EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
        etIncome.requestFocus();

        tvName.setText(payModeEntity.getPaymentName());
        double restMoney = AmountUtils.multiply(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId),0.01);
        tvReceivable.setText(String.valueOf(restMoney));
        etIncome.setText(String.valueOf(restMoney < 0? 0:restMoney));
        tvChange.setText(String.valueOf(restMoney < 0 ? -restMoney : 0.0));
    }

    private void setLayoutBackground() {
        if ((int) layoutIncome.getTag() == 1) {
            layoutIncome.setBackgroundResource(R.drawable.edittext_focus_background);
        } else {
            layoutIncome.setBackgroundResource(R.drawable.edittext_background);
        }
    }

    private void setListener() {
        layoutIncome.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                layoutIncome.setTag(1);
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
                etIncome.requestFocus();
                setLayoutBackground();
            }
        });
        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layoutIncome.setTag(1);
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
                etIncome.requestFocus();
                setLayoutBackground();
                return false;
            }
        });
        etIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() > 0) {
                    String value = s.toString();
                    double income = AmountUtils.multiply1(value,"1");
                    double receivable = AmountUtils.multiply1(tvReceivable.getText().toString(),"1");
                    if (income > receivable) {
                        tvChange.setText(AmountUtils.subtract(income+"",receivable+""));
                    } else {
                        tvChange.setText("0.0");
                    }
                }else{
                    tvChange.setText("0.0");
                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackOpenTopRightEvent());
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                if(etIncome.getText() != null && etIncome.getText().length() > 0 && tvChange.getText() != null && tvChange.getText().length() > 0) {
                    double income = AmountUtils.multiply1(etIncome.getText().toString(),"1");
                    double change = AmountUtils.multiply1(tvChange.getText().toString(),"1");
                    double payMoney = AmountUtils.subtract(income, change);
                    DBHelper.getInstance(getActivity().getApplicationContext()).insertPayModeCash(orderId, payModeEntity.getPaymentId(), payModeEntity.getPaymentName(), 0, Float.valueOf(""+payMoney), 0,income+"`"+change);
                    EventBus.getDefault().post(new SnackCashConfirmEvent(etIncome.getText().toString()));
                    if(payModeEntity != null && payModeEntity.getIsOpenCashBox() != null && payModeEntity.getIsOpenCashBox() == 1) {
                        EventBus.getDefault().post(new SnackOpenCashBoxEvent());
                    }

                    if (change > 0) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("现金找零");
                        alertDialog.setMessage("应找零￥" + change + "元");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }else{
                    CustomMethod.showMessage(getContext(),"请输入实收金额");
                }
            }
        });
    }
}
