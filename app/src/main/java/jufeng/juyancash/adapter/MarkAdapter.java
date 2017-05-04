package jufeng.juyancash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;

/**
 * Created by 15157_000 on 2016/6/17 0017.
 */
public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> datas;
    private ArrayList<String> selectedMarks;

    public MarkAdapter(Context context,List<String> selectedMarks) {
        this.mContext = context;
        initData(selectedMarks);
    }

    public String getSelectedMarks(){
        String result = "";
        try {
            String[] marks = selectedMarks.toArray(new String[selectedMarks.size()]);
            for (int i = 0; i < marks.length ;i++){
                if(i == marks.length - 1){
                    result += marks[i];
                }else{
                    result += marks[i] + "`";
                }
            }
        }catch (Exception e){

        }
        return result;
    }

    private void initData(List<String> selectedMarks) {
        datas = new ArrayList<>();
        datas.addAll(DBHelper.getInstance(mContext).getAllMarks());
        this.selectedMarks = new ArrayList<>();
        this.selectedMarks.addAll(selectedMarks);
    }

    @Override
    public MarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_mark_item, parent, false);
        ViewHolder holder0 = new ViewHolder(view);
        return holder0;
    }

    @Override
    public void onBindViewHolder(MarkAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(datas.get(position));
        if(selectedMarks.contains(datas.get(position))){
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.blue_layout));
        }else{
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.black_layout));
        }
        holder.tvName.setTag(datas.get(position));
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mark = (String) view.getTag();
                if(selectedMarks.contains(mark)){
                    selectedMarks.remove(mark);
                }else{
                    selectedMarks.add(mark);
                }
                int position = datas.indexOf(mark);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
