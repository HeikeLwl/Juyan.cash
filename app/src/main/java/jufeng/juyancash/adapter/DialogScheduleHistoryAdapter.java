package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class DialogScheduleHistoryAdapter extends RecyclerView.Adapter<DialogScheduleHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ScheduleEntity> mScheduleEntities;
    private OnDialogScheduleHistoryClickListener mOnDialogScheduleHistoryClickListener;
    private int selectedPosition;

    public DialogScheduleHistoryAdapter(Context context,String tableId) {
        this.mContext = context;
        selectedPosition = -1;
        mScheduleEntities = new ArrayList<>();
        initData(tableId);
    }

    private void initData(String tableId) {
        mScheduleEntities.clear();
        mScheduleEntities.addAll(DBHelper.getInstance(mContext).queryScheduleByTable(tableId));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleEntity scheduleEntity = mScheduleEntities.get(position);
        if(position == selectedPosition){
            holder.mLinearLayout.setBackgroundResource(R.drawable.grid_odd_background);
        }else{
            holder.mLinearLayout.setBackgroundResource(R.drawable.grid_even_background);
        }
        holder.tvName.setText(scheduleEntity.getGuestName());
        holder.tvPhone.setText(scheduleEntity.getGuestPhone());
        if(scheduleEntity.getTableId() == null){
            holder.tvTable.setText("");
        }else{
            holder.tvTable.setText(DBHelper.getInstance(mContext).getTableNameByTableId(scheduleEntity.getTableId()));
        }
        holder.tvTime.setText(CustomMethod.parseTime(scheduleEntity.getMealTime(), "yyyy-MM-dd HH:mm"));
        holder.tvCount.setText(scheduleEntity.getMealPeople() + "人");
        holder.mLinearLayout.setTag(position);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if(position == selectedPosition){
                    selectedPosition = -1;
                    mOnDialogScheduleHistoryClickListener.onDialogScheduleHistoryClick(null);
                }else {
                    selectedPosition = position;
                    mOnDialogScheduleHistoryClickListener.onDialogScheduleHistoryClick(mScheduleEntities.get(selectedPosition));
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.schedule_history_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mScheduleEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;
        private TextView tvName, tvPhone, tvTable, tvTime, tvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_schedule_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_schedule_phone);
            tvTable = (TextView) itemView.findViewById(R.id.tv_schedule_table);
            tvTime = (TextView) itemView.findViewById(R.id.tv_schedule_time);
            tvCount = (TextView) itemView.findViewById(R.id.tv_schedule_guests);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnDialogScheduleHistoryClickListener(OnDialogScheduleHistoryClickListener listener) {
        this.mOnDialogScheduleHistoryClickListener = listener;
    }

    public interface OnDialogScheduleHistoryClickListener {
        void onDialogScheduleHistoryClick(ScheduleEntity scheduleEntity);
    }
}
