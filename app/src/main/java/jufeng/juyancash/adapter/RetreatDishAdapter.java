package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.ui.customview.CustomeEditCountDialog;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class RetreatDishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDishEntity> datas;
    private ArrayList<OrderDishEntity> retreatDishes;
    private String orderId;
    private boolean mIsOpenJoinOrder;
    private final int ORDER_DETAIL = 0;
    private final int ORDER_DISH_DETAIL = 1;
    private HashMap<String, Double> allDishMap, checkDishMap;

    public RetreatDishAdapter(Context context, String orderId, boolean isOpenJoinOrder) {
        this.mContext = context;
        retreatDishes = new ArrayList<>();
        this.orderId = orderId;
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        allDishMap = new HashMap<>();
        checkDishMap = new HashMap<>();

        datas = new ArrayList<>();
        datas.addAll(DBHelper.getInstance(mContext).queryRetreatableOrderDish(orderId));
        //将所有已点商品的id和数量加入allDishMap中
        allDishMap.clear();
        for (OrderDishEntity orderDish :
                datas) {
            allDishMap.put(orderDish.getOrderDishId(), AmountUtils.multiply1("" + orderDish.getDishCount(), "1"));
        }
        checkDishMap.clear();
    }

    public void updateData(String orderId, boolean isOpenJoinOrder) {
        retreatDishes.clear();
        this.orderId = orderId;
        this.mIsOpenJoinOrder = isOpenJoinOrder;
        allDishMap.clear();
        checkDishMap.clear();

        datas.clear();
        datas.addAll(DBHelper.getInstance(mContext).queryRetreatableOrderDish(orderId));
        //将所有已点商品的id和数量加入allDishMap中
        for (OrderDishEntity orderDish :
                datas) {
            allDishMap.put(orderDish.getOrderDishId(), orderDish.getDishCount());
        }

        notifyDataSetChanged();
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
                view = LayoutInflater.from(mContext).inflate(R.layout.retreat_dish_item_layout, parent, false);
                ViewHolder0 holder0 = new ViewHolder0(view);
                return holder0;
            default:
                return null;
        }
    }

    public void setReteatAll() {
        checkDishMap.clear();
        checkDishMap.putAll(allDishMap);
        retreatDishes.clear();
        retreatDishes.addAll(datas);
        notifyDataSetChanged();
    }

    public ArrayList<OrderDishEntity> getRetreatDishes() {
        retreatDishes.clear();
        for (Map.Entry<String, Double> entry :
                checkDishMap.entrySet()) {
            for (OrderDishEntity orderDish :
                    datas) {
                if (orderDish.getOrderDishId().equals(entry.getKey())) {
                    OrderDishEntity orderDishEntity = new OrderDishEntity(orderDish);
                    orderDishEntity.setDishCount(entry.getValue());
                    retreatDishes.add(orderDishEntity);
                }
            }
        }
        return retreatDishes;
    }

    private void changeDishCount(String orderDishId, double count) {
        if (checkDishMap.containsKey(orderDishId)) {
            if (count <= 0) {
                CustomMethod.showMessage(mContext, "数量不得小于或等于0");
                return;
            }
            if (count > allDishMap.get(orderDishId)) {
                CustomMethod.showMessage(mContext, "不能超过点菜数量");
                return;
            }
            checkDishMap.put(orderDishId, count);
        } else {
            CustomMethod.showMessage(mContext, "请先选择该菜品");
        }
    }

    private void addDish(String orderDishId) {
        if (checkDishMap.containsKey(orderDishId)) {
            //该菜品以选中
            if (checkDishMap.get(orderDishId) < allDishMap.get(orderDishId)) {
                //选中的菜品数量小于总数
                checkDishMap.put(orderDishId, checkDishMap.get(orderDishId) + 1);
            } else {
                CustomMethod.showMessage(mContext, "不能超过点菜数量");
            }
        }
    }

    private void reduceDish(String orderDishId) {
        if (checkDishMap.containsKey(orderDishId)) {
            //该菜品以选中
            if (checkDishMap.get(orderDishId) > 1) {
                //选中的菜品数量大于1
                checkDishMap.put(orderDishId, checkDishMap.get(orderDishId) - 1);
            } else {
                CustomMethod.showMessage(mContext, "数量不得小于或等于0");
            }
        }
    }

    private void checkDish(String orderDishId) {
        if (checkDishMap.containsKey(orderDishId)) {//该菜品已选中
            checkDishMap.remove(orderDishId);
        } else {
            checkDishMap.put(orderDishId, allDishMap.get(orderDishId));
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
                }
            }
        } else if (holder instanceof ViewHolder0) {
            ((ViewHolder0) holder).cbRetreatDish.setAnimation(null);
            //设置是否选中为退菜菜品
            if (checkDishMap.containsKey(datas.get(position).getOrderDishId())) {
                //选中
                ((ViewHolder0) holder).cbRetreatDish.setChecked(true);
                ((ViewHolder0) holder).tvCount.setText(AmountUtils.multiply("" + checkDishMap.get(datas.get(position).getOrderDishId()), "1"));
            } else {
                //未选中
                ((ViewHolder0) holder).cbRetreatDish.setChecked(false);
                ((ViewHolder0) holder).tvCount.setText(AmountUtils.multiply("" + datas.get(position).getDishCount(), "1"));
            }

            if (datas.get(position).getIsAbleDiscount() == 1) {
                //允许打折
                ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder0) holder).ivDiscount.setVisibility(ImageView.GONE);
            }

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

            //设置增加减少的点击事件，并且设置是否可点击
            ((ViewHolder0) holder).ibReduce.setTag(datas.get(position).getOrderDishId());
            ((ViewHolder0) holder).ibReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderDishId = (String) v.getTag();
                    reduceDish(orderDishId);
                    notifyDataSetChanged();
                }
            });
            ((ViewHolder0) holder).ibAdd.setTag(datas.get(position).getOrderDishId());
            ((ViewHolder0) holder).ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderDishId = (String) v.getTag();
                    addDish(orderDishId);
                    notifyDataSetChanged();
                }
            });
            //设置增加减少是否可点击
            if (checkDishMap.containsKey(datas.get(position).getOrderDishId())) {
                ((ViewHolder0) holder).ibAdd.setClickable(true);
                ((ViewHolder0) holder).ibReduce.setClickable(true);
            } else {
                ((ViewHolder0) holder).ibAdd.setClickable(false);
                ((ViewHolder0) holder).ibReduce.setClickable(false);
            }

            //设置菜品价格
            ((ViewHolder0) holder).tvPrice.setText("￥" + datas.get(position).getDishPrice());

            ((ViewHolder0) holder).rootLayout.setTag(datas.get(position).getOrderDishId());
            ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderDishId = (String) v.getTag();
                    checkDish(orderDishId);
                    notifyDataSetChanged();
                }
            });

            ((ViewHolder0) holder).tvCount.setTag(datas.get(position));
            ((ViewHolder0) holder).tvCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OrderDishEntity orderDishEntity = (OrderDishEntity) v.getTag();
                    if (orderDishEntity == null) {
                        return;
                    }
                    final CustomeEditCountDialog dialog = new CustomeEditCountDialog(mContext, orderDishEntity.getDishName() + "(总数量：" + orderDishEntity.getDishCount() + ")", "");
                    dialog.setExitDialogClickListener(new CustomeEditCountDialog.OnEditCountDialogClickListener() {
                        @Override
                        public void onCancle() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm(String count) {
                            if (orderDishEntity == null) {
                                return;
                            }
                            changeDishCount(orderDishEntity.getOrderDishId(), AmountUtils.multiply1(count, "1"));
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tvName, tvExtras, tvPrice, tvCount;
        private ImageView ivDiscount;
        private LinearLayout rootLayout;
        private ImageButton ibReduce, ibAdd;
        private CheckBox cbRetreatDish;

        public ViewHolder0(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tvExtras = (TextView) itemView.findViewById(R.id.tv_dish_extras);
            ibReduce = (ImageButton) itemView.findViewById(R.id.ib_reduce);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            ibAdd = (ImageButton) itemView.findViewById(R.id.ib_add);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_dish_price);
            cbRetreatDish = (CheckBox) itemView.findViewById(R.id.cb_retreat_dish);
            ivDiscount = (ImageView) itemView.findViewById(R.id.iv_can_discount);
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
}
