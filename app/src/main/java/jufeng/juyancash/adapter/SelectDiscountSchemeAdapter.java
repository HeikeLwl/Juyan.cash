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
import jufeng.juyancash.dao.DiscountEntity;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class SelectDiscountSchemeAdapter extends RecyclerView.Adapter<SelectDiscountSchemeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DiscountEntity> mDiscountEntities;
    private OnSelectDiscountSchemeItemClickListener mOnSelectDiscountSchemeItemClickListener;

    public SelectDiscountSchemeAdapter(Context context){
        this.mContext = context;
        mDiscountEntities = new ArrayList<>();
        mDiscountEntities.addAll(DBHelper.getInstance(mContext).getAllDiscountEntities(0));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_discount_reason_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDiscountEntities.get(position).getDiscountName());
        holder.tvName.setTag(mDiscountEntities.get(position).getDiscountId());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectDiscountSchemeItemClickListener.onSelectDiscountSchemeItemClick((String) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiscountEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_reason_name);
        }
    }

    public void setOnSelectDiscountReasonItemClickListener(OnSelectDiscountSchemeItemClickListener listener){
        this.mOnSelectDiscountSchemeItemClickListener = listener;
    }

    public interface OnSelectDiscountSchemeItemClickListener{
        void onSelectDiscountSchemeItemClick(String discountId);
    }
}
