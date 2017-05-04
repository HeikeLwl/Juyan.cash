package jufeng.juyancash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.PayModeEntity;

/**
 * Created by Administrator102 on 2016/8/9.
 */
public class OrderCollectionLeftAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PayModeEntity> mPayModeEntity;
    private int mType;
    private String mCashierId;
    private int mShift;
    private String mPayModeId;
    private int mDate;
    private String mOrderType;
    private String mlMoney, discountMoney, couponMoney, bjlMoney, cashieredMoney, totalMoney, unCashieredMoney, presentMoney, receivableMoney, incomeMoney;
    private int cashieredCount, unCashieredCount, totalCount;
    private float vipRechargeMoney;

    public OrderCollectionLeftAdapter(Context context, int type, String cashierId, int shift, String payModeId, int date, String orderType) {
        this.mContext = context;
        this.mType = type;
        this.mCashierId = cashierId;
        this.mShift = shift;
        this.mPayModeId = payModeId;
        this.mDate = date;
        this.mOrderType = orderType;
        mPayModeEntity = new ArrayList<>();
        receivableMoney = DBHelper.getInstance(mContext).getAllOrderReceivableMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        incomeMoney = DBHelper.getInstance(mContext).getAllOrderReceivedMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        mlMoney = DBHelper.getInstance(mContext).getAllOrderTreatmentMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        String[] discount = DBHelper.getInstance(mContext).getAllOrderDiscountMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        discountMoney = discount[0];
        couponMoney = discount[1];
        bjlMoney = DBHelper.getInstance(mContext).getAllOrderMantissaMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        cashieredCount = DBHelper.getInstance(mContext).getCashieredCount(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        cashieredMoney = DBHelper.getInstance(mContext).getCashieredMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        unCashieredCount = DBHelper.getInstance(mContext).getUnCashierdOrderCount(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        unCashieredMoney = DBHelper.getInstance(mContext).getUnCashieredMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        totalCount = DBHelper.getInstance(mContext).getAllOrderCount(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        totalMoney = DBHelper.getInstance(mContext).getAllOrderMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        presentMoney = DBHelper.getInstance(mContext).getAllOrderPresentMoney(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType);
        mPayModeEntity.addAll(DBHelper.getInstance(mContext).getAllPayMode(mContext, mCashierId, mShift, mPayModeId, mDate, mOrderType));
    }

    public Map<String,String> getOrderDetialCollection(){
        Map<String,String> map = new HashMap<>();
        map.put("cashieredCount",String.valueOf(cashieredCount));
        map.put("cashieredMoney",String.valueOf(cashieredMoney));
        map.put("unCashieredCount",String.valueOf(unCashieredCount));
        map.put("unCashieredMoney",String.valueOf(unCashieredMoney));
        map.put("totalCount",String.valueOf(totalCount));
        map.put("totalMoney",String.valueOf(totalMoney));
        map.put("receivableMoney",String.valueOf(receivableMoney));
        map.put("incomeMoney",String.valueOf(incomeMoney));
        map.put("mlMoney",String.valueOf(mlMoney));
        map.put("discountMoney",String.valueOf(discountMoney));
        map.put("bjlMoney",String.valueOf(bjlMoney));
        map.put("presentMoney",String.valueOf(presentMoney));
        map.put("couponMoney",String.valueOf(couponMoney));
        return map;
    }

    public ArrayList<PayModeEntity> getPayModeEntity(){
        return mPayModeEntity;
    }

    @Override
    public int getCount() {
        if (mType == 0)
            return mPayModeEntity.size() + 2;
        else
            return mPayModeEntity.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (mType == 0 && position > 0 && position < mPayModeEntity.size() + 1)
            return mPayModeEntity.get(position);
        else if (mType == 1 && position < mPayModeEntity.size())
            return mPayModeEntity.get(position);
        else
            return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mType == 0) {
            if (position == 0) {
                return 2;
            } else if (position == mPayModeEntity.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (position == mPayModeEntity.size()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        if (mType == 0)
            return 3;
        else
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
                if (mType == 0) {
                    holder.tvName.setText(mPayModeEntity.get(position - 1).getPaymentName());
                    holder.tvMoney.setText(mPayModeEntity.get(position - 1).getPayMoney()+"");
                } else {
                    holder.tvName.setText(mPayModeEntity.get(position).getPaymentName());
                    holder.tvMoney.setText(mPayModeEntity.get(position).getPayMoney()+"");
                }
                break;
            case 2:
                HeaderViewHolder headerHolder = null;
                if (convertView == null) {
                    headerHolder = new HeaderViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.collection_header, null);
                    headerHolder.tv0 = (TextView) convertView.findViewById(R.id.tv_0);
                    headerHolder.tv1 = (TextView) convertView.findViewById(R.id.tv_1);
                    headerHolder.tv2 = (TextView) convertView.findViewById(R.id.tv_2);
                    headerHolder.tv3 = (TextView) convertView.findViewById(R.id.tv_3);
                    headerHolder.tv4 = (TextView) convertView.findViewById(R.id.tv_4);
                    headerHolder.tv5 = (TextView) convertView.findViewById(R.id.tv_5);
                    convertView.setTag(headerHolder);
                } else {
                    headerHolder = (HeaderViewHolder) convertView.getTag();
                }
                headerHolder.tv0.setText(String.valueOf(cashieredCount));
                headerHolder.tv1.setText(String.valueOf(cashieredMoney));
                headerHolder.tv2.setText(String.valueOf(unCashieredCount));
                headerHolder.tv3.setText(String.valueOf(unCashieredMoney));
                headerHolder.tv4.setText(String.valueOf(totalCount));
                headerHolder.tv5.setText(String.valueOf(totalMoney));
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvMoney;
    }

    class HeaderViewHolder {
        TextView tv0;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
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
