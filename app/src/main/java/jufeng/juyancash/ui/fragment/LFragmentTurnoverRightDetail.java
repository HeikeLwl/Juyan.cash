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
 * Created by 15157_000 on 2016/6/28 0028.
 */
public class LFragmentTurnoverRightDetail extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OrderAdapter adapter;
    private ImageButton ibPre,ibNext;
    private TextView tvPageNum;
    private MainFragmentListener mOnTurnoverRightDetailClickListener;
    private Map<Integer,List<OrderEntity>> mListMap;
    private CustomLoadingProgress mLoadingProgress;
    private int employId;
    private String type;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    employId = msg.getData().getInt("employId",0);
                    type = msg.getData().getString("type");
                    new GetDataAsyncTask().execute(employId,0,null,-1,type);
                    break;
                case 1:
                    new GetDataAsyncTask().execute(employId,0,null,-1,type);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setTurnoverRightDetailHandler(mHandler);
        try {
            mOnTurnoverRightDetailClickListener = (MainFragmentListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_turnover_right_detail,container,false);
        initView(mView);
        return mView;
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ibPre = (ImageButton) view.findViewById(R.id.ib_pre_page);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next_page);
        tvPageNum = (TextView) view.findViewById(R.id.tv_page_number);
        employId = getArguments().getInt("employId",0);
        type = getArguments().getString("type");
    }

    class GetDataAsyncTask extends AsyncTask<Object,String,ArrayList<OrderEntity>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mListMap == null){
                mListMap = new HashMap<>();
            }else{
                mListMap.clear();
            }
            showLoadingAnim("正在加载...");
        }

        @Override
        protected ArrayList<OrderEntity> doInBackground(Object... params) {
            ArrayList<OrderEntity> results = new ArrayList<>();
            int cashier = (int) params[0];
            int shift = (int) params[1];
            String payModeId = (String) params[2];
            int date = (int) params[3];
            String type = (String) params[4];
            if(cashier == 0){
                //全部收银员
                results.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(null,shift,payModeId,date,type));
            }else if(cashier == 1){
                //当前收银员
                SharedPreferences spf = getContext().getSharedPreferences("loginData",Context.MODE_PRIVATE);
                String cashierId = spf.getString("employeeId",null);
                results.addAll(DBHelper.getInstance(getContext()).getSomeOrderEntity(cashierId,shift,payModeId,date,type));
            }
            int length = results.size();
            int pageCount = length/24 + 1;
            for (int i = 1; i <= pageCount; i++){
                if(i * 24 < length){
                    if(mListMap != null) {
                        mListMap.put(i, results.subList((i - 1) * 24, i * 24));
                    }
                }else{//最后一页
                    if(mListMap!= null) {
                        mListMap.put(i, results.subList((i - 1) * 24, length));
                    }
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderEntity> orderEntities) {
            setTvPageNum(1);
            setAdapter();
            setListener();
            mOnTurnoverRightDetailClickListener.onTurnoverRightClick(null,employId,0,null,-1,type);
            hideLoadingAnim();
            super.onPostExecute(orderEntities);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetDataAsyncTask().execute(employId,0,null,-1,type);
    }

    private void setTvPageNum(Integer pageNum){
        tvPageNum.setText(String.valueOf(pageNum));
        tvPageNum.setTag(pageNum);
    }

    private void setAdapter(){
        if(adapter == null) {
            if(mListMap!= null) {
                adapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<>(mListMap.get(1)));
            }else{
                adapter = new OrderAdapter(getActivity().getApplicationContext(), new ArrayList<OrderEntity>());
            }
            WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setFocusable(false);
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setHasFixedSize(true);
        }else{
            if(mListMap != null) {
                adapter.updateData(new ArrayList<>(mListMap.get(1)));
            }
        }
        setTvPageNum(1);
    }

    private void setListener(){
        adapter.setOnOrderItemClickListener(new OrderAdapter.OnOrderItemClickListener() {
            @Override
            public void onOrderItemClick(OrderEntity orderEntity) {
                mOnTurnoverRightDetailClickListener.onTurnoverRightClick(orderEntity,employId,0,null,-1,type);
            }
        });
        ibPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvPageNum.getTag() != null){
                    int currentPage = (int) tvPageNum.getTag();
                    if(currentPage > 1){
                        if(mListMap != null) {
                            adapter.updateData(new ArrayList<>(mListMap.get(currentPage - 1)));
                        }
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
                    if(mListMap != null && currentPage < mListMap.size()){
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
