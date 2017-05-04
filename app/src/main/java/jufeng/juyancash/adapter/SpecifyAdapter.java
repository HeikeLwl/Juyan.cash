package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.DishSpecifyEntity;
import jufeng.juyancash.dao.SpecifyEntity;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class SpecifyAdapter extends RecyclerView.Adapter<SpecifyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DishSpecifyEntity> mDishSpecifyEntities;
    private ArrayList<String> datas;
    private String selectedSpecifyId;
    private DishEntity dishEntity;

    public SpecifyAdapter(Context context, String dishId,String selectedSpecifyId) {
        this.mContext = context;
        this.selectedSpecifyId = selectedSpecifyId;
        initData(dishId);
    }
    
    public String getselectedSpecifyId(){
        return selectedSpecifyId;
    }

    private void initData(String dishId) {
        datas = new ArrayList<>();
        mDishSpecifyEntities = new ArrayList<>();
        mDishSpecifyEntities.addAll(DBHelper.getInstance(mContext).queryAllSpecify(dishId));
        if(selectedSpecifyId == null && mDishSpecifyEntities.size() > 0){
            selectedSpecifyId = mDishSpecifyEntities.get(0).getDishSpecifyId();
        }
        for (DishSpecifyEntity dishSpecify :
                mDishSpecifyEntities) {
            datas.add(dishSpecify.getDishSpecifyId());
        }
        dishEntity = DBHelper.getInstance(mContext).queryOneDishEntity(dishId);
    }

    @Override
    public SpecifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_mark_item, parent, false);
        ViewHolder holder0 = new ViewHolder(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(SpecifyAdapter.ViewHolder holder, int position) {
        SpecifyEntity specifyEntity = DBHelper.getInstance(mContext).getSpecifyeById(mDishSpecifyEntities.get(position).getSpecifyId());
        if (mDishSpecifyEntities.get(position).getCustomPrice() == 0 && mDishSpecifyEntities.get(position).getDefaultPrice() == 0) {
            holder.tvName.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(mContext).getSpecifyName(mDishSpecifyEntities.get(position).getSpecifyId()) + "</font><font color=\"red\">/" + specifyEntity.getPriceMultiple() * dishEntity.getDishPrice() + "元</font>"));
        } else {
            holder.tvName.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(mContext).getSpecifyName(mDishSpecifyEntities.get(position).getSpecifyId()) + "</font><font color=\"red\">/" + mDishSpecifyEntities.get(position).getCustomPrice() + "元</font>"));
        }
        if (selectedSpecifyId != null && selectedSpecifyId.equals(mDishSpecifyEntities.get(position).getDishSpecifyId())) {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.blue_layout));
        } else {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.black_layout));
        }
        holder.tvName.setTag(mDishSpecifyEntities.get(position).getDishSpecifyId());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dishSpecifyId = (String) view.getTag();
                if (selectedSpecifyId != null && !selectedSpecifyId.equals(dishSpecifyId)) {
                    int oldPosition = datas.indexOf(selectedSpecifyId);
                    notifyItemChanged(oldPosition);
                }
                selectedSpecifyId = dishSpecifyId;
                int position = datas.indexOf(dishSpecifyId);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDishSpecifyEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
