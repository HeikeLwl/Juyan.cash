package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishTypeEntity;
import jufeng.juyancash.dao.TaocanTypeEntity;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class DishChildMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<DishTypeEntity> datas;
    private ArrayList<TaocanTypeEntity> mTaocanTypeEntities;
    private OnChildDishMenuClickListener mOnChildDishMenuClickListener;
    private String selectedTypeId;
    private int type;

    public DishChildMenuAdapter(Context context,String dishTypeId,int type) {
        this.mContext = context;
        datas = new ArrayList<>();
        mTaocanTypeEntities = new ArrayList<>();
        initData(dishTypeId,type);
    }

    private void initData(String dishTypeId,int type) {
        datas.clear();
        mTaocanTypeEntities.clear();
        this.type = type;
        selectedTypeId = null;

        if(type == 0){
            //非套餐
            datas.addAll(DBHelper.getInstance(mContext).queryChildDishTypeData(dishTypeId));
            if(datas.size() > 0){
                selectedTypeId = datas.get(0).getDishTypeId();
            }
        }else{
            mTaocanTypeEntities.addAll(DBHelper.getInstance(mContext).queryChildTaocanTypeData(dishTypeId));
            if (mTaocanTypeEntities.size() > 0) {
                selectedTypeId = mTaocanTypeEntities.get(0).getTaocanTypeId();
            }
        }
    }

    public String getSelectedTypeId() {
        return selectedTypeId;
    }

    public int getType() {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dish_child_menu_item, parent, false);
        ViewHolder0 holder0 = new ViewHolder0(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {
            if (type == 0) {
                if (selectedTypeId.equals(datas.get(position).getDishTypeId())) {
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.white));
                    ((ViewHolder0) holder).tv0.setBackgroundColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                } else {
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.dark));
                    ((ViewHolder0) holder).tv0.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
                ((ViewHolder0) holder).tv0.setText(datas.get(position).getDishTypeName());
                ((ViewHolder0) holder).tv0.setTag(datas.get(position).getDishTypeId());
                ((ViewHolder0) holder).tv0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 0;
                        selectedTypeId = (String) v.getTag();
                        mOnChildDishMenuClickListener.onChildDishMenuClicked(selectedTypeId,type);
                        notifyDataSetChanged();
                    }
                });
            } else if (type == 1) {
                if (selectedTypeId.equals(mTaocanTypeEntities.get(position).getTaocanTypeId())) {
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.white));
                    ((ViewHolder0) holder).tv0.setBackgroundColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                } else {
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.dark));
                    ((ViewHolder0) holder).tv0.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
                ((ViewHolder0) holder).tv0.setText(mTaocanTypeEntities.get(position).getTaocanTypeName());
                ((ViewHolder0) holder).tv0.setTag(mTaocanTypeEntities.get(position).getTaocanTypeId());
                ((ViewHolder0) holder).tv0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 1;
                        selectedTypeId = (String) v.getTag();
                        mOnChildDishMenuClickListener.onChildDishMenuClicked(selectedTypeId,type);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(type == 0){
            return datas.size();
        }else{
            return mTaocanTypeEntities.size();
        }
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tv0;

        public ViewHolder0(View itemView) {
            super(itemView);
            tv0 = (TextView) itemView.findViewById(R.id.tv_child_type_name);
        }
    }

    public void updateData(String dishTypeId,int type){
        initData(dishTypeId,type);
        notifyDataSetChanged();
    }

    public void setOnChildDishMenuClickListener(OnChildDishMenuClickListener listener) {
        this.mOnChildDishMenuClickListener = listener;
    }

    public interface OnChildDishMenuClickListener {
        void onChildDishMenuClicked(String typeId, int type);
    }
}
