package jufeng.juyancash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.R;
import jufeng.juyancash.dao.EmployeeEntity;

/**
 * Created by Administrator102 on 2016/7/21.
 */
public class SpinnerAdapter1 extends BaseAdapter {
    private Context mContext;
    private ArrayList<EmployeeEntity> mEmployeeEntities;

    public SpinnerAdapter1(Context context, ArrayList<EmployeeEntity> employeeEntities){
        this.mContext = context.getApplicationContext();
        mEmployeeEntities = employeeEntities;
    }

    @Override
    public int getCount() {
        return mEmployeeEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mEmployeeEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_item_layout,null);
            holder.tvOrderNumber = (TextView) convertView.findViewById(R.id.tv_order_number);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOrderNumber.setText(mEmployeeEntities.get(position).getEmployeeName());
        return convertView;
    }

    class ViewHolder{
        TextView tvOrderNumber;
    }
}
