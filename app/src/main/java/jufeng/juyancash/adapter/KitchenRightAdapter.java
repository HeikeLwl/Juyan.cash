package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PrinterFailedHistoryEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/9/8.
 */
public class KitchenRightAdapter extends RecyclerView.Adapter<KitchenRightAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PrinterFailedHistoryEntity> mPrinterFailedHistoryEntities;
    private String selectedPrintKitchenHistoryId;
    private OnKitchenRightItemClickListener mOnKitchenRightItemClickListener;
    private Drawable mDrawable0,mDrawable1,mDrawable2,mDrawable3;
    private Map<String,Integer> mAllMap;

    public KitchenRightAdapter(Context context, ArrayList<PrinterFailedHistoryEntity> printerFailedHistoryEntities, Map<String,Integer> allMap) {
        this.mContext = context;
        mPrinterFailedHistoryEntities = new ArrayList<>();
        mAllMap = new HashMap<>();
        mPrinterFailedHistoryEntities.addAll(printerFailedHistoryEntities);
        mAllMap.putAll(allMap);
        mDrawable0 = context.getResources().getDrawable(R.drawable.add_dish);
        mDrawable0.setBounds(0,0,mDrawable0.getMinimumWidth(),mDrawable0.getMinimumHeight());
        mDrawable1 = context.getResources().getDrawable(R.drawable.retreat_dish);
        mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
        mDrawable2 = context.getResources().getDrawable(R.drawable.remind_dish);
        mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
        mDrawable3 = context.getResources().getDrawable(R.drawable.print_dish);
        mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kitchen_right_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PrinterFailedHistoryEntity printerFailedHistoryEntity = mPrinterFailedHistoryEntities.get(position);
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(printerFailedHistoryEntity.getOrderId());
        String tableName = null;
        String orderNumber = "";
        if(orderEntity != null){
            if(orderEntity.getOrderType() == 1){
                tableName = "外卖";
            }else{
                tableName = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
            }
            orderNumber = "NO."+orderEntity.getOrderNumber1();
        }

        holder.tvTableName.setText(tableName);
        holder.tvTime.setText(CustomMethod.parseTime(printerFailedHistoryEntity.getPrintTime(), "yyyy-MM-dd HH:mm"));
        holder.tvOrderNumber.setText(orderNumber);

        String printType = "";
        switch (printerFailedHistoryEntity.getPrintType()){
            case 0:
                holder.tvDishTypeName.setCompoundDrawables(mDrawable3,null,null,null);
                printType = "配菜单";
                break;
            case 1:
                holder.tvDishTypeName.setCompoundDrawables(mDrawable0,null,null,null);
                printType = "配菜单（加菜）";
                break;
            case 2:
                holder.tvDishTypeName.setCompoundDrawables(mDrawable1,null,null,null);
                printType = "退菜单";
                break;
            case 3:
                holder.tvDishTypeName.setCompoundDrawables(mDrawable2,null,null,null);
                printType = "催菜单";
                break;
        }
        holder.tvDishTypeName.setText(printType);
        holder.tvDishList.setText(printerFailedHistoryEntity.getPrintDishStr());

        if (printerFailedHistoryEntity.getPrinterFailedHistoryId().equals(selectedPrintKitchenHistoryId)) {
            holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.graydark));
        } else {
            holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        holder.rootLayout.setTag(printerFailedHistoryEntity.getPrinterFailedHistoryId());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printHistoryId = (String) v.getTag();
                int oldPosition = -1;
                if(selectedPrintKitchenHistoryId != null && mAllMap.containsKey(selectedPrintKitchenHistoryId)){
                    oldPosition = mAllMap.get(selectedPrintKitchenHistoryId);
                }
                if (printHistoryId.equals(selectedPrintKitchenHistoryId)) {
                    selectedPrintKitchenHistoryId = null;
                } else {
                    selectedPrintKitchenHistoryId = printHistoryId;
                }
                mOnKitchenRightItemClickListener.onKitchenRightItemClick(selectedPrintKitchenHistoryId);
                if(mAllMap.containsKey(printHistoryId)) {
                    int position = mAllMap.get(printHistoryId);
                    if(oldPosition >= 0){
                        notifyItemChanged(oldPosition);
                    }
                    notifyItemChanged(position);
                }else {
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPrinterFailedHistoryEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootLayout;
        private TextView tvTime, tvTableName, tvOrderNumber, tvDishTypeName, tvDishList;

        public ViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvTableName = (TextView) view.findViewById(R.id.tv_table_name);
            tvOrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
            tvDishTypeName = (TextView) view.findViewById(R.id.tv_dish_type_name);
            tvDishList = (TextView) view.findViewById(R.id.tv_dish_list);
        }
    }

    public void updateData(ArrayList<PrinterFailedHistoryEntity> printerFailedHistoryEntities,Map<String,Integer> allMap) {
        selectedPrintKitchenHistoryId = null;
        mPrinterFailedHistoryEntities.clear();
        mAllMap.clear();
        mPrinterFailedHistoryEntities.addAll(printerFailedHistoryEntities);
        mAllMap.putAll(allMap);
        notifyDataSetChanged();
    }

    public void setOnKitchenRightItemClickListener(OnKitchenRightItemClickListener listener) {
        this.mOnKitchenRightItemClickListener = listener;
    }

    public interface OnKitchenRightItemClickListener {
        void onKitchenRightItemClick(String printHistoryId);
    }
}
