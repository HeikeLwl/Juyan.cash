package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.ClearModel;

/**
 * Created by Administrator102 on 2016/7/18.
 */
public class ClearAllAdapter extends RecyclerView.Adapter<ClearAllAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ClearModel> mClearModels;
    private ArrayList<ClearModel> mSelectedDatas;

    public ClearAllAdapter(Context context, String orderId) {
        this.mContext = context;
        mClearModels = new ArrayList<>();
        mSelectedDatas = new ArrayList<>();
        if (orderId != null)
            mClearModels.addAll(DBHelper.getInstance(context).getAllClearModel(orderId));
    }

    public ArrayList<ClearModel> getSelectedDatas() {
        return mSelectedDatas;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckBox.setAnimation(null);
        holder.mCheckBox.setChecked(mSelectedDatas.contains(mClearModels.get(position)));
        switch (mClearModels.get(position).getType()) {
            case 0:
                holder.mTvValue.setText(((float) mClearModels.get(position).getMoney()) / 100 + "元");
                break;
            case 1:
                holder.mTvValue.setText(mClearModels.get(position).getMoney() + "%");
                break;
            case 2:
                holder.mTvValue.setText(((float) mClearModels.get(position).getMoney()) / 100 + "元");
                break;
            case 3:
                holder.mTvValue.setText(((float) mClearModels.get(position).getMoney()) / 100 + "元");
                break;
        }
        holder.mCheckBox.setText(mClearModels.get(position).getName());
        holder.mCheckBox.setTag(mClearModels.get(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSelectedDatas.add((ClearModel) buttonView.getTag());
                } else {
                    mSelectedDatas.remove(buttonView.getTag());
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.clear_all_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mClearModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;
        private TextView mTvValue;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mTvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
