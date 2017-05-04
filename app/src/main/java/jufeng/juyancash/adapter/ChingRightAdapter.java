package jufeng.juyancash.adapter;

import android.content.Context;
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
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.SellCheckEntity;

/**
 * Created by Administrator102 on 2016/8/5.
 */
public class ChingRightAdapter extends RecyclerView.Adapter<ChingRightAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<DishEntity> mDishEntities;
    private ArrayList<SellCheckEntity> mSellCheckEntities;
    private OnChingRightItemClickListener mOnChingRightItemClickListener;

    public ChingRightAdapter(Context context, String matchStr, ArrayList<SellCheckEntity> sellCheckEntities){
        this.mContext = context;
        this.mSellCheckEntities = sellCheckEntities;
        mDishEntities = new ArrayList<>();
        initData(matchStr);
    }

    private void initData(String matchStr){
        new GetDataAsyncTask().execute(matchStr);
    }

    //判断菜品是否已售完
    private boolean isSellOut(String dishId){
        for ( int i = 0 ; i < mSellCheckEntities.size(); i++){
            if(dishId.equals(mSellCheckEntities.get(i).getDishId())){
                return true;
            }else{
                continue;
            }
        }
        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ching_right_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDishEntities.get(position).getDishName());
        holder.rootLayout.setTag(mDishEntities.get(position).getDishId());
        if(isSellOut(mDishEntities.get(position).getDishId())){
            //已售完
            holder.tvPrice.setVisibility(TextView.GONE);
            holder.tvSellOut.setVisibility(TextView.VISIBLE);
        }else{
            //未售完
            holder.tvPrice.setVisibility(TextView.VISIBLE);
            holder.tvSellOut.setVisibility(TextView.GONE);
            holder.tvPrice.setText("￥"+mDishEntities.get(position).getDishPrice());
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dishId = (String) v.getTag();
                    mOnChingRightItemClickListener.onChingRignthItemClick(dishId);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDishEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvPrice,tvSellOut;
        private LinearLayout rootLayout;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_dish_name);
            tvPrice = (TextView) view.findViewById(R.id.tv_dish_price);
            tvSellOut = (TextView) view.findViewById(R.id.tv_sell_out);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
        }
    }

    public void updateData(String matchStr,ArrayList<SellCheckEntity> sellCheckEntities){
        initData(matchStr);
        this.mSellCheckEntities = sellCheckEntities;
        notifyDataSetChanged();
    }

    public void setOnChingRightItemClickListener(OnChingRightItemClickListener listener){
        this.mOnChingRightItemClickListener = listener;
    }

    public interface OnChingRightItemClickListener{
        void onChingRignthItemClick(String dishId);
    }

    class GetDataAsyncTask extends AsyncTask<String,String,ArrayList<DishEntity>>{
        @Override
        protected void onPreExecute() {
            mDishEntities.clear();
            notifyDataSetChanged();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<DishEntity> doInBackground(String[] params) {
            if(params[0].isEmpty()){
                return new ArrayList<>();
            }else {
                return DBHelper.getInstance(mContext).searchDishByName(params[0]);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DishEntity> o) {
            mDishEntities.addAll(o);
            notifyDataSetChanged();
            super.onPostExecute(o);
        }
    }
}
