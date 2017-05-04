package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.TurnoverHistoryEntity;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class DialogTurnoverHistoryAdapter extends RecyclerView.Adapter<DialogTurnoverHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<TurnoverHistoryEntity> mTurnoverHistoryEntities;
    private OnDialogTurnoverHistoryClickListener mOnDialogScheduleHistoryClickListener;

    public DialogTurnoverHistoryAdapter(Context context) {
        this.mContext = context;
        mTurnoverHistoryEntities = new ArrayList<>();
        initData();
    }

    private void initData() {
        mTurnoverHistoryEntities.clear();
        mTurnoverHistoryEntities.addAll(DBHelper.getInstance(mContext).getTurnoverHistory());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TurnoverHistoryEntity turnoverHistoryEntity = mTurnoverHistoryEntities.get(position);
        if(turnoverHistoryEntity != null){
            Log.d("###", "onBindViewHolder: "+turnoverHistoryEntity.toString());
            holder.tvCashierName.setText(turnoverHistoryEntity.getCashierName());
            holder.tvAreaType.setText(turnoverHistoryEntity.getAreaType());
            holder.tvTurnoverStartTime.setText(turnoverHistoryEntity.getTurnoverStartTime());
            holder.tvTurnoverEndTime.setText(turnoverHistoryEntity.getTurnoverEndTime());
            holder.tvTurnoverTime.setText(turnoverHistoryEntity.getStartTurnoverTime());
            holder.tvPrintAgain.setTag(turnoverHistoryEntity);
            holder.tvPrintAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnDialogScheduleHistoryClickListener != null){
                        mOnDialogScheduleHistoryClickListener.onDialogTurnoverHistoryClick((TurnoverHistoryEntity) view.getTag());
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.turnover_history_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mTurnoverHistoryEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCashierName, tvTurnoverTime, tvTurnoverStartTime,tvTurnoverEndTime ,tvAreaType,tvPrintAgain;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCashierName = (TextView) itemView.findViewById(R.id.tv_cashier_name);
            tvTurnoverTime = (TextView) itemView.findViewById(R.id.tv_turnover_time);
            tvTurnoverStartTime = (TextView) itemView.findViewById(R.id.tv_turnover_start_time);
            tvTurnoverEndTime = (TextView) itemView.findViewById(R.id.tv_turnover_end_time);
            tvAreaType = (TextView) itemView.findViewById(R.id.tv_area_type);
            tvPrintAgain = (TextView) itemView.findViewById(R.id.tv_print_again);
        }
    }

    public void setOnDialogScheduleHistoryClickListener(OnDialogTurnoverHistoryClickListener listener) {
        this.mOnDialogScheduleHistoryClickListener = listener;
    }

    public interface OnDialogTurnoverHistoryClickListener {
        void onDialogTurnoverHistoryClick(TurnoverHistoryEntity turnoverHistoryEntity);
    }
}
