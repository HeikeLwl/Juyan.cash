package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishSelectedMaterialEntity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by Administrator102 on 2017/4/19.
 */

public class SelectMaterialAdapter extends RecyclerView.Adapter<SelectMaterialAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DishSelectedMaterialEntity> mDishSelectedMaterialEntities;
    private String orderDishId;
    private String dishId;
    private String orderId;

    public SelectMaterialAdapter(Context context, String orderDishId, String dishId,String orderId) {
        this.mContext = context;
        this.orderDishId = orderDishId;
        this.dishId = dishId;
        this.orderId = orderId;
        initData();
    }

    public ArrayList<DishSelectedMaterialEntity> getDishSelectedMaterialEntities(){
        return mDishSelectedMaterialEntities;
    }

    private void initData() {
        mDishSelectedMaterialEntities = new ArrayList<>();
        mDishSelectedMaterialEntities.addAll(DBHelper.getInstance(mContext).getSelectedMaterial(orderId,dishId,orderDishId));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_material_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvName.setText(mDishSelectedMaterialEntities.get(position).getMaterialName());
        holder.mTvPrice.setText("￥"+ AmountUtils.multiply(mDishSelectedMaterialEntities.get(position).getMaterialPrice(),0.01)+"/份");
        holder.mTvCount.setText(""+mDishSelectedMaterialEntities.get(position).getSelectedCount());
        holder.mIbAdd.setTag(mDishSelectedMaterialEntities.get(position));
        holder.mIbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DishSelectedMaterialEntity dishSelectedMaterialEntity = (DishSelectedMaterialEntity) view.getTag();
                dishSelectedMaterialEntity.setSelectedCount(dishSelectedMaterialEntity.getSelectedCount() + 1);
                dishSelectedMaterialEntity.setTotalPrice(dishSelectedMaterialEntity.getMaterialPrice()*dishSelectedMaterialEntity.getSelectedCount());
                int position = mDishSelectedMaterialEntities.indexOf(dishSelectedMaterialEntity);
                mDishSelectedMaterialEntities.remove(position);
                mDishSelectedMaterialEntities.add(position,dishSelectedMaterialEntity);
                notifyItemChanged(position);
            }
        });
        holder.mIbReduce.setTag(mDishSelectedMaterialEntities.get(position));
        holder.mIbReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DishSelectedMaterialEntity dishSelectedMaterialEntity = (DishSelectedMaterialEntity) view.getTag();
                if(dishSelectedMaterialEntity.getSelectedCount() > 0) {
                    dishSelectedMaterialEntity.setSelectedCount(dishSelectedMaterialEntity.getSelectedCount() - 1);
                    dishSelectedMaterialEntity.setTotalPrice(dishSelectedMaterialEntity.getMaterialPrice() * dishSelectedMaterialEntity.getSelectedCount());
                    int position = mDishSelectedMaterialEntities.indexOf(dishSelectedMaterialEntity);
                    mDishSelectedMaterialEntities.remove(position);
                    mDishSelectedMaterialEntities.add(position, dishSelectedMaterialEntity);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDishSelectedMaterialEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.ib_add)
        ImageButton mIbAdd;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.ib_reduce)
        ImageButton mIbReduce;
        @BindView(R.id.tv_price)
        TextView mTvPrice;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
