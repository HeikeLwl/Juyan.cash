package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.Payment;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class SelectPaymentAdapter extends RecyclerView.Adapter<SelectPaymentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Payment> mPayments;
    private OnPaymentClickListener mOnPaymentClickListener;

    public SelectPaymentAdapter(Context context,int type){
        this.mContext = context;
        mPayments = DBHelper.getInstance(mContext).queryPaymentByType(type);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_payment_item_layout, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mPayments.get(position).getName());
        holder.rootLayout.setTag(mPayments.get(position));
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payment payment = (Payment) v.getTag();
                mOnPaymentClickListener.onPaymentItemClick(payment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPayments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private LinearLayout rootLayout;

        public ViewHolder(View view){
            super(view);
            tvName = (TextView)view.findViewById(R.id.tv_payment_name);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnPaymentClickListener(OnPaymentClickListener listener){
        this.mOnPaymentClickListener = listener;
    }

    public interface OnPaymentClickListener{
        void onPaymentItemClick(Payment payment);
    }
}
