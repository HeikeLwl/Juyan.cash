package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.bean.OrderCollectionBean;

/**
 * Created by Administrator102 on 2016/7/20.
 */
public class OrderColletionAdapter extends RecyclerView.Adapter<OrderColletionAdapter.ViewHolder> {
    private Context mContext;
//    private ArrayList<String> mCashierIds;
    private int selectedPosition;
    private OnOrderColletionItemClickListener mOnOrderColletionItemClickListener;
//    private int mShift;
//    private String mPayModeId;
//    private int mDate;
//    private int mCashier;
//    private String mType;
//    private String mPayModeName;
    private ArrayList<OrderCollectionBean> mOrderCollectionBeen;

    public OrderColletionAdapter(Context context,ArrayList<OrderCollectionBean> orderCollectionBeen) {
        this.mContext = context;
        mOrderCollectionBeen = new ArrayList<>();
        selectedPosition = -1;
        mOrderCollectionBeen.addAll(orderCollectionBeen);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_collection_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(selectedPosition != -1 && selectedPosition == position){
            holder.rootLayout.setBackgroundResource(R.drawable.order_selected_background);
        }else{
            holder.rootLayout.setBackgroundColor(0x00ffffff);
        }
        holder.tvName.setText(mOrderCollectionBeen.get(position).getCashierName());
        holder.tvSpend.setText(mOrderCollectionBeen.get(position).getSpendMoney());
        holder.rootLayout.setTag(position);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if(selectedPosition != -1 && selectedPosition == position){
                    selectedPosition = -1;
                }else{
                    selectedPosition = position;
                }
                notifyDataSetChanged();
                if(selectedPosition == 0){
                    mOnOrderColletionItemClickListener.onOrderCollectionClick();
                }else if(selectedPosition == 1){
                    mOnOrderColletionItemClickListener.onTypeCollectionClick();
                }else if(selectedPosition == -1){
                    mOnOrderColletionItemClickListener.onNothingClick();
                }else{
                    mOnOrderColletionItemClickListener.onOrderCashierClick(mOrderCollectionBeen.get(position).getCashierId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderCollectionBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvSpend;
        private LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvSpend = (TextView) itemView.findViewById(R.id.tv_spend);
        }
    }

    public void updateData(ArrayList<OrderCollectionBean> orderCollectionBeen) {
        selectedPosition = -1;
        mOrderCollectionBeen.clear();
        mOrderCollectionBeen.addAll(orderCollectionBeen);
        notifyDataSetChanged();
    }

    public void setOnOrderColletionItemClickListener(OnOrderColletionItemClickListener listener) {
        this.mOnOrderColletionItemClickListener = listener;
    }

    public interface OnOrderColletionItemClickListener {
        void onOrderCollectionClick();
        void onOrderCashierClick(String cashierId);
        void onTypeCollectionClick();
        void onNothingClick();
    }
}
