package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.util.FilterArrayList;

/**
 * 用于GridView装载数据的适配器
 *
 * @author xxs
 */
public class TablesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private FilterArrayList<TableEntity> datas;
    private Map<String, OrderEntity> tableOrderMap;
    private Map<String, Integer> mScheduleMap;
    private Drawable mWeChatPayed, mWeChatUnPay, mStoreUnPay, mStorePay;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public TablesAdapter(Context context, List<TableEntity> tableEntities) {
        mContext = context.getApplicationContext();
        datas = new FilterArrayList<>();
        this.datas.addAll(tableEntities);
        tableOrderMap = new HashMap<>();
        mScheduleMap = new HashMap<>();
        for (TableEntity tableEntity :
                datas) {
            int count = DBHelper.getInstance(mContext.getApplicationContext()).queryScheduleOrderCount(tableEntity.getTableId());
            mScheduleMap.put(tableEntity.getTableId(), count);
            if (tableEntity.getTableStatus() == 1) {
                OrderEntity orderEntity = DBHelper.getInstance(mContext.getApplicationContext()).queryFirstOrder(tableEntity.getTableId(), 0, 0);
                if (orderEntity != null) {
                    tableOrderMap.put(tableEntity.getTableId(), orderEntity);
                }
            }
        }
        mWeChatPayed = mContext.getResources().getDrawable(R.drawable.wechat_pay);
        mWeChatPayed.setBounds(0, 0, mWeChatPayed.getMinimumWidth(), mWeChatPayed.getMinimumHeight());
        mWeChatUnPay = mContext.getResources().getDrawable(R.drawable.wechat_unpay);
        mWeChatUnPay.setBounds(0, 0, mWeChatUnPay.getMinimumWidth(), mWeChatUnPay.getMinimumHeight());
        mStoreUnPay = mContext.getResources().getDrawable(R.drawable.store_unpay);
        mStoreUnPay.setBounds(0, 0, mStoreUnPay.getMinimumWidth(), mStoreUnPay.getMinimumHeight());
        mStorePay = mContext.getResources().getDrawable(R.drawable.store_pay);
        mStorePay.setBounds(0, 0, mStorePay.getMinimumWidth(), mStorePay.getMinimumHeight());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder0) {
            if (mScheduleMap.containsKey(datas.get(position).getTableId()) && mScheduleMap.get(datas.get(position).getTableId()) > 0) {
                ((ViewHolder0) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder0) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
            ((ViewHolder0) holder).tv0.setText(datas.get(position).getTableName());
            ((ViewHolder0) holder).tv1.setText(datas.get(position).getTableSeat() + "座");
            ((ViewHolder0) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewItemClickListener != null) {
                        mOnRecyclerViewItemClickListener.onItemClick((TableEntity) v.getTag());
                    }
                }
            });
        } else if (holder instanceof ViewHolder1) {
            try {
                OrderEntity orderEntity = DBHelper.getInstance(mContext.getApplicationContext()).queryFirstOrder(datas.get(position).getTableId(), 0, 0);
                ((ViewHolder1) holder).tv0.setText(datas.get(position).getTableName());
                ((ViewHolder1) holder).tv1.setText(orderEntity.getOrderGuests() + "人");
                if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                    //微信点餐
                    if (orderEntity.getSerialNumber() == null) {
                        //没有已下单商品
                        ((ViewHolder1) holder).tv2.setText("未下单");
                        ((ViewHolder1) holder).tv2.setCompoundDrawables(mWeChatUnPay, null, null, null);
                    } else {
                        //未支付
                        if (DBHelper.getInstance(mContext).isOrderPayOver(orderEntity.getOrderId())) {
                            //已经支付
                            ((ViewHolder1) holder).tv2.setText("已结账");
                            ((ViewHolder1) holder).tv2.setCompoundDrawables(mWeChatPayed, null, null, null);
                        } else {
                            //还需继续支付
                            ((ViewHolder1) holder).tv2.setText("待结账");
                            ((ViewHolder1) holder).tv2.setCompoundDrawables(mWeChatPayed, null, null, null);
                        }
                    }
                } else {
                    //到店点餐
                    if (orderEntity.getSerialNumber() == null) {
                        //没有已下单商品
                        ((ViewHolder1) holder).tv2.setText("未下单");
                        ((ViewHolder1) holder).tv2.setCompoundDrawables(mStoreUnPay, null, null, null);
                    } else {
                        //未支付
                        if (DBHelper.getInstance(mContext).isOrderPayOver(orderEntity.getOrderId())) {
                            ((ViewHolder1) holder).tv2.setText("已结账");
                            ((ViewHolder1) holder).tv2.setCompoundDrawables(mStorePay, null, null, null);
                        } else {
                            ((ViewHolder1) holder).tv2.setText("待结账");
                            ((ViewHolder1) holder).tv2.setCompoundDrawables(mStorePay, null, null, null);
                        }
                    }
                }
                String limitStr = "";
                if (orderEntity != null) {
                    if (orderEntity.getIsLimited() == 0) {
                        limitStr = "不限时";
                        ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.dark));
                    } else {
                        int time = (int) ((System.currentTimeMillis() - orderEntity.getOpenTime()) / 60000);
                        if (time < orderEntity.getRemindTime()) {
                            limitStr = time + "分钟";
                            ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.dark));
                        } else if (time >= orderEntity.getRemindTime() && time < orderEntity.getLimitedTime()) {
                            //提醒
                            limitStr = "剩余" + (orderEntity.getLimitedTime() - time) + "分钟";
                            ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.blue));
                        } else {
                            //超时
                            limitStr = "已超时";
                            ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.red));
                        }
                    }
                } else {
                    limitStr = "不限时";
                    ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.dark));
                }
                ((ViewHolder1) holder).tv3.setText(limitStr);

                //合台
                ((ViewHolder1) holder).ivJoinTable.setVisibility(DBHelper.getInstance(mContext).isJoinTable(datas.get(position).getTableId()) ? ImageView.VISIBLE : ImageView.GONE);

                if (mScheduleMap.containsKey(datas.get(position).getTableId()) && mScheduleMap.get(datas.get(position).getTableId()) > 0) {
                    //有预定
                    ((ViewHolder1) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
                } else {
                    ((ViewHolder1) holder).ivSchedule.setVisibility(ImageView.GONE);
                }
            } catch (Exception e) {
                ((ViewHolder1) holder).tv0.setText(datas.get(position).getTableName());
                ((ViewHolder1) holder).tv1.setText(datas.get(position).getTableSeat() + "人");
                ((ViewHolder1) holder).tv2.setText("未知");
                ((ViewHolder1) holder).tv2.setCompoundDrawables(null, null, null, null);
                ((ViewHolder1) holder).tv3.setTextColor(mContext.getResources().getColor(R.color.dark));
                ((ViewHolder1) holder).tv3.setText("不限时");
                //合台
                ((ViewHolder1) holder).ivJoinTable.setVisibility(ImageView.GONE);
                ((ViewHolder1) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
            ((ViewHolder1) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder1) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewItemClickListener != null) {
                        mOnRecyclerViewItemClickListener.onItemClick((TableEntity) v.getTag());
                    }
                }
            });
        } else if (holder instanceof ViewHolder3) {
            ((ViewHolder3) holder).tv0.setText(datas.get(position).getTableName());
            ((ViewHolder3) holder).tv1.setText(datas.get(position).getTableSeat() + "座");
            ((ViewHolder3) holder).tv2.setText("已结账");
            if (mScheduleMap.containsKey(datas.get(position).getTableId()) && mScheduleMap.get(datas.get(position).getTableId()) > 0) {
                //有预定
                ((ViewHolder3) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder3) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
            ((ViewHolder3) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder3) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewItemClickListener != null) {
                        mOnRecyclerViewItemClickListener.onItemClick((TableEntity) v.getTag());
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.table_item_layout_green, parent, false);
                viewHolder = new ViewHolder0(itemView);
                break;
            case 1:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.table_item_layout_red, parent, false);
                viewHolder = new ViewHolder1(itemView);
                break;
            case 3:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.table_item_layout_blue, parent, false);
                viewHolder = new ViewHolder3(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getTableStatus();
    }

    static class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1;
        private ImageView ivSchedule;
        private LinearLayout rootLayout;

        public ViewHolder0(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tv0 = (TextView) view.findViewById(R.id.tv_table_number);
            tv1 = (TextView) view.findViewById(R.id.tv_table_seat_count);
            ivSchedule = (ImageView) view.findViewById(R.id.iv_schedule);
        }
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1, tv2, tv3;
        private ImageView ivJoinTable;
        private ImageView ivSchedule;
        private LinearLayout rootLayout;

        public ViewHolder1(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tv0 = (TextView) view.findViewById(R.id.tv_table_number);
            tv1 = (TextView) view.findViewById(R.id.tv_table_people_count);
            tv2 = (TextView) view.findViewById(R.id.tv_table_money);
            tv3 = (TextView) view.findViewById(R.id.tv_table_time);
            ivJoinTable = (ImageView) view.findViewById(R.id.iv_join_table);
            ivSchedule = (ImageView) view.findViewById(R.id.iv_schedule);
        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1, tv2;
        private ImageView ivSchedule;
        private LinearLayout rootLayout;

        public ViewHolder3(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tv0 = (TextView) view.findViewById(R.id.tv_table_number);
            tv1 = (TextView) view.findViewById(R.id.tv_table_status);
            tv2 = (TextView) view.findViewById(R.id.tv_table_seat_count);
            ivSchedule = (ImageView) view.findViewById(R.id.iv_schedule);
        }
    }

    public void updateItem(TableEntity tableEntity) {
        if (tableEntity.getTableStatus() == 1) {
            OrderEntity orderEntity = DBHelper.getInstance(mContext.getApplicationContext()).queryFirstOrder(tableEntity.getTableId(), 0, 0);
            if (orderEntity != null) {
                tableOrderMap.put(tableEntity.getTableId(), orderEntity);
            } else {
                tableOrderMap.remove(tableEntity.getTableId());
            }
        } else {
            tableOrderMap.remove(tableEntity.getTableId());
        }
    }

    public void update(List<TableEntity> tableEntities) {
        datas.clear();
        datas.addAll(tableEntities);
        tableOrderMap.clear();
        mScheduleMap.clear();
        for (TableEntity tableEntity :
                datas) {
            int count = DBHelper.getInstance(mContext.getApplicationContext()).queryScheduleOrderCount(tableEntity.getTableId());
            mScheduleMap.put(tableEntity.getTableId(), count);
            if (tableEntity.getTableStatus() == 1) {
                OrderEntity orderEntity = DBHelper.getInstance(mContext.getApplicationContext()).queryFirstOrder(tableEntity.getTableId(), 0, 0);
                if (orderEntity != null) {
                    tableOrderMap.put(tableEntity.getTableId(), orderEntity);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(TableEntity tableEntity);
    }
}
