package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by 15157_000 on 2016/6/23 0023.
 */
public class SnackSelectedTaocanAdapter extends RecyclerView.Adapter<SnackSelectedTaocanAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderTaocanGroupDishEntity> selectedTaocans;
    private OrderDishEntity mOrderDishEntity;
    private OnSelectedTaocanItemClickListener mOnSelectedTaocanItemClickListener;
    private Map<String,Integer> map;
    private Map<String,OrderTaocanGroupDishEntity> selectTaocanMap;

    public SnackSelectedTaocanAdapter(Context context, OrderDishEntity orderDishEntity, int status) {
        this.mContext = context;
        this.mOrderDishEntity = orderDishEntity;
        selectedTaocans = new ArrayList<>();
        map = new HashMap<>();
        selectTaocanMap = new HashMap<>();
        initData(status);
    }

    private void initData(int status) {
        selectedTaocans.clear();
        map.clear();
        selectTaocanMap.clear();
        if(status == 1){
            selectedTaocans.addAll(DBHelper.getInstance(mContext).getHadOrderedTaocanDish(mOrderDishEntity));
        }else{
            selectedTaocans.addAll(DBHelper.getInstance(mContext).getOrderedTaocanDish(mOrderDishEntity));
        }
        for (int i = 0; i < selectedTaocans.size(); i++){
            map.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),i);
            selectTaocanMap.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),selectedTaocans.get(i));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = selectedTaocans.get(position);
        holder.itemView.setTag(orderTaocanGroupDishEntity);
        int status = orderTaocanGroupDishEntity.getStatus();
        switch (status) {
            case -1:
                holder.ivIsOrdered.setImageBitmap(null);
                break;
            case 0:
                holder.ivIsOrdered.setImageResource(R.drawable.iv_unorder);
                break;
            case 1:
                holder.ivIsOrdered.setImageResource(R.drawable.iv_order);
                break;
        }
        holder.ibAdd.setVisibility(ImageButton.GONE);
        holder.ibReduce.setVisibility(ImageButton.GONE);
        holder.ibAdd.setClickable(false);
        holder.ibReduce.setClickable(false);

        holder.tvName.setText(orderTaocanGroupDishEntity.getDishName());
        holder.tvCount.setText(String.valueOf(orderTaocanGroupDishEntity.getTaocanGroupDishCount()));
        String specifyName = orderTaocanGroupDishEntity.getSpecifyName();
        String practiceName = orderTaocanGroupDishEntity.getPracticeName();
        String extras = "";
        if (specifyName != null && !specifyName.isEmpty()) {
            extras += "(" + specifyName;
        }
        if (practiceName != null && !practiceName.isEmpty()) {
            if (extras.length() > 0) {
                extras += "," + practiceName + ")";
            } else {
                extras += "(" + practiceName + ")";
            }
        }else{
            extras = extras.length() > 1 ? extras+")":"";
        }
        holder.tvExtras.setText(extras);
        float extrasPrice = orderTaocanGroupDishEntity.getExtraPrice();
        holder.tvExtraPrice.setText("+" + extrasPrice);

        holder.rootLayout.setTag(orderTaocanGroupDishEntity);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectedTaocanItemClickListener.onTaocanItemClick((OrderTaocanGroupDishEntity) v.getTag());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.selected_taocan_item_layout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public int getItemCount() {
        return selectedTaocans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvExtras, tvCount, tvExtraPrice;
        private ImageButton ibAdd, ibReduce;
        private ImageView ivIsOrdered;
        private LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tvExtras = (TextView) itemView.findViewById(R.id.tv_dish_extras);
            ibReduce = (ImageButton) itemView.findViewById(R.id.ib_reduce);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            ibAdd = (ImageButton) itemView.findViewById(R.id.ib_add);
            tvExtraPrice = (TextView) itemView.findViewById(R.id.tv_extra_price);
            ivIsOrdered = (ImageView) itemView.findViewById(R.id.iv_is_ordered);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
        }
    }

    public void updateData(OrderDishEntity orderDishEntity,int status) {
        this.mOrderDishEntity = orderDishEntity;
        selectedTaocans.clear();
        map.clear();
        selectTaocanMap.clear();
        if(status == 1){
            selectedTaocans.addAll(DBHelper.getInstance(mContext).getHadOrderedTaocanDish(mOrderDishEntity));
        }else{
            selectedTaocans.addAll(DBHelper.getInstance(mContext).getOrderedTaocanDish(mOrderDishEntity));
        }
        for (int i = 0; i < selectedTaocans.size(); i++){
            map.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),i);
            selectTaocanMap.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),selectedTaocans.get(i));
        }
        notifyDataSetChanged();
    }

    public void changeItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity){
        String orderTaocanGroupDishId = orderTaocanGroupDishEntity.getOrderTaocanGroupDishId();
        if(selectTaocanMap.containsKey(orderTaocanGroupDishId)){
            int position = selectedTaocans.indexOf(selectTaocanMap.get(orderTaocanGroupDishId));
            selectedTaocans.remove(position);
            selectedTaocans.add(position,orderTaocanGroupDishEntity);
            selectTaocanMap.put(orderTaocanGroupDishId,orderTaocanGroupDishEntity);
            notifyItemChanged(position);
        }
    }

    //加菜
    public void addItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity){
        selectedTaocans.add(0,orderTaocanGroupDishEntity);
        for (int i = 0; i < selectedTaocans.size(); i++){
            map.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),i);
            selectTaocanMap.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),selectedTaocans.get(i));
        }
        notifyItemInserted(0);
    }

    //删除菜品
    public void deleteItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity){
        if(selectTaocanMap.containsKey(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId())){
            int position = selectedTaocans.indexOf(selectTaocanMap.get(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId()));
            selectedTaocans.remove(position);
            for (int i = 0; i < selectedTaocans.size(); i++){
                map.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),i);
                selectTaocanMap.put(selectedTaocans.get(i).getOrderTaocanGroupDishId(),selectedTaocans.get(i));
            }
            notifyItemRemoved(position);
        }
    }

    public void setOnSelectedTaocanItemClickListener(OnSelectedTaocanItemClickListener listener) {
        this.mOnSelectedTaocanItemClickListener = listener;
    }

    public interface OnSelectedTaocanItemClickListener {
        void onTaocanItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

        void onTaocanAddClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

        void onTaocanReduceClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);
    }
}
