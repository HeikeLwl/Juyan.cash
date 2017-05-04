package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.myinterface.CashierTopRightChangeListener;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class LFragmentDiscountSome extends BaseFragment {
    private TextView tvCancle,tvConfirm;
    private LinearLayout layoutRate,layoutReason, layoutGoods;
    private TextView tvReason, tvGoods;
    private EditText etRate;
    private CheckBox cbIsDiscount;
    private String orderId;
    private boolean isOpenJoinOrder;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoodsEntities;
    private MainFragmentListener mOnDiscountSomeClickListener;
    private InitKeyboardInterface mInitKeyboardInterface;
    private CashierTopRightChangeListener mCashierTopRightChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDiscountSomeClickListener = (MainFragmentListener) context;
            mInitKeyboardInterface = (InitKeyboardInterface) context;
            mCashierTopRightChangeListener = (CashierTopRightChangeListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_discount_some, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        layoutRate = (LinearLayout) view.findViewById(R.id.layout_discount_rate);
        layoutReason = (LinearLayout) view.findViewById(R.id.layout_discount_reason);
        layoutGoods = (LinearLayout) view.findViewById(R.id.layout_discount_goods);
        etRate = (EditText) view.findViewById(R.id.et_discount_rate);
        tvReason = (TextView) view.findViewById(R.id.tv_discount_reason);
        tvGoods = (TextView) view.findViewById(R.id.tv_discount_goods);
        cbIsDiscount = (CheckBox) view.findViewById(R.id.cb_is_discount);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        orderId = getArguments().getString("orderId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
        mSomeDiscountGoodsEntities = getArguments().getParcelableArrayList("someDiscountGoods");
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
    }

    public void setNewParam(String param0,boolean param1,ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities,DiscountHistoryEntity discountHistoryEntity){
        this.orderId = param0;
        this.isOpenJoinOrder = param1;
        this.mSomeDiscountGoodsEntities = someDiscountGoodsEntities;
        this.mDiscountHistoryEntity = discountHistoryEntity;

        initData();
    }

    private void initData(){
        if (mDiscountHistoryEntity == null) {
            mDiscountHistoryEntity = new DiscountHistoryEntity();
            mDiscountHistoryEntity.setDiscountHistoryId(UUID.randomUUID().toString());
            mDiscountHistoryEntity.setDiscountRate(100);
            mDiscountHistoryEntity.setDiscountType(1);
            mDiscountHistoryEntity.setIsAbleDiscount(0);
            mSomeDiscountGoodsEntities = new ArrayList<>();
        }
        mDiscountHistoryEntity.setOrderId(orderId);
        etRate.setText(String.valueOf(mDiscountHistoryEntity.getDiscountRate()));
        cbIsDiscount.setChecked(mDiscountHistoryEntity.getIsAbleDiscount() != 0);
        tvReason.setText(mDiscountHistoryEntity.getDiscountReason());
        tvGoods.setText(String.valueOf(mSomeDiscountGoodsEntities.size()));
        CustomMethod.setMyInputType(etRate,getActivity());
        if(mInitKeyboardInterface != null)
            mInitKeyboardInterface.setEdittext(etRate);
        etRate.requestFocus();
    }

    private void setListener() {
        etRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mInitKeyboardInterface != null)
                    mInitKeyboardInterface.setEdittext(etRate);
                etRate.requestFocus();
                return false;
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDiscountSomeClickListener.onDiscountSomeCancle();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate = TextUtils.isEmpty(etRate.getText().toString()) ? "100" : etRate.getText().toString();
                if(rate.length() > 1) {
                    final int discountVal = Integer.valueOf(rate);
                    if(discountVal <= 100) {
                        mDiscountHistoryEntity.setDiscountRate(discountVal);
                        boolean isHasVipOrCouponDiscount = DBHelper.getInstance(getContext().getApplicationContext()).isHasVipOrCouponDiscount(orderId);
                        if(isHasVipOrCouponDiscount){//当前已使用优惠券或会员卡优惠，
                            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                            dialog.setTitle("提示信息");
                            dialog.setMessage("当前订单已使用优惠券或会员卡优惠，请清除其他优惠方式！");
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    DBHelper.getInstance(getContext().getApplicationContext()).cancleVipDiscount(orderId);
//                                    DBHelper.getInstance(getContext().getApplicationContext()).cancleCouponDiscount(orderId);
//                                    authorityDiscountAll(discountVal, mDiscountHistoryEntity.getIsAbleDiscount());
                                    mCashierTopRightChangeListener.onTopRightCancle();
                                    dialog.dismiss();
                                }
                            });
//                            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if(mOnDiscountSomeClickListener != null){
//                                        mOnDiscountSomeClickListener.onDiscountSomeCancle();
//                                    }
//                                }
//                            });
                            dialog.show();
                        }else {
                            authorityDiscountAll(discountVal, mDiscountHistoryEntity.getIsAbleDiscount());
                        }
                    }else{
                        CustomMethod.showMessage(getContext(),"折扣率不能大于100%");
                    }
                }else{
                    CustomMethod.showMessage(getContext(),"请输入折扣率");
                }
            }
        });
        layoutReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate = TextUtils.isEmpty(etRate.getText().toString()) ? "100" : etRate.getText().toString();
                int discountVal = Integer.valueOf(rate);
                mDiscountHistoryEntity.setDiscountRate(discountVal);
                mOnDiscountSomeClickListener.onSelectSomeReason(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });
        layoutGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate = TextUtils.isEmpty(etRate.getText().toString()) ? "100" : etRate.getText().toString();
                int discountVal = Integer.valueOf(rate);
                mDiscountHistoryEntity.setDiscountRate(discountVal);
                mOnDiscountSomeClickListener.onSelectSomeGoods(mDiscountHistoryEntity,mSomeDiscountGoodsEntities);
            }
        });
        cbIsDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDiscountHistoryEntity.setIsAbleDiscount(isChecked ? 1 : 0);
            }
        });
    }

    /**
     * 确认打折，进行权限验证，权限值为1：不允许打折；2：允许对可打折商品打折；3：允许对可打折商品打折；4：不打折；5：对不可打折商品打折；
     * 6：对可打折商品打折；7：全部商品打折
     * @param rateVal
     * @param isAbleDiscount
     */
    private void authorityDiscountAll(final int rateVal, final int isAbleDiscount){
        EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
        int authorityVal = 0;
        authorityVal += isAbleDiscount*1+employeeEntity.getAllowDiscountBargain()*2+employeeEntity.getAllowDiscountNotBargain()*4;
        switch (authorityVal) {
            case 0://需要获取允许对打折商品打折的权限
                showAuthorityDialog(1,rateVal,isAbleDiscount,authorityVal);
                break;
            case 1://需要获取所有打折权限
                showAuthorityDialog(11,rateVal,isAbleDiscount,authorityVal);
                break;
            case 3://需要获取对不允许打折商品打折的权限
                showAuthorityDialog(11,rateVal,isAbleDiscount,authorityVal);
                break;
            case 4://需要获取允许对打折商品打折的权限
                showAuthorityDialog(1,rateVal,isAbleDiscount,authorityVal);
                break;
            case 5://需要获取所有打折权限
                showAuthorityDialog(11,rateVal,isAbleDiscount,authorityVal);
                break;
            case 2:
            case 6://有权限，需要先判断是否高于最低折扣率
                if(employeeEntity.getBargainMinPayment() <= rateVal){
                    if (isOpenJoinOrder) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                    } else {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                    }
                    mOnDiscountSomeClickListener.onDiscountSomeConfirm(orderId,isOpenJoinOrder);
                }else{
                    showAuthorityDialog(1,rateVal,isAbleDiscount,authorityVal);
                }
                break;
            case 7://有权限，需要先判断是否高于最低折扣率
                if(employeeEntity.getBargainMinPayment() <= rateVal && employeeEntity.getNotBargainMinPayment() <= rateVal){
                    if (isOpenJoinOrder) {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                    } else {
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                    }
                    mOnDiscountSomeClickListener.onDiscountSomeConfirm(orderId,isOpenJoinOrder);
                }else{
                    showAuthorityDialog(11,rateVal,isAbleDiscount,authorityVal);
                }
                break;
        }
    }

    public void showAuthorityDialog(int type,final int rateVal,final int isAbleDiscount, final int authorityVale){
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
                            if (isOpenJoinOrder) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                            } else {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                            }
                            mOnDiscountSomeClickListener.onDiscountSomeConfirm(orderId,isOpenJoinOrder);
                            dialog.dismiss();
                        }else{
                            CustomMethod.showMessage(getContext(),"该员工无权限");
                        }
                        break;
                    case 1:
                    case 3:
                    case 5://需要获取所有打折权限
                    case 7:
                        if(employeeEntity.getAuthCashier() == 1 && employeeEntity.getAllowDiscountNotBargain() == 1 && employeeEntity.getAllowDiscountBargain() == 1 && employeeEntity.getNotBargainMinPayment() <= rateVal &&  employeeEntity.getBargainMinPayment() <= rateVal){
                            if (isOpenJoinOrder) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                            } else {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertSomeDiscount(mDiscountHistoryEntity, mSomeDiscountGoodsEntities);
                            }
                            mOnDiscountSomeClickListener.onDiscountSomeConfirm(orderId,isOpenJoinOrder);
                            dialog.dismiss();
                        }else{
                            CustomMethod.showMessage(getContext(),"该员工无权限");
                        }
                        break;
                }
            }
        });
        dialog.show(getFragmentManager(),"");
    }
}
