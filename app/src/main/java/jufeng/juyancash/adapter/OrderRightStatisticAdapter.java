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
import jufeng.juyancash.bean.DishTypeModel;

/**
 * Created by Administrator102 on 2016/8/12.
 */
public class OrderRightStatisticAdapter extends RecyclerView.Adapter<OrderRightStatisticAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DishTypeModel> mDishTypeModels;
    private OnOrderRightStatisticItemClickListener mOnOrderRightStatisticItemClickListener;
    private DishTypeModel selectedDishTypeModel;

    public OrderRightStatisticAdapter(Context context) {
        this.mContext = context;
        mDishTypeModels = new ArrayList<>();
        initData();
    }

    private void initData() {
        new GetDataAsyncTask().execute();
    }

    class GetDataAsyncTask extends AsyncTask<String,Integer,ArrayList<DishTypeModel>>{

        @Override
        protected void onPreExecute() {
            selectedDishTypeModel = null;
            super.onPreExecute();
        }

        @Override
        protected ArrayList<DishTypeModel> doInBackground(String... params) {
            return DBHelper.getInstance(mContext).queryDishTypeModelData();
        }

        @Override
        protected void onPostExecute(ArrayList<DishTypeModel> dishTypeModels) {
            mDishTypeModels.clear();
            mDishTypeModels.addAll(dishTypeModels);
            notifyDataSetChanged();
            super.onPostExecute(dishTypeModels);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_right_statistic_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            if (selectedDishTypeModel.getDishTypeModelId().equals(mDishTypeModels.get(position).getDishTypeModelId())) {
                holder.rootLayout.setBackgroundResource(R.drawable.order_selected_background);
            } else {
                holder.rootLayout.setBackgroundColor(0x00ffffff);
            }
        }catch (Exception e){
            holder.rootLayout.setBackgroundColor(0x00ffffff);
        }
        holder.tvName.setText(mDishTypeModels.get(position).getDishTypeModelTypeName());
        holder.rootLayout.setTag(mDishTypeModels.get(position));
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DishTypeModel dishTypeModel = (DishTypeModel) v.getTag();
                try {
                    if (selectedDishTypeModel != null && selectedDishTypeModel.getDishTypeModelId().equals(dishTypeModel.getDishTypeModelId())) {
                        selectedDishTypeModel = null;
                    } else {
                        selectedDishTypeModel = dishTypeModel;
                    }
                }catch (Exception e){
                    selectedDishTypeModel = dishTypeModel;
                }
                mOnOrderRightStatisticItemClickListener.onOrderRightStatisticItemClick(selectedDishTypeModel);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDishTypeModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        LinearLayout rootLayout;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
        }
    }

    public void updateData(ArrayList<DishTypeModel> dishTypeModels){
        selectedDishTypeModel = null;
        mDishTypeModels.clear();
        mDishTypeModels.addAll(dishTypeModels);
        notifyDataSetChanged();
    }

    public void updateData(){
        initData();
    }

    public void setOnOrderRightStatisticItemClickListener(OnOrderRightStatisticItemClickListener listener) {
        this.mOnOrderRightStatisticItemClickListener = listener;
    }

    public interface OnOrderRightStatisticItemClickListener {
        void onOrderRightStatisticItemClick(DishTypeModel dishTypeModel);
    }
}
