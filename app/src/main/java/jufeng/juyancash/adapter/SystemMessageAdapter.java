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
import jufeng.juyancash.dao.SystemMessageEntity;

/**
 * Created by Administrator102 on 2016/8/18.
 */
public class SystemMessageAdapter extends RecyclerView.Adapter<SystemMessageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SystemMessageEntity> mSystemMessageEntities;

    public SystemMessageAdapter(Context context){
        this.mContext = context.getApplicationContext();
        mSystemMessageEntities = new ArrayList<>();
        mSystemMessageEntities.addAll(DBHelper.getInstance(mContext).getSystemMessages());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(mSystemMessageEntities.get(position).getSystemTitle());
        holder.tvDate.setText(mSystemMessageEntities.get(position).getSystemTime());
        holder.tvContent.setText(mSystemMessageEntities.get(position).getSystemContent());
        if(mSystemMessageEntities.get(position).getIsRead() == 0){
            holder.ivNote.setVisibility(ImageView.VISIBLE);
        }else{
            holder.ivNote.setVisibility(ImageView.GONE);
        }
        holder.rootLayout.setTag(mSystemMessageEntities.get(position));
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemMessageEntity systemMessageEntity = (SystemMessageEntity) v.getTag();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mSystemMessageEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvDate,tvContent;
        private ImageView ivNote;
        private LinearLayout rootLayout;

        public ViewHolder(View view){
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            ivNote = (ImageView) view.findViewById(R.id.iv_unread);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
        }
    }

    public void updateData(){
        mSystemMessageEntities.clear();
        mSystemMessageEntities.addAll(DBHelper.getInstance(mContext).getSystemMessages());
        notifyDataSetChanged();
    }
}
