package jufeng.juyancash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.PayModeEntity;

/**
 * Created by Administrator102 on 2016/8/9.
 */
public class OrderTurnOverAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PayModeEntity> mPayModeEntity;
    private String mCashierId;
    private String mOrderType;
    private String mlMoney,discountMoney,couponMoney,bjlMoney,presentMoney,receivableMoney,incomeMoney;

    public OrderTurnOverAdapter(Context context, String cashierId, String orderType) {
        this.mContext = context;
        this.mCashierId = cashierId;
        this.mOrderType = orderType;
        mPayModeEntity = new ArrayList<>();
        receivableMoney = DBHelper.getInstance(mContext).getAllOrderReceivableMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        incomeMoney = DBHelper.getInstance(mContext).getAllOrderReceivedMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        mlMoney = DBHelper.getInstance(mContext).getAllOrderTreatmentMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        String[] discount = DBHelper.getInstance(mContext).getAllOrderDiscountMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        discountMoney = discount[0];
        couponMoney = discount[1];
        bjlMoney = DBHelper.getInstance(mContext).getAllOrderMantissaMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        presentMoney = DBHelper.getInstance(mContext).getAllOrderPresentMoney(mContext, mCashierId, 0, null, -1, mOrderType);
        mPayModeEntity.addAll(DBHelper.getInstance(mContext).getAllPayMode(mContext, cashierId, 0, null, -1, mOrderType));
    }

    @Override
    public int getCount() {
        return mPayModeEntity.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (position < mPayModeEntity.size())
            return mPayModeEntity.get(position);
        else
            return null;
    }

    @Override
    public int getItemViewType(int position) {
            if (position == mPayModeEntity.size()) {
                return 0;
            } else {
                return 1;
            }
    }

    @Override
    public int getViewTypeCount() {
            return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                FooterViewHolder footerHolder = null;
                if (convertView == null) {
                    footerHolder = new FooterViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.collection_footer, null);
                    footerHolder.tv0 = (TextView) convertView.findViewById(R.id.tv_0);
                    footerHolder.tv1 = (TextView) convertView.findViewById(R.id.tv_1);
                    footerHolder.tv2 = (TextView) convertView.findViewById(R.id.tv_2);
                    footerHolder.tv3 = (TextView) convertView.findViewById(R.id.tv_3);
                    footerHolder.tv4 = (TextView) convertView.findViewById(R.id.tv_4);
                    footerHolder.tv5 = (TextView) convertView.findViewById(R.id.tv_5);
                    footerHolder.tv6 = (TextView) convertView.findViewById(R.id.tv_6);
                    convertView.setTag(footerHolder);
                } else {
                    footerHolder = (FooterViewHolder) convertView.getTag();
                }
                //应收
                footerHolder.tv0.setText(String.valueOf(receivableMoney));
                //实收
                footerHolder.tv1.setText(String.valueOf(incomeMoney));
                //抹零
                footerHolder.tv2.setText(String.valueOf(mlMoney));
                //折扣
                footerHolder.tv3.setText(String.valueOf(discountMoney));
                //不吉利尾数
                footerHolder.tv4.setText(String.valueOf(bjlMoney));
                //赠菜金额
                footerHolder.tv5.setText(String.valueOf(presentMoney));
                //优惠券金额
                footerHolder.tv6.setText(String.valueOf(couponMoney));
                break;
            case 1:
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.collection_item, null);
                    holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                    holder.tvName.setText(mPayModeEntity.get(position).getPaymentName());
                    holder.tvMoney.setText(String.valueOf(mPayModeEntity.get(position).getPayMoney()));
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvMoney;
    }

    class FooterViewHolder {
        TextView tv0;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
    }
}
