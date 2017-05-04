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

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.PrintKitchenEntity;

/**
 * Created by Administrator102 on 2016/9/8.
 */
public class KitchenLeftAdapter extends RecyclerView.Adapter<KitchenLeftAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PrintKitchenEntity> mPrintKitchenEntities;
    private OnKitchenLeftItemClickListener mOnKitchenLeftItemClickListener;
    private Drawable mDrawable0, mDrawable1;
    private String selectedPrintKitchenId;

    public KitchenLeftAdapter(Context context) {
        this.mContext = context;
        mPrintKitchenEntities = new ArrayList<>();
        PrintKitchenEntity printKitchenEntity = new PrintKitchenEntity();
        printKitchenEntity.setPrintKitchenId(null);
        printKitchenEntity.setPrintKitchenName("全部");
        mPrintKitchenEntities.add(printKitchenEntity);
        mPrintKitchenEntities.addAll(DBHelper.getInstance(mContext).getAllKichenPrinter());
        mDrawable0 = mContext.getResources().getDrawable(R.drawable.connect);
        mDrawable1 = mContext.getResources().getDrawable(R.drawable.unconnect);
        mDrawable0.setBounds(0, 0, mDrawable0.getMinimumWidth(), mDrawable0.getMinimumHeight());
        mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(), mDrawable1.getMinimumHeight());
        selectedPrintKitchenId = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kitchen_left_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PrintKitchenEntity printKitchenEntity = mPrintKitchenEntities.get(position);
        if(printKitchenEntity.getPrintKitchenId() == null){
            holder.tvName.setText(printKitchenEntity.getPrintKitchenName());
            holder.tvConnect.setVisibility(TextView.GONE);
            holder.layout0.setVisibility(LinearLayout.GONE);
            holder.layout1.setVisibility(LinearLayout.GONE);
            holder.layout2.setVisibility(LinearLayout.GONE);
            holder.layout3.setVisibility(LinearLayout.GONE);
            if (selectedPrintKitchenId == null) {
                holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.graydark));
            } else {
                holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }else{
            holder.tvConnect.setVisibility(TextView.VISIBLE);
            holder.layout0.setVisibility(LinearLayout.VISIBLE);
            holder.layout1.setVisibility(LinearLayout.VISIBLE);
            holder.layout2.setVisibility(LinearLayout.VISIBLE);
            holder.layout3.setVisibility(LinearLayout.VISIBLE);
            holder.tvName.setText(printKitchenEntity.getPrintKitchenName());
            holder.tvIp.setText(printKitchenEntity.getPrintKitchenIp());
            String isCut = printKitchenEntity.getIsOneDishOneCut() == 1 ? "是" : "否";
            holder.tvCut.setText(isCut);
            holder.tvCount.setText(String.valueOf(printKitchenEntity.getPrintCount()+1)+"份");
            holder.tvChars.setText("48");
            if (printKitchenEntity.getConnectStatus() != null && printKitchenEntity.getConnectStatus() == 0) {
                holder.tvConnect.setTextColor(mContext.getResources().getColor(R.color.reddark));
                holder.tvConnect.setText("未连接");
                holder.tvConnect.setCompoundDrawables(mDrawable1, null, null, null);
            } else {
                holder.tvConnect.setTextColor(mContext.getResources().getColor(R.color.greendark));
                holder.tvConnect.setText("已连接");
                holder.tvConnect.setCompoundDrawables(mDrawable0, null, null, null);
            }

            if (printKitchenEntity.getPrintKitchenIp() != null && printKitchenEntity.getPrintKitchenId().equals(selectedPrintKitchenId)) {
                holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.graydark));
            } else {
                holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

        holder.rootLayout.setTag(printKitchenEntity.getPrintKitchenId());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printKitchenId = (String) v.getTag();
                if (selectedPrintKitchenId != null && printKitchenId != null && printKitchenId.equals(selectedPrintKitchenId)) {
                    selectedPrintKitchenId = null;
                } else {
                    selectedPrintKitchenId = printKitchenId;
                }
                mOnKitchenLeftItemClickListener.onKitchenLeftItemClick(selectedPrintKitchenId);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPrintKitchenEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootLayout,layout0,layout1,layout2,layout3;
        private TextView tvName, tvConnect, tvIp, tvCut, tvCount, tvChars;

        public ViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
            tvName = (TextView) view.findViewById(R.id.tv_printer_name);
            tvConnect = (TextView) view.findViewById(R.id.tv_printer_connnect);
            tvIp = (TextView) view.findViewById(R.id.tv_printer_ip);
            tvCut = (TextView) view.findViewById(R.id.tv_printer_cut);
            tvCount = (TextView) view.findViewById(R.id.tv_printer_count);
            tvChars = (TextView) view.findViewById(R.id.tv_printer_chars);
            layout0 = (LinearLayout) view.findViewById(R.id.layout_0);
            layout1 = (LinearLayout) view.findViewById(R.id.layout_1);
            layout2 = (LinearLayout) view.findViewById(R.id.layout_2);
            layout3 = (LinearLayout) view.findViewById(R.id.layout_3);
        }
    }

    public void setOnKitchenLeftItemClickListener(OnKitchenLeftItemClickListener listener) {
        this.mOnKitchenLeftItemClickListener = listener;
    }

    public String getSelectedPrintKitchenId() {
        return selectedPrintKitchenId;
    }

    public interface OnKitchenLeftItemClickListener {
        void onKitchenLeftItemClick(String printKitchenEntityId);
    }

    public void updateData() {
        mPrintKitchenEntities.clear();
        PrintKitchenEntity printKitchenEntity = new PrintKitchenEntity();
        printKitchenEntity.setPrintKitchenId(null);
        printKitchenEntity.setPrintKitchenName("全部");
        mPrintKitchenEntities.add(printKitchenEntity);
        mPrintKitchenEntities.addAll(DBHelper.getInstance(mContext).getAllKichenPrinter());
        notifyDataSetChanged();
    }
}
