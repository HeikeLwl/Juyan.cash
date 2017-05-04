package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class ScheduleHistoryAdapter extends RecyclerView.Adapter<ScheduleHistoryAdapter.ViewHolder> {
    private Context mContext;
    private int mStatus;
    private ArrayList<ScheduleEntity> mScheduleEntities;
    private OnScheduleHistoryClickListener mOnScheduleHistoryClickListener;
    private int selectedPosition;

    public ScheduleHistoryAdapter(Context context, int status) {
        this.mContext = context;
        this.mStatus = status;
        selectedPosition = -1;
        mScheduleEntities = new ArrayList<>();
        initData(mStatus);
    }

    private void initData(int type) {
        mScheduleEntities.clear();
        mScheduleEntities.addAll(DBHelper.getInstance(mContext).queryScheduleByStatus(type));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleEntity scheduleEntity = mScheduleEntities.get(position);

        if(mStatus > 1){
            holder.mLayout.setVisibility(CheckBox.GONE);
        }else{
            holder.mLayout.setVisibility(CheckBox.VISIBLE);
            if(position == selectedPosition){
                holder.mCheckBox.setChecked(true);
                holder.mLinearLayout.setBackgroundResource(R.drawable.grid_odd_background);
            }else{
                holder.mCheckBox.setChecked(false);
                holder.mLinearLayout.setBackgroundResource(R.drawable.grid_even_background);
            }
        }

        holder.tvItem.setText(String.valueOf(position + 1));
        holder.tvName.setText(scheduleEntity.getGuestName());
        holder.tvPhone.setText(scheduleEntity.getGuestPhone());
        if(scheduleEntity.getTableId() == null){
            holder.tvTable.setText("");
        }else{
            holder.tvTable.setText(DBHelper.getInstance(mContext).getTableNameByTableId(scheduleEntity.getTableId()));
        }
        holder.tvTime.setText(CustomMethod.parseTime(scheduleEntity.getMealTime(), "yyyy-MM-dd HH:mm"));
        holder.tvCount.setText(scheduleEntity.getMealPeople() + "人");
        holder.tvFrom.setText(scheduleEntity.getScheduleFrom() == 0 ? "电话" : "微信");
        holder.tvHasMark.setText((scheduleEntity.getScheduleMark() != null && !scheduleEntity.getScheduleMark().isEmpty())?"查看备注":"无备注");
        holder.tvHasMark.setTag(scheduleEntity);
        holder.tvHasMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleEntity scheduleEntity1 = (ScheduleEntity) v.getTag();
                if(scheduleEntity1 != null && scheduleEntity1.getScheduleMark() != null && !scheduleEntity1.getScheduleMark().isEmpty()){
                    mOnScheduleHistoryClickListener.onClickMark(scheduleEntity1.getScheduleMark());
                }else{
                    mOnScheduleHistoryClickListener.onClickMark("抱歉！没有备注信息！");
                }
            }
        });
        holder.mLinearLayout.setTag(position);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if(position == selectedPosition){
                    selectedPosition = -1;
                    mOnScheduleHistoryClickListener.onScheduleHistoryClick(null);
                }else {
                    selectedPosition = position;
                    mOnScheduleHistoryClickListener.onScheduleHistoryClick(mScheduleEntities.get(selectedPosition));
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.schedule_history_item_even, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mScheduleEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout,mLayout;
        private CheckBox mCheckBox;
        private TextView tvItem, tvName, tvPhone, tvTable, tvTime, tvCount,tvFrom,tvHasMark;

        public ViewHolder(View itemView) {
            super(itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.layout);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvTable = (TextView) itemView.findViewById(R.id.tv_table);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            tvFrom = (TextView) itemView.findViewById(R.id.tv_from);
            tvHasMark = (TextView) itemView.findViewById(R.id.tv_has_mark);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnScheduleHistoryClickListener(OnScheduleHistoryClickListener listener) {
        this.mOnScheduleHistoryClickListener = listener;
    }

    public interface OnScheduleHistoryClickListener {
        void onScheduleHistoryClick(ScheduleEntity scheduleEntity);
        void onClickMark(String markStr);
    }

    public void updateData(int type){
        initData(type);
        notifyDataSetChanged();
        selectedPosition = -1;
        mOnScheduleHistoryClickListener.onScheduleHistoryClick(null);
    }
}
