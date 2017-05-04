package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.util.ReasonCodes;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class SelectCancelMeituanOrderReasonAdapter extends RecyclerView.Adapter<SelectCancelMeituanOrderReasonAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Integer> mReasones;
    private int mSelectReason = -1;

    public SelectCancelMeituanOrderReasonAdapter(Context context) {
        this.mContext = context;
        mReasones = new ArrayList<>();
        mReasones.addAll(ReasonCodes.toArrayList());
        if (mReasones.size() > 0) {
            mSelectReason = mReasones.get(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_order_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckBox.setText(ReasonCodes.getValueByKey(mReasones.get(position)));
        Log.d("###", "选中的code：" + mSelectReason);
        if (mSelectReason != -1) {
            holder.mCheckBox.setChecked(mReasones.get(position) == mSelectReason);
        } else {
            holder.mCheckBox.setChecked(false);
        }

        holder.mCheckBox.setTag(mReasones.get(position));
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int reasonCode = (Integer) view.getTag();
                Log.d("###", "点击的code: " + reasonCode);
                mSelectReason = reasonCode;
                notifyDataSetChanged();
            }
        });
    }

    public int getSelectReason() {
        return mSelectReason;
    }

    @Override
    public int getItemCount() {
        return mReasones.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
