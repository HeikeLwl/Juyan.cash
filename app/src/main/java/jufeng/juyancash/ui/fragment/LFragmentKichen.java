package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.KitchenLeftAdapter;
import jufeng.juyancash.adapter.KitchenRightAdapter;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.PrinterFailedHistoryEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.printer.KitchenPrintAgain;
import jufeng.juyancash.printer.RemindPrintAgainRunnable;
import jufeng.juyancash.printer.RetreatPrintAgainRunnable;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.LCustomeRadioGroup;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.IObjectFilter;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentKichen extends BaseFragment {
    private RecyclerView mRecyclerView;
    private KitchenLeftAdapter adapter;
    private RecyclerView mRecyclerView1;
    private KitchenRightAdapter adapter1;
    private LCustomeRadioGroup mLCustomeRadioGroup;
    private Button btnClear, btnPrintAgain;
    private MainFragmentListener mOnKitchenPrintClickListener;
    private ArrayList<PrinterFailedHistoryEntity> mPrinterFailedHistoryEntities;
    private Map<String, Integer> allMap;
    private String mPrintKitchenId;
    private int mStatus;
    private IObjectFilter filter = new IObjectFilter() {
        public boolean filter(Object object) {
            PrinterFailedHistoryEntity printerFailedHistoryEntity = (PrinterFailedHistoryEntity) object;
            if (mPrintKitchenId == null) {
                return mStatus == printerFailedHistoryEntity.getPrintStatus();
            } else {
                return printerFailedHistoryEntity.getPrintKitchenId().equals(mPrintKitchenId) && mStatus == printerFailedHistoryEntity.getPrintStatus();
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.updateData();
                    int status = 0;
                    switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                        case R.id.radiobutton_1:
                            status = 1;
                            break;
                        case R.id.radiobutton_2:
                            status = 0;
                            break;
                    }
                    new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), status).execute();
                    btnPrintAgain.setClickable(false);
                    btnPrintAgain.setTag(null);
                    break;
                case 1:
                    adapter.updateData();
                    break;
                case 100:
                    Log.d("###", "打印机返回数据: " + msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setKitchenPrintHandler(mHandler);
        try {
            mOnKitchenPrintClickListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_kichen, container, false);
        initView(mView);
        setAdapter();
        new MyAsyncTask1(null, 1).execute();
        setListener();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        mLCustomeRadioGroup = (LCustomeRadioGroup) view.findViewById(R.id.lcustomeradiogroup);
        btnClear = (Button) view.findViewById(R.id.btn_clear);
        btnPrintAgain = (Button) view.findViewById(R.id.btn_print_again);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mPrinterFailedHistoryEntities = new ArrayList<>();
        allMap = new HashMap<>();
    }

    private void setAdapter() {
        adapter = new KitchenLeftAdapter(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);

        adapter1 = new KitchenRightAdapter(getActivity().getApplicationContext(), mPrinterFailedHistoryEntities, allMap);
        mRecyclerView1.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView1.setAdapter(adapter1);
    }

    private void setListener() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printKitchenId = adapter.getSelectedPrintKitchenId();
                switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_1:
                        DBHelper.getInstance(getActivity().getApplicationContext()).clearPrintHistory(printKitchenId, 1);
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 1).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                    case R.id.radiobutton_2:
                        DBHelper.getInstance(getActivity().getApplicationContext()).clearPrintHistory(printKitchenId, 0);
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 0).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                }
            }
        });

        btnPrintAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printHistoryId = (String) v.getTag();
                if (printHistoryId != null) {
                    new MyAsyckTask().execute(printHistoryId);
                } else {
                    CustomMethod.showMessage(getContext(), "请选择要补打的账单");
                }
            }
        });

        adapter.setOnKitchenLeftItemClickListener(new KitchenLeftAdapter.OnKitchenLeftItemClickListener() {
            @Override
            public void onKitchenLeftItemClick(String printKitchenId) {
                switch (mLCustomeRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_1:
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 1).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                    case R.id.radiobutton_2:
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 0).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                }
            }
        });
        mLCustomeRadioGroup.setOnCheckedChangeListener(new LCustomeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LCustomeRadioGroup group, int checkedId, Bundle bundle) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_1:
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 1).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                    case R.id.radiobutton_2:
                        new MyAsyncTask1(adapter.getSelectedPrintKitchenId(), 0).execute();
                        btnPrintAgain.setTag(null);
                        btnPrintAgain.setClickable(false);
                        break;
                }
            }
        });

        adapter1.setOnKitchenRightItemClickListener(new KitchenRightAdapter.OnKitchenRightItemClickListener() {
            @Override
            public void onKitchenRightItemClick(String printHistoryId) {
                btnPrintAgain.setClickable(printHistoryId != null);
                btnPrintAgain.setTag(printHistoryId);
            }
        });
    }


    class MyAsyckTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingAnim("正在补打...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            return printAgain(params[0]);
        }

        @Override
        protected void onPostExecute(Integer o) {
            super.onPostExecute(o);
            hideLoadingAnim();
            if(o == 0){
                CustomMethod.showMessage(getContext(),"补打命令发送成功");
            }else{
                CustomMethod.showMessage(getContext(),"补打命令发送失败");
            }
        }
    }

    //落单厨打
    private int printAgain(String printHistoryId) {
        try {
            PrinterFailedHistoryEntity mPrinterFailedHistoryEntity = DBHelper.getInstance(getContext()).getPrintHistory(printHistoryId);
            String orderId = mPrinterFailedHistoryEntity.getOrderId();
            String printKitchenId = mPrinterFailedHistoryEntity.getPrintKitchenId();
            PrintKitchenEntity printKitchenEntity = DBHelper.getInstance(getContext()).getPrintKitchen(printKitchenId);
            if (printKitchenEntity != null && orderId != null && mPrinterFailedHistoryEntity.getPrintDishArray() != null && mPrinterFailedHistoryEntity.getPrintDishArray().length > 0) {
                PrintBean callable = null;
                switch (mPrinterFailedHistoryEntity.getPrintType()) {
                    case 0:
                        callable = new KitchenPrintAgain(getContext().getApplicationContext(), mPrinterFailedHistoryEntity, mPrinterFailedHistoryEntity.getPrintDishArray(), orderId, printKitchenEntity, false);
                        break;
                    case 1:
                        callable = new KitchenPrintAgain(getContext().getApplicationContext(), mPrinterFailedHistoryEntity, mPrinterFailedHistoryEntity.getPrintDishArray(), orderId, printKitchenEntity, true);
                        break;
                    case 2:
                        callable = new RetreatPrintAgainRunnable(getContext().getApplicationContext(), mPrinterFailedHistoryEntity, mPrinterFailedHistoryEntity.getPrintDishArray(), orderId, printKitchenEntity, null, null);
                        break;
                    case 3:
                        callable = new RemindPrintAgainRunnable(getContext().getApplicationContext(), mPrinterFailedHistoryEntity, mPrinterFailedHistoryEntity.getPrintDishArray(), orderId, printKitchenEntity, null);
                        break;
                }
                if (callable != null) {
                    mOnKitchenPrintClickListener.onPrintAgain(printKitchenEntity.getPrintKitchenIp(), callable);
                    return 0;
                }else{
                    return 1;
                }
            }else{
                return 2;
            }
        } catch (Exception e) {
            return 3;
        }
    }

    class MyAsyncTask1 extends AsyncTask<String, Integer, Object> {

        public MyAsyncTask1(String printKitchenId, int status) {
            mPrintKitchenId = printKitchenId;
            mStatus = status;
        }

        @Override
        protected void onPreExecute() {
            showLoadingAnim("正在加载数据...");
            mPrinterFailedHistoryEntities.clear();
            allMap.clear();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {
            mPrinterFailedHistoryEntities.addAll(DBHelper.getInstance(getContext().getApplicationContext()).getAllFailedPrinterHistory(mPrintKitchenId, mStatus));
            for (int i = 0; i < mPrinterFailedHistoryEntities.size(); i++) {
                allMap.put(mPrinterFailedHistoryEntities.get(i).getPrinterFailedHistoryId(), i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter1.updateData(mPrinterFailedHistoryEntities, allMap);
            hideLoadingAnim();
        }
    }
}
