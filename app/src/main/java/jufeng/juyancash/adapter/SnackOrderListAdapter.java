package jufeng.juyancash.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by Administrator102 on 2017/3/24.
 */

public class SnackOrderListAdapter extends RecyclerView.Adapter<SnackOrderListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderEntity> mDatas;
    private int oldPosition = -1;
    private String selectOrderId;
    private Drawable mDrawable1, mDrawable2, mDrawable3, mDrawable4;

    public SnackOrderListAdapter(Context context, String orderId) {
        this.mContext = context;
        this.selectOrderId = orderId;
        mDatas = new ArrayList<>();
        mDatas.addAll(DBHelper.getInstance(mContext).getAllSnackOrders());
        mDrawable1 = mContext.getResources().getDrawable(R.drawable.store_unpay);
        mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(), mDrawable1.getMinimumHeight());
        mDrawable2 = mContext.getResources().getDrawable(R.drawable.store_pay);
        mDrawable2.setBounds(0, 0, mDrawable2.getMinimumWidth(), mDrawable2.getMinimumHeight());
        mDrawable3 = mContext.getResources().getDrawable(R.drawable.wechat_unpay);
        mDrawable3.setBounds(0, 0, mDrawable3.getMinimumWidth(), mDrawable3.getMinimumHeight());
        mDrawable4 = mContext.getResources().getDrawable(R.drawable.wechat_pay);
        mDrawable4.setBounds(0, 0, mDrawable4.getMinimumWidth(), mDrawable4.getMinimumHeight());
    }

    public void updateData() {
        mDatas.clear();
        mDatas.addAll(DBHelper.getInstance(mContext).getAllSnackOrders());
        notifyDataSetChanged();
    }


    public boolean deleteItem(OrderEntity orderEntity) {
        mDatas.remove(orderEntity);
        notifyDataSetChanged();
        if (selectOrderId != null && orderEntity.getOrderId().equals(selectOrderId)) {
            //当前选中的订单被删除
            oldPosition = -1;
            selectOrderId = null;
            return true;
        } else {
            return false;
        }
    }

    public void notifyItem(OrderEntity orderEntity) {
        int position = mDatas.indexOf(orderEntity);
        if (position != oldPosition) {
            if (oldPosition >= 0)
                notifyItemChanged(oldPosition);
            if (position >= 0)
                notifyItemChanged(position);
            oldPosition = position;
            selectOrderId = orderEntity.getOrderId();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item_layout, viewGroup, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        OrderEntity orderEntity = mDatas.get(i);
        viewHolder.itemView.setTag(orderEntity);
        if (orderEntity.getOrderId() != null && selectOrderId != null && orderEntity.getOrderId().equals(selectOrderId)) {
            oldPosition = i;
            viewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.theme_0_red_1));
        } else {
            viewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        viewHolder.mTvSerialNumber.setText(orderEntity.getSerialNumber() == null ? "" : orderEntity.getSerialNumber());
        viewHolder.mTvOrderNumber.setText("NO." + orderEntity.getOrderNumber1());
        double billMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderEntity.getOrderId(), 1);
        viewHolder.mTvTotalMoney.setText(AmountUtils.multiply(billMoney + "", "1"));
        double receivableMoney = DBHelper.getInstance(mContext).getReceivableMoney(orderEntity.getOrderId(), 1);
        viewHolder.mTvReceivableMoney.setText(AmountUtils.multiply(receivableMoney + "", "1"));
        double payMoney = DBHelper.getInstance(mContext).getHadPayMoneyByOrderId(orderEntity.getOrderId());
        viewHolder.mTvPayMoney.setText(AmountUtils.multiply(payMoney + "", "1"));
//        viewHolder.mTvTableCode.setText(orderEntity.getTableId());
        boolean isHasPrintedDish = DBHelper.getInstance(mContext).isHasPrintedDish(orderEntity.getOrderId());
        boolean isHasUnPrintedDish = DBHelper.getInstance(mContext).isHasUnPrintedDish(orderEntity.getOrderId());
        if (isHasPrintedDish && isHasUnPrintedDish) {
            //有已打印商品
            viewHolder.mTvPrintStatus.setText("部分打印");
            viewHolder.mTvPrintStatus.setTextColor(mContext.getResources().getColor(R.color.bluelight));
        } else if (isHasPrintedDish && !isHasUnPrintedDish) {
            viewHolder.mTvPrintStatus.setText("全部打印");
            viewHolder.mTvPrintStatus.setTextColor(mContext.getResources().getColor(R.color.greenlight));
        } else if (!isHasPrintedDish && isHasUnPrintedDish) {
            viewHolder.mTvPrintStatus.setText("全部未打印");
            viewHolder.mTvPrintStatus.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
        } else {
            viewHolder.mTvPrintStatus.setText("暂无商品");
            viewHolder.mTvPrintStatus.setTextColor(mContext.getResources().getColor(R.color.dark));
        }

        boolean isHasOrderedDish = DBHelper.getInstance(mContext).isHasSnackOrderedDish(orderEntity.getOrderId());
        if(isHasOrderedDish && payMoney == receivableMoney){
            viewHolder.mTvOrderStatus.setText("已结账");
            viewHolder.mTvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.greenlight));
            if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                //微信订单
                viewHolder.mTvSerialNumber.setCompoundDrawables(mDrawable4, null, null, null);
            } else {
                //普通订单
                viewHolder.mTvSerialNumber.setCompoundDrawables(mDrawable2, null, null, null);
            }
        }else{
            viewHolder.mTvOrderStatus.setText("待结账");
            viewHolder.mTvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.theme_0_red_0));
            if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                //微信订单
                viewHolder.mTvSerialNumber.setCompoundDrawables(mDrawable3, null, null, null);
            } else {
                //普通订单
                viewHolder.mTvSerialNumber.setCompoundDrawables(mDrawable1, null, null, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_serial_number)
        TextView mTvSerialNumber;
        @BindView(R.id.tv_order_number)
        TextView mTvOrderNumber;
        @BindView(R.id.tv_total_money)
        TextView mTvTotalMoney;
        @BindView(R.id.tv_order_status)
        TextView mTvOrderStatus;
        @BindView(R.id.item_root_layout)
        LinearLayout mLinearLayout;
        @BindView(R.id.tv_receivable_money)
        TextView mTvReceivableMoney;
        @BindView(R.id.tv_pay_money)
        TextView mTvPayMoney;
        @BindView(R.id.tv_print_status)
        TextView mTvPrintStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
