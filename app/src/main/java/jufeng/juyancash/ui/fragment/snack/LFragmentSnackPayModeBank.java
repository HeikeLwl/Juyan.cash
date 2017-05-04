package jufeng.juyancash.ui.fragment.snack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.eventbus.SnackBankConfirmEvent;
import jufeng.juyancash.eventbus.SnackOpenCashBoxEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentSnackPayModeBank extends BaseFragment implements View.OnClickListener {
    private TextView tvName;
    private TextView tvReceivable;
    private EditText etIncome;
    private TextView tvCancle, tvConfirm;
    private String orderId;
    private ShopPaymentEntity payModeEntity;

    public static LFragmentSnackPayModeBank newInstance(String orderId,ShopPaymentEntity payModeEntity){
        LFragmentSnackPayModeBank fragment = new LFragmentSnackPayModeBank();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        bundle.putParcelable("payment",payModeEntity);
        fragment.setArguments(bundle);
        return fragment;
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
        payModeEntity = getArguments().getParcelable("payment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initData(){
        tvName.setText(payModeEntity.getPaymentName());
            tvReceivable.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
            etIncome.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));

        CustomMethod.setMyInputType1(etIncome,getActivity());
        EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
        etIncome.requestFocus();
    }

    private void setListener() {
        tvCancle.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        etIncome.setOnTouchListener(new View.OnTouchListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etIncome));
                etIncome.requestFocus();
                return false;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                EventBus.getDefault().post(new SnackOpenTopRightEvent());
                break;
            case R.id.tv_confirm:
                if(etIncome.getText() != null && etIncome.getText().length() > 0) {
                    String receivableMoney = tvReceivable.getText().toString();
                    String income = etIncome.getText().toString();
                    float money = 0f;
                    if (income.length() > 0) {
                        money = Float.valueOf(income);
                    }
                    if (money > Float.valueOf(receivableMoney) || money <= 0) {
                        CustomMethod.showMessage(getContext(),"实收金额不允许大于应收金额，请确认实收金额");
                    }else if(money <= 0){
                        CustomMethod.showMessage(getContext(),"实收金额必须大于0，请确认实收金额");
                    } else {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, payModeEntity.getPaymentId(), payModeEntity.getPaymentName(), 1, money, 0);
                        EventBus.getDefault().post(new SnackBankConfirmEvent());
                        if(payModeEntity != null && payModeEntity.getIsOpenCashBox() != null && payModeEntity.getIsOpenCashBox() == 1) {
                            EventBus.getDefault().post(new SnackOpenCashBoxEvent());
                        }
                    }
                }else{
                    CustomMethod.showMessage(getContext(),"请输入实收金额");
                }
                break;
        }
    }
}
