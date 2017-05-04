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
import jufeng.juyancash.dao.DishPracticeEntity;
import jufeng.juyancash.dao.DishSpecifyEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupDishEntity;
import jufeng.juyancash.dao.TaocanGroupEntity;

/**
 * Created by 15157_000 on 2016/6/23 0023.
 */
public class TaocanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static final int LISTHEADER = 0;
    public static final int LISTITEM = 1;
    private OrderDishEntity mOrderDishEntity;
    private ArrayList<Object> mDatas;
    private OnTaoCanListItemClickListener mOnTaoCanListItemClickListener;
    private int mStatus;
    private Map<String, Integer> allDishMap;
    private Map<String, Integer> checkDishMap;
    private ArrayList<OrderTaocanGroupDishEntity> mOrderTaocanGroupDishEntities;//已点菜品
    private Map<String, OrderTaocanGroupDishEntity> mOrderTaocanGroupDishEntityMap;//已点菜品map
    private Map<String, Integer> groupMap;//分组map
    private Map<String, TaocanGroupDishEntity> mTaocanGroupDishEntityMap;//菜品map

    public TaocanListAdapter(Context context, OrderDishEntity orderDishEntity, int status) {
        this.mContext = context.getApplicationContext();
        this.mStatus = status;
        this.mOrderDishEntity = orderDishEntity;
        mDatas = new ArrayList<>();
        mOrderTaocanGroupDishEntities = new ArrayList<>();
        allDishMap = new HashMap<>();
        checkDishMap = new HashMap<>();
        groupMap = new HashMap<>();
        mTaocanGroupDishEntityMap = new HashMap<>();
        mOrderTaocanGroupDishEntityMap = new HashMap<>();
        initData();
    }

    public void updateData(OrderDishEntity orderDishEntity, int status) {
        this.mStatus = status;
        this.mOrderDishEntity = orderDishEntity;
        mDatas.clear();
        mOrderTaocanGroupDishEntities.clear();
        allDishMap.clear();
        checkDishMap.clear();
        groupMap.clear();
        mTaocanGroupDishEntityMap.clear();
        mOrderTaocanGroupDishEntityMap.clear();
        initData();
        notifyDataSetChanged();
    }

    //删除套餐内商品
    public void deleteItem(String orderedTaocanGroupDishId) {
        OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = mOrderTaocanGroupDishEntityMap.get(orderedTaocanGroupDishId);
        mOrderTaocanGroupDishEntities.remove(orderTaocanGroupDishEntity);
        mOrderTaocanGroupDishEntityMap.remove(orderedTaocanGroupDishId);
        int currentCount = checkDishMap.get(orderTaocanGroupDishEntity.getTaocanGroupDishId()) - orderTaocanGroupDishEntity.getTaocanGroupDishCount();
        checkDishMap.put(orderTaocanGroupDishEntity.getTaocanGroupDishId(), currentCount < 0 ? 0 : currentCount);
        currentCount = groupMap.get(orderTaocanGroupDishEntity.getTaocanGroupId()) - orderTaocanGroupDishEntity.getTaocanGroupDishCount();
        groupMap.put(orderTaocanGroupDishEntity.getTaocanGroupId(), currentCount < 0 ? 0 : currentCount);
        int groupDishPosition = allDishMap.get(orderTaocanGroupDishEntity.getTaocanGroupDishId());
        int groupPosition = allDishMap.get(orderTaocanGroupDishEntity.getTaocanGroupId());
        notifyItemChanged(groupPosition);
        notifyItemChanged(groupDishPosition);
    }

    private void initData() {
        ArrayList<TaocanGroupEntity> mTaocanGroupEntities = new ArrayList<>();
        if(mOrderDishEntity != null) {
            mTaocanGroupEntities.addAll(DBHelper.getInstance(mContext).queryTaocanGroup(mOrderDishEntity.getDishId()));
        }
        for (TaocanGroupEntity taocanGroupEntity :
                mTaocanGroupEntities) {
            groupMap.put(taocanGroupEntity.getTaocanGroupId(), 0);
            mDatas.add(taocanGroupEntity);
            mDatas.addAll(DBHelper.getInstance(mContext).queryTaocanGroupDish(taocanGroupEntity.getTaocanGroupId()));
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i) instanceof TaocanGroupEntity) {
                allDishMap.put(((TaocanGroupEntity) mDatas.get(i)).getTaocanGroupId(), i);
            } else if (mDatas.get(i) instanceof TaocanGroupDishEntity) {
                allDishMap.put(((TaocanGroupDishEntity) mDatas.get(i)).getTaocanGroupDishId(), i);
                mTaocanGroupDishEntityMap.put(((TaocanGroupDishEntity) mDatas.get(i)).getTaocanGroupDishId(), (TaocanGroupDishEntity) mDatas.get(i));
            }
        }
        if (mStatus == 0) {
            mOrderTaocanGroupDishEntities.addAll(DBHelper.getInstance(mContext).getOrderedTaocanDish(mOrderDishEntity));
        } else {
            mOrderTaocanGroupDishEntities.addAll(DBHelper.getInstance(mContext).getHadOrderedTaocanDish(mOrderDishEntity));
        }
        for (int i = 0; i < mOrderTaocanGroupDishEntities.size(); i++) {
            String groupDishId = mOrderTaocanGroupDishEntities.get(i).getTaocanGroupDishId();
            String groupId = mOrderTaocanGroupDishEntities.get(i).getTaocanGroupId();
            if (checkDishMap.containsKey(groupDishId)) {
                checkDishMap.put(groupDishId, checkDishMap.get(groupDishId) + mOrderTaocanGroupDishEntities.get(i).getTaocanGroupDishCount());
            } else {
                checkDishMap.put(groupDishId, mOrderTaocanGroupDishEntities.get(i).getTaocanGroupDishCount());
            }
            if (groupMap.containsKey(groupId)) {
                groupMap.put(groupId, groupMap.get(groupId) + mOrderTaocanGroupDishEntities.get(i).getTaocanGroupDishCount());
            } else {
                groupMap.put(groupId, mOrderTaocanGroupDishEntities.get(i).getTaocanGroupDishCount());
            }
            mOrderTaocanGroupDishEntityMap.put(mOrderTaocanGroupDishEntities.get(i).getOrderTaocanGroupDishId(), mOrderTaocanGroupDishEntities.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position) instanceof TaocanGroupEntity) {
            return LISTHEADER;
        } else {
            return LISTITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder0) {
            //设置已点数量和可点数量
            TaocanGroupEntity taocanGroupEntity = (TaocanGroupEntity) mDatas.get(position);
            int canSelectCount, selectedCount;
            selectedCount = groupMap.get(taocanGroupEntity.getTaocanGroupId());
            if (taocanGroupEntity.getSelectMode() == 0) {
                //允许用户自选
                canSelectCount = taocanGroupEntity.getSelectCount();//可点数量
                ((ViewHolder0) holder).tvSelectCount.setText("已点" + selectedCount + "/可点" + canSelectCount);
            } else {
                //必须全部选定
                canSelectCount = DBHelper.getInstance(mContext).getMustTaocanGroupDishCount(taocanGroupEntity.getTaocanGroupId());
                ((ViewHolder0) holder).tvSelectCount.setText("已点" + selectedCount + "/必点" + canSelectCount);
            }
            ((ViewHolder0) holder).ivOver.setVisibility(selectedCount == canSelectCount ? ImageView.VISIBLE : ImageView.GONE);
            ((ViewHolder0) holder).tvName.setText(((TaocanGroupEntity) mDatas.get(position)).getTaocanGroupName());
        } else if (holder instanceof ViewHolder1) {
            TaocanGroupDishEntity taocanGroupDishEntity = (TaocanGroupDishEntity) mDatas.get(position);
            final TaocanGroupEntity taocanGroupEntity = DBHelper.getInstance(mContext).getTaocanGroupById(taocanGroupDishEntity.getTaocanGroupId());
            if (taocanGroupEntity != null) {
                //设置商品名称
                ((ViewHolder1) holder).tvName.setText(DBHelper.getInstance(mContext).getDishNameByDishId(taocanGroupDishEntity.getDishId()));
                //设置商品规格
                if (taocanGroupDishEntity.getSpecifyId() != null) {
                    DishSpecifyEntity dishSpecifyEntity = DBHelper.getInstance(mContext).getDishSpecifyById(taocanGroupDishEntity.getSpecifyId());
                    if (dishSpecifyEntity != null) {
                        ((ViewHolder1) holder).tvSpecify.setText(DBHelper.getInstance(mContext).getSpecifyName(dishSpecifyEntity.getSpecifyId()));
                    } else {
                        ((ViewHolder1) holder).tvSpecify.setText("");
                    }
                } else {
                    ((ViewHolder1) holder).tvSpecify.setText("");
                }
                //设置是否有配置选项
                ArrayList<DishPracticeEntity> dishPracticeEntities = new ArrayList<>();
                dishPracticeEntities.addAll(DBHelper.getInstance(mContext).queryAllPractice(taocanGroupDishEntity.getDishId()));
                if (dishPracticeEntities.size() > 0) {
                    //有做法
                    ((ViewHolder1) holder).ivConfig.setVisibility(ImageView.VISIBLE);
                    ((ViewHolder1) holder).ivConfig.setImageResource(R.drawable.btn_config);
                } else {
                    ((ViewHolder1) holder).ivConfig.setVisibility(ImageView.GONE);
                }
                //设置商品加价
                final boolean isDishChing = DBHelper.getInstance(mContext).isDishChing(taocanGroupDishEntity.getDishId());
                if (isDishChing) {
                    //估清
                    ((ViewHolder1) holder).tvExtraPrice.setTextSize(18);
                    ((ViewHolder1) holder).tvExtraPrice.setText("已售完");
                } else {
                    ((ViewHolder1) holder).tvExtraPrice.setTextSize(16);
                    if (taocanGroupDishEntity.getIncreasePrice() > 0) {
                        ((ViewHolder1) holder).tvExtraPrice.setText("+" + taocanGroupDishEntity.getIncreasePrice() + "元");
                    } else {
                        ((ViewHolder1) holder).tvExtraPrice.setText("不加价");
                    }
                }
                ((ViewHolder1) holder).mLinearLayout.setTag(taocanGroupDishEntity.getTaocanGroupDishId());
                ((ViewHolder1) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOrderDishEntity.getIsPrint() != null && mOrderDishEntity.getIsPrint() == 1) {
                            return;
                        }
                        if (taocanGroupEntity.getSelectMode() == 0) {
                            //允许自选
                            int count1 = groupMap.get(taocanGroupEntity.getTaocanGroupId());
                            if (count1 < taocanGroupEntity.getSelectCount() && !isDishChing) {
                                if (mStatus == 0) {
                                    String taocanGroupDishId = (String) v.getTag();
                                    OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = DBHelper.getInstance(mContext).insertSnackTaocanDish(mOrderDishEntity.getOrderId(), mOrderDishEntity.getOrderDishId(), mOrderDishEntity.getDishId(), mTaocanGroupDishEntityMap.get(taocanGroupDishId), null, 1, -1);
                                    mOnTaoCanListItemClickListener.onDishClick(orderTaocanGroupDishEntity);
                                    mOrderTaocanGroupDishEntities.add(orderTaocanGroupDishEntity);
                                    mOrderTaocanGroupDishEntityMap.put(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId(), orderTaocanGroupDishEntity);
                                    if (checkDishMap.containsKey(orderTaocanGroupDishEntity.getTaocanGroupDishId())) {
                                        checkDishMap.put(orderTaocanGroupDishEntity.getTaocanGroupDishId(), checkDishMap.get(orderTaocanGroupDishEntity.getTaocanGroupDishId()) + orderTaocanGroupDishEntity.getTaocanGroupDishCount());
                                    } else {
                                        checkDishMap.put(orderTaocanGroupDishEntity.getTaocanGroupDishId(), orderTaocanGroupDishEntity.getTaocanGroupDishCount());
                                    }
                                    if (groupMap.containsKey(orderTaocanGroupDishEntity.getTaocanGroupId())) {
                                        groupMap.put(orderTaocanGroupDishEntity.getTaocanGroupId(), groupMap.get(orderTaocanGroupDishEntity.getTaocanGroupId()) + orderTaocanGroupDishEntity.getTaocanGroupDishCount());
                                    } else {
                                        groupMap.put(orderTaocanGroupDishEntity.getTaocanGroupId(), orderTaocanGroupDishEntity.getTaocanGroupDishCount());
                                    }
                                    notifyItemChanged(allDishMap.get(taocanGroupDishId));
                                    notifyItemChanged(allDishMap.get(mTaocanGroupDishEntityMap.get(taocanGroupDishId).getTaocanGroupId()));
                                }
                            }
                        }
                    }
                });
                //设置商品数量
                int count = 0;
                if (checkDishMap.containsKey(taocanGroupDishEntity.getTaocanGroupDishId())) {
                    count = checkDishMap.get(taocanGroupDishEntity.getTaocanGroupDishId());
                }
                if (count > 0) {
                    ((ViewHolder1) holder).tvCount.setText("已点：" + count);
                    ((ViewHolder1) holder).tvCount.setVisibility(TextView.VISIBLE);
//                    ((ViewHolder1) holder).mLinearLayout.setClickable(false);
                } else {
                    ((ViewHolder1) holder).tvCount.setText("");
                    ((ViewHolder1) holder).tvCount.setVisibility(TextView.GONE);
//                    ((ViewHolder1) holder).mLinearLayout.setClickable(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LISTHEADER) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.taocan_item_layout_0, parent, false);
            return new ViewHolder0(mView);
        } else {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.taocan_item_layout_1, parent, false);
            return new ViewHolder1(mView);
        }
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivOver;
        private TextView tvSelectCount;

        public ViewHolder0(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivOver = (ImageView) itemView.findViewById(R.id.iv_over);
            tvSelectCount = (TextView) itemView.findViewById(R.id.tv_select_count);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvName, tvSpecify, tvExtraPrice, tvCount;
        private LinearLayout mLinearLayout;
        private ImageView ivConfig;

        public ViewHolder1(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_dish_name);
            tvSpecify = (TextView) itemView.findViewById(R.id.tv_guige);
            tvExtraPrice = (TextView) itemView.findViewById(R.id.tv_extra_price);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tvCount = (TextView) itemView.findViewById(R.id.tv_dish_ordered_count);
            ivConfig = (ImageView) itemView.findViewById(R.id.iv_dish_type);
        }
    }

    public void setOnTaocanClickListener(OnTaoCanListItemClickListener listener) {
        this.mOnTaoCanListItemClickListener = listener;
    }

    public interface OnTaoCanListItemClickListener {
        void onDishClick(OrderTaocanGroupDishEntity orderTaocanGroupDish);
    }

    public void changeAddItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        String taocanGroupDishId = orderTaocanGroupDishEntity.getTaocanGroupDishId();
        String taocanGroupId = orderTaocanGroupDishEntity.getTaocanGroupId();
        if (checkDishMap.containsKey(taocanGroupDishId)) {
            checkDishMap.put(taocanGroupDishId, checkDishMap.get(taocanGroupDishId) + 1);
        } else {
            checkDishMap.put(taocanGroupDishId, orderTaocanGroupDishEntity.getTaocanGroupDishCount());
        }
        if (groupMap.containsKey(taocanGroupId)) {
            groupMap.put(taocanGroupId, groupMap.get(taocanGroupId) + 1);
        } else {
            groupMap.put(taocanGroupId, orderTaocanGroupDishEntity.getTaocanGroupDishCount());
        }
        if (mOrderTaocanGroupDishEntityMap.containsKey(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId())) {
            int position = mOrderTaocanGroupDishEntities.indexOf(mOrderTaocanGroupDishEntityMap.get(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId()));
            mOrderTaocanGroupDishEntities.remove(position);
            mOrderTaocanGroupDishEntities.add(position, orderTaocanGroupDishEntity);
            mOrderTaocanGroupDishEntityMap.put(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId(), orderTaocanGroupDishEntity);
        } else {
            mOrderTaocanGroupDishEntities.add(orderTaocanGroupDishEntity);
            mOrderTaocanGroupDishEntityMap.put(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId(), orderTaocanGroupDishEntity);
        }
        notifyItemChanged(allDishMap.get(taocanGroupId));
        notifyItemChanged(allDishMap.get(taocanGroupDishId));

    }

    public void changeReduceItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        String taocanGroupDishId = orderTaocanGroupDishEntity.getTaocanGroupDishId();
        String taocanGroupId = orderTaocanGroupDishEntity.getTaocanGroupId();
        if (checkDishMap.containsKey(taocanGroupDishId)) {
            checkDishMap.put(taocanGroupDishId, checkDishMap.get(taocanGroupDishId) - 1);
        } else {
            checkDishMap.put(taocanGroupDishId, orderTaocanGroupDishEntity.getTaocanGroupDishCount());
        }
        if (groupMap.containsKey(taocanGroupId)) {
            groupMap.put(taocanGroupId, groupMap.get(taocanGroupId) - 1);
        } else {
            groupMap.put(taocanGroupId, orderTaocanGroupDishEntity.getTaocanGroupDishCount());
        }
        if (mOrderTaocanGroupDishEntityMap.containsKey(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId())) {
            int position = mOrderTaocanGroupDishEntities.indexOf(mOrderTaocanGroupDishEntityMap.get(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId()));
            mOrderTaocanGroupDishEntities.remove(position);
            mOrderTaocanGroupDishEntities.add(position, orderTaocanGroupDishEntity);
            mOrderTaocanGroupDishEntityMap.put(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId(), orderTaocanGroupDishEntity);
        } else {
            mOrderTaocanGroupDishEntities.add(orderTaocanGroupDishEntity);
            mOrderTaocanGroupDishEntityMap.put(orderTaocanGroupDishEntity.getOrderTaocanGroupDishId(), orderTaocanGroupDishEntity);
        }
        notifyItemChanged(allDishMap.get(taocanGroupId));
        notifyItemChanged(allDishMap.get(taocanGroupDishId));
    }

    public void deleteItem(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        String taocanGroupDishId = orderTaocanGroupDishEntity.getTaocanGroupDishId();
        String taocanGroupId = orderTaocanGroupDishEntity.getTaocanGroupId();
        String orderTaocanGroupDishId = orderTaocanGroupDishEntity.getOrderTaocanGroupDishId();
        if (checkDishMap.containsKey(taocanGroupDishId)) {
            checkDishMap.remove(taocanGroupDishId);
        }
        if (groupMap.containsKey(taocanGroupId)) {
            groupMap.put(taocanGroupId, groupMap.get(taocanGroupId) - orderTaocanGroupDishEntity.getTaocanGroupDishCount());
        }
        if (mOrderTaocanGroupDishEntityMap.containsKey(orderTaocanGroupDishId)) {
            int position = mOrderTaocanGroupDishEntities.indexOf(mOrderTaocanGroupDishEntityMap.get(orderTaocanGroupDishId));
            mOrderTaocanGroupDishEntities.remove(position);
            mOrderTaocanGroupDishEntityMap.remove(orderTaocanGroupDishId);
        }
        notifyItemChanged(allDishMap.get(taocanGroupId));
        notifyItemChanged(allDishMap.get(taocanGroupDishId));
    }
}
