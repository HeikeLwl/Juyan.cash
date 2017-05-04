package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.myinterface.CashierTopRightChangeListener;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;

/**
 * Created by Administrator102 on 2016/8/23.
 */
public class LFragmentDiscountScheme extends BaseFragment {
    private TextView tvCancle,tvConfirm;
    private LinearLayout layoutScheme,layoutReason;
    private TextView tvScheme,tvReason;
    private String orderId;
    private boolean isOpenJoinOrder;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private MainFragmentListener mOnDiscountSchemeClickListener;
    private CashierTopRightChangeListener mCashierTopRightChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDiscountSchemeClickListener = (MainFragmentListener) context;
            mCashierTopRightChangeListener = (CashierTopRightChangeListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_discount_scheme, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    private void initView(View view) {
        layoutScheme = (LinearLayout) view.findViewById(R.id.layout_discount_scheme);
        layoutReason = (LinearLayout) view.findViewById(R.id.layout_discount_reason);
        tvScheme = (TextView) view.findViewById(R.id.tv_discount_scheme);
        tvReason = (TextView) view.findViewById(R.id.tv_discount_reason);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        orderId = getArguments().getString("orderId");
        isOpenJoinOrder = getArguments().getBoolean("isOpenJoinOrder");
        mDiscountHistoryEntity = getArguments().getParcelable("discountHistory");
    }

    public void setNewParam(String param0,boolean param1,DiscountHistoryEntity discountHistoryEntity){
        this.orderId = param0;
        this.isOpenJoinOrder = param1;
        this.mDiscountHistoryEntity = discountHistoryEntity;

        initData();
    }

    private void initData(){
        if (mDiscountHistoryEntity == null) {
            mDiscountHistoryEntity = new DiscountHistoryEntity();
            mDiscountHistoryEntity.setDiscountType(2);
        }
        mDiscountHistoryEntity.setOrderId(orderId);
        tvReason.setText(mDiscountHistoryEntity.getDiscountReason());
        tvScheme.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getDiscountName(mDiscountHistoryEntity.getDiscountId()));
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDiscountSchemeClickListener.onDiscountSchemeCancle();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDiscountHistoryEntity.getDiscountId() != null) {
                    final DiscountEntity discountEntity = DBHelper.getInstance(getContext().getApplicationContext()).getDiscountEntity(mDiscountHistoryEntity.getDiscountId());
                    if(discountEntity != null) {
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
//                                    authorityDiscountAll(discountEntity.getDiscountPercentage(), discountEntity.getIsEnforcement());
                                    mCashierTopRightChangeListener.onTopRightCancle();
                                    dialog.dismiss();
                                }
                            });
//                            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if(mOnDiscountSchemeClickListener != null){
//                                        mOnDiscountSchemeClickListener.onDiscountSchemeCancle();
//                                    }
//                                }
//                            });
                            dialog.show();
                        }else {
                            authorityDiscountAll(discountEntity.getDiscountPercentage(), discountEntity.getIsEnforcement());
                        }
                    }
                }else{
                    Snackbar.make(layoutReason,"请选择打折方案",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        layoutReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDiscountSchemeClickListener.onSelectSchemeReason(mDiscountHistoryEntity);
            }
        });
        layoutScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDiscountSchemeClickListener.onSelectScheme(mDiscountHistoryEntity);
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
                    if(isOpenJoinOrder){
                        //总单
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderDiscountScheme(mDiscountHistoryEntity);
                    }else{
                        //子单
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertDiscountScheme(mDiscountHistoryEntity);
                    }
                    mOnDiscountSchemeClickListener.onDiscountSchemeConfirm(orderId,isOpenJoinOrder);
                }else{
                    showAuthorityDialog(1,rateVal,isAbleDiscount,authorityVal);
                }
                break;
            case 7://有权限，需要先判断是否高于最低折扣率
                if(employeeEntity.getBargainMinPayment() <= rateVal && employeeEntity.getNotBargainMinPayment() <= rateVal){
                    if(isOpenJoinOrder){
                        //总单
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderDiscountScheme(mDiscountHistoryEntity);
                    }else{
                        //子单
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertDiscountScheme(mDiscountHistoryEntity);
                    }
                    mOnDiscountSchemeClickListener.onDiscountSchemeConfirm(orderId,isOpenJoinOrder);
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
                            if(isOpenJoinOrder){
                                //总单
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderDiscountScheme(mDiscountHistoryEntity);
                            }else{
                                //子单
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertDiscountScheme(mDiscountHistoryEntity);
                            }
                            mOnDiscountSchemeClickListener.onDiscountSchemeConfirm(orderId,isOpenJoinOrder);
                            dialog.dismiss();
                        }else{
                            Snackbar.make(view,"该员工无权限",Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                    case 3:
                    case 5://需要获取所有打折权限
                    case 7:
                        if(employeeEntity.getAuthCashier() == 1 && employeeEntity.getAllowDiscountNotBargain() == 1 && employeeEntity.getAllowDiscountBargain() == 1 && employeeEntity.getNotBargainMinPayment() <= rateVal &&  employeeEntity.getBargainMinPayment() <= rateVal){
                            if(isOpenJoinOrder){
                                //总单
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertJoinOrderDiscountScheme(mDiscountHistoryEntity);
                            }else{
                                //子单
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertDiscountScheme(mDiscountHistoryEntity);
                            }
                            mOnDiscountSchemeClickListener.onDiscountSchemeConfirm(orderId,isOpenJoinOrder);
                            dialog.dismiss();
                        }else{
                            Snackbar.make(view,"该员工无权限",Snackbar.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
        dialog.show(getFragmentManager(),"");
    }
}
