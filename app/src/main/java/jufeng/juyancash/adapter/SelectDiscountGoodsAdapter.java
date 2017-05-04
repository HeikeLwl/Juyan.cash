package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class SelectDiscountGoodsAdapter extends RecyclerView.Adapter<SelectDiscountGoodsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SomeDiscountGoodsEntity> mSomeDiscountGoods;
    private ArrayList<OrderDishEntity> mOrderDishEntities;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private Map<String,Integer> allDishMap;
    private Map<String,SomeDiscountGoodsEntity> checkDishMap;

    public SelectDiscountGoodsAdapter(Context context, ArrayList<SomeDiscountGoodsEntity> someDiscountGoods, String orderId, DiscountHistoryEntity discountHistoryEntity,boolean isOpenJoinOrder){
        this.mContext = context;
        mDiscountHistoryEntity = discountHistoryEntity;
        mSomeDiscountGoods = new ArrayList<>();
        mSomeDiscountGoods.addAll(someDiscountGoods);
        mOrderDishEntities = new ArrayList<>();
        allDishMap = new HashMap<>();
        checkDishMap = new HashMap<>();
        if(isOpenJoinOrder){
            mOrderDishEntities.addAll(DBHelper.getInstance(mContext).queryJoinDiscountNormalDish(orderId));
        }else{
            mOrderDishEntities.addAll(DBHelper.getInstance(mContext).queryOrderedDish3(orderId));
        }
        for (int i = 0; i < mOrderDishEntities.size(); i++) {
            allDishMap.put(mOrderDishEntities.get(i).getOrderDishId(),i);
        }
        for (SomeDiscountGoodsEntity item :
                mSomeDiscountGoods) {
            if(!checkDishMap.containsKey(item.getOrderDishId()))
                checkDishMap.put(item.getOrderDishId(),item);
        }
    }

    public void updateData(ArrayList<SomeDiscountGoodsEntity> someDiscountGoods, String orderId, DiscountHistoryEntity discountHistoryEntity,boolean isOpenJoinOrder){
        mSomeDiscountGoods.clear();
        mSomeDiscountGoods.addAll(someDiscountGoods);
        mDiscountHistoryEntity = discountHistoryEntity;
        mOrderDishEntities.clear();
        allDishMap.clear();
        checkDishMap.clear();
        if(isOpenJoinOrder){
            mOrderDishEntities.addAll(DBHelper.getInstance(mContext).queryJoinDiscountNormalDish(orderId));
        }else{
            mOrderDishEntities.addAll(DBHelper.getInstance(mContext).queryOrderedDish3(orderId));
        }
        for (int i = 0; i < mOrderDishEntities.size(); i++) {
            allDishMap.put(mOrderDishEntities.get(i).getOrderDishId(),i);
        }
        for (SomeDiscountGoodsEntity item :
                mSomeDiscountGoods) {
            if(!checkDishMap.containsKey(item.getOrderDishId()))
                checkDishMap.put(item.getOrderDishId(),item);
        }
        notifyDataSetChanged();
    }

    public ArrayList<SomeDiscountGoodsEntity> getSomeDiscountGoods(){
        return mSomeDiscountGoods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_discount_goods_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cbDishName.setAnimation(null);
        holder.cbDishName.setChecked(checkDishMap.containsKey(mOrderDishEntities.get(position).getOrderDishId()));
        holder.cbDishName.setText(mOrderDishEntities.get(position).getDishName());
        holder.cbDishName.setTag(mOrderDishEntities.get(position).getOrderDishId());
        holder.cbDishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderDishId = (String) v.getTag();
                OrderDishEntity orderDishEntity = mOrderDishEntities.get(allDishMap.get(orderDishId));
                String dishId = orderDishEntity.getDishId();
                if(checkDishMap.containsKey(orderDishId)){
                    //该菜品已经选中
                    mSomeDiscountGoods.remove(checkDishMap.get(orderDishId));
                    checkDishMap.remove(orderDishId);
                }else{
                    SomeDiscountGoodsEntity someDiscountGoodsEntity = new SomeDiscountGoodsEntity();
                    someDiscountGoodsEntity.setDiscountHistoryId(mDiscountHistoryEntity.getDiscountHistoryId());
                    someDiscountGoodsEntity.setSomeDiscountGoodsId(UUID.randomUUID().toString());
                    someDiscountGoodsEntity.setDishId(dishId);
                    someDiscountGoodsEntity.setOrderDishId(orderDishId);
                    someDiscountGoodsEntity.setDiscountHistoryId(mDiscountHistoryEntity.getDiscountHistoryId());
                    checkDishMap.put(orderDishId,someDiscountGoodsEntity);
                    mSomeDiscountGoods.add(someDiscountGoodsEntity);
                }
                notifyItemChanged(allDishMap.get(orderDishId));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderDishEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox cbDishName;
        public ViewHolder(View view){
            super(view);
            cbDishName = (CheckBox) view.findViewById(R.id.cb_dish_name);
        }
    }
}
