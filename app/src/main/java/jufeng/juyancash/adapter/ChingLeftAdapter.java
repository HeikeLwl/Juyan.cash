package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.SellCheckEntity;

/**
 * Created by Administrator102 on 2016/8/5.
 */
public class ChingLeftAdapter extends RecyclerView.Adapter<ChingLeftAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SellCheckEntity> mSellCheckEntities;
    private OnChingLeftItemClickListener mOnChingLeftItemClickListener;

    public ChingLeftAdapter(Context context) {
        this.mContext = context;
        mSellCheckEntities = new ArrayList<>();
        initData();
    }

    private void initData() {
        mSellCheckEntities.clear();
        mSellCheckEntities.addAll(DBHelper.getInstance(mContext).getAllSellOut());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ching_left_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(DBHelper.getInstance(mContext).getDishNameByDishId(mSellCheckEntities.get(position).getDishId()));
        holder.tvItem.setText(String.valueOf(position + 1));
        holder.btnStock.setTag(mSellCheckEntities.get(position));
        holder.btnStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellCheckEntity sellCheckEntity = (SellCheckEntity) v.getTag();
                mOnChingLeftItemClickListener.onChingLeftItemClick(sellCheckEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSellCheckEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem, tvName;
        //        private Button btnStock;
        private ImageButton btnStock;
        private LinearLayout rootLayout;

        public ViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tvItem = (TextView) view.findViewById(R.id.tv_item_position);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            btnStock = (ImageButton) view.findViewById(R.id.btn_stock);
        }
    }

    public void updateData() {
        initData();
    }

    public void setOnChingLeftItemClickListener(OnChingLeftItemClickListener listener) {
        this.mOnChingLeftItemClickListener = listener;
    }

    public interface OnChingLeftItemClickListener {
        void onChingLeftItemClick(SellCheckEntity sellCheckEntity);
    }
}
