package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class SelectOrderAdapter extends RecyclerView.Adapter<SelectOrderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderEntity> mOrderEntities;
    private ArrayList<OrderEntity> mSelectOrderes;

    public SelectOrderAdapter(Context context, String tableId,ArrayList<OrderEntity> selectOrderes,String orderId){
        this.mContext = context;
        mOrderEntities = new ArrayList<>();
        mOrderEntities.addAll(DBHelper.getInstance(mContext).queryOrderData(tableId,orderId));
        mSelectOrderes = new ArrayList<>();
        mSelectOrderes.addAll(selectOrderes);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_order_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckBox.setText("NO."+mOrderEntities.get(position).getOrderNumber1());
        holder.mCheckBox.setChecked(isExist(mOrderEntities.get(position).getOrderId()));
        holder.mCheckBox.setTag(mOrderEntities.get(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OrderEntity orderEntity = (OrderEntity) buttonView.getTag();
                if(isChecked){
                    mSelectOrderes.add(orderEntity);
                }else{
                    for (OrderEntity orderEntity1 :
                            mSelectOrderes) {
                        if (orderEntity1.getOrderId().equals(orderEntity.getOrderId())) {
                            mSelectOrderes.remove(orderEntity1);
                            break;
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public ArrayList<OrderEntity> getSelectOrderes(){
        return mSelectOrderes;
    }

    //判断该订单是否已选中
    private boolean isExist(String orderId){
        for (OrderEntity orderEntity :
                mSelectOrderes) {
            if (orderEntity.getOrderId().equals(orderId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mOrderEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;

        public ViewHolder(View view){
            super(view);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
