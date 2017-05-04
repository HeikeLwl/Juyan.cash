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
import jufeng.juyancash.dao.SellCheckEntity;

/**
 * Created by Administrator102 on 2016/8/5.
 */
public class StockLeftAdapter extends RecyclerView.Adapter<StockLeftAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SellCheckEntity> mSellCheckEntities;

    public StockLeftAdapter(Context context) {
        this.mContext = context.getApplicationContext();
        mSellCheckEntities = new ArrayList<>();
        initData();
    }

    private void initData() {
        mSellCheckEntities.clear();
        mSellCheckEntities.addAll(DBHelper.getInstance(mContext).getStockData());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stock_left_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(DBHelper.getInstance(mContext).getDishNameByDishId(mSellCheckEntities.get(position).getDishId()));
        holder.tvItem.setText(String.valueOf(position + 1));
        holder.tvStock.setText(String.valueOf(mSellCheckEntities.get(position).getStock()));
        if(mSellCheckEntities.get(position).getEarlyWarning() != null&&mSellCheckEntities.get(position).getStock() <= mSellCheckEntities.get(position).getEarlyWarning()){
            holder.tvStock.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            holder.tvStock.setTextColor(mContext.getResources().getColor(R.color.dark));
        }
    }

    @Override
    public int getItemCount() {
        return mSellCheckEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem, tvName,tvStock;

        public ViewHolder(View view) {
            super(view);
            tvItem = (TextView) view.findViewById(R.id.tv_item_position);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvStock = (TextView) view.findViewById(R.id.tv_stock);
        }
    }

    public void updateData() {
        initData();
    }
}
