package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;


/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class SelectedDishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> datas;
    private OnSelectedDishClickListener mOnSelectedDishClickListener;
    private int mStatus;
    private boolean mIsOpenJoinOrder;
    private final int ORDER_DETAIL = 0;
    private final int ORDER_DISH_DETAIL = 1;
//    private int editPosition;
    private OrderEntity mOrderEntity;
    private DiscountHistoryEntity mDiscountHistoryEntity;
    private Drawable mDrawable;
    private Map<String,Integer> map;

    public SelectedDishAdapter(Context context, String orderId) {
        this.mContext = context;
        mStatus = 0;
//        editPosition = -1;
        mIsOpenJoinOrder = false;
        datas = new ArrayList<>();
        map = new HashMap<>();
        if (orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            mDiscountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
            new GetAsyncData().execute(orderId);
        }
        mDrawable = mContext.getResources().getDrawable(R.drawable.taocan_1);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
    }

    public SelectedDishAdapter(Context context, String orderId, int status, boolean isOpenJoinOrder) {
        this.mContext = context;
        this.mStatus = status;
//        editPosition = -1;
        map = new HashMap<>();
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        datas = new ArrayList<>();
        if (orderId != null) {
            mDiscountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            new GetAsyncData().execute(orderId);
        }
        mDrawable = mContext.getResources().getDrawable(R.drawable.taocan_1);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
    }

    class GetAsyncData extends AsyncTask<String, Integer, Object> {
        @Override
        protected Object doInBackground(String... params) {
            datas.clear();
            map.clear();
            if (mStatus == 0) {
                datas.addAll(DBHelper.getInstance(mContext).queryOrderDishEntity(params[0]));
            } else {
                datas.addAll(DBHelper.getInstance(mContext).queryOrderDishEntity(params[0], mStatus));
            }
            for(int i = 0;i < datas.size(); i++){
                map.put(datas.get(i).getOrderDishId(),i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object orderDishEntities) {
            notifyDataSetChanged();
            super.onPostExecute(orderDishEntities);
        }
    }

    public void addItem(OrderDishEntity orderDishEntity) {
//        editPosition = 0;
        try {
            datas.add(0, orderDishEntity);
            notifyItemInserted(0);
        }catch (Exception e){
            if(mOrderEntity != null) {
                new GetAsyncData().execute(mOrderEntity.getOrderId());
            }
        }
    }

    public void changeItem(OrderDishEntity orderDishEntity) {
        try {
            int position = map.get(orderDishEntity.getOrderDishId());
            datas.remove(position);
            datas.add(position, orderDishEntity);
            notifyItemChanged(position);
        }catch (Exception e){
            if(mOrderEntity != null) {
                new GetAsyncData().execute(mOrderEntity.getOrderId());
            }
        }
    }

    public void deleteItem(String orderDishId) {
        try {
            int position = map.get(orderDishId);
            datas.remove(position);
            map.clear();
            for (int i = 0; i < datas.size(); i++) {
                map.put(datas.get(i).getOrderDishId(), i);
            }
            notifyItemRemoved(position);
        }catch (Exception e){
            if(mOrderEntity != null) {
                new GetAsyncData().execute(mOrderEntity.getOrderId());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsOpenJoinOrder) {
            if (datas.get(position).getOrderId() != null && datas.get(position).getOrderDishId() == null) {
                //客单信息
                return ORDER_DETAIL;
            } else {
                //菜品列表信息
                return ORDER_DISH_DETAIL;
            }
        } else {
            return ORDER_DISH_DETAIL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ORDER_DETAIL:
                view = LayoutInflater.from(mContext).inflate(R.layout.select_order_head_layout, parent, false);
                ViewHolder1 holder1 = new ViewHolder1(view);
                return holder1;
            case ORDER_DISH_DETAIL:
                view = LayoutInflater.from(mContext).inflate(R.layout.selected_dish_item_layout, parent, false);
                ViewHolder0 holder0 = new ViewHolder0(view);
                return holder0;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder1) {
            if (datas.get(position).getOrderId() != null && datas.get(position).getOrderDishId() == null) {
                OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(datas.get(position).getOrderId());
                if (orderEntity != null) {
                    String tableName = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
                    tableName = tableName == null ? "" : tableName;
                    ((ViewHolder1) holder).tvSeat.setText("桌位：" + tableName);
                    ((ViewHolder1) holder).tvGuest.setText("人数：" + orderEntity.getOrderGuests());
                    ((ViewHolder1) holder).tvTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(), "yyyy-MM-dd HH:mm"));
                    ((ViewHolder1) holder).tvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
                    ((ViewHolder1) holder).rootLayout.setTag(orderEntity.getOrderId());
                    ((ViewHolder1) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnSelectedDishClickListener.changeChildOrder((String) v.getTag());
                        }
                    });
                }
            }
        } else if (holder instanceof ViewHolder0) {
            //设置菜品状态：可打折
            if (datas.get(position).getIsAbleDiscount() == 1) {
                //允许打折
                ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.GONE);
            }

            //菜品名称
            if (datas.get(position).getType() == 0) {
                ((ViewHolder0) holder).tvName.setCompoundDrawables(null, null, null, null);
            } else {
                ((ViewHolder0) holder).tvName.setCompoundDrawables(null, null, mDrawable, null);
            }
            ((ViewHolder0) holder).tvName.setText(datas.get(position).getDishName());

            //菜品配置
            String extras = "";
            if (datas.get(position).getIsOrdered() == 1) {
                //已经下单
                if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() != null) {//有做法和规格
                    extras = "(" + datas.get(position).getDishPractice() + "," + datas.get(position).getDishSpecify() + ")";
                } else if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() == null) {//有做法
                    extras = "(" + datas.get(position).getDishPractice() + ")";
                } else if (datas.get(position).getPracticeId() == null && datas.get(position).getSpecifyId() != null) {//有规格
                    extras = "(" + datas.get(position).getDishSpecify() + ")";
                }
            } else {
                //未下单
                if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() != null) {//有做法和规格
                    extras = "(" + datas.get(position).getDishPractice() + "," + datas.get(position).getDishSpecify() + ")";
                } else if (datas.get(position).getPracticeId() != null && datas.get(position).getSpecifyId() == null) {//有做法
                    extras = "(" + datas.get(position).getDishPractice() + ")";
                } else if (datas.get(position).getPracticeId() == null && datas.get(position).getSpecifyId() != null) {//有规格
                    extras = "(" + datas.get(position).getDishSpecify() + ")";
                }
            }
            if (extras.isEmpty()) {
                ((ViewHolder0) holder).tvExtras.setText("");
            } else {
                ((ViewHolder0) holder).tvExtras.setText(extras);
            }

            ((ViewHolder0) holder).ibReduce.setTag(datas.get(position));
            ((ViewHolder0) holder).ibReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDishEntity orderDishEntity = (OrderDishEntity) v.getTag();
                    int position = datas.indexOf(orderDishEntity);
                    orderDishEntity = DBHelper.getInstance(mContext).changeCountByOrderDishId(orderDishEntity.getOrderDishId(), -1);
                    mOnSelectedDishClickListener.onReduce(orderDishEntity);
                    datas.remove(position);
                    datas.add(position, orderDishEntity);
                    notifyItemChanged(position);
                }
            });
            ((ViewHolder0) holder).ibAdd.setTag(datas.get(position));
            ((ViewHolder0) holder).ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDishEntity orderDishEntity = (OrderDishEntity) v.getTag();
                    int position = datas.indexOf(orderDishEntity);
                    orderDishEntity = DBHelper.getInstance(mContext).changeCountByOrderDishId(orderDishEntity.getOrderDishId(), 1);
                    mOnSelectedDishClickListener.onAdd(orderDishEntity);
                    datas.remove(position);
                    datas.add(position, orderDishEntity);
                    notifyItemChanged(position);
                }
            });

            //设置菜品数量
            ((ViewHolder0) holder).tvCount.setText("" + AmountUtils.multiply(datas.get(position).getDishCount(), 1));

            //设置菜品价格
            double dishPrice = 0;//商品原价
            if (datas.get(position).getType() == 0) {
                //非套餐
                String dishPriceStr = AmountUtils.changeY2F(datas.get(position).getDishPrice() + "");
                try {
                    dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    dishPrice = datas.get(position).getDishPrice();
                }
            } else {
                //套餐
                if (mStatus == 1) {
                    String dishPriceStr = AmountUtils.changeY2F(DBHelper.getInstance(mContext).getOrderedTaocanPrice(datas.get(position)) * datas.get(position).getDishCount() + "");
                    try {
                        dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dishPrice = DBHelper.getInstance(mContext).getOrderedTaocanPrice(datas.get(position)) * datas.get(position).getDishCount();
                    }
                } else {
                    String dishPriceStr = AmountUtils.changeY2F(DBHelper.getInstance(mContext).getTaocanPrice(datas.get(position)) * datas.get(position).getDishCount() + "");
                    try {
                        dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dishPrice = DBHelper.getInstance(mContext).getTaocanPrice(datas.get(position)) * datas.get(position).getDishCount();
                    }
                }
            }
            holder.itemView.setTag(datas.get(position));
            switch (datas.get(position).getIsOrdered()) {
                case -2://赠菜
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.present);
                    ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    ((ViewHolder0) holder).tvRate.setText("");
                    ((ViewHolder0) holder).tvDiscountPrice.setText("");
                    ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.graydark));
                    ((ViewHolder0) holder).tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                    ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                    ((ViewHolder0) holder).tvDiscountPrice.setText("￥0.0");
                    break;
                case -1://退菜
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.retreat);
                    ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    ((ViewHolder0) holder).tvRate.setText("");
                    ((ViewHolder0) holder).tvDiscountPrice.setText("");
                    ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                    ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                    ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
                    break;
                case 0://未下单
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.iv_unorder);
                    if (datas.get(position).getIsFromWX() != null && datas.get(position).getIsFromWX() == 1) {
                        ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
                        ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
                    } else {
                        ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.VISIBLE);
                        ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.VISIBLE);
                    }
                    ((ViewHolder0) holder).rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    ((ViewHolder0) holder).tvRate.setText("");
                    ((ViewHolder0) holder).tvDiscountPrice.setText("");
                    ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                    ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                    ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
                    break;
                case 1://下单
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.iv_order);
                    ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
                    ((ViewHolder0) holder).rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    double discountPrice = 0.0;
                    int rate[] = {100, 100};
                    if (datas.get(position).getType() == 0) {
                        //非套餐
                        rate = DBHelper.getInstance(mContext).getDishDiscountRate(mOrderEntity, datas.get(position), mDiscountHistoryEntity);
                        double rate0 = (double) (rate[0]) / 100;
                        double rate1 = (double) (rate[1]) / 100;
                        try {
                            discountPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            discountPrice = rate0 * dishPrice * rate1;
                        }
                    } else {
                        //套餐
                        rate = DBHelper.getInstance(mContext).getTaocanDishDiscountRate(mOrderEntity, datas.get(position), mDiscountHistoryEntity);
                        double rate0 = (double) (rate[0]) / 100;
                        double rate1 = (double) (rate[1]) / 100;
                        try {
                            discountPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            discountPrice = rate0 * dishPrice * rate1;
                        }
                    }
                    if (rate[0] == 100 && rate[1] == 100) {//无打折
                        ((ViewHolder0) holder).tvRate.setText("");
                        ((ViewHolder0) holder).tvDiscountPrice.setText("");
                        ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                        ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                        ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
                    } else {//有打折
                        ((ViewHolder0) holder).tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                        ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                        ((ViewHolder0) holder).tvDiscountPrice.setText("￥" + discountPrice);
                        String rateStr = "";
                        if (rate[0] < 100) {
                            rateStr += rate[0] + "%";
                        }
                        if (rate[1] < 100) {
                            rateStr += rate[1] + "%";
                        }
                        ((ViewHolder0) holder).tvRate.setText(rateStr);
                        ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.graydark));
                    }
                    break;
            }

            //设置菜品点击事件
            ((ViewHolder0) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDishEntity orderDishEntity = (OrderDishEntity) v.getTag();
                    if (orderDishEntity.getIsOrdered() != -1 && !orderDishEntity.getDishId().equals("voucherdish")) {
                        mOnSelectedDishClickListener.onSelectedDishClicked(orderDishEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tvName, tvExtras, tvCount, tvPrice, tvDiscountPrice, tvRate;
        private ImageButton ibReduce, ibAdd;
        private ImageView ivIsOrdered, ivDiscount;
        private LinearLayout rootLayout;

        public ViewHolder0(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tvExtras = (TextView) itemView.findViewById(R.id.tv_dish_extras);
            ibReduce = (ImageButton) itemView.findViewById(R.id.ib_reduce);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            ibAdd = (ImageButton) itemView.findViewById(R.id.ib_add);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_dish_price);
            ivIsOrdered = (ImageView) itemView.findViewById(R.id.iv_is_ordered);
            ivDiscount = (ImageView) itemView.findViewById(R.id.iv_can_discount);
            tvDiscountPrice = (TextView) itemView.findViewById(R.id.tv_dish_discount_price);
            tvRate = (TextView) itemView.findViewById(R.id.tv_dish_discount_rate);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvOrderNumber, tvSeat, tvGuest, tvTime;
        private RelativeLayout rootLayout;

        public ViewHolder1(View itemView) {
            super(itemView);
            tvOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tvSeat = (TextView) itemView.findViewById(R.id.tv_seat_number);
            tvGuest = (TextView) itemView.findViewById(R.id.tv_seat_count);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnSelectedDishClickListener(OnSelectedDishClickListener listener) {
        this.mOnSelectedDishClickListener = listener;
    }

    public interface OnSelectedDishClickListener {
        void onSelectedDishClicked(OrderDishEntity orderDishEntity);

        void onReduce(OrderDishEntity orderDishEntity);

        void onAdd(OrderDishEntity orderDishEntity);

        void changeChildOrder(String orderId);
    }

    public void updateData(String orderId) {
        datas.clear();
        mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        mDiscountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
        new GetAsyncData().execute(orderId);
    }

    public void updateData(String orderId, int status, boolean isOpenJoinOrder) {
        datas.clear();
        this.mStatus = status;
        mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        mDiscountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        new GetAsyncData().execute(orderId);
    }
}
