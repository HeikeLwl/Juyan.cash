package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.BillAccountEntity;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentPayModeAccount extends BaseFragment implements View.OnClickListener {
    private TextView tvName;
    private TextView tvReceivable;
    private EditText etIncome;
    private TextView tvAccountUnit;
    private TextView tvAccountPeople;
    private TextView tvSignPeople;
    private TextView tvCancle, tvConfirm;
    private String orderId;
    private boolean isOpenJoinOrder;
    private MainFragmentListener mOnPayModeAccountClickListener;
    private BillAccountHistoryEntity mBillAccountHistoryEntity;
    private InitKeyboardInterface mInitKeyboardInterface;
    private BillAccountEntity billAccountEntity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPayModeAccountClickListener = (MainFragmentListener) context;
            mInitKeyboardInterface = (InitKeyboardInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_paymode_account, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvReceivable = (TextView) view.findViewById(R.id.tv_receivable_money);
        etIncome = (EditText) view.findViewById(R.id.et_income);
        tvAccountUnit = (TextView) view.findViewById(R.id.tv_account_unit);
        tvAccountPeople = (TextView) view.findViewById(R.id.tv_account_people);
        tvSignPeople = (TextView) view.findViewById(R.id.tv_sign_people);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        orderId = getArguments().getString("orderId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
        mBillAccountHistoryEntity = getArguments().getParcelable("billHistory");
        billAccountEntity = getArguments().getParcelable("payment");
    }

    public void setNewParam(String orderId, boolean isOpenJoinOrder, BillAccountHistoryEntity billHistory, Payment payMent) {
        this.orderId = orderId;
        this.isOpenJoinOrder = isOpenJoinOrder;
        mBillAccountHistoryEntity = billHistory;
        billAccountEntity = (BillAccountEntity) payMent;

        initData();
    }

    private void initData() {
        if (mBillAccountHistoryEntity == null) {
            mBillAccountHistoryEntity = new BillAccountHistoryEntity();
            mBillAccountHistoryEntity.setBillAccountHistoryId(UUID.randomUUID().toString());
            mBillAccountHistoryEntity.setBillAccountId(billAccountEntity.getBillAccountId());
            mBillAccountHistoryEntity.setBillAccountName(billAccountEntity.getBillAccountName());
            mBillAccountHistoryEntity.setOrderId(orderId);
            mBillAccountHistoryEntity.setBillAccountMoney(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId));
        }
        if (mBillAccountHistoryEntity.getBillAccountPersonName() != null) {
            tvAccountPeople.setText(mBillAccountHistoryEntity.getBillAccountPersonName());
        } else {
            tvAccountPeople.setText("选择挂账人");
        }
        if (mBillAccountHistoryEntity.getBillAccountUnitName() != null) {
            tvAccountUnit.setText(mBillAccountHistoryEntity.getBillAccountUnitName());
        } else {
            tvAccountUnit.setText("选择挂账单位");
        }
        if (mBillAccountHistoryEntity.getBillAccountSignName() != null) {
            tvSignPeople.setText(mBillAccountHistoryEntity.getBillAccountSignName());
        } else {
            tvSignPeople.setText("选择签字人");
        }
        tvReceivable.setText(AmountUtils.changeF2Y(DBHelper.getInstance(getActivity().getApplicationContext()).getReceivableMoneyByOrderId(orderId)));
        etIncome.setText(String.valueOf((float) mBillAccountHistoryEntity.getBillAccountMoney() / 100));
        tvName.setText(mBillAccountHistoryEntity.getBillAccountName());
        CustomMethod.setMyInputType1(etIncome, getActivity());
        if (mInitKeyboardInterface != null) {
            mInitKeyboardInterface.setEdittext(etIncome);
        }
        etIncome.requestFocus();
    }

    private void setListener() {
        tvAccountUnit.setOnClickListener(this);
        tvAccountPeople.setOnClickListener(this);
        tvSignPeople.setOnClickListener(this);
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
        etIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    float fCurValue = Float.valueOf(s.toString());
                    mBillAccountHistoryEntity.setBillAccountMoney((int) (fCurValue * 100));
                } else {
                    mBillAccountHistoryEntity.setBillAccountMoney(0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_account_unit:
                mOnPayModeAccountClickListener.onAccountUnitClick(mBillAccountHistoryEntity);
                break;
            case R.id.tv_account_people:
                mOnPayModeAccountClickListener.onAccountPeopleClick(mBillAccountHistoryEntity);
                break;
            case R.id.tv_sign_people:
                mOnPayModeAccountClickListener.onSignPeopleClick(mBillAccountHistoryEntity);
                break;
            case R.id.tv_cancle:
                mOnPayModeAccountClickListener.onAccountCancle();
                break;
            case R.id.tv_confirm:
                if (mBillAccountHistoryEntity.getBillAccountMoney() <= 0) {
                    CustomMethod.showMessage(getContext(), "挂账金额不允许小于或等于0，请确认挂账金额");
                } else if ((float) mBillAccountHistoryEntity.getBillAccountMoney() / 100 > Float.valueOf(tvReceivable.getText().toString())) {
                    CustomMethod.showMessage(getContext(), "挂账金额不允许大于应收金额，请确认挂账金额");
                } else {
                    EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
                    if (employeeEntity.getAuthBillAccount() == 1) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, mBillAccountHistoryEntity.getBillAccountId(), mBillAccountHistoryEntity.getBillAccountName(), 6, (float) mBillAccountHistoryEntity.getBillAccountMoney() / 100, isOpenJoinOrder ? 1 : 0);
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertBillAccountHistory(mBillAccountHistoryEntity, isOpenJoinOrder ? 1 : 0);
                        mOnPayModeAccountClickListener.onAccountConfirm(orderId, mBillAccountHistoryEntity);
                    } else {
                        final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 7);
                        dialog.setArguments(bundle);
                        dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                            @Override
                            public void onAuthorityCancle() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                                if (employeeEntity != null && employeeEntity.getAuthBillAccount() == 1) {
                                    DBHelper.getInstance(getActivity().getApplicationContext()).insertPayMode(orderId, mBillAccountHistoryEntity.getBillAccountId(), mBillAccountHistoryEntity.getBillAccountName(), 6, (float) mBillAccountHistoryEntity.getBillAccountMoney() / 100, isOpenJoinOrder ? 1 : 0);
                                    DBHelper.getInstance(getActivity().getApplicationContext()).insertBillAccountHistory(mBillAccountHistoryEntity, isOpenJoinOrder ? 1 : 0);
                                    mOnPayModeAccountClickListener.onAccountConfirm(orderId, mBillAccountHistoryEntity);
                                    dialog.dismiss();
                                } else {
                                    Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.show(getFragmentManager(), "");
                    }
                }
                break;
        }
    }
}
