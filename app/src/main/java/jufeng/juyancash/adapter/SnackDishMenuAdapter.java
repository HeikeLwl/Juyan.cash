package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishTypeEntity;
import jufeng.juyancash.dao.TaocanTypeEntity;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class SnackDishMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<DishTypeEntity> datas;
    private ArrayList<TaocanTypeEntity> mTaocanTypeEntities;
    private OnDishMenuClickListener mOnDishMenuClickListener;
    private String selectedTypeId;
    private int type;

    public SnackDishMenuAdapter(Context context) {
        this.mContext = context;
        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        mTaocanTypeEntities = new ArrayList<>();
        datas.addAll(DBHelper.getInstance(mContext).queryDishTypeData());
        mTaocanTypeEntities.addAll(DBHelper.getInstance(mContext).queryTaocanTypeData());
        if (datas.size() > 0) {
            type = 0;
            selectedTypeId = datas.get(0).getDishTypeId();
        } else if (mTaocanTypeEntities.size() > 0) {
            type = 1;
            selectedTypeId = mTaocanTypeEntities.get(0).getTaocanTypeId();
        }
    }

    public String getSelectedTypeId() {
        return selectedTypeId;
    }

    public int getType() {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dishmenu_item_layout, parent, false);
        ViewHolder0 holder0 = new ViewHolder0(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        } else if (holder instanceof ViewHolder0) {
            if (position < datas.size()) {
                if (selectedTypeId.equals(datas.get(position).getDishTypeId())) {
                    ((ViewHolder0) holder).view.setBackgroundResource(R.drawable.dishmenu_background);
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                    ((ViewHolder0) holder).tv0.getPaint().setFakeBoldText(true);
                } else {
                    ((ViewHolder0) holder).view.setBackgroundColor(Color.TRANSPARENT);
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.dark));
                    ((ViewHolder0) holder).tv0.getPaint().setFakeBoldText(false);
                }
                ((ViewHolder0) holder).tv0.setText(datas.get(position).getDishTypeName());
                ((ViewHolder0) holder).rootLayout.setTag(datas.get(position).getDishTypeId());
                ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTypeId = (String) v.getTag();
                        type = 0;
                        mOnDishMenuClickListener.onDishMenuClicked(selectedTypeId, 0);
                        notifyDataSetChanged();
                    }
                });
            } else if (position < datas.size() + mTaocanTypeEntities.size()) {
                if (selectedTypeId.equals(mTaocanTypeEntities.get(position - datas.size()).getTaocanTypeId())) {
                    ((ViewHolder0) holder).view.setBackgroundResource(R.drawable.dishmenu_background);
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
                } else {
                    ((ViewHolder0) holder).view.setBackgroundColor(Color.TRANSPARENT);
                    ((ViewHolder0) holder).tv0.setTextColor(mContext.getResources().getColor(R.color.dark));
                }
                ((ViewHolder0) holder).tv0.setText(mTaocanTypeEntities.get(position - datas.size()).getTaocanTypeName());
                ((ViewHolder0) holder).rootLayout.setTag(mTaocanTypeEntities.get(position - datas.size()).getTaocanTypeId());
                ((ViewHolder0) holder).rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTypeId = (String) v.getTag();
                        type = 1;
                        mOnDishMenuClickListener.onDishMenuClicked(selectedTypeId, 1);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size()+mTaocanTypeEntities.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        private TextView tv0;
        private LinearLayout rootLayout;
        private View view;

        public ViewHolder0(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.item_root_layout);
            tv0 = (TextView) itemView.findViewById(R.id.tv_dishtype_name);
            view = itemView.findViewById(R.id.view);
        }
    }

    public void setOnDishMenuClickListener(OnDishMenuClickListener listener) {
        this.mOnDishMenuClickListener = listener;
    }

    public interface OnDishMenuClickListener {
        void onDishMenuClicked(String typeId, int type);
    }
}
