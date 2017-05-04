package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.PayModeEntity;

/**
 * Created by Administrator102 on 2016/7/27.
 */
public class CashierHistoryAdapter extends RecyclerView.Adapter<CashierHistoryAdapter.ViewHolder> {
    private Context mContext;
    private String mOrderId;
    private ArrayList<PayModeEntity> mPayModeEntities;
    private OnCashierHistoryClickListener mOnCashierHistoryClickListener;

    public CashierHistoryAdapter(Context context, String orderId, boolean isOpenJoinOrder) {
        this.mContext = context;
        this.mOrderId = orderId;
        mPayModeEntities = new ArrayList<>();
        initData(mOrderId, isOpenJoinOrder);
    }

    private void initData(String orderId, boolean isOpenJoinOrder) {
        mPayModeEntities.clear();
        mPayModeEntities.addAll(DBHelper.getInstance(mContext).getAllPayModeByOrderId(orderId));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final PayModeEntity payModeEntity = mPayModeEntities.get(position);
            holder.tvName.setText(payModeEntity.getPaymentName());
            if (payModeEntity.getIsJoinOrderPay() != null && payModeEntity.getIsJoinOrderPay() == 1){
                holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.reddark));
                holder.tvMoney.setText("支付结果未知，点击处理");
            }else{
                holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.dark));
                holder.tvMoney.setText(String.valueOf(mPayModeEntities.get(position).getPayMoney()));
            }
            holder.tvMoney.setTag(payModeEntity);
            holder.tvMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PayModeEntity payModeEntity1 = (PayModeEntity) v.getTag();
                    if(payModeEntity1.getIsJoinOrderPay() != null && payModeEntity1.getIsJoinOrderPay() == 1){
                        if(mOnCashierHistoryClickListener != null){
                            mOnCashierHistoryClickListener.onCashierHistoryClick(payModeEntity1);
                        }
                    }
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cashier_history_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mPayModeEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvMoney;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvMoney = (TextView) view.findViewById(R.id.tv_money);
        }
    }

    public void updateData(String orderId, boolean isOpenJoinOrder) {
        initData(orderId, isOpenJoinOrder);
    }

    public void setOnCashierHistoryClickListener(OnCashierHistoryClickListener listener){
        this.mOnCashierHistoryClickListener = listener;
    }

    public interface OnCashierHistoryClickListener{
        void onCashierHistoryClick(PayModeEntity payModeEntity);
    }
}
