package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.util.FilterArrayList;
import jufeng.juyancash.util.IObjectFilter;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class SelectTableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static final int SELECTSINGLE = 0;
    public static final int SELECTMULTIPLE = 1;
    private int selectMode;
    private FilterArrayList<TableEntity> datas;
    private int tableStatus = -1;
    private ArrayList<TableEntity> mSelectedTables;
    private OnTableClickListener mOnTableClickListener;
    private Map<String, Integer> tableScheduleMap;
    IObjectFilter filter = new IObjectFilter() {
        public boolean filter(Object object) {
            TableEntity tableEntity = (TableEntity) object;
            return tableEntity.getTableStatus() == tableStatus;
        }
    };

    public SelectTableListAdapter(Context context, String areaId, int tableStatus, int selectedMode, ArrayList<TableEntity> selectedTables, String oldTableId) {
        this.mContext = context;
        this.tableStatus = tableStatus;
        this.selectMode = selectedMode;
        datas = new FilterArrayList<>();
        this.mSelectedTables = new ArrayList<>();
        tableScheduleMap = new HashMap<>();
        mSelectedTables.addAll(selectedTables);
        initData(areaId, oldTableId);
    }

    public void updateData(String areaId, int tableStatus, int selectedMode, ArrayList<TableEntity> selectedTables, String oldTableId){
        this.tableStatus = tableStatus;
        this.selectMode = selectedMode;
        mSelectedTables.clear();
        mSelectedTables.addAll(selectedTables);
        initData(areaId, oldTableId);
        notifyDataSetChanged();
    }

    private void initData(String areaId, String oldTableId) {
        tableScheduleMap.clear();
        datas.clear();
        if (areaId != null && oldTableId == null) {
            datas.addAll(DBHelper.getInstance(mContext).queryAllTableData(areaId));
            datas.setFilter(null);
        } else if(areaId != null){
            datas.addAll(DBHelper.getInstance(mContext).queryTableData(areaId, oldTableId));
            datas.setFilter(filter);
        }
        for (TableEntity tableEntity :
                datas) {
            tableScheduleMap.put(tableEntity.getTableId(), DBHelper.getInstance(mContext).queryOrderData(tableEntity.getTableId(), 0, 2).size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(mContext).inflate(R.layout.select_table_item_layout_green, parent, false);
                ViewHolder0 holder0 = new ViewHolder0(view);
                return holder0;
            case 1:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.select_table_item_layout_red, parent, false);
                ViewHolder1 holder1 = new ViewHolder1(view1);
                return holder1;
            case 2:
                View view2 = LayoutInflater.from(mContext).inflate(R.layout.select_table_item_layout_orange, parent, false);
                ViewHolder2 holder2 = new ViewHolder2(view2);
                return holder2;
            case 3:
                View view3 = LayoutInflater.from(mContext).inflate(R.layout.select_table_item_layout_blue, parent, false);
                ViewHolder3 holder3 = new ViewHolder3(view3);
                return holder3;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {//空闲状态
            ((ViewHolder0) holder).tv0.setText(datas.get(position).getTableName());
            ((ViewHolder0) holder).tv1.setText(datas.get(position).getTableSeat() + "座");
            ((ViewHolder0) holder).ivSelected.setVisibility(isSelected(datas.get(position)) ? ImageView.VISIBLE : ImageView.GONE);
            ((ViewHolder0) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableEntity tableEntity = (TableEntity) v.getTag();
                    mOnTableClickListener.onTableClick(selectMode, tableEntity);
                }
            });
            if (tableScheduleMap.containsKey(datas.get(position).getTableId()) && tableScheduleMap.get(datas.get(position).getTableId()) > 0) {
                ((ViewHolder0) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder0) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
        } else if (holder instanceof ViewHolder1) {//使用中
            ((ViewHolder1) holder).tv0.setText(datas.get(position).getTableName());
            ((ViewHolder1) holder).tv1.setText(datas.get(position).getTableSeat() + "座");
            ((ViewHolder1) holder).ivSelected.setVisibility(isSelected(datas.get(position)) ? ImageView.VISIBLE : ImageView.GONE);
            ((ViewHolder1) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder1) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableEntity tableEntity = (TableEntity) v.getTag();
                    mOnTableClickListener.onTableClick(selectMode, tableEntity);
                }
            });
            if (tableScheduleMap.containsKey(datas.get(position).getTableId()) && tableScheduleMap.get(datas.get(position).getTableId()) > 0) {
                ((ViewHolder1) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder1) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
        } else if (holder instanceof ViewHolder2) {
            ((ViewHolder2) holder).tv0.setText(datas.get(position).getTableName());
            ArrayList<ScheduleEntity> scheduleEntities = DBHelper.getInstance(mContext).queryScheduleData(datas.get(position).getTableId(), 1);
            ((ViewHolder2) holder).tv1.setText(scheduleEntities.get(0).getMealPeople() + "人");
            ((ViewHolder2) holder).tv2.setText(scheduleEntities.get(0).getGuestPhone());
            ((ViewHolder2) holder).tv3.setText(scheduleEntities.get(0).getIsOrdered() == 0 ? "未点菜" : "已点菜");
            ((ViewHolder2) holder).ivSelected.setVisibility(isSelected(datas.get(position)) ? ImageView.VISIBLE : ImageView.GONE);
            ((ViewHolder2) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder2) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableEntity tableEntity = (TableEntity) v.getTag();
                    mOnTableClickListener.onTableClick(selectMode, tableEntity);
                }
            });
            if (tableScheduleMap.containsKey(datas.get(position).getTableId()) && tableScheduleMap.get(datas.get(position).getTableId()) > 0) {
                ((ViewHolder2) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder2) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
        } else if (holder instanceof ViewHolder3) {
            ((ViewHolder3) holder).tv0.setText(datas.get(position).getTableName());
            ((ViewHolder3) holder).tv1.setText(datas.get(position).getTableSeat() + "座");
            ((ViewHolder3) holder).ivSelected.setVisibility(isSelected(datas.get(position)) ? ImageView.VISIBLE : ImageView.GONE);
            ((ViewHolder3) holder).rootLayout.setTag(datas.get(position));
            ((ViewHolder3) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableEntity tableEntity = (TableEntity) v.getTag();
                    mOnTableClickListener.onTableClick(selectMode, tableEntity);
                }
            });
            if (tableScheduleMap.containsKey(datas.get(position).getTableId()) && tableScheduleMap.get(datas.get(position).getTableId()) > 0) {
                ((ViewHolder3) holder).ivSchedule.setVisibility(ImageView.VISIBLE);
            } else {
                ((ViewHolder3) holder).ivSchedule.setVisibility(ImageView.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getTableStatus();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1;
        private ImageView ivSelected, ivSchedule;
        private CardView rootLayout;

        public ViewHolder0(View itemView) {
            super(itemView);
            rootLayout = (CardView) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_table_number);
            tv1 = (TextView) itemView.findViewById(R.id.tv_table_seat_count);
            ivSchedule = (ImageView) itemView.findViewById(R.id.iv_schedule);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1;
        private ImageView ivSelected, ivSchedule;
        private CardView rootLayout;

        public ViewHolder1(View itemView) {
            super(itemView);
            rootLayout = (CardView) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_table_number);
            tv1 = (TextView) itemView.findViewById(R.id.tv_table_seat_count);
            ivSchedule = (ImageView) itemView.findViewById(R.id.iv_schedule);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1, tv2, tv3;
        private ImageView ivSelected, ivSchedule;
        private RelativeLayout rootLayout;

        public ViewHolder2(View itemView) {
            super(itemView);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_table_number);
            tv1 = (TextView) itemView.findViewById(R.id.tv_table_people_count);
            tv2 = (TextView) itemView.findViewById(R.id.tv_table_phone);
            tv3 = (TextView) itemView.findViewById(R.id.tv_table_status);
            ivSchedule = (ImageView) itemView.findViewById(R.id.iv_schedule);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    class ViewHolder3 extends RecyclerView.ViewHolder {
        private TextView tv0, tv1;
        private ImageView ivSelected, ivSchedule;
        private CardView rootLayout;

        public ViewHolder3(View itemView) {
            super(itemView);
            rootLayout = (CardView) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_table_number);
            tv1 = (TextView) itemView.findViewById(R.id.tv_table_seat_count);
            ivSchedule = (ImageView) itemView.findViewById(R.id.iv_schedule);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }

    //判断桌位是否有选中
    public boolean isSelected(TableEntity tableEntity) {
        for (int i = 0; i < mSelectedTables.size(); i++) {
            if (tableEntity.getTableId().equals(mSelectedTables.get(i).getTableId())) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    public void changeAreaData(String areaId, String oldTableId) {
        initData(areaId, oldTableId);
        notifyDataSetChanged();
    }

    public void setOnTableClickListener(OnTableClickListener listener) {
        this.mOnTableClickListener = listener;
    }

    public interface OnTableClickListener {
        void onTableClick(int operationType, TableEntity tableEntity);
    }

    public void updateData(ArrayList<TableEntity> selectedTables) {
        this.mSelectedTables.clear();
        this.mSelectedTables.addAll(selectedTables);
        notifyDataSetChanged();
    }
}
