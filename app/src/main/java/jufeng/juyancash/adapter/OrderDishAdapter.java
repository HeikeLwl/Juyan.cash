package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
public class OrderDishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> datas;
    private int mStatus;
    private boolean mIsOpenJoinOrder;
    private final int ORDER_DETAIL = 0;
    private final int ORDER_DISH_DETAIL = 1;
    private DiscountHistoryEntity discountHistoryEntity;
    private OrderEntity mOrderEntity;

    public OrderDishAdapter(Context context, String orderId, int status,boolean isOpenJoinOrder) {
        this.mContext = context;
        this.mStatus = status;
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        datas = new ArrayList<>();
        if(orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            new GetAsyncData().execute(orderId,status);
            discountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
        }
    }

    class GetAsyncData extends AsyncTask<Object,Integer,ArrayList<OrderDishEntity>>{

        @Override
        protected ArrayList<OrderDishEntity> doInBackground(Object... params) {
            String orderId = (String) params[0];
            int status = (int) params[1];
            if (mIsOpenJoinOrder) {
                //总单
                return DBHelper.getInstance(mContext).queryJoinOrderDish(orderId, status);
            } else {
                //子单
                return DBHelper.getInstance(mContext).queryOrderDishEntity(orderId, status);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<OrderDishEntity> orderDishEntities) {
            datas.addAll(orderDishEntities);
            notifyDataSetChanged();
            super.onPostExecute(orderDishEntities);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mIsOpenJoinOrder) {
            if (datas.get(position).getOrderId() != null && datas.get(position).getOrderDishId() == null) {
                //客单信息
                return ORDER_DETAIL;
            } else {
                //菜品列表信息
                return ORDER_DISH_DETAIL;
            }
        }else{
            return ORDER_DISH_DETAIL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
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
        }else if(holder instanceof ViewHolder1){
            if(datas.get(position).getOrderId() != null && datas.get(position).getOrderDishId() == null){
                OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(datas.get(position).getOrderId());
                if(orderEntity != null){
                    String tableName = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
                    tableName = tableName == null ? "":tableName;
                    ((ViewHolder1) holder).tvSeat.setText("桌位："+tableName);
                    ((ViewHolder1) holder).tvGuest.setText("人数："+orderEntity.getOrderGuests());
                    ((ViewHolder1) holder).tvTime.setText(CustomMethod.parseTime(orderEntity.getOpenTime(),"yyyy-MM-dd HH:mm"));
                    ((ViewHolder1) holder).tvOrderNumber.setText("NO."+orderEntity.getOrderNumber1());
                }
            }
        } else if (holder instanceof ViewHolder0) {
            //设置菜品状态：已下单，未下单，可打折
            switch (datas.get(position).getIsOrdered()){
                case -2:
                    //赠菜
                    ((ViewHolder0) holder).ivIsOrdered.setVisibility(ImageView.VISIBLE);
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.present);
                    break;
                case -1:
                    //退菜
                    ((ViewHolder0) holder).ivIsOrdered.setVisibility(ImageView.VISIBLE);
                    ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.retreat);
                    break;
                case 1:
                    //该菜品已下单
                    ((ViewHolder0) holder).ivIsOrdered.setVisibility(ImageView.GONE);
                    break;
            }

            ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
            ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);

            ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.GONE);
            ((ViewHolder0) holder).tvName.setText(datas.get(position).getDishName());
            String extras = "";
            if(datas.get(position).getIsOrdered() == 1){
                //已经下单
                if(datas.get(position).getPracticeId()!=null && datas.get(position).getSpecifyId()!=null){//有做法和规格
                    extras = "("+datas.get(position).getDishPractice()+","+datas.get(position).getDishSpecify()+")";
                }else if(datas.get(position).getPracticeId()!=null && datas.get(position).getSpecifyId()==null){//有做法
                    extras = "("+datas.get(position).getDishPractice()+")";
                }else if(datas.get(position).getPracticeId()==null && datas.get(position).getSpecifyId()!=null){//有规格
                    extras = "("+datas.get(position).getDishSpecify()+")";
                }
            }
            if(extras.isEmpty()){
                ((ViewHolder0) holder).tvExtras.setText("");
            }else{
                ((ViewHolder0) holder).tvExtras.setText(extras);
            }

            //设置菜品数量
            ((ViewHolder0) holder).tvCount.setText(AmountUtils.multiply(""+datas.get(position).getDishCount(),"1"));

            //设置菜品价格
            double dishPrice = 0.0;//商品原价
            if(datas.get(position).getIsOrdered() == -2){
                //赠菜
                ((ViewHolder0) holder).tvRate.setText("");
                ((ViewHolder0) holder).tvDiscountPrice.setText("");
                ((ViewHolder0) holder).tvPrice.setText("￥0.0");
                ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
            }else {
                if (datas.get(position).getType() == 0) {
                    //非套餐
                    String dishPriceStr = AmountUtils.changeY2F(datas.get(position).getDishPrice()+"");
                    try {
                        dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dishPrice = datas.get(position).getDishPrice();
                    }
                } else {
                    //套餐
                    if (mStatus == 1) {
                        String dishPriceStr = AmountUtils.changeY2F(DBHelper.getInstance(mContext).getOrderedTaocanPrice(datas.get(position))+"");
                        try {
                            dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dishPrice = DBHelper.getInstance(mContext).getOrderedTaocanPrice(datas.get(position));
                        }
                    } else {
                        String dishPriceStr = AmountUtils.changeY2F(DBHelper.getInstance(mContext).getTaocanPrice(datas.get(position))+"");
                        try {
                            dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dishPrice = DBHelper.getInstance(mContext).getTaocanPrice(datas.get(position));
                        }
                    }
                }
                if(datas.get(position).getIsOrdered() == -1){
                    //退菜
                    ((ViewHolder0) holder).tvRate.setText("");
                    ((ViewHolder0) holder).tvDiscountPrice.setText("");
                    ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                    ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                    ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
                }else {
                    if (datas.get(position).getIsOrdered() == 1) {//已下单的商品需要计算打折价
                        double discountPrice = 0.0f;
                        int rate[] = {100,100};
                        if (datas.get(position).getType() == 0) {
                            //非套餐
                            rate = DBHelper.getInstance(mContext).getDishDiscountRate(mOrderEntity,datas.get(position), discountHistoryEntity);
                            double rate0 = (double)(rate[0])/100;
                            double rate1 = (double)(rate[1])/100;
                            try {
                                discountPrice = AmountUtils.multiply(rate0,dishPrice,rate1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                discountPrice = rate0*dishPrice*rate1;
                            }
                        } else {
                            //套餐
                            rate = DBHelper.getInstance(mContext).getTaocanDishDiscountRate(mOrderEntity,datas.get(position), discountHistoryEntity);
                            double rate0 = (double)(rate[0])/100;
                            double rate1 = (double)(rate[1])/100;
                            try {
                                discountPrice = AmountUtils.multiply(rate0,dishPrice,rate1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                discountPrice = rate0*dishPrice*rate1;
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
                            if(rate[0] < 100){
                                rateStr += rate[0]+"%";
                            }
                            if(rate[1] < 100){
                                rateStr += rate[1]+"%";
                            }
                            ((ViewHolder0) holder).tvRate.setText(rateStr);
                            ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.graydark));
                        }
                    } else {//无打折
                        ((ViewHolder0) holder).tvRate.setText("");
                        ((ViewHolder0) holder).tvDiscountPrice.setText("");
                        ((ViewHolder0) holder).tvPrice.setText("￥" + dishPrice);
                        ((ViewHolder0) holder).tvPrice.getPaint().setFlags(0);
                        ((ViewHolder0) holder).tvPrice.setTextColor(mContext.getResources().getColor(R.color.dark));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tvName,tvExtras,tvCount,tvPrice,tvDiscountPrice,tvRate;
        private ImageButton ibReduce, ibAdd;
        private ImageView ivIsOrdered,ivDiscount;

        public ViewHolder0(View itemView) {
            super(itemView);
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
        private TextView tvOrderNumber,tvSeat,tvGuest,tvTime;

        public ViewHolder1(View itemView) {
            super(itemView);
            tvOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tvSeat = (TextView) itemView.findViewById(R.id.tv_seat_number);
            tvGuest = (TextView) itemView.findViewById(R.id.tv_seat_count);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    public void updateData(String orderId,int status,boolean isOpenJoinOrder){
        datas.clear();
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        this.mStatus = status;
        if(orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            new GetAsyncData().execute(orderId,status);
            discountHistoryEntity = DBHelper.getInstance(mContext).getDiscount(orderId);
        }
    }
}
