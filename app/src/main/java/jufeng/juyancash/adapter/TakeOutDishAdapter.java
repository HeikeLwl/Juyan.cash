package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class TakeOutDishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> datas;
    private String orderId;
    private OrderEntity mOrderEntity;

    public TakeOutDishAdapter(Context context, String orderId, int status) {
        this.mContext = context.getApplicationContext();
        datas = new ArrayList<>();
        this.orderId = orderId;
        if (orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        } else {
            mOrderEntity = null;
        }
        datas.addAll(DBHelper.getInstance(mContext).queryOrderDishEntity(orderId, status));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selected_dish_item_layout, parent, false);
        ViewHolder0 holder0 = new ViewHolder0(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {
            //设置菜品状态：已下单，未下单，可打折

            ((ViewHolder0) holder).ivIsOrdered.setVisibility(ImageView.GONE);

            ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.GONE);
            ((ViewHolder0) holder).tvName.setText(datas.get(position).getDishName());
            String extras = "";
            //已经下单
            if (!TextUtils.isEmpty(datas.get(position).getDishPractice()) && !TextUtils.isEmpty(datas.get(position).getDishSpecify())) {//有做法和规格
                extras = "(" + datas.get(position).getDishPractice() + "," + datas.get(position).getDishSpecify() + ")";
            } else if (!TextUtils.isEmpty(datas.get(position).getDishPractice()) && !TextUtils.isEmpty(datas.get(position).getDishSpecify())) {//有做法
                extras = "(" + datas.get(position).getDishPractice() + ")";
            } else if (!TextUtils.isEmpty(datas.get(position).getDishPractice()) && !TextUtils.isEmpty(datas.get(position).getDishSpecify())) {//有规格
                extras = "(" + datas.get(position).getDishSpecify() + ")";
            }
            if (extras.isEmpty()) {
                ((ViewHolder0) holder).tvExtras.setVisibility(TextView.GONE);
            } else {
                ((ViewHolder0) holder).tvExtras.setVisibility(TextView.VISIBLE);
                ((ViewHolder0) holder).tvExtras.setText(extras);
            }

            //设置菜品数量
            ((ViewHolder0) holder).tvCount.setText(String.valueOf(datas.get(position).getDishCount()));

            ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
            ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);

            //设置菜品价格
            ((ViewHolder0) holder).tvPrice.setText("￥" + datas.get(position).getDishPrice());
            double dishPrice = 0;//商品原价
            String dishPriceStr = AmountUtils.changeY2F(datas.get(position).getDishPrice() + "");
            try {
                dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
                dishPrice = datas.get(position).getDishPrice();
            }
            if (datas.get(position).getIsOrdered() == 1) {
                ((ViewHolder0) holder).ivIsOrdered.setImageResource(R.drawable.iv_order);
                ((ViewHolder0) holder).ibReduce.setVisibility(ImageButton.GONE);
                ((ViewHolder0) holder).ibAdd.setVisibility(ImageButton.GONE);
                ((ViewHolder0) holder).rootLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
                double discountPrice = 0.0;
                int rate[] = {100, 100};
                if (datas.get(position).getType() == 0) {
                    //非套餐
                    rate = DBHelper.getInstance(mContext).getDishDiscountRate(mOrderEntity, datas.get(position), null);
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
            }
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

    public void updateData(String orderId) {
        datas.clear();
        if (orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        } else {
            mOrderEntity = null;
        }
        datas.addAll(DBHelper.getInstance(mContext).queryOrderDishEntity(orderId));
        notifyDataSetChanged();
    }

    public void updateData(String orderId, int status) {
        datas.clear();
        if (orderId != null) {
            mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        } else {
            mOrderEntity = null;
        }
        datas.addAll(DBHelper.getInstance(mContext).queryOrderDishEntity(orderId, status));
        notifyDataSetChanged();
    }
}
