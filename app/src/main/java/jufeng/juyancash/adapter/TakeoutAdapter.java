package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/7/20.
 */
public class TakeoutAdapter extends RecyclerView.Adapter<TakeoutAdapter.ViewHolder> {
    private Context mContext;
    private int mType;
    private ArrayList<TakeOutOrderEntity> mTakeOutOrderEntities;
    private OnTakeOutItemClickListener mOnTakeOutItemClickListener;
    private TakeOutOrderEntity selectedTakeOutOrder;
    private Drawable mDrawable,mDrawable1,mDrawable2;

    public TakeoutAdapter(Context context, int status) {
        this.mContext = context.getApplicationContext();
        this.mType = status;
        selectedTakeOutOrder = null;
        mTakeOutOrderEntities = new ArrayList<>();
        initData(status);
        mDrawable = mContext.getResources().getDrawable(R.drawable.schedule1);
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        mDrawable1 = mContext.getResources().getDrawable(R.drawable.wechat_takeout);
        mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
        mDrawable2 = mContext.getResources().getDrawable(R.drawable.meituan_takeount);
        mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
    }

    private void initData(int status) {
        new GetDataAsyncTask().execute(status);
    }

    class GetDataAsyncTask extends AsyncTask<Integer,Integer,ArrayList<TakeOutOrderEntity>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<TakeOutOrderEntity> doInBackground(Integer... params) {
            return DBHelper.getInstance(mContext).queryTakeoutByStatus(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<TakeOutOrderEntity> takeOutOrderEntities) {
            mTakeOutOrderEntities.clear();
            mTakeOutOrderEntities.addAll(takeOutOrderEntities);
            notifyDataSetChanged();
            super.onPostExecute(takeOutOrderEntities);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.takeout_item_0, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TakeOutOrderEntity takeOutOrderEntity = mTakeOutOrderEntities.get(position);
        if (selectedTakeOutOrder != null && selectedTakeOutOrder.getTakeoutId().equals(takeOutOrderEntity.getTakeoutId())) {
            holder.rootLayout.setBackgroundResource(R.drawable.takeout_selected_background);
        } else {
            holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(takeOutOrderEntity.getOrderId());
        if(orderEntity != null) {
            holder.tvNumber.setText("NO." + String.valueOf(orderEntity.getOrderNumber1()));
        }else{
            holder.tvNumber.setText("未知");
        }
        if(takeOutOrderEntity.getTakeoutFrom() == 0){
            holder.tvNumber.setCompoundDrawables(mDrawable1,null,null,null);
        }else if(takeOutOrderEntity.getTakeoutFrom() == 1){
            holder.tvNumber.setCompoundDrawables(mDrawable2,null,null,null);
        }else{
            holder.tvNumber.setCompoundDrawables(null,null,null,null);
        }

        if(CustomMethod.isTomorrow(takeOutOrderEntity.getTakeoutTime())){
            holder.tvTime.setCompoundDrawables(mDrawable,null,null,null);
        }else{
            holder.tvTime.setCompoundDrawables(null,null,null,null);
        }
        holder.tvTime.setText(CustomMethod.parseTime(takeOutOrderEntity.getTakeoutTime(),"yyyy-MM-dd HH:mm"));
        holder.tvPhone.setText(takeOutOrderEntity.getGuestPhone());
        holder.tvAddress.setText(takeOutOrderEntity.getTakeoutAddress());
        holder.rootLayout.setTag(takeOutOrderEntity);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeOutOrderEntity takeOutOrderEntity = (TakeOutOrderEntity) v.getTag();
                if (selectedTakeOutOrder != null && selectedTakeOutOrder.getTakeoutId().equals(takeOutOrderEntity.getTakeoutId())) {
                    selectedTakeOutOrder = null;
                } else {
                    selectedTakeOutOrder = takeOutOrderEntity;
                }
                mOnTakeOutItemClickListener.onTakeOutItemClick(selectedTakeOutOrder, mType);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTakeOutOrderEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPhone, tvNumber, tvAddress,tvTime;
        private LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public TakeOutOrderEntity getSelectedTakeOutOrder(){
        return selectedTakeOutOrder;
    }

    public void updateData(int status) {
        selectedTakeOutOrder = null;
        this.mType = status;
        initData(status);
    }

    public void updateData1(int status) {
        this.mType = status;
        initData(status);
    }

    public void setOnTakeOutItemClickListener(OnTakeOutItemClickListener listener) {
        this.mOnTakeOutItemClickListener = listener;
    }

    public interface OnTakeOutItemClickListener {
        void onTakeOutItemClick(TakeOutOrderEntity takeOutOrderEntity, int type);
    }
}
