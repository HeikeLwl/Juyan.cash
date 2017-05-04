package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.SendPersonEntity;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class SelectEmployeeAdapter extends RecyclerView.Adapter<SelectEmployeeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SendPersonEntity> mEmployees;
    private OnSelectEmployeeListener mOnSelectEmployeeListener;

    public SelectEmployeeAdapter(Context context){
        this.mContext = context;
        mEmployees = DBHelper.getInstance(mContext).getSendPerson();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_employee_item_layout, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mEmployees.get(position).getSendPersonName());
        holder.rootLayout.setTag(mEmployees.get(position));
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPersonEntity employeeEntity = (SendPersonEntity) v.getTag();
                mOnSelectEmployeeListener.onSelectEmployee(employeeEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEmployees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private LinearLayout rootLayout;

        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_employee_name);
            rootLayout = (LinearLayout) view.findViewById(R.id.item_root_layout);
        }
    }

    public void setOnSelectEmployeeListener(OnSelectEmployeeListener listener){
        this.mOnSelectEmployeeListener = listener;
    }

    public interface OnSelectEmployeeListener{
        void onSelectEmployee(SendPersonEntity employeeEntity);
    }
}
