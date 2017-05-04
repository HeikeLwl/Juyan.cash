package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class LFragmentPayModeBank extends BaseFragment implements View.OnClickListener {
    private TextView tvName;
    private TextView tvReceivable;
    private EditText etIncome;
    private TextView tvCancle, tvConfirm;
    private String orderId;
    private boolean isOpenJoinOrder;
    private ShopPaymentEntity payModeEntity;
    private MainFragmentListener mOnPayModeCahsClickListener;
    private InitKeyboardInterface mInitKeyboardInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPayModeCahsClickListener = (MainFragmentListener) context;
            mInitKeyboardInterface = (InitKeyboardInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_paymode_bank, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvReceivable = (TextView) view.findViewById(R.id.tv_receivable_money);
        etIncome = (EditText) view.findViewById(R.id.et_income);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        orderId = getArguments().getString("orderId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
        payModeEntity = getArguments().getParcelable("payment");
    }

    public void setNewParam(String param0, boolean param1, Payment payment) {
        this.orderId = param0;
        this.isOpenJoinOrder = param1;
        this.payModeEntity = (ShopPaymentEntity) payment;

        initData();
    }

    private void initData() {
        tvName.setText(payModeEntity.getPaymentName());
        tvReceivable.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
        etIncome.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));

        CustomMethod.setMyInputType1(etIncome, getActivity());
        if (mInitKeyboardInterface != null) {
            mInitKeyboardInterface.setEdittext(etIncome);
        }
        etIncome.requestFocus();
    }

    private void setListener() {
        tvCancle.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mInitKeyboardInterface != null) {
                    mInitKeyboardInterface.setEdittext(etIncome);
                }
                etIncome.requestFocus();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                mOnPayModeCahsClickListener.onBankCancle();
                break;
            case R.id.tv_confirm:
                if (etIncome.getText() != null && etIncome.getText().length() > 0) {
                    String receivableMoney = tvReceivable.getText().toString();
                    String income = etIncome.getText().toString();
                    float money = 0f;
                    if (income.length() > 0) {
                        money = Float.valueOf(income);
                    }
                    if (money > Float.valueOf(receivableMoney) || money <= 0) {
                        CustomMethod.showMessage(getContext(), "实收金额不允许大于应收金额，请确认实收金额");
                    } else if (money <= 0) {
                        CustomMethod.showMessage(getContext(), "实收金额必须大于0，请确认实收金额");
                    } else {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, payModeEntity.getPaymentId(), payModeEntity.getPaymentName(), 1, money, isOpenJoinOrder ? 1 : 0);
                        mOnPayModeCahsClickListener.onBankConfirm();
                        if (payModeEntity != null && payModeEntity.getIsOpenCashBox() != null && payModeEntity.getIsOpenCashBox() == 1) {
                            mOnPayModeCahsClickListener.openCashBox();
                        }
                    }
                } else {
                    CustomMethod.showMessage(getContext(), "请输入实收金额");
                }
                break;
        }
    }
}
