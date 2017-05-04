package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.RemindBean;
import jufeng.juyancash.dao.OrderDishEntity;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class RemindDialogAdapter extends RecyclerView.Adapter<RemindDialogAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> mOrderDishEntities;

    public RemindDialogAdapter(Context context, RemindBean remindBean){
        this.mContext = context;
        ArrayList<HashMap<String,Object>> datas = new ArrayList<>();
        datas.addAll(remindBean.getRemindDish());
        mOrderDishEntities = new ArrayList<>();
        for (HashMap<String, Object> map :
                datas) {
            OrderDishEntity orderDishEntity = DBHelper.getInstance(mContext).queryOneOrderDishEntity((String) map.get("orderDishId"));
            orderDishEntity.setDishCount((double) map.get("remindCount"));
            mOrderDishEntities.add(orderDishEntity);
        }
    }

    public ArrayList<OrderDishEntity> getOrderDishEntities(){
        return mOrderDishEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.remind_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            OrderDishEntity orderDishEntity = mOrderDishEntities.get(position);
            holder.tvDishCount.setText(String.valueOf(orderDishEntity.getDishCount()));
            holder.tvDishName.setText(orderDishEntity.getDishName());
        }catch (NullPointerException e){
            e.printStackTrace();
            holder.tvDishCount.setText("");
            holder.tvDishName.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mOrderDishEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDishName,tvDishCount;

        public ViewHolder(View view){
            super(view);
            tvDishName = (TextView)view.findViewById(R.id.tv_dish_name);
            tvDishCount = (TextView) view.findViewById(R.id.tv_dish_count);
        }
    }
}
