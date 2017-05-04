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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.RemindBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.dao.WXMessageEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/18.
 */
public class WXMessageAdapter extends RecyclerView.Adapter<WXMessageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WXMessageEntity> mWXMessageEntities;
    private OnWXMessageItemClickListener mOnWXMessageItemClickListener;
    private Drawable mDrawable0,mDrawable1;
    private Map<String,Integer> allMessageMap;

    public WXMessageAdapter(Context context){
        this.mContext = context.getApplicationContext();
        mWXMessageEntities = new ArrayList<>();
        mWXMessageEntities.addAll(DBHelper.getInstance(mContext).getWXMessages());
        mDrawable0 = context.getResources().getDrawable(R.drawable.read);
        mDrawable0.setBounds(0,0,mDrawable0.getMinimumWidth(),mDrawable0.getMinimumHeight());
        mDrawable1 = context.getResources().getDrawable(R.drawable.unread);
        mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
        allMessageMap = new HashMap<>();
        initMap();
    }

    private void initMap(){
        allMessageMap.clear();
        for (int i = 0; i < mWXMessageEntities.size(); i++){
            allMessageMap.put(mWXMessageEntities.get(i).getWxMessageId(),i);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.tvTitle.setText(mWXMessageEntities.get(position).getWxTitle());
            holder.tvDate.setText(mWXMessageEntities.get(position).getWxTime());
            if(mWXMessageEntities.get(position).getWxType() == 0) {
                RemindBean remindBean = JSON.parseObject(mWXMessageEntities.get(position).getWxContent(), RemindBean.class);
                OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(remindBean.getOrderId());
                TableEntity tableEntity = DBHelper.getInstance(mContext).queryOneTableData(orderEntity.getTableId());
                if(tableEntity == null){
                    holder.tvContent.setText("桌位：无"+"\n单号：NO." + orderEntity.getOrderNumber1());
                }else{
                    holder.tvContent.setText("桌位："+tableEntity.getTableName()+ "(" + tableEntity.getTableCode() + ")\n单号：NO." + orderEntity.getOrderNumber1());
                }
            }else{
                holder.tvContent.setText(mWXMessageEntities.get(position).getWxContent());
            }
            if (mWXMessageEntities.get(position).getIsRead() == 0) {
                holder.ivNote.setImageDrawable(mDrawable1);
            } else {
                holder.ivNote.setImageDrawable(mDrawable0);
            }
            holder.rootLayout.setTag(mWXMessageEntities.get(position));
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                WXMessageEntity wxMessageEntity = (WXMessageEntity) v.getTag();
                mOnWXMessageItemClickListener.onWxMessageItemClick(wxMessageEntity);
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            holder.tvTitle.setText("微信催菜");
            holder.tvDate.setText(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"));
            holder.tvContent.setText("");
            holder.ivNote.setVisibility(ImageView.GONE);
        }catch (JSONException e){
            e.printStackTrace();
            holder.tvTitle.setText("微信催菜");
            holder.tvDate.setText(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"));
            holder.tvContent.setText("");
            holder.ivNote.setVisibility(ImageView.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wx_message_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mWXMessageEntities.size();
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

    public void addItem(WXMessageEntity wxMessageEntity){
        mWXMessageEntities.add(0,wxMessageEntity);
        initMap();
        notifyItemInserted(0);
    }

    public void changeItem(WXMessageEntity wxMessageEntity){
        if(allMessageMap.containsKey(wxMessageEntity.getWxMessageId())){
            int position = allMessageMap.get(wxMessageEntity.getWxMessageId());
            mWXMessageEntities.remove(position);
            mWXMessageEntities.add(position,wxMessageEntity);
            notifyItemChanged(position);
        }
    }

    public void updateData(){
        mWXMessageEntities.clear();
        mWXMessageEntities.addAll(DBHelper.getInstance(mContext).getWXMessages());
        initMap();
        notifyDataSetChanged();
    }

    public void setOnWXMessageItemClickListener(OnWXMessageItemClickListener listener){
        this.mOnWXMessageItemClickListener = listener;
    }

    public interface OnWXMessageItemClickListener{
        void onWxMessageItemClick(WXMessageEntity wxMessageEntity);
    }
}
