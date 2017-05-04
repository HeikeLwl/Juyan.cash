package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderAdapter;
import jufeng.juyancash.adapter.OrderColletionAdapter;
import jufeng.juyancash.adapter.OrderRightStatisticAdapter;
import jufeng.juyancash.adapter.OrderSpinnerAdapter;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.OrderCollectionBean;
import jufeng.juyancash.dao.AreaEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrder extends BaseFragment {
    private LinearLayout mLayout0, mLayout2, mLayout4;
    private Spinner mSpinner0, mSpinner1, mSpinner2, mSpinner3, mSpinner4;
    private LFragmentOrderLeftCollection mLeftCollection;
    private LFragmentOrderLeftDetail mLeftDetail;
    private LFragmentOrderLeftStatistic mLeftStatistic;
    private LCustomeRadioGroup mRadioGroup;
    private static final int ORDERDETAIL = 0;
    private static final int ORDERCOLLECTION = 1;
    private static final int ORDERSTATISTIC = 2;
    private MainFragmentListener mOnOrderSpinnerChangeListener;
    private static ArrayList<AreaEntity> areaEntities;
    private static ArrayList<String> typeNames;
    private static ArrayList<String> payModes;
    private static ArrayList<Payment> payments;
    private static ArrayList<String> shiftes;
    private static ArrayList<String> cashieres;
    private static ArrayList<String> dates;

    private RecyclerView mRecyclerView;
    private int employId = 0;
    private int shift = -1;
    private String paymodeId = null;
    private String payModeName = null;
    private int date = -1;
    private String type = "-1";
    //账单明细
    private OrderAdapter mOrderAdapter;
    private ArrayList<OrderEntity> mListMap;
    private TextView tvPageNum;
    private ImageButton ibPre, ibNext;
    private LinearLayout layoutPageNum;
    //账单汇总
    private OrderColletionAdapter mOrderColletionAdapter;
    private ArrayList<OrderCollectionBean> mOrderCollectionBeen;
    //销售统计
    private OrderRightStatisticAdapter mOrderRightStatisticAdapter;
    private ArrayList<DishTypeModel> mDishTypeModels;

    static {
        cashieres = new ArrayList<>();
        cashieres.add("全部");
        cashieres.add("当前收银员");

        shiftes = new ArrayList<>();
        shiftes.add("全部");
        shiftes.add("未交接");
        shiftes.add("已交接");

        dates = new ArrayList<>();
        dates.add("两日内");
        dates.add("今日");
        dates.add("昨日");

        payModes = new ArrayList<>();
        payments = new ArrayList<>();
        payModes.add("全部");

        typeNames = new ArrayList<>();
        areaEntities = new ArrayList<>();
        typeNames.add("全部");
        typeNames.add("外卖单");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://反结账
                    new GetOrderDataAsyncTask().execute();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setFragmentOrderHandler(mHandler);
        try {
            mOnOrderSpinnerChangeListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order, container, false);
        initView(mView);
        initView1(mView);
        initData();
        setListener1();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyAsyncTask().execute();
    }

    private void initView(View view) {
        mSpinner0 = (Spinner) view.findViewById(R.id.spinner_cashier);
        mSpinner1 = (Spinner) view.findViewById(R.id.spinner_shift);
        mSpinner2 = (Spinner) view.findViewById(R.id.spinner_paymode);
        mSpinner3 = (Spinner) view.findViewById(R.id.spinner_date);
        mSpinner4 = (Spinner) view.findViewById(R.id.spinner_type);
        mLayout0 = (LinearLayout) view.findViewById(R.id.layout_0);
        mLayout2 = (LinearLayout) view.findViewById(R.id.layout_2);
        mLayout4 = (LinearLayout) view.findViewById(R.id.layout_4);
        mRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, Object> {

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在加载...");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            areaEntities.clear();
            payments.clear();
            payModes.clear();
            payModes.add("全部");
            payments.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).getAllPayment());
            for (Payment payment :
                    payments) {
                payModes.add(payment.getName());
            }
            areaEntities.addAll(DBHelper.getInstance(getActivity().getApplicationContext()).queryAreaData());
            typeNames.clear();
            typeNames.add("全部");
            for (AreaEntity areaEntity :
                    areaEntities) {
                typeNames.add(areaEntity.getAreaName());
            }
            typeNames.add("外卖单");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mSpinner2.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), payModes));
            mSpinner4.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), typeNames));
            setSpinner();
            setListener();
            setFragment(ORDERDETAIL);
            new GetOrderDataAsyncTask().execute();
        }
    }

    private void setSpinner() {
        mSpinner0.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), cashieres));
        mSpinner1.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), shiftes));
        mSpinner2.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), payModes));
        mSpinner3.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), dates));
        mSpinner4.setAdapter(new OrderSpinnerAdapter(getActivity().getApplicationContext(), typeNames));
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                if (group.getCheckedRadioButtonId() == R.id.radiobutton_1) {
                    changeRightAdapter(0, false);
                } else if (group.getCheckedRadioButtonId() == R.id.radiobutton_2) {
                    changeRightAdapter(1, false);
                } else if (group.getCheckedRadioButtonId() == R.id.radiobutton_3) {
                    changeRightAdapter(2, false);
                }
            }
        });
        mSpinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner0.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner0.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner1.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner1.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner2.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner2.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner3.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner3.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner4.getTag() != null) {
                    onSpinnerChanged();
                }
                mSpinner4.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onSpinnerChanged() {
        employId = mSpinner0.getSelectedItemPosition();
        shift = mSpinner1.getSelectedItemPosition() - 1;
        if (mSpinner2.getSelectedItemPosition() > 0) {
            paymodeId = payments.get(mSpinner2.getSelectedItemPosition() - 1).getPayId();
        } else {
            paymodeId = null;
        }
        payModeName = payModes.get(mSpinner2.getSelectedItemPosition());
        date = mSpinner3.getSelectedItemPosition() - 1;
        if (mSpinner4.getSelectedItemPosition() == 0) {
            type = "-1";
        } else if (mSpinner4.getSelectedItemPosition() == typeNames.size() - 1) {
            type = "0";
        } else {
            type = areaEntities.get(mSpinner4.getSelectedItemPosition() - 1).getAreaId();
        }
        new GetOrderDataAsyncTask().execute();
    }

    private void setFragment(int tag) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        Bundle bundle = new Bundle();
        bundle.putInt("employeeId", employId);
        bundle.putInt("shift", shift);
        payModeName = payModes.get(mSpinner2.getSelectedItemPosition());
        bundle.putString("payModeId", paymodeId);
        bundle.putString("payModeName", payModeName);
        bundle.putInt("date", mSpinner3.getSelectedItemPosition() - 1);
        if (mSpinner4.getSelectedItemPosition() == 0) {
            bundle.putString("type", "-1");
        } else if (mSpinner4.getSelectedItemPosition() == typeNames.size() - 1) {
            bundle.putString("type", "0");
        } else {
            bundle.putString("type", areaEntities.get(mSpinner4.getSelectedItemPosition() - 1).getAreaId());
        }
        switch (tag) {
            case ORDERDETAIL:
                if (mLeftDetail == null) {
                    mLeftDetail = new LFragmentOrderLeftDetail();
                    mLeftDetail.setArguments(bundle);
                    fragmentTransaction.add(R.id.containerleft, mLeftDetail);
                } else {
                    fragmentTransaction.show(mLeftDetail);
                }
                break;
            case ORDERCOLLECTION:
                if (mLeftCollection == null) {
                    mLeftCollection = new LFragmentOrderLeftCollection();
                    mLeftCollection.setArguments(bundle);
                    fragmentTransaction.add(R.id.containerleft, mLeftCollection);
                } else {
                    fragmentTransaction.show(mLeftCollection);
                }
                break;
            case ORDERSTATISTIC:
                if (mLeftStatistic == null) {
                    mLeftStatistic = new LFragmentOrderLeftStatistic();
                    mLeftStatistic.setArguments(bundle);
                    fragmentTransaction.add(R.id.containerleft, mLeftStatistic);
                } else {
                    fragmentTransaction.show(mLeftStatistic);
                }
                break;
            default:
                break;
        }
        try {
            fragmentTransaction.commit();
        } catch (Exception e) {

        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mLeftDetail != null) {
            transaction.hide(mLeftDetail);
        }
        if (mLeftCollection != null) {
            transaction.hide(mLeftCollection);
        }
        if (mLeftStatistic != null) {
            transaction.hide(mLeftStatistic);
        }
    }

    private void initView1(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvPageNum = (TextView) view.findViewById(R.id.tv_page_number);
        layoutPageNum = (LinearLayout) view.findViewById(R.id.layout_page_number);
        ibPre = (ImageButton) view.findViewById(R.id.ib_pre_page);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next_page);
    }

    private void initData() {
        mListMap = new ArrayList<>();
        mOrderCollectionBeen = new ArrayList<>();
        mDishTypeModels = new ArrayList<>();
        setRecyclerView();
        initAdapter();
    }

    private void setTvPageNum(Integer pageNum) {
        tvPageNum.setText(String.valueOf(pageNum));
        tvPageNum.setTag(pageNum);
    }

    private void setRecyclerView() {
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void initAdapter() {
        mOrderAdapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<OrderEntity>());
        mOrderColletionAdapter = new OrderColletionAdapter(getActivity().getApplicationContext(), mOrderCollectionBeen);
        mOrderRightStatisticAdapter = new OrderRightStatisticAdapter(getActivity().getApplicationContext());
    }

    //设置账单明细列表adapter
    private void setOrderDetail(boolean isChanged) {
        if (mOrderAdapter == null) {
            mOrderAdapter = new OrderAdapter(getActivity().getApplicationContext(), mListMap);
        }
        mRecyclerView.setAdapter(mOrderAdapter);
        updateOrderRightDetailAdapter(isChanged);
    }

    //设置账单汇总列表adapter
    private void setOrderRightCollection() {
        if (mOrderColletionAdapter == null) {
            mOrderColletionAdapter = new OrderColletionAdapter(getActivity().getApplicationContext(), mOrderCollectionBeen);
        }
        mRecyclerView.setAdapter(mOrderColletionAdapter);
        updateOrderRightCollectionAdapter();
    }

    //设置销售统计列表adapter
    private void setOrderStatistic() {
        if (mOrderRightStatisticAdapter == null) {
            mOrderRightStatisticAdapter = new OrderRightStatisticAdapter(getActivity().getApplicationContext());
        }
        mRecyclerView.setAdapter(mOrderRightStatisticAdapter);
        updateOrderRightStatisticAdapter();
    }

    //更新账单明细列表adapter
    private void updateOrderRightDetailAdapter(boolean isChanged) {
        if (isChanged)
            setTvPageNum(1);
        if (mOrderAdapter != null)
            mOrderAdapter.updateData(mListMap);
        if (mOnOrderSpinnerChangeListener != null)
            mOnOrderSpinnerChangeListener.onOrderRightClick(null, employId, shift, paymodeId, date, type);
    }

    //更新账单汇总列表adapter
    private void updateOrderRightCollectionAdapter() {
        if (mOrderColletionAdapter != null) {
            mOrderColletionAdapter.updateData(mOrderCollectionBeen);
        }
        if (mOnOrderSpinnerChangeListener != null)
            mOnOrderSpinnerChangeListener.nothing();
    }

    //更新账单销售统计列表adapter
    private void updateOrderRightStatisticAdapter() {
        if (mOrderRightStatisticAdapter != null) {
            mOrderRightStatisticAdapter.updateData(mDishTypeModels);
        }
        if (mOnOrderSpinnerChangeListener != null)
            mOnOrderSpinnerChangeListener.onOrderRightStatisticClick(null, shift, date);
    }

    //切换右边adapter
    private void changeRightAdapter(int type, boolean isChanged) {
        switch (type) {
            case 0://显示账单明细的列表
                mLayout0.setVisibility(LinearLayout.VISIBLE);
                mLayout2.setVisibility(LinearLayout.VISIBLE);
                mLayout4.setVisibility(LinearLayout.VISIBLE);
                layoutPageNum.setVisibility(LinearLayout.VISIBLE);
                setOrderDetail(isChanged);
                setFragment(ORDERDETAIL);
                break;
            case 1://显示账单汇总的列表
                mLayout0.setVisibility(LinearLayout.VISIBLE);
                mLayout2.setVisibility(LinearLayout.VISIBLE);
                mLayout4.setVisibility(LinearLayout.VISIBLE);
                layoutPageNum.setVisibility(LinearLayout.GONE);
                setOrderRightCollection();
                setFragment(ORDERCOLLECTION);
                break;
            case 2://显示销售统计的列表
                mLayout0.setVisibility(LinearLayout.GONE);
                mLayout2.setVisibility(LinearLayout.GONE);
                mLayout4.setVisibility(LinearLayout.GONE);
                layoutPageNum.setVisibility(LinearLayout.GONE);
                setOrderStatistic();
                setFragment(ORDERSTATISTIC);
                break;
        }
    }

    //切换左边adapter
    private void changeLeftAdapter(int type) {
        switch (type) {
            case 0://显示账单明细空白页

                break;
            case 1://显示账单汇总空白页

                break;
            case 2://显示销售统计空白页

                break;
        }
    }

    private void setListener1() {
        ibPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvPageNum.getTag() != null) {
                    int currentPage = (int) tvPageNum.getTag();
                    if (currentPage > 1 && mOrderAdapter != null) {
                        new GetOrderDataByPageAsyncTask(currentPage - 1).execute();
                    } else {
                        CustomMethod.showMessage(getContext(), "已是第一页");
                    }
                }
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvPageNum.getTag() != null) {
                    int currentPage = (int) tvPageNum.getTag();
                    new GetOrderDataByPageAsyncTask(currentPage + 1).execute();
                }
            }
        });

        mOrderAdapter.setOnOrderItemClickListener(new OrderAdapter.OnOrderItemClickListener() {
            @Override
            public void onOrderItemClick(OrderEntity orderEntity) {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.onOrderRightClick(orderEntity, employId, shift, paymodeId, date, type);
            }
        });
        mOrderColletionAdapter.setOnOrderColletionItemClickListener(new OrderColletionAdapter.OnOrderColletionItemClickListener() {
            @Override
            public void onNothingClick() {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.nothing();
            }

            @Override
            public void onOrderCashierClick(String cashierId) {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.orderCashier(cashierId, shift, paymodeId, payModeName, date, type);
            }

            @Override
            public void onOrderCollectionClick() {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.orderCollection(employId, shift, paymodeId, payModeName, date, type);
            }

            @Override
            public void onTypeCollectionClick() {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.typeCollection(employId, shift, paymodeId, payModeName, date, type);
            }
        });
        mOrderRightStatisticAdapter.setOnOrderRightStatisticItemClickListener(new OrderRightStatisticAdapter.OnOrderRightStatisticItemClickListener() {
            @Override
            public void onOrderRightStatisticItemClick(DishTypeModel dishTypeModel) {
                if (mOnOrderSpinnerChangeListener != null)
                    mOnOrderSpinnerChangeListener.onOrderRightStatisticClick(dishTypeModel, shift, date);
            }
        });
    }

    class GetOrderDataAsyncTask extends AsyncTask<Object, Integer, Object> {

        public GetOrderDataAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在统计账单数据...");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            //获取账单明细数据
            ArrayList<OrderEntity> orderEntities = new ArrayList<>();
            if (employId == 0) {
                //全部收银员
                orderEntities.addAll(DBHelper.getInstance(getContext()).getOrderByPage(null, shift, paymodeId, date, type, 1));
            } else if (employId == 1) {
                //当前收银员
                SharedPreferences spf = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                String cashierId = spf.getString("employeeId", null);
                orderEntities.addAll(DBHelper.getInstance(getContext()).getOrderByPage(cashierId, shift, paymodeId, date, type, 1));
            }
            if (orderEntities.size() > 0) {
                mListMap.clear();
                mListMap.addAll(orderEntities);
            }

            //获取账单汇总数据
            mOrderCollectionBeen.clear();
            ArrayList<String> mCashierIds = new ArrayList<>();
            if (employId == 0) {
                //全部收银员
                mCashierIds.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getCashierIds(null, shift, paymodeId, date, type));
            } else if (employId == 1) {
                //当前收银员
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                mCashierIds.add(sharedPreferences.getString("employeeId", null));
            }
            OrderCollectionBean orderCollectionBean = new OrderCollectionBean();
            orderCollectionBean.setCashierId(null);
            orderCollectionBean.setCashierName("账单汇总");
            if (employId == 1 && mCashierIds.size() > 0) {
                orderCollectionBean.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(mCashierIds.get(0), shift, paymodeId, date, type)));
            } else {
                orderCollectionBean.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(null, shift, paymodeId, date, type)));
            }
            mOrderCollectionBeen.add(orderCollectionBean);

            OrderCollectionBean orderCollectionBean1 = new OrderCollectionBean();
            orderCollectionBean1.setCashierId(null);
            orderCollectionBean1.setCashierName("分类汇总");
            if (employId == 1 && mCashierIds.size() > 0) {
                orderCollectionBean1.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(mCashierIds.get(0), shift, paymodeId, date, type)));
            } else {
                orderCollectionBean1.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(null, shift, paymodeId, date, type)));
            }
            mOrderCollectionBeen.add(orderCollectionBean1);
            for (String cashierId :
                    mCashierIds) {
                OrderCollectionBean orderCollectionBean2 = new OrderCollectionBean();
                orderCollectionBean2.setCashierName(DBHelper.getInstance(getContext().getApplicationContext()).getEmployeeNameById(cashierId));
                orderCollectionBean2.setCashierId(cashierId);
                orderCollectionBean2.setSpendMoney(String.valueOf(DBHelper.getInstance(getContext().getApplicationContext()).getOrderCollectionSpend(cashierId, shift, paymodeId, date, type)));
                mOrderCollectionBeen.add(orderCollectionBean2);
            }

            //销售统计数据
            mDishTypeModels.clear();
            mDishTypeModels.addAll(DBHelper.getInstance(getContext().getApplicationContext()).queryDishTypeModelData());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radiobutton_1:
                    changeRightAdapter(0, true);
                    break;
                case R.id.radiobutton_2:
                    changeRightAdapter(1, true);
                    break;
                case R.id.radiobutton_3:
                    changeRightAdapter(2, true);
                    break;
            }
            hideLoadingAnim();
        }
    }

    class GetOrderDataByPageAsyncTask extends AsyncTask<Object, Integer, ArrayList<OrderEntity>> {
        private int pageNumber = 0;

        public GetOrderDataByPageAsyncTask(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在获取账单数据...");
            super.onPreExecute();
        }

        @Override
        protected ArrayList<OrderEntity> doInBackground(Object... params) {
            //获取账单明细数据
            ArrayList<OrderEntity> orderEntities = new ArrayList<>();
            if (employId == 0) {
                //全部收银员
                orderEntities.addAll(DBHelper.getInstance(getContext()).getOrderByPage(null, shift, paymodeId, date, type, pageNumber));
            } else if (employId == 1) {
                //当前收银员
                SharedPreferences spf = getContext().getSharedPreferences("loginData", Context.MODE_PRIVATE);
                String cashierId = spf.getString("employeeId", null);
                orderEntities.addAll(DBHelper.getInstance(getContext()).getOrderByPage(cashierId, shift, paymodeId, date, type, pageNumber));
            }
            return orderEntities;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderEntity> o) {
            super.onPostExecute(o);
            hideLoadingAnim();
            if (o.size() > 0) {
                mListMap.clear();
                mListMap.addAll(o);
                setTvPageNum(pageNumber);
                mOrderAdapter.updateData(mListMap);
                mOnOrderSpinnerChangeListener.onOrderRightClick(null, employId, shift, paymodeId, date, type);
            } else {
                CustomMethod.showMessage(getContext(), "已无更多数据");
            }
        }
    }

}
