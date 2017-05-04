package jufeng.juyancash.ui.fragment;

import android.content.Context;
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

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentPayModeCash extends BaseFragment {
    private RelativeLayout layoutIncome, layoutChange;
    private TextView tvReceivable, tvChange;
    private EditText etIncome;
    private TextView tvCancle, tvConfirm, tvName;
    private ShopPaymentEntity payModeEntity;
    private String orderId;
    private boolean isOpenJoinOrder;
    private MainFragmentListener mOnPayModeCashClickListener;
    private InitKeyboardInterface mInitKeyboardInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPayModeCashClickListener = (MainFragmentListener) context;
            mInitKeyboardInterface = (InitKeyboardInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
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
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
        payModeEntity = getArguments().getParcelable("payment");
    }

    public void setNewParam(String param0, boolean param1, Payment param2){
        this.orderId = param0;
        this.isOpenJoinOrder = param1;
        this.payModeEntity = (ShopPaymentEntity) param2;

        initData();
    }

    private void initData(){
        layoutIncome.setTag(1);
        layoutChange.setTag(0);
        setLayoutBackground();

        CustomMethod.setMyInputType1(etIncome,getActivity());
        if(mInitKeyboardInterface != null){
            mInitKeyboardInterface.setEdittext(etIncome);
        }
        etIncome.requestFocus();

        tvName.setText(payModeEntity.getPaymentName());
        double restMoney = jufeng.juyancash.util.AmountUtils.multiply(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId),0.01);
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
            @Override
            public void onClick(View v) {
                layoutIncome.setTag(1);
                if(mInitKeyboardInterface != null)
                    mInitKeyboardInterface.setEdittext(etIncome);
                etIncome.requestFocus();
                setLayoutBackground();
            }
        });
        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layoutIncome.setTag(1);
                if(mInitKeyboardInterface != null)
                    mInitKeyboardInterface.setEdittext(etIncome);
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
            @Override
            public void onClick(View v) {
                mOnPayModeCashClickListener.onCashCancle();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etIncome.getText() != null && etIncome.getText().length() > 0 && tvChange.getText() != null && tvChange.getText().length() > 0) {
                    double income = AmountUtils.multiply1(etIncome.getText().toString(),"1");
                    double change = AmountUtils.multiply1(tvChange.getText().toString(),"1");
                    double payMoney = AmountUtils.subtract(income, change);
                    DBHelper.getInstance(getActivity().getApplicationContext()).insertPayModeCash(orderId, payModeEntity.getPaymentId(), payModeEntity.getPaymentName(), 0, Float.valueOf(""+payMoney), isOpenJoinOrder ? 1 : 0,income+"`"+change);
                    mOnPayModeCashClickListener.onCashConfirm(etIncome.getText().toString());
                    if(payModeEntity != null && payModeEntity.getIsOpenCashBox() != null && payModeEntity.getIsOpenCashBox() == 1) {
                        mOnPayModeCashClickListener.openCashBox();
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
