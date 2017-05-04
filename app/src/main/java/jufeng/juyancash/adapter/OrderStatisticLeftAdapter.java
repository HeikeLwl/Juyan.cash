package jufeng.juyancash.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by Administrator102 on 2016/8/9.
 */
public class OrderStatisticLeftAdapter extends RecyclerView.Adapter<OrderStatisticLeftAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DishModel> mDishModels;
    private int mShift;
    private int mDate;
    private DishTypeModel mDishTypeModel;

    public OrderStatisticLeftAdapter(Context context, DishTypeModel dishTypeModel, int shift, int date) {
        this.mContext = context;
        this.mDishTypeModel = dishTypeModel;
        this.mDishModels = new ArrayList<>();
        if(mDishTypeModel == null){

        }else {
            new GetDataAsyncTask().execute(shift, date);
        }
    }

    class GetDataAsyncTask extends AsyncTask<Integer,Integer,ArrayList<DishModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<DishModel> doInBackground(Integer... params) {
            return DBHelper.getInstance(mContext).getSingleStatisticDish(mContext, mDishTypeModel, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(ArrayList<DishModel> dishTypeModels) {
            mDishModels.clear();
            mDishModels.addAll(dishTypeModels);
            notifyDataSetChanged();
            super.onPostExecute(dishTypeModels);
        }
    }

    public ArrayList<DishModel> getDishModels(){
        return mDishModels;
    }

    public DishTypeModel getDishTypeModel(){
        return mDishTypeModel;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvItem.setText(String.valueOf(position + 1));
        holder.tvName.setText(mDishModels.get(position).getDishModelName());
        holder.tvCount.setText(AmountUtils.multiply(""+mDishModels.get(position).getCount(),"1"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_statistic_left_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mDishModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem, tvName, tvCount;

        public ViewHolder(View view) {
            super(view);
            tvItem = (TextView) view.findViewById(R.id.tv_item);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
        }
    }

    public void updateData(DishTypeModel dishTypeModel, int shift, int date) {
        this.mShift = shift;
        this.mDate = date;
        this.mDishTypeModel = dishTypeModel;
        if(mDishTypeModel == null){
            mDishModels.clear();
            notifyDataSetChanged();
        }else {
            new GetDataAsyncTask().execute(shift, date);
        }
    }
}
