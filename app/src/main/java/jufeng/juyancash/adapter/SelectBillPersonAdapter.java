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
import jufeng.juyancash.dao.BillPerson;

/**
 * Created by Administrator102 on 2016/8/25.
 */
public class SelectBillPersonAdapter extends RecyclerView.Adapter<SelectBillPersonAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BillPerson> mBillPersons;
    private OnSelectBillPersonItemClickListener mOnSelectBillPersonItemClickListener;

    public SelectBillPersonAdapter(Context context,int type,String billAccountId){
        this.mContext = context;
        mBillPersons = new ArrayList<>();
        mBillPersons.addAll(DBHelper.getInstance(mContext).queryBillPersonByType(type,billAccountId));
    }

    public void updateData(int type,String billAccountId){
        mBillPersons.clear();
        mBillPersons.addAll(DBHelper.getInstance(mContext).queryBillPersonByType(type,billAccountId));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_discount_reason_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mBillPersons.get(position).getName());
        holder.tvName.setTag(mBillPersons.get(position));
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectBillPersonItemClickListener.onSelectBillPersonItemClick((BillPerson) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBillPersons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_reason_name);
        }
    }

    public void setOnSelectBillPersonItemClickListener(OnSelectBillPersonItemClickListener listener){
        this.mOnSelectBillPersonItemClickListener = listener;
    }

    public interface OnSelectBillPersonItemClickListener{
        void onSelectBillPersonItemClick(BillPerson billPerson);
    }
}
