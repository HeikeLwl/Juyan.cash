package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderAdapter;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.customview.CustomLoadingProgress;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderRightDetail extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OrderAdapter adapter;
    private ImageButton ibPre,ibNext;
    private TextView tvPageNum;
    private MainFragmentListener mOnOrderRightDetailClickListener;
    private int employId;
    private int shift;
    private String paymodeId;
    private int date;
    private String type;
    private Map<Integer,List<OrderEntity>> mListMap;
    private CustomLoadingProgress mLoadingProgress;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    employId = msg.getData().getInt("employeeId",0);
                    shift = msg.getData().getInt("shift",-1);
                    paymodeId = msg.getData().getString("payModeId");
                    date = msg.getData().getInt("date",-1);
                    type = msg.getData().getString("type");
                    new GetDataAsyncTask().execute(employId,shift,paymodeId,date,type);
                    break;
                case 1:
                    new GetDataAsyncTask().execute(employId,shift,paymodeId,date,type);
                    mOnOrderRightDetailClickListener.onOrderRightClick(null,employId,shift,paymodeId,date,type);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setOrderRightDetailHandler(mHandler);
        try {
            mOnOrderRightDetailClickListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_right_detail,container,false);
        initView(mView);
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ibPre = (ImageButton) view.findViewById(R.id.ib_pre_page);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next_page);
        tvPageNum = (TextView) view.findViewById(R.id.tv_page_number);
        employId = getArguments().getInt("employeeId",0);
        shift = getArguments().getInt("shift",-1);
        paymodeId = getArguments().getString("payModeId");
        date = getArguments().getInt("date",-1);
        type = getArguments().getString("type");
        mListMap = new HashMap<>();
        setTvPageNum(1);
    }

    class GetDataAsyncTask extends AsyncTask<Object,String,ArrayList<OrderEntity>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingAnim("正在加载...");
        }

        @Override
        protected ArrayList<OrderEntity> doInBackground(Object... params) {
            int cashier = (int) params[0];
            int shift = (int) params[1];
            String payModeId = (String) params[2];
            int date = (int) params[3];
            String type = (String) params[4];
            ArrayList<OrderEntity> orderEntities = new ArrayList<>();
            if(cashier == 0){
                //全部收银员
                orderEntities.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(null,shift,payModeId,date,type));
            }else if(cashier == 1){
                //当前收银员
                SharedPreferences spf = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE);
                String cashierId = spf.getString("employeeId",null);
                orderEntities.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(cashierId,shift,payModeId,date,type));
            }
            mListMap.clear();
            int length = orderEntities.size();
            int pageCount = length/24 + 1;
            for (int i = 1; i <= pageCount; i++){
                if(i * 24 < length){
                    mListMap.put(i, orderEntities.subList((i-1) * 24,i*24));
                }else{//最后一页
                    mListMap.put(i, orderEntities.subList((i-1) * 24,length));
                }
            }
            return orderEntities;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderEntity> orderEntities) {
            setTvPageNum(1);
            setAdapter();
            setListener();
            mOnOrderRightDetailClickListener.onOrderRightClick(null,employId,shift,paymodeId,date,type);
            hideLoadingAnim();
            super.onPostExecute(orderEntities);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetDataAsyncTask().execute(employId,shift,paymodeId,date,type);
    }

    private void setTvPageNum(Integer pageNum){
        tvPageNum.setText(String.valueOf(pageNum));
        tvPageNum.setTag(pageNum);
    }

    private void setAdapter(){
        if(adapter == null) {
            adapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<>( mListMap.get(1)));
            WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setFocusable(false);
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setHasFixedSize(true);
        }else{
            adapter.updateData(new ArrayList<>(mListMap.get(1)));
        }
    }

    private void setListener(){
        adapter.setOnOrderItemClickListener(new OrderAdapter.OnOrderItemClickListener() {
            @Override
            public void onOrderItemClick(OrderEntity orderEntity) {
                mOnOrderRightDetailClickListener.onOrderRightClick(orderEntity,employId,shift,paymodeId,date,type);
            }
        });
        ibPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvPageNum.getTag() != null){
                    int currentPage = (int) tvPageNum.getTag();
                    if(currentPage > 1){
                        adapter.updateData(new ArrayList<>(mListMap.get(currentPage - 1)));
                        setTvPageNum(currentPage - 1);
                    }else{
                        CustomMethod.showMessage(getContext(),"已是第一页");
                    }
                }
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvPageNum.getTag() != null){
                    int currentPage = (int) tvPageNum.getTag();
                    if(currentPage < mListMap.size()){
                        adapter.updateData(new ArrayList<>(mListMap.get(currentPage + 1)));
                        setTvPageNum(currentPage + 1);
                    }else{
                        CustomMethod.showMessage(getContext(),"已是最后一页");
                    }
                }
            }
        });
    }
}
