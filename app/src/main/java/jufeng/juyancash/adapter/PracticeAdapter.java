package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.DishPracticeEntity;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DishPracticeEntity> mDishPracticeEntities;
    private ArrayList<String> datas;
    private String selectedPractices;

    public PracticeAdapter(Context context, String dishId ,String practiceId) {
        this.mContext = context;
        initData(dishId,practiceId);
    }

    public String getSelectedPractices(){
        return selectedPractices;
    }

    private void initData(String dishId,String practiceId) {
        datas = new ArrayList<>();
        selectedPractices = practiceId;
        mDishPracticeEntities = new ArrayList<>();
        mDishPracticeEntities.addAll(DBHelper.getInstance(mContext).getAllPracticeByDishId(dishId));
        if(selectedPractices == null && mDishPracticeEntities.size() > 0){
            selectedPractices = mDishPracticeEntities.get(0).getDishPracticeId();
        }
        for (DishPracticeEntity dishPractice :
                mDishPracticeEntities) {
            datas.add(dishPractice.getDishPracticeId());
        }
    }

    @Override
    public PracticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_mark_item, parent, false);
        ViewHolder holder0 = new ViewHolder(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(PracticeAdapter.ViewHolder holder, int position) {
        switch (mDishPracticeEntities.get(position).getIncreaseMode()) {
            case 0://不加价
                holder.tvName.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(mContext).getPracticeName(mDishPracticeEntities.get(position).getPracticeId()) + "</font>"));
                break;
            case 1://一次性加价
                holder.tvName.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(mContext).getPracticeName(mDishPracticeEntities.get(position).getPracticeId()) + "</font><font color=\"red\">+" + mDishPracticeEntities.get(position).getIncreaseValue() + "元" + "</font>"));
                break;
            case 2://每购买单位加价
                holder.tvName.setText(Html.fromHtml("<font color=\"white\">" + DBHelper.getInstance(mContext).getPracticeName(mDishPracticeEntities.get(position).getPracticeId()) + "</font><font color=\"red\">+" + mDishPracticeEntities.get(position).getIncreaseValue() + "元" + "</font>"));
                break;
            case 3:
                holder.tvName.setText(DBHelper.getInstance(mContext).getPracticeName(mDishPracticeEntities.get(position).getPracticeId()));
                break;
            default:
                holder.tvName.setText(DBHelper.getInstance(mContext).getPracticeName(mDishPracticeEntities.get(position).getPracticeId()));
                break;
        }
        if (selectedPractices.contains(mDishPracticeEntities.get(position).getDishPracticeId())) {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.blue_layout));
        } else {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.black_layout));
        }
        holder.tvName.setTag(mDishPracticeEntities.get(position).getDishPracticeId());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String practiceId = (String) view.getTag();
                if (selectedPractices != null && !selectedPractices.equals(practiceId)) {
                    int oldPosition = datas.indexOf(selectedPractices);
                    notifyItemChanged(oldPosition);
                }
                selectedPractices = practiceId;
                int position = datas.indexOf(selectedPractices);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDishPracticeEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
