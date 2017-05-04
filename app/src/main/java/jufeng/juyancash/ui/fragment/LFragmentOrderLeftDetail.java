package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderDishAdapter;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.SpecialEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.CustomOpenInvoiceDialog;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeSelectReturnOrderReasonDialog;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderLeftDetail extends BaseFragment implements View.OnClickListener {
    private TextView tvOrderNumber, tvTableNumber, tvMealCount, tvDate, tvTotal, tvBillMoney, tvPayMode, tvSerialNumber, tvCashierName, tvWaiterName, tvIsOpenInvoice, tvCloseTime;
    private Button btnOpenInvoice, btnReturnCashier, btnPrint;
    private RecyclerView mRecyclerView;
    private OrderDishAdapter adapter;
    private OrderEntity mOrderEntity;
    private TextView tvReturnReason;
    private LinearLayout layoutReturnOrderReason;
    private TextView tvTitle;
    private MainFragmentListener mOnOrderLeftDetailListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mOrderEntity = (OrderEntity) msg.getData().getParcelable("orderEntity");
                    initData(mOrderEntity);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setOrderLeftDetailHandler(mHandler);
        try {
            mOnOrderLeftDetailListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_left_detail, container, false);
        initView(mView);
        setListener();
        setAdapter();
        initData(mOrderEntity);
        return mView;
    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_titl);
        tvReturnReason = (TextView) view.findViewById(R.id.tv_return_order_reason);
        layoutReturnOrderReason = (LinearLayout) view.findViewById(R.id.layout_return_order_reason);
        tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
        tvTableNumber = (TextView) view.findViewById(R.id.tv_table_number);
        tvMealCount = (TextView) view.findViewById(R.id.tv_meal_count);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        tvBillMoney = (TextView) view.findViewById(R.id.tv_bill_money);
        tvPayMode = (TextView) view.findViewById(R.id.tv_paymode);
        tvSerialNumber = (TextView) view.findViewById(R.id.tv_serial_number);
        tvCashierName = (TextView) view.findViewById(R.id.tv_cashier);
        tvWaiterName = (TextView) view.findViewById(R.id.tv_waiter);
        tvIsOpenInvoice = (TextView) view.findViewById(R.id.tv_invoice);
        tvCloseTime = (TextView) view.findViewById(R.id.tv_close_time);
        btnOpenInvoice = (Button) view.findViewById(R.id.btn_draw_bill);
        btnReturnCashier = (Button) view.findViewById(R.id.btn_return_cashier);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void setListener() {
        btnOpenInvoice.setOnClickListener(this);
        btnReturnCashier.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
    }

    private void initData(OrderEntity orderEntity) {
        mOrderEntity = orderEntity;
        if (mOrderEntity == null) {
            tvTitle.setText("账单");
            tvOrderNumber.setText("NO.");
            tvTableNumber.setText("座号：");
            tvMealCount.setText("人数：");
            tvDate.setText("");
            tvTotal.setText("共   项");
            tvBillMoney.setText("合计：");
            tvPayMode.setText("");
            tvSerialNumber.setText("");
            tvCashierName.setText("");
            tvWaiterName.setText("");
            tvIsOpenInvoice.setText("");
            tvCloseTime.setText("");
            btnPrint.setClickable(false);
            btnReturnCashier.setClickable(false);
            btnOpenInvoice.setClickable(false);
            adapter.updateData(null, 1, false);
        } else {
            if (orderEntity.getIsReturnOrder() != null && orderEntity.getIsReturnOrder() == 1) {
                tvTitle.setText("账单（反）");
                layoutReturnOrderReason.setVisibility(LinearLayout.VISIBLE);
                tvReturnReason.setText(orderEntity.getReturnOrderReason() == null ? "无" : orderEntity.getReturnOrderReason());
            } else {
                if (orderEntity.getOrderType() == 1) {
                    tvTitle.setText("外卖单");
                } else {
                    tvTitle.setText("账单");
                }
                layoutReturnOrderReason.setVisibility(LinearLayout.GONE);
            }
            tvOrderNumber.setText("NO." + mOrderEntity.getOrderNumber1());
            tvTableNumber.setText("座号：" + DBHelper.getInstance(getActivity().getApplicationContext()).getTableNameByTableId(mOrderEntity.getTableId()));
            tvMealCount.setText(mOrderEntity.getOrderGuests() == null ? "人数：>=1" : "人数：" + mOrderEntity.getOrderGuests());
            tvDate.setText(CustomMethod.parseTime(mOrderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
            tvTotal.setText("共" + AmountUtils.multiply("" + DBHelper.getInstance(getActivity().getApplicationContext()).getOrderedDishCountByOrderId(mOrderEntity.getOrderId()), "1") + "项");
            tvBillMoney.setText("合计：" + AmountUtils.multiply(orderEntity.getTotalMoney(), 0.01));
            tvPayMode.setText(DBHelper.getInstance(getActivity().getApplicationContext()).getPayModeStrByOrderId(mOrderEntity.getOrderId()));
            tvSerialNumber.setText(mOrderEntity.getSerialNumber());
            tvCashierName.setText(mOrderEntity.getCashierId() != null ? DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(mOrderEntity.getCashierId()) : "");
            tvWaiterName.setText(mOrderEntity.getWaiterId() != null ? DBHelper.getInstance(getActivity().getApplicationContext()).getEmployeeNameById(mOrderEntity.getWaiterId()) : "");
            tvIsOpenInvoice.setText(mOrderEntity.getInvoiceMoney() != null ? String.valueOf(((float) mOrderEntity.getInvoiceMoney()) / 100) : "");
            tvCloseTime.setText(CustomMethod.parseTime(mOrderEntity.getCloseTime(), "yyyy-MM-dd HH:mm"));
            btnOpenInvoice.setClickable(true);
            btnReturnCashier.setClickable(true);
            btnPrint.setClickable(true);
            adapter.updateData(mOrderEntity.getOrderId(), 1, false);
        }
    }

    private void setAdapter() {
        adapter = new OrderDishAdapter(getActivity().getApplicationContext(), null, 1, false);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_draw_bill:
                //开发票
                if (mOrderEntity != null) {
                    final CustomOpenInvoiceDialog dialog = new CustomOpenInvoiceDialog(getActivity(), mOrderEntity.getOrderId());
                    dialog.setOpenInvoiceListener(new CustomOpenInvoiceDialog.OpenInvoiceListener() {
                        @Override
                        public void onOpenClick(OrderEntity orderEntity, float money) {
                            dialog.dismiss();
                            DBHelper.getInstance(getActivity().getApplicationContext()).openInvoiceOfOrder(orderEntity, money);
                            initData(orderEntity);
                        }

                        @Override
                        public void onCancleClick() {
                            dialog.dismiss();
                        }
                    });
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("未选择账单，无法进行开发票操作");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_return_cashier:
                //反结账
                if (mOrderEntity != null) {
                    long currentTime = System.currentTimeMillis();
                    long cashierOverTime = mOrderEntity.getCloseTime();
                    long overTime = DBHelper.getInstance(getContext()).getOverTime();
                    //判断是否是外卖单
                    if (mOrderEntity.getOrderType() == 1) {
                        CustomMethod.showMessage(getContext(), "外卖单不允许反结账");
                        return;
                    }
                    if (mOrderEntity.getIsShift() == 0 && cashierOverTime <= overTime) {
                        //需要反结账所有未交接订单权限
                        authoReturnover(8);
                        return;
                    }
                    if (mOrderEntity.getIsShift() == 1) {
                        //需要反结账所有订单权限
                        authoReturnover(9);
                        return;
                    }
                    authoReturnover(5);
                } else {
                    CustomMethod.showMessage(getContext(), "请选择需要反结账的订单后再进行操作");
                }
                break;
            case R.id.btn_print:
                //打印
                if (mOrderEntity != null) {
                    if (mOrderEntity.getOrderType() == 1) {
                        //外卖单
                        mOnOrderLeftDetailListener.printWaimai(mOrderEntity.getOrderId());
                    } else {
                        //堂食单
                        mOnOrderLeftDetailListener.printCaiwulian(mOrderEntity.getOrderId());
                    }
                }
                break;
        }
    }

    //权限验证后进行反结账
    private void authoReturnover(int type){
        EmployeeEntity employeeEntity = DBHelper.getInstance(getContext().getApplicationContext()).getCurrentEmployee(getContext().getApplicationContext());
        switch (type){
            case 5:
                if (employeeEntity.getAuthReturnOrder() == 1) {
                    final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                    dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                        @Override
                        public void onCancleClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClick(SpecialEntity specialEntity) {
                            dialog.dismiss();
                            String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                            if (mOrderEntity != null) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                    DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                }
                                mOnOrderLeftDetailListener.onReturnOrderClick();
                            }
                        }
                    });
                } else {
                    final CustomeAuthorityDialog authorityDialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    authorityDialog.setArguments(bundle);
                    authorityDialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            authorityDialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if (employeeEntity != null && employeeEntity.getAuthReturnOrder() == 1) {
                                authorityDialog.dismiss();
                                final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                                dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                                    @Override
                                    public void onCancleClick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClick(SpecialEntity specialEntity) {
                                        dialog.dismiss();
                                        String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                                        if (mOrderEntity != null) {
                                            DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                            if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                            }
                                            mOnOrderLeftDetailListener.onReturnOrderClick();
                                        }
                                    }
                                });
                            } else {
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    authorityDialog.show(getFragmentManager(), "");
                }
                break;
            case 8:
                if (employeeEntity.getIsReturnSomeOrder() == 1) {
                    final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                    dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                        @Override
                        public void onCancleClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClick(SpecialEntity specialEntity) {
                            dialog.dismiss();
                            String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                            if (mOrderEntity != null) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                    DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                }
                                mOnOrderLeftDetailListener.onReturnOrderClick();
                            }
                        }
                    });
                } else {
                    final CustomeAuthorityDialog authorityDialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    authorityDialog.setArguments(bundle);
                    authorityDialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            authorityDialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if (employeeEntity != null && employeeEntity.getIsReturnSomeOrder() == 1) {
                                authorityDialog.dismiss();
                                final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                                dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                                    @Override
                                    public void onCancleClick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClick(SpecialEntity specialEntity) {
                                        dialog.dismiss();
                                        String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                                        if (mOrderEntity != null) {
                                            DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                            if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                            }
                                            mOnOrderLeftDetailListener.onReturnOrderClick();
                                        }
                                    }
                                });
                            } else {
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    authorityDialog.show(getFragmentManager(), "");
                }
                break;
            case 9:
                if (employeeEntity.getIsRetrurnAllOrder() == 1) {
                    final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                    dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                        @Override
                        public void onCancleClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClick(SpecialEntity specialEntity) {
                            dialog.dismiss();
                            String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                            if (mOrderEntity != null) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                    DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                }
                                mOnOrderLeftDetailListener.onReturnOrderClick();
                            }
                        }
                    });
                } else {
                    final CustomeAuthorityDialog authorityDialog = new CustomeAuthorityDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    authorityDialog.setArguments(bundle);
                    authorityDialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                        @Override
                        public void onAuthorityCancle() {
                            authorityDialog.dismiss();
                        }

                        @Override
                        public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                            if (employeeEntity != null && employeeEntity.getIsRetrurnAllOrder() == 1) {
                                authorityDialog.dismiss();
                                final CustomeSelectReturnOrderReasonDialog dialog = new CustomeSelectReturnOrderReasonDialog(getActivity());
                                dialog.setOnReturnOrderReasonSelectedListener(new CustomeSelectReturnOrderReasonDialog.OnReturnOrderReasonSelectedListener() {
                                    @Override
                                    public void onCancleClick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClick(SpecialEntity specialEntity) {
                                        dialog.dismiss();
                                        String specialName = specialEntity == null ? "无" : specialEntity.getSpecialName();
                                        if (mOrderEntity != null) {
                                            DBHelper.getInstance(getActivity().getApplicationContext()).returnOrder(mOrderEntity, specialName);
                                            DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 8);
                                            if (mOrderEntity.getIsUpload() != null && mOrderEntity.getIsUpload() == 1) {
                                                DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(mOrderEntity.getOrderId(), mOrderEntity.getOrderId(), 17);
                                            }
                                            mOnOrderLeftDetailListener.onReturnOrderClick();
                                        }
                                    }
                                });
                            } else {
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    authorityDialog.show(getFragmentManager(), "");
                }
                break;
        }
    }
}
