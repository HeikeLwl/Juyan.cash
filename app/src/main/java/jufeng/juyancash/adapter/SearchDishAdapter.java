package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.DishBean;
import jufeng.juyancash.bean.TaocanBean;
import jufeng.juyancash.dao.TaocanEntity;
import jufeng.juyancash.eventbus.SearchDishUpdateEvent;

/**
 * Created by Administrator102 on 2016/8/5.
 */
public class SearchDishAdapter extends RecyclerView.Adapter<SearchDishAdapter.ViewHolder0> {
    private Context mContext;
    private ArrayList<DishBean> datas;
    private ArrayList<TaocanBean> mTaocanEntities;
    private int mType;
    private OnSearchDishClickListener mOnSearchDishClickListener;
    private Map<String,Integer> map;
    private String orderId;

    public SearchDishAdapter(Context context,int type) {
        this.mContext = context;
        this.mType = type;
        datas = new ArrayList<>();
        mTaocanEntities = new ArrayList<>();
        map = new HashMap<>();
        this.orderId = null;
    }

    public void changeItem(String dishId,int type){
        if(map.containsKey(dishId)) {
            int position = map.get(dishId);
            if (type == 0 && position >= 0) {
                DishBean dishBean = DBHelper.getInstance(mContext).queryOneDishData(orderId, datas.get(position).getDishEntity());
                datas.remove(position);
                datas.add(position, dishBean);
                notifyItemChanged(position);
            } else if (type == 1 && position >= 0) {
                TaocanBean taocanBean = DBHelper.getInstance(mContext).queryOneTaocanData(orderId, mTaocanEntities.get(position).getTaocanEntity());
                mTaocanEntities.remove(position);
                mTaocanEntities.add(position, taocanBean);
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public ViewHolder0 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dish_item_layout_1, parent, false);
        ViewHolder0 holder = new ViewHolder0(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder0 holder, int position) {
        if (mType == 0) {
            if (datas.get(position).isHasConfig()) {
                //有规格或做法
                holder.ivRightTop.setVisibility(ImageView.VISIBLE);
                holder.ivRightTop.setImageResource(R.drawable.btn_config);
            } else if (datas.get(position).getDishEntity().getIsAblePresent() == 1) {
                //可作为赠菜
                holder.ivRightTop.setVisibility(ImageView.VISIBLE);
                holder.ivRightTop.setImageResource(R.drawable.present_1);
            } else {
                holder.ivRightTop.setVisibility(ImageView.GONE);
            }
            holder.tv0.setText(datas.get(position).getDishEntity().getDishName());
            double orderedCount = 0;
            if ((orderedCount = datas.get(position).getDishCount()) > 0) {
                holder.tvOrderedCount.setText("已点："+String.valueOf(orderedCount));
                holder.tvOrderedCount.setVisibility(TextView.VISIBLE);
            } else {
                holder.tvOrderedCount.setText("已点：0");
                holder.tvOrderedCount.setVisibility(TextView.GONE);
            }

            if(!datas.get(position).isChing()){
                holder.tv1.setTextColor(mContext.getResources().getColor(R.color.bluelight));
                holder.tv1.setText("￥" + datas.get(position).getDishEntity().getDishPrice() + "/" + datas.get(position).getDishEntity().getCheckOutUnit());
                holder.rootLayout.setTag(datas.get(position));
                holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DishBean dishBean = (DishBean) v.getTag();
                        if(mOnSearchDishClickListener != null && dishBean != null){
                            mOnSearchDishClickListener.onNormalDishClick(dishBean);
                            if(!dishBean.isHasConfig()){
                                changeItem(dishBean.getDishEntity().getDishId(),0);
                            }
                        }
                    }
                });
            }else{
                holder.tv1.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                holder.tv1.setText("已售完");
                holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mOnSearchDishClickListener != null){
                            mOnSearchDishClickListener.onDishChing();
                        }
                    }
                });
            }
        } else if (mType == 1) {
            holder.ivRightTop.setVisibility(ImageView.VISIBLE);
            holder.ivRightTop.setImageResource(R.drawable.taocan);

            holder.tv0.setText(mTaocanEntities.get(position).getTaocanEntity().getTaocanName());
            holder.tv1.setText("￥" + mTaocanEntities.get(position).getTaocanEntity().getTaocanPrice() + "/" + mTaocanEntities.get(position).getTaocanEntity().getUnitName());

            double orderedCount = 0;
            if ((orderedCount = datas.get(position).getDishCount()) > 0) {
                holder.tvOrderedCount.setText("已点："+String.valueOf(orderedCount));
                holder.tvOrderedCount.setVisibility(TextView.VISIBLE);
            } else {
                holder.tvOrderedCount.setText("已点：0");
                holder.tvOrderedCount.setVisibility(TextView.GONE);
            }
            holder.rootLayout.setTag(mTaocanEntities.get(position).getTaocanEntity());
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaocanEntity taocanEntity = (TaocanEntity) v.getTag();
                    if(mOnSearchDishClickListener != null && taocanEntity != null){
                        mOnSearchDishClickListener.onTaocanClick(taocanEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mType == 0){
            return datas.size();
        }else{
            return mTaocanEntities.size();
        }
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1;
        private ImageView ivRightTop;
        private TextView tvOrderedCount;
        private LinearLayout rootLayout;

        public ViewHolder0(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tv1 = (TextView) itemView.findViewById(R.id.tv_dish_price);
            ivRightTop = (ImageView) itemView.findViewById(R.id.iv_dish_type);
            tvOrderedCount = (TextView) itemView.findViewById(R.id.tv_dish_ordered_count);
        }
    }

    public void updateData(SearchDishUpdateEvent event){
        this.orderId = event.getOrderId();
        if(event != null){
            mTaocanEntities.clear();
            datas.clear();
            if(event.getType() == 0){
                //普通菜品
                datas.addAll(DBHelper.getInstance(mContext).searchDishByName(event.getOrderId(),event.getMatchStr()));
                this.mType = 0;
            }else{
                //套餐
                this.mType = 1;
                mTaocanEntities.addAll(DBHelper.getInstance(mContext).searchTaocanByName(event.getOrderId(),event.getMatchStr()));
            }
            map.clear();
            for (int i = 0; i < datas.size(); i++){
                map.put(datas.get(i).getDishEntity().getDishId(),i);
            }
            for (int i = 0; i < mTaocanEntities.size(); i++){
                map.put(mTaocanEntities.get(i).getTaocanEntity().getTaocanId(),i);
            }
            notifyDataSetChanged();
        }
    }

    public void setOnSearchDishClickListener(OnSearchDishClickListener listener){
        this.mOnSearchDishClickListener = listener;
    }

    public interface OnSearchDishClickListener{
        void onNormalDishClick(DishBean dishBean);
        void onTaocanClick(TaocanEntity taocanEntity);
        void onDishChing();
    }
}
