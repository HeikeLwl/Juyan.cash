package jufeng.juyancash.adapter;

import android.content.Context;
import android.os.AsyncTask;
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
import jufeng.juyancash.dao.ArrangeEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class ArrangeSortAdapter extends RecyclerView.Adapter<ArrangeSortAdapter.ViewHolder> {
    private Context mContext;
    private int mStatus;
    private ArrayList<ArrangeEntity> mArrangeEntities;
    private OnArrangeEntityClickListener mOnArrangeEntityClickListener;
    private ArrangeEntity mArrangeEntity;

    public ArrangeSortAdapter(Context context,int status) {
        this.mContext = context;
        mStatus = status;
        mArrangeEntity = null;
        mArrangeEntities = new ArrayList<>();
        initData(mStatus);
    }

    private void initData(int status) {
        new GetDataAsyncTask().execute(status);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrangeEntity arrangeEntity = mArrangeEntities.get(position);
        if(mStatus == 1){//排队完成
            holder.layout.setVisibility(CheckBox.GONE);
            holder.tvTimes.setVisibility(TextView.GONE);
        }else{//未完成的排队
            holder.layout.setVisibility(CheckBox.VISIBLE);
            holder.tvTimes.setVisibility(TextView.VISIBLE);
            holder.mCheckBox.setChecked(mArrangeEntity != null && mArrangeEntity.getArrangeId().equals(arrangeEntity.getArrangeId()));
            holder.tvTimes.setText(String.valueOf(arrangeEntity.getRemainCount()));
        }
        if(holder.mCheckBox.isChecked()){
            holder.mLinearLayout.setBackgroundResource(R.drawable.grid_odd_background);
        }else{
            holder.mLinearLayout.setBackgroundResource(R.drawable.grid_even_background);
        }
        holder.tvTel.setText(arrangeEntity.getTel() != null ? arrangeEntity.getTel():"无");
        holder.tvNumber.setText(arrangeEntity.getArrangeNumber());
        holder.tvTime.setText(CustomMethod.parseTime(arrangeEntity.getSignTime(), "yyyy-MM-dd HH:mm"));
        holder.tvCount.setText(arrangeEntity.getMealPeople() + "人");
        holder.mLinearLayout.setTag(arrangeEntity);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrangeEntity arrangeEntity= (ArrangeEntity) v.getTag();
                if(mArrangeEntity != null && arrangeEntity.getArrangeId().equals(mArrangeEntity.getArrangeId())){
                    mArrangeEntity = null;
                }else {
                    mArrangeEntity = arrangeEntity;
                }
                mOnArrangeEntityClickListener.onArrangeEntityClick(mArrangeEntity);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.arrange_history_item_even, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mArrangeEntities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;
        private CheckBox mCheckBox;
        private LinearLayout layout;
        private TextView tvNumber, tvTime, tvCount, tvTimes,tvTel;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvTel = (TextView) itemView.findViewById(R.id.tv_user_tel);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            tvTimes = (TextView) itemView.findViewById(R.id.tv_times);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnArrangeEntityClickListener(OnArrangeEntityClickListener listener) {
        this.mOnArrangeEntityClickListener = listener;
    }

    public interface OnArrangeEntityClickListener {
        void onArrangeEntityClick(ArrangeEntity arrangeEntity);
    }

    public void updateData(int status,ArrangeEntity arrangeEntity){
        initData(status);
        mArrangeEntity = arrangeEntity;
        mOnArrangeEntityClickListener.onArrangeEntityClick(mArrangeEntity);
    }

    class GetDataAsyncTask extends AsyncTask<Integer,String,ArrayList<ArrangeEntity>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ArrangeEntity> doInBackground(Integer... params) {
            return DBHelper.getInstance(mContext).getSomeArrange(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<ArrangeEntity> arrangeEntities) {
            mArrangeEntities.clear();
            mArrangeEntities.addAll(arrangeEntities);
            notifyDataSetChanged();
            super.onPostExecute(arrangeEntities);
        }
    }
}
