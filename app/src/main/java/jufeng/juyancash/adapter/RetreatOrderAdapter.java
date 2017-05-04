package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class RetreatOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> datas;

    public RetreatOrderAdapter(Context context, ArrayList<OrderDishEntity> orderDishEntities) {
        this.mContext = context;
        datas = new ArrayList<>();
        datas.addAll(orderDishEntities);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retreat_order_item_layout, parent, false);
        ViewHolder0 holder0 = new ViewHolder0(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {
            ((ViewHolder0) holder).tvName.setText(datas.get(position).getDishName());

            String extras = "";
            //已经下单
            if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() != null) {//有做法和规格
                extras = "(" + datas.get(position).getDishPractice() + "," + datas.get(position).getDishSpecify() + ")";
            } else if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() == null) {//有做法
                extras = "(" + datas.get(position).getDishPractice() + ")";
            } else if (datas.get(position).getPracticeId() == null && datas.get(position).getSpecifyId() != null) {//有规格
                extras = "(" + datas.get(position).getDishSpecify() + ")";
            }
            if (extras.isEmpty()) {
                ((ViewHolder0) holder).tvExtras.setVisibility(TextView.GONE);
            } else {
                ((ViewHolder0) holder).tvExtras.setVisibility(TextView.VISIBLE);
                ((ViewHolder0) holder).tvExtras.setText(extras);
            }

            ((ViewHolder0) holder).tvCount.setText("X "+ AmountUtils.multiply1(""+datas.get(position).getDishCount(),"1"));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tvName, tvExtras, tvCount;

        public ViewHolder0(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tvExtras = (TextView) itemView.findViewById(R.id.tv_dish_extras);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }

    public void updateData(ArrayList<OrderDishEntity> retreatDishes) {
        datas.clear();
        datas.addAll(retreatDishes);
        notifyDataSetChanged();
    }
}
