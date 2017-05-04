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
import jufeng.juyancash.dao.SpecialEntity;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class SelectDiscountReasonAdapter extends RecyclerView.Adapter<SelectDiscountReasonAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SpecialEntity> mSpecialEntities;
    private OnSelectDiscountReasonItemClickListener mOnSelectDiscountReasonItemClickListener;

    public SelectDiscountReasonAdapter(Context context){
        this.mContext = context;
        mSpecialEntities = new ArrayList<>();
        mSpecialEntities.addAll(DBHelper.getInstance(mContext).queryAllSpecialByType(1));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_discount_reason_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mSpecialEntities.get(position).getSpecialName());
        holder.tvName.setTag(mSpecialEntities.get(position).getSpecialName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectDiscountReasonItemClickListener.onSelectDiscountReasonItemClick((String) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSpecialEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_reason_name);
        }
    }

    public void setOnSelectDiscountReasonItemClickListener(OnSelectDiscountReasonItemClickListener listener){
        this.mOnSelectDiscountReasonItemClickListener = listener;
    }

    public interface OnSelectDiscountReasonItemClickListener{
        void onSelectDiscountReasonItemClick(String reasonName);
    }
}
