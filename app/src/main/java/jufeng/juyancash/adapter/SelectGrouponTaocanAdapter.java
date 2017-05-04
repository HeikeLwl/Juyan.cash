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
import jufeng.juyancash.dao.GrouponTaocanEntity;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class SelectGrouponTaocanAdapter extends RecyclerView.Adapter<SelectGrouponTaocanAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<GrouponTaocanEntity> mGrouponTaocanes;
    private OnSelectGrouponTaocanItemClickListener mOnSelectGrouponTaocanItemClickListener;

    public SelectGrouponTaocanAdapter(Context context,String grouponId){
        this.mContext = context;
        mGrouponTaocanes = new ArrayList<>();
        mGrouponTaocanes.addAll(DBHelper.getInstance(mContext).queryGrouponTaocanById(grouponId));
    }

    public void updateData(String grouponId){
        mGrouponTaocanes.clear();
        mGrouponTaocanes.addAll(DBHelper.getInstance(mContext).queryGrouponTaocanById(grouponId));

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_discount_reason_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mGrouponTaocanes.get(position).getGrouponTaocanName());
        holder.tvName.setTag(mGrouponTaocanes.get(position));
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectGrouponTaocanItemClickListener.onSelectGrouponTaocanItemClick((GrouponTaocanEntity) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGrouponTaocanes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_reason_name);
        }
    }

    public void setOnSelectGrouponTaocanItemClickListener(OnSelectGrouponTaocanItemClickListener listener){
        this.mOnSelectGrouponTaocanItemClickListener = listener;
    }

    public interface OnSelectGrouponTaocanItemClickListener{
        void onSelectGrouponTaocanItemClick(GrouponTaocanEntity grouponTaocanEntity);
    }
}
