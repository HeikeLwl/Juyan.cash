package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.SpecialEntity;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class SelectReturnOrderReasonAdapter extends RecyclerView.Adapter<SelectReturnOrderReasonAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SpecialEntity> mSpecialEntities;
    private SpecialEntity mSelectSpecial;

    public SelectReturnOrderReasonAdapter(Context context){
        this.mContext = context;
        mSpecialEntities = new ArrayList<>();
        mSpecialEntities.addAll(DBHelper.getInstance(mContext).queryAllSpecialByType(3));
        mSelectSpecial = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_order_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckBox.setText(mSpecialEntities.get(position).getSpecialName());
        if(mSelectSpecial != null){
            holder.mCheckBox.setChecked(mSpecialEntities.get(position).getSpecialId().equals(mSelectSpecial.getSpecialId()));
        }else{
            holder.mCheckBox.setChecked(false);
        }

        holder.mCheckBox.setTag(mSpecialEntities.get(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpecialEntity specialEntity = (SpecialEntity) buttonView.getTag();
                if(isChecked){
                    mSelectSpecial = specialEntity;
                }else{
                    mSelectSpecial = null;
                }
                notifyDataSetChanged();
            }
        });
    }

    public SpecialEntity getSelectSpecial(){
        return mSelectSpecial;
    }

    @Override
    public int getItemCount() {
        return mSpecialEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;

        public ViewHolder(View view){
            super(view);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
