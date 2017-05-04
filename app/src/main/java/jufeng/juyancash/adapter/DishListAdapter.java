package jufeng.juyancash.adapter;

import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class DishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<DishBean> datas;
    private ArrayList<TaocanBean> mTaocanEntities;
    private OnDishClickListener mOnDishClickListener;
    private String dishTypeId;
    private String taocanTypeId;
    private int type;
    private String orderId;
    private Map<String,Integer> map;

    public DishListAdapter(Context context, String typeId, String orderId, int type) {
        this.mContext = context;
        this.orderId = orderId;
        datas = new ArrayList<>();
        mTaocanEntities = new ArrayList<>();
        map = new HashMap<>();
        initData(typeId, type);
    }

    private void initData(String typeId, int type) {
        this.type = type;
        map.clear();
        datas.clear();
        mTaocanEntities.clear();
        if(typeId != null) {
            if(type == 0){
                this.dishTypeId = typeId;
            }else if(type == 1){
                this.taocanTypeId = typeId;
            }
            new GetDataAsyncData().execute(type);
        }
    }

    class GetDataAsyncData extends AsyncTask<Integer,Integer,Object>{

        @Override
        protected Object doInBackground(Integer... params) {
            if(params[0] == 0){
                datas.addAll(DBHelper.getInstance(mContext).queryDishDataByType(dishTypeId, orderId));
                for (int i = 0;i < datas.size(); i++) {
                    map.put(datas.get(i).getDishEntity().getDishId(),i);
                }
            }else {
                mTaocanEntities.addAll(DBHelper.getInstance(mContext).queryTaocanDataByTypeId(taocanTypeId, orderId));
                for (int i = 0;i < mTaocanEntities.size(); i++) {
                    map.put(mTaocanEntities.get(i).getTaocanEntity().getTaocanId(),i);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object taocanBeen) {
            notifyDataSetChanged();
            super.onPostExecute(taocanBeen);
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dish_item_layout_1, parent, false);
        ViewHolder0 holder0 = new ViewHolder0(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {
            if (type == 0) {
                if (datas.get(position).isHasConfig()) {
                    //有规格或做法
                    ((ViewHolder0) holder).ivRightTop.setVisibility(ImageView.VISIBLE);
                    ((ViewHolder0) holder).ivRightTop.setImageResource(R.drawable.btn_config);
                } else if (datas.get(position).getDishEntity().getIsAblePresent() == 1) {
                    //可作为赠菜
                    ((ViewHolder0) holder).ivRightTop.setVisibility(ImageView.VISIBLE);
                    ((ViewHolder0) holder).ivRightTop.setImageResource(R.drawable.present_2);
                } else {
                    ((ViewHolder0) holder).ivRightTop.setVisibility(ImageView.GONE);
                }

                ((ViewHolder0) holder).tv0.setText(datas.get(position).getDishEntity().getDishName());
                double orderedCount = 0;
                if (orderId != null && (orderedCount = datas.get(position).getDishCount()) > 0) {
                    ((ViewHolder0) holder).tvOrderedCount.setText("已点："+String.valueOf(orderedCount));
                    ((ViewHolder0) holder).tvOrderedCount.setVisibility(TextView.VISIBLE);
                } else {
                    ((ViewHolder0) holder).tvOrderedCount.setText("已点：0");
                    ((ViewHolder0) holder).tvOrderedCount.setVisibility(TextView.GONE);
                }

                if(!datas.get(position).isChing()){
                    ((ViewHolder0) holder).tv1.setTextColor(mContext.getResources().getColor(R.color.bluelight));
                    ((ViewHolder0) holder).tv1.setText("￥" + datas.get(position).getDishEntity().getDishPrice() + "/" + datas.get(position).getDishEntity().getCheckOutUnit());
                    ((ViewHolder0) holder).rootLayout.setTag(datas.get(position));
                    ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DishBean dishBean = (DishBean) v.getTag();
                            mOnDishClickListener.onDishClicked(v,dishBean);
                            if(!dishBean.isHasConfig()){
                                changeItem(dishBean.getDishEntity().getDishId(),type);
                            }
                        }
                    });
                }else{
                    ((ViewHolder0) holder).tv1.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                    ((ViewHolder0) holder).tv1.setText("已售完");
                    ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnDishClickListener.onDishChing();
                        }
                    });
                }
            } else if (type == 1) {
                ((ViewHolder0) holder).ivRightTop.setVisibility(ImageView.VISIBLE);
                ((ViewHolder0) holder).ivRightTop.setImageResource(R.drawable.taocan_2);

                ((ViewHolder0) holder).tv0.setText(mTaocanEntities.get(position).getTaocanEntity().getTaocanName());
                ((ViewHolder0) holder).tv1.setText("￥" + mTaocanEntities.get(position).getTaocanEntity().getTaocanPrice() + "/" + mTaocanEntities.get(position).getTaocanEntity().getUnitName());

                double orderedCount = 0;
                if (orderId != null && (orderedCount = mTaocanEntities.get(position).getTaocanCount()) > 0) {
//                    ((ViewHolder0) holder).rootLayout.setBackgroundResource(R.drawable.dish_item_background);
                    ((ViewHolder0) holder).tvOrderedCount.setText("已点："+String.valueOf(orderedCount));
                    ((ViewHolder0) holder).tvOrderedCount.setVisibility(TextView.VISIBLE);
                } else {
//                    ((ViewHolder0) holder).rootLayout.setBackgroundResource(R.drawable.dish_item_background_default);
                    ((ViewHolder0) holder).tvOrderedCount.setText("已点：0");
                    ((ViewHolder0) holder).tvOrderedCount.setVisibility(TextView.GONE);
                }
                ((ViewHolder0) holder).rootLayout.setTag(mTaocanEntities.get(position).getTaocanEntity());
                ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaocanEntity taocanEntity = (TaocanEntity) v.getTag();
                        mOnDishClickListener.onTaocanClicked(taocanEntity, taocanEntity.getTaocanTypeId());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            return datas.size();
        } else {
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

    public void setOnDishClickListener(OnDishClickListener listener) {
        this.mOnDishClickListener = listener;
    }

    public interface OnDishClickListener {
        void onDishChing();
        void onDishClicked(View view,DishBean dishBean);
        void onTaocanClicked(TaocanEntity taocanEntity, String typeId);
    }

    public void updateData(String typeId, String orderId, int type) {
        this.orderId = orderId;
        initData(typeId,type);
    }
}
