package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.StoreMessageEntity;

/**
 * Created by Administrator102 on 2016/8/18.
 */
public class StoreMessageAdapter extends RecyclerView.Adapter<StoreMessageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<StoreMessageEntity> mStoreMessageEntities;
    private Drawable mDrawable0,mDrawable1;
    private Map<String,Integer> allMessageMap;
    private OnStoreMessageItemClickListener mOnStoreMessageItemClickListener;

    public StoreMessageAdapter(Context context){
        this.mContext = context.getApplicationContext();
        mStoreMessageEntities = new ArrayList<>();
        mStoreMessageEntities.addAll(DBHelper.getInstance(mContext).getStoreMessages());
        mDrawable0 = context.getResources().getDrawable(R.drawable.read);
        mDrawable0.setBounds(0,0,mDrawable0.getMinimumWidth(),mDrawable0.getMinimumHeight());
        mDrawable1 = context.getResources().getDrawable(R.drawable.unread);
        mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
        allMessageMap = new HashMap<>();
        initMap();
    }

    private void initMap(){
        for (int i = 0; i < mStoreMessageEntities.size(); i++){
            allMessageMap.put(mStoreMessageEntities.get(i).getStoreMessageId(),i);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(mStoreMessageEntities.get(position).getStoreTitle());
        holder.tvDate.setText(mStoreMessageEntities.get(position).getStoreTime());
        holder.tvContent.setText(mStoreMessageEntities.get(position).getStoreContent());
        if (mStoreMessageEntities.get(position).getIsRead() == 0) {
            holder.ivNote.setImageDrawable(mDrawable1);
        } else {
            holder.ivNote.setImageDrawable(mDrawable0);
        }
        holder.rootLayout.setTag(mStoreMessageEntities.get(position));
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreMessageEntity storeMessageEntity = (StoreMessageEntity) v.getTag();
                if(storeMessageEntity.getIsRead() == 0){
                    DBHelper.getInstance(mContext).changeStoreReaded(storeMessageEntity);
                    if(allMessageMap.containsKey(storeMessageEntity.getStoreMessageId())){
                        int position = allMessageMap.get(storeMessageEntity.getStoreMessageId());
                        notifyItemChanged(position);
                        mOnStoreMessageItemClickListener.onStoreMessageItemClick();
                    }
                }
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
        return mStoreMessageEntities.size();
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

    public void addItem(StoreMessageEntity storeMessageEntity){
        mStoreMessageEntities.add(0,storeMessageEntity);
        initMap();
        notifyItemInserted(0);
    }

    public void updateData(){
        mStoreMessageEntities.clear();
        mStoreMessageEntities.addAll(DBHelper.getInstance(mContext).getStoreMessages());
        initMap();
        notifyDataSetChanged();
    }

    public void setStoreMessageItemClickListener(OnStoreMessageItemClickListener listener){
        this.mOnStoreMessageItemClickListener = listener;
    }

    public interface OnStoreMessageItemClickListener{
        void onStoreMessageItemClick();
    }
}
