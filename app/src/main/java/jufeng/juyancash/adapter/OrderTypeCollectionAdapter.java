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
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/8/9.
 */
public class OrderTypeCollectionAdapter extends RecyclerView.Adapter<OrderTypeCollectionAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<DishTypeCollectionItemBean> mDishTypeCollectionItemBean;
    private String mCashierId;
    private int mShift;
    private String mPayModeId;
    private int mDate;
    private String mType;

    public OrderTypeCollectionAdapter(Context context){
        this.mContext = context;
        this.mDishTypeCollectionItemBean = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvItem.setText(String.valueOf(position+1));
        holder.tvName.setText(mDishTypeCollectionItemBean.get(position).getDishTypeName());
        holder.tvCount.setText(AmountUtils.multiply(""+mDishTypeCollectionItemBean.get(position).getDishTypeCount(),"1"));
        holder.tvMoney.setText(String.valueOf(CustomMethod.decimalFloat(mDishTypeCollectionItemBean.get(position).getDishTypeMoney())));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_collection_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mDishTypeCollectionItemBean.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvItem,tvName,tvCount,tvMoney;

        public ViewHolder(View view){
            super(view);
            tvItem = (TextView) view.findViewById(R.id.tv_item);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            tvMoney = (TextView) view.findViewById(R.id.tv_money);
        }
    }

    public void updateData(String cashierId,int shift,String payModeId,int date,String type){
        this.mCashierId = cashierId;
        this.mShift = shift;
        this.mPayModeId = payModeId;
        this.mDate = date;
        this.mType = type;
        mDishTypeCollectionItemBean.clear();
        mDishTypeCollectionItemBean.addAll(DBHelper.getInstance(mContext).getTypeCollectionDishType(cashierId,shift,payModeId,date,mType));
        notifyDataSetChanged();
    }

    public ArrayList<DishTypeCollectionItemBean> getDishTypeCollectionItemBean(){
        return mDishTypeCollectionItemBean;
    }
}
