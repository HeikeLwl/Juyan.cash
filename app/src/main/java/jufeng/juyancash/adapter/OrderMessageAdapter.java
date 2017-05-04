package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.WxOrderMessageEntity;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class OrderMessageAdapter extends RecyclerView.Adapter<OrderMessageAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WxOrderMessageEntity> mWxOrderMessages;

    public OrderMessageAdapter(Context context){
        this.mContext = context;
        mWxOrderMessages = DBHelper.getInstance(mContext).getAllWxOrderMessages();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wx_order_message_item_layout, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mWxOrderMessages.get(position).getWxOrderMessageName() == 0){
            holder.tvName.setText("微信餐厅-点餐");
        }else if(mWxOrderMessages.get(position).getWxOrderMessageName() == 1){
            holder.tvName.setText("微信餐厅-支付");
        }else {
            holder.tvName.setText("扫描账单二维码-支付");
        }
        holder.tvArea.setText(mWxOrderMessages.get(position).getAreaName());
        holder.tvTable.setText(mWxOrderMessages.get(position).getTableName()+"("+mWxOrderMessages.get(position).getTableCode()+")");
        holder.tvOrder.setText("NO."+mWxOrderMessages.get(position).getOrderNumber());
    }

    @Override
    public int getItemCount() {
        return mWxOrderMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvArea,tvTable,tvOrder;

        public ViewHolder(View view){
            super(view);
            tvName = (TextView)view.findViewById(R.id.tv_name);
            tvArea = (TextView) view.findViewById(R.id.tv_area);
            tvTable = (TextView) view.findViewById(R.id.tv_table_name);
            tvOrder = (TextView) view.findViewById(R.id.tv_order_number);
        }
    }
}
