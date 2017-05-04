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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.eventbus.SnackDiscountAllConfirmEvent;
import jufeng.juyancash.eventbus.SnackOpenTopRightEvent;
import jufeng.juyancash.eventbus.SnackSelectDiscountReasonEvent;
import jufeng.juyancash.eventbus.SnackSetKeyboardEdittextEvent;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/23.
 */
public class LFragmentSnackDiscountAll extends BaseFragment {
    private TextView tvCancle, tvConfirm;
    private LinearLayout layoutReason;
    private TextView tvReason;
    private EditText etRate;
    private CheckBox cbIsDiscount;
    private String orderId;
    private DiscountHistoryEntity mDiscountHistoryEntity;

    public static LFragmentSnackDiscountAll newInstance(String orderId,DiscountHistoryEntity discountHistoryEntity){
        LFragmentSnackDiscountAll fragment = new LFragmentSnackDiscountAll();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        bundle.putParcelable("discountHistory",discountHistoryEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_discount_all, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        layoutReason = (LinearLayout) view.findViewById(R.id.layout_discount_reason);
        etRate = (EditText) view.findViewById(R.id.et_discount_rate);
        tvReason = (TextView) view.findViewById(R.id.tv_discount_reason);
        cbIsDiscount = (CheckBox) view.findViewById(R.id.cb_is_discount);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        orderId = getArguments().getString("orderId");
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initData(){
        if (mDiscountHistoryEntity == null) {
            mDiscountHistoryEntity = new DiscountHistoryEntity();
            mDiscountHistoryEntity.setDiscountRate(100);
            mDiscountHistoryEntity.setDiscountType(0);
            mDiscountHistoryEntity.setIsAbleDiscount(0);
        }
        mDiscountHistoryEntity.setOrderId(orderId);
        etRate.setText(String.valueOf(mDiscountHistoryEntity.getDiscountRate()));
        cbIsDiscount.setChecked(mDiscountHistoryEntity.getIsAbleDiscount() != 0);
        tvReason.setText(mDiscountHistoryEntity.getDiscountReason());
        CustomMethod.setMyInputType(etRate,getActivity());
        EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etRate));
        etRate.requestFocus();
    }

    private void setListener() {
        etRate.setOnTouchListener(new View.OnTouchListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBus.getDefault().post(new SnackSetKeyboardEdittextEvent(etRate));
                etRate.requestFocus();
                return false;
            }
        });
        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length() > 0){
                    mDiscountHistoryEntity.setDiscountRate(Integer.valueOf(s.toString()));
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
            @Override
            public void onClick(View v) {
                String rate = etRate.getText().toString();
                if(rate.length() > 0) {
                    final int rateVal = Integer.valueOf(rate);
                    if (rateVal <= 100) {
                        final String reason = tvReason.getText().toString();
                        final int isAbleDiscount = cbIsDiscount.isChecked() ? 1 : 0;
                        boolean isHasVipOrCouponDiscount = DBHelper.getInstance(getContext().getApplicationContext()).isHasVipOrCouponDiscount(orderId);
                        if(isHasVipOrCouponDiscount){//当前已使用优惠券或会员卡优惠，
                            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                            dialog.setTitle("提示信息");
                            dialog.setMessage("当前订单已使用优惠券或会员卡优惠，请先清除其他优惠方式！");
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                                @Subscribe(threadMode = ThreadMode.MAIN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EventBus.getDefault().post(new SnackOpenTopRightEvent());
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }else {
                            authorityDiscountAll(rateVal, reason, isAbleDiscount);
                        }
                    } else {
                        showMessage("折扣率不能大于100%");
                    }
                }else{
                    showMessage("请输入折扣率");
                }
            }
        });
        layoutReason.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SnackSelectDiscountReasonEvent(mDiscountHistoryEntity));
            }
        });
        cbIsDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDiscountHistoryEntity.setIsAbleDiscount(isChecked ? 1 : 0);
            }
        });
    }

    private void showMessage(String message){
        CustomMethod.showMessage(getContext(),message);
    }

    /**
     * 确认打折，进行权限验证，权限值为1：不允许打折；2：允许对可打折商品打折；3：允许对可打折商品打折；4：不打折；5：对不可打折商品打折；
     * 6：对可打折商品打折；7：全部商品打折
     * @param rateVal
     * @param reason
     * @param isAbleDiscount
     */
    private void authorityDiscountAll(final int rateVal, final String reason, final int isAbleDiscount){
        EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
        int authorityVal = 0;
        authorityVal += isAbleDiscount*1+employeeEntity.getAllowDiscountBargain()*2+employeeEntity.getAllowDiscountNotBargain()*4;
        switch (authorityVal) {
            case 0://需要获取允许对打折商品打折的权限
                showAuthorityDialog(1,rateVal,reason,isAbleDiscount,authorityVal);
                break;
            case 1://需要获取所有打折权限
                showAuthorityDialog(11,rateVal,reason,isAbleDiscount,authorityVal);
                break;
            case 3://需要获取对不允许打折商品打折的权限
                showAuthorityDialog(11,rateVal,reason,isAbleDiscount,authorityVal);
                break;
            case 4://需要获取允许对打折商品打折的权限
                showAuthorityDialog(1,rateVal,reason,isAbleDiscount,authorityVal);
                break;
            case 5://需要获取所有打折权限
                showAuthorityDialog(11,rateVal,reason,isAbleDiscount,authorityVal);
                break;
            case 2:
            case 6://有权限，需要先判断是否高于最低折扣率
                if(employeeEntity.getBargainMinPayment() <= rateVal){
                    confirmDiscount(rateVal,reason,isAbleDiscount);
                }else{
                    showAuthorityDialog(1,rateVal,reason,isAbleDiscount,authorityVal);
                }
                break;
            case 7://有权限，需要先判断是否高于最低折扣率
                if(employeeEntity.getBargainMinPayment() < rateVal && employeeEntity.getNotBargainMinPayment() <= rateVal){
                    confirmDiscount(rateVal,reason,isAbleDiscount);
                }else{
                    showAuthorityDialog(11,rateVal,reason,isAbleDiscount,authorityVal);
                }
                break;
        }
    }

    public void showAuthorityDialog(int type,final int rateVal, final String reason, final int isAbleDiscount, final int authorityVale){
        final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        dialog.setArguments(bundle);
        dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
            @Override
            public void onAuthorityCancle() {
                dialog.dismiss();
            }
            @Override
            public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                switch (authorityVale){
                    case 0:
                    case 2://需要获取允许对打折商品打折的权限
                    case 4:
                    case 6:
                        if(employeeEntity.getAuthCashier() == 1 && employeeEntity.getAllowDiscountBargain() == 1 && employeeEntity.getBargainMinPayment() <= rateVal){
                            confirmDiscount(rateVal,reason,isAbleDiscount);
                            dialog.dismiss();
                        }else{
                            showMessage("该员工无权限");
                        }
                        break;
                    case 1:
                    case 3:
                    case 5://需要获取所有打折权限
                    case 7:
                        if(employeeEntity.getAuthCashier() == 1 && employeeEntity.getAllowDiscountNotBargain() == 1 && employeeEntity.getAllowDiscountBargain() == 1 && employeeEntity.getNotBargainMinPayment() <= rateVal &&  employeeEntity.getBargainMinPayment() <= rateVal){
                            confirmDiscount(rateVal,reason,isAbleDiscount);
                            dialog.dismiss();
                        }else{
                            showMessage("该员工无权限");
                        }
                        break;
                }
            }
        });
        dialog.show(getFragmentManager(),"");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void confirmDiscount(final int rateVal, final String reason, final int isAbleDiscount){
        DBHelper.getInstance(getActivity().getApplicationContext()).insertAllDiscount(rateVal, reason, isAbleDiscount, orderId);
        EventBus.getDefault().post(new SnackDiscountAllConfirmEvent(orderId));
    }
}
