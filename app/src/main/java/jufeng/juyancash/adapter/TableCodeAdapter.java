package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.TableCodeBean;

/**
 * Created by Administrator102 on 2017/3/22.
 */

public class TableCodeAdapter extends RecyclerView.Adapter<TableCodeAdapter.ViewHolder> {
    private ArrayList<TableCodeBean> mDatas;
    private Context mContext;
    private Drawable mDrawable;

    public TableCodeAdapter(Context context,ArrayList<TableCodeBean> datas) {
        mDatas = new ArrayList<>();
        mDatas.addAll(datas);
        mContext = context;
        mDrawable = mContext.getResources().getDrawable(R.drawable.call);
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
    }

    public void update(){
        notifyDataSetChanged();
    }

    public void addItem(TableCodeBean tableCodeBean){
        mDatas.add(tableCodeBean);
        notifyItemChanged(mDatas.size() - 1);
    }

    public void addItem(int position,TableCodeBean tableCodeBean){
        mDatas.add(position,tableCodeBean);
        notifyItemChanged(position);
    }

    public void deleteItem(TableCodeBean tableCodeBean){
        int position = mDatas.indexOf(tableCodeBean);
        mDatas.remove(tableCodeBean);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_code_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(mDatas.get(position));
        if(mDatas.get(position).getType() == 0) {
            viewHolder.tvTableCode.setText(mDatas.get(position).getAreaName());
            viewHolder.tvTableCode.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
            viewHolder.tvTableCode.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tvTableCode.setClickable(false);
        }else if(mDatas.get(position).getType() == 1){
            viewHolder.tvTableCode.setClickable(true);
            viewHolder.tvTableCode.setText(mDatas.get(position).getTableCode());
            viewHolder.tvTableCode.setTextColor(mContext.getResources().getColor(R.color.dark));
            viewHolder.tvTableCode.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            boolean isTableCodeUsed = DBHelper.getInstance(mContext).isTableCodeUsed(mDatas.get(position).getTableCode());
            if(isTableCodeUsed){
                //该牌号正在使用
                viewHolder.tvTableCode.setCompoundDrawables(null,null,mDrawable,null);
            }else{
                //该牌号未使用
                viewHolder.tvTableCode.setCompoundDrawables(null,null,null,null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_table_code)
        public TextView tvTableCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
