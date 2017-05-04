package jufeng.juyancash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.DiscountEntity;

/**
 * Created by Administrator102 on 2016/8/9.
 */
public class DiscountSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DiscountEntity> mObjects;

    public DiscountSpinnerAdapter(Context context, ArrayList<DiscountEntity> objects) {
        this.mContext = context;
        this.mObjects = objects;
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.discount_spinner_item_selected_layout, null);
            holder.tvStr = (TextView) convertView.findViewById(R.id.tv_str);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == 0){
            holder.tvStr.setBackground(mContext.getResources().getDrawable(R.drawable.default_spinner_background));
        }else{
            holder.tvStr.setBackground(mContext.getResources().getDrawable(R.drawable.green_spinner_background));
        }
        holder.tvStr.setText(mObjects.get(position).getDiscountName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_spinner_item_layout, null);
            holder.tvStr = (TextView) convertView.findViewById(R.id.tv_str);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvStr.setText(mObjects.get(position).getDiscountName());
        return convertView;
    }

    class ViewHolder {
        TextView tvStr;
    }
}
