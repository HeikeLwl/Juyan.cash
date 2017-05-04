package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;


/**
 * Created by Administrator102 on 2016/7/20.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderEntity> mOrderEntities;
    private OrderEntity selectedOrderEntity;
    private OnOrderItemClickListener mOnOrderItemClickListener;
    private int oldPosition;

    public OrderAdapter(Context context,ArrayList<OrderEntity> orderEntities) {
        this.mContext = context;
        mOrderEntities = new ArrayList<>();
        mOrderEntities.addAll(orderEntities);
        selectedOrderEntity = null;
        oldPosition = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderEntity orderEntity = mOrderEntities.get(position);
        if(selectedOrderEntity != null && selectedOrderEntity.getOrderId().equals(orderEntity.getOrderId())){
            holder.rootLayout.setBackgroundResource(R.drawable.order_selected_background);
        }else{
            holder.rootLayout.setBackgroundColor(0x00ffffff);
        }
        if(orderEntity.getIsShift() != null && orderEntity.getIsShift() == 1){
            holder.ivLock.setVisibility(ImageView.VISIBLE);
        }else{
            holder.ivLock.setVisibility(ImageView.GONE);
        }
        holder.tvTime.setText(CustomMethod.parseTime(orderEntity.getCloseTime(),"HH:mm"));
        holder.tvTableNumber.setText(DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId()));
        holder.tvEmployeeName.setText(DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId()));
        holder.tvSpend.setText(AmountUtils.multiply(orderEntity.getTotalMoney()+"","0.01"));
        holder.rootLayout.setTag(position);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                OrderEntity orderEntity1 = mOrderEntities.get(position);
                if(selectedOrderEntity != null && selectedOrderEntity.getOrderId().equals(orderEntity1.getOrderId())){
                    selectedOrderEntity = null;
                    position = -1;
                }else{
                    selectedOrderEntity = orderEntity1;
                }
                if(position >= 0) {
                    notifyItemChanged(position);
                }
                if(oldPosition >= 0){
                    notifyItemChanged(oldPosition);
                }
                oldPosition = position;
                mOnOrderItemClickListener.onOrderItemClick(selectedOrderEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime,tvTableNumber,tvEmployeeName,tvSpend;
        private LinearLayout rootLayout;
        private ImageView ivLock;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTableNumber = (TextView) itemView.findViewById(R.id.tv_table_number);
            tvEmployeeName = (TextView) itemView.findViewById(R.id.tv_employee_name);
            tvSpend = (TextView) itemView.findViewById(R.id.tv_spend);
            ivLock = (ImageView) itemView.findViewById(R.id.iv_lock);
        }
    }

    public void updateData(ArrayList<OrderEntity> orderEntities) {
        selectedOrderEntity = null;
        mOrderEntities.clear();
        mOrderEntities.addAll(orderEntities);
        notifyDataSetChanged();
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener listener) {
        this.mOnOrderItemClickListener = listener;
    }

    public interface OnOrderItemClickListener {
        void onOrderItemClick(OrderEntity orderEntity);
    }
}
