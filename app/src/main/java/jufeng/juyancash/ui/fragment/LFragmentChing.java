package jufeng.juyancash.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.VolleyInterface;
import jufeng.juyancash.VolleyRequest;
import jufeng.juyancash.adapter.ChingLeftAdapter;
import jufeng.juyancash.adapter.ChingRightAdapter;
import jufeng.juyancash.adapter.StockLeftAdapter;
import jufeng.juyancash.bean.EstimateModel;
import jufeng.juyancash.bean.PublicModule;
import jufeng.juyancash.dao.SellCheckEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.syncdata.GoodsInventoryVo;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.KeyboardUtil1;
import jufeng.juyancash.ui.customview.WrapContentGridLayoutManager;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.MD5Util;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentChing extends BaseFragment {
    private RecyclerView mRecyclerView;
    private TextView tvClear, tvRefresh;
    private EditText etIndex;
    private RecyclerView mRecyclerView1;
    private ChingRightAdapter rightAdapter;
    private ChingLeftAdapter leftAdapter;
    private StockLeftAdapter mStockLeftAdapter;
    private TextView tvChange,tvLabel,tvTitle;
    private LinearLayout mLinearLayout;
    private LinearLayout layoutStock;
    private TextView tvRefreshStock;
    private KeyboardUtil1 mKeyboardUtil1;
    private MainFragmentListener mMainFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mMainFragmentListener = (MainFragmentListener) context;
        }catch (Exception e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_ching,container,false);
        initView(mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLeftAdapter();
        setRightAdapter();
        setListener();
    }

    private void initView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvClear = (TextView) view.findViewById(R.id.tv_clear);
        tvRefresh = (TextView) view.findViewById(R.id.tv_refresh);
        etIndex = (EditText) view.findViewById(R.id.et_search);
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        tvChange = (TextView) view.findViewById(R.id.tv_change);
        tvLabel = (TextView) view.findViewById(R.id.tv_label);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.layout_ching);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        layoutStock = (LinearLayout) view.findViewById(R.id.layout_stock_refresh);
        tvRefreshStock = (TextView) view.findViewById(R.id.tv_refresh1);
        mKeyboardUtil1 = new KeyboardUtil1(view,getContext(),etIndex,2,R.id.ching_keyboard);
        CustomMethod.setMyInputType(etIndex,getActivity());
        etIndex.requestFocus();
        mKeyboardUtil1.showKeyboard();

        tvChange.setTag(0);
        leftAdapter = new ChingLeftAdapter(getActivity().getApplicationContext());
        mStockLeftAdapter = new StockLeftAdapter(getActivity().getApplicationContext());
    }

    private void setListener(){
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChingList();
            }
        });

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChingList();
            }
        });

        tvRefreshStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockList();
            }
        });

        etIndex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String matchStr = etIndex.getText().toString();
                rightAdapter.updateData(matchStr,DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
            }
        });

        rightAdapter.setOnChingRightItemClickListener(new ChingRightAdapter.OnChingRightItemClickListener() {
            @Override
            public void onChingRignthItemClick(String dishId) {
                if(!DBHelper.getInstance(getActivity().getApplicationContext()).isDishChing(dishId)) {
                    final SellCheckEntity sellCheckEntity = new SellCheckEntity();
                    sellCheckEntity.setDishId(dishId);
                    sellCheckEntity.setSellCheckId(UUID.randomUUID().toString());
                    sellCheckEntity.setIsSellOut(1);

                    EstimateModel estimateModel = new EstimateModel();
                    estimateModel.setId(sellCheckEntity.getSellCheckId());
                    estimateModel.setDishId(dishId);
                    estimateModel.setIsSellOut(1);
                    estimateModel.setPartnerCode(getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode", null));
                    estimateModel.setType(0);
                    estimateModel.setUnitName("份");

                    String partnerCode = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode", null);
                    String data = JSON.toJSONString(estimateModel);
                    long ts = System.currentTimeMillis();
                    String sign = MD5Util.getMD5String(partnerCode + data + ts + getContext().getResources().getString(R.string.APP_KEY));
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("partnerCode", partnerCode);
                    map.put("data", data);
                    map.put("ts", String.valueOf(ts));
                    map.put("sign", sign);
                    VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.ADD_CHING), dishId, map, new VolleyInterface(VolleyInterface.listener, VolleyInterface.errorListener) {
                        @Override
                        public void onSuccess(String arg0) {
                            PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                            if (publicModule.getCode() == 0) {
                                DBHelper.getInstance(getActivity().getApplicationContext()).insertChing(sellCheckEntity);
                                rightAdapter.updateData(etIndex.getText().toString(), DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
                                leftAdapter.updateData();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "添加估清失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError arg0) {
                            Toast.makeText(getActivity().getApplicationContext(), "添加估清失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    CustomMethod.showMessage(getContext(),"该商品已估清");
                }
            }
        });

        leftAdapter.setOnChingLeftItemClickListener(new ChingLeftAdapter.OnChingLeftItemClickListener() {
            @Override
            public void onChingLeftItemClick(final SellCheckEntity sellCheckEntity) {
                String partnerCode = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode",null);
                long ts = System.currentTimeMillis();
                String id = sellCheckEntity.getSellCheckId();
                String sign = MD5Util.getMD5String(partnerCode+id+ts+getContext().getResources().getString(R.string.APP_KEY));
                Map<String,String> map = new HashMap<String, String>();
                map.put("partnerCode",partnerCode);
                map.put("ts",String.valueOf(ts));
                map.put("id",id);
                map.put("sign",sign);
                VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.DELETE_CHING), "DELETE_CHING", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
                    @Override
                    public void onSuccess(String arg0) {
                        PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                        if(publicModule.getCode() == 0) {
                            DBHelper.getInstance(getActivity().getApplicationContext()).dropChing(sellCheckEntity);
                            rightAdapter.updateData(etIndex.getText().toString(),DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
                            leftAdapter.updateData();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "删除估清失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError arg0) {
                        Toast.makeText(getActivity().getApplicationContext(), "删除估清失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)tvChange.getTag() == 0){
                    //当前为估清列表
                    tvChange.setTag(1);
                    tvTitle.setText("库存列表");
                    tvChange.setText("查看估清");
                    tvLabel.setText("库存");
                    mRecyclerView.setAdapter(mStockLeftAdapter);
                    mLinearLayout.setVisibility(LinearLayout.GONE);
                    layoutStock.setVisibility(LinearLayout.VISIBLE);
                }else{
                    //当前为库存列表
                    tvChange.setTag(0);
                    tvTitle.setText("估清列表");
                    tvChange.setText("查看库存");
                    tvLabel.setText("恢复");
                    mRecyclerView.setAdapter(leftAdapter);
                    mLinearLayout.setVisibility(LinearLayout.VISIBLE);
                    layoutStock.setVisibility(LinearLayout.GONE);
                }
            }
        });
    }

    private void setLeftAdapter(){
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(leftAdapter);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setRightAdapter(){
        rightAdapter = new ChingRightAdapter(getActivity().getApplicationContext(),"", DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
        WrapContentGridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getActivity().getApplicationContext(), 6);
        mRecyclerView1.setLayoutManager(gridLayoutManager);
        mRecyclerView1.setAdapter(rightAdapter);
        mRecyclerView1.setFocusable(false);
        mRecyclerView1.setItemAnimator(null);
        mRecyclerView1.setHasFixedSize(true);
    }

    private void getChingList(){
        showLoadingAnim("正在刷新估清数据");
        String partnerCode = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode",null);
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.GET_CHING_LIST), "DELETE_CHING", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "估清: "+arg0);
                PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                if(publicModule.getCode() == 0) {
                    ArrayList<EstimateModel> estimateModeles = (ArrayList<EstimateModel>) JSONArray.parseArray(publicModule.getData(),EstimateModel.class);
                    for(int i = 0; i < estimateModeles.size();i++){
                        EstimateModel estimateModel = estimateModeles.get(i);
                        if(!DBHelper.getInstance(getActivity().getApplicationContext()).isDishExist(estimateModel.getDishId())) {
                            break;
                        }
                        SellCheckEntity sellCheckEntity = new SellCheckEntity();
                        sellCheckEntity.setSellCheckId(estimateModel.getId());
                        sellCheckEntity.setIsSellOut(1);
                        sellCheckEntity.setType(estimateModel.getType());
                        sellCheckEntity.setDishId(estimateModel.getDishId());
                        DBHelper.getInstance(getActivity().getApplicationContext()).insertChing(sellCheckEntity);
                    }
                    rightAdapter.updateData(etIndex.getText().toString(),DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
                    leftAdapter.updateData();

                    hideLoadingAnim();
                }else{
                    hideLoadingAnim();
                    CustomMethod.showMessage(getContext(),"刷新估清数据失败，请稍后重试！");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"刷新估清数据失败，请稍后重试！");
            }
        });
    }

    //刷新库存列表
    private void getStockList(){
        showLoadingAnim("正在刷新库存数据");
        String partnerCode = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode",null);
        String data = "0";
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode+data+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        map.put("ts",String.valueOf(ts));
        map.put("data",data);
        map.put("sign",sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.REFRESH_STOCK), "REFRESH_STOCK", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "刷新库存: "+arg0);
                try {
                    PublicModule publicModule = JSON.parseObject(arg0, PublicModule.class);
                    if (publicModule.getCode() == 0) {
                        ArrayList<GoodsInventoryVo> goodsInventoryVos = (ArrayList<GoodsInventoryVo>) JSONArray.parseArray(publicModule.getData(), GoodsInventoryVo.class);
                        DBHelper.getInstance(getContext().getApplicationContext()).refreshStock(goodsInventoryVos);
                        rightAdapter.updateData(etIndex.getText().toString(), DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
                        mStockLeftAdapter.updateData();
                        mMainFragmentListener.refreshStock();
                        hideLoadingAnim();
                    } else {
                        hideLoadingAnim();
                        CustomMethod.showMessage(getContext(),"刷新库存失败，请稍后重试！");
                    }
                }catch (Exception e){
                    hideLoadingAnim();
                    CustomMethod.showMessage(getContext(),"刷新库存失败，请稍后重试！");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"刷新库存失败，请稍后重试！");
            }
        });
    }

    //清空
    private void clearChingList(){
        showLoadingAnim("正在恢复售卖");
        String partnerCode = getActivity().getApplicationContext().getSharedPreferences("loginData", Activity.MODE_PRIVATE).getString("partnerCode",null);
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String(partnerCode+"all"+ts+getContext().getResources().getString(R.string.APP_KEY));
        Map<String,String> map = new HashMap<>();
        map.put("partnerCode",partnerCode);
        map.put("ts",String.valueOf(ts));
        map.put("id","all");
        map.put("sign",sign);
        VolleyRequest.RequestPost(getActivity().getApplicationContext(), getContext().getResources().getString(R.string.DELETE_CHING_LIST), "DELETE_CHING_LIST", map, new VolleyInterface(VolleyInterface.listener,VolleyInterface.errorListener) {
            @Override
            public void onSuccess(String arg0) {
                Log.d("###", "onSuccess: "+arg0);
                PublicModule publicModule = JSON.parseObject(arg0,PublicModule.class);
                if(publicModule.getCode() == 0) {
                    DBHelper.getInstance(getActivity().getApplicationContext()).clearChing();
                    rightAdapter.updateData(etIndex.getText().toString(),DBHelper.getInstance(getActivity().getApplicationContext()).getAllSellOut());
                    leftAdapter.updateData();
                    hideLoadingAnim();
                }else{
                    hideLoadingAnim();
                    CustomMethod.showMessage(getContext(),"恢复售卖失败，请稍后重试！");
                }
            }

            @Override
            public void onError(VolleyError arg0) {
                hideLoadingAnim();
                CustomMethod.showMessage(getContext(),"恢复售卖失败，请稍后重试！");
            }
        });
    }
}
