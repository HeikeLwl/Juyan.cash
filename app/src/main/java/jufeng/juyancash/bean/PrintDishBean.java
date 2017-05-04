package jufeng.juyancash.bean;

import android.text.TextUtils;

import java.util.ArrayList;

import jufeng.juyancash.dao.DishSelectedMaterialEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.util.AmountUtils;

/**
 * Created by Administrator102 on 2016/11/18.
 */

public class PrintDishBean {
    private String orderDishId;
    private String dishId;
    private String dishName;
    private String dishTypeId;
    private String dishTypeName;
    private String dishConfig;
    private String mark;
    private double dishCount;
    private String orderId;
    private double dishPrice;//总金额
    private int type;
    private PrintDishBean parentBean;//父级
    private double dishUnitPrice;//单价
    private double discountMoney;//折后额
    private int discountRate[] = new int[2];//折扣率
    private int status;
    private String unitName;//单位
    private String mDishSelectedMaterialEntities;

    public PrintDishBean(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity, PrintDishBean printTaocanBean, String unitName) {
        this.dishName = orderTaocanGroupDishEntity.getDishName();
        this.dishId = orderTaocanGroupDishEntity.getDishId();
        this.orderDishId = orderTaocanGroupDishEntity.getOrderDishId();
        this.dishTypeId = orderTaocanGroupDishEntity.getDishTypeId();
        this.dishTypeName = orderTaocanGroupDishEntity.getDishTypeName();
        String config = "";
        config = TextUtils.isEmpty(orderTaocanGroupDishEntity.getSpecifyName()) ? "" : "(" + orderTaocanGroupDishEntity.getSpecifyName();
        config = TextUtils.isEmpty(orderTaocanGroupDishEntity.getPracticeName()) ? (config.length() > 0 ? config + ")" : "") : ((config.length() > 0 ? config + "," + orderTaocanGroupDishEntity.getPracticeName() + ")" : "(" + orderTaocanGroupDishEntity.getPracticeName() + ")"));
        this.dishConfig = config;
        this.dishCount = AmountUtils.multiply1(orderTaocanGroupDishEntity.getTaocanGroupDishCount() + "", "1");
        this.dishPrice = orderTaocanGroupDishEntity.getExtraPrice();
        this.type = 1;
        this.parentBean = printTaocanBean;
        dishUnitPrice = 0;
        discountMoney = AmountUtils.multiply(dishPrice, 1);
        discountRate[0] = 100;
        discountRate[1] = 100;
        this.status = 1;
        this.unitName = unitName;
        if (orderTaocanGroupDishEntity.getRemark() != null && orderTaocanGroupDishEntity.getRemark().length() > 0) {
            mark = orderTaocanGroupDishEntity.getRemark().replace("`", "、");
        }
    }

    public PrintDishBean(OrderDishEntity orderDishEntity, String unitName, double uniPrice, double discountMoney, int[] discountRate) {
        this.dishName = orderDishEntity.getDishName();
        this.dishId = orderDishEntity.getDishId();
        this.orderDishId = orderDishEntity.getOrderDishId();
        this.dishTypeId = orderDishEntity.getDishTypeId();
        this.dishTypeName = orderDishEntity.getDishTypeName();
        String config = "";
        config = TextUtils.isEmpty(orderDishEntity.getDishSpecify()) ? "" : "(" + orderDishEntity.getDishSpecify();
        config = TextUtils.isEmpty(orderDishEntity.getDishPractice()) ? (config.length() > 0 ? config + ")" : "") : ((config.length() > 0 ? config + "," + orderDishEntity.getDishPractice() + ")" : "(" + orderDishEntity.getDishPractice() + ")"));
        if (orderDishEntity.getDishNote() != null && orderDishEntity.getDishNote().length() > 0) {
            mark = orderDishEntity.getDishNote().replace("`","、");
        }
        this.dishConfig = config;
        this.dishCount = orderDishEntity.getDishCount();
        this.dishPrice = AmountUtils.multiply(orderDishEntity.getDishPrice(), 1);
        this.type = 0;
        this.parentBean = null;
        this.dishUnitPrice = uniPrice;
        this.discountMoney = AmountUtils.multiply(discountMoney, 1);
        this.discountRate = discountRate;
        this.status = orderDishEntity.getIsOrdered();
        this.unitName = unitName;
    }

    public PrintDishBean(OrderDishEntity orderDishEntity, String unitName, double uniPrice, double discountMoney, int[] discountRate, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        this.dishName = orderDishEntity.getDishName();
        this.dishId = orderDishEntity.getDishId();
        this.orderDishId = orderDishEntity.getOrderDishId();
        this.dishTypeId = orderDishEntity.getDishTypeId();
        this.dishTypeName = orderDishEntity.getDishTypeName();
        String config = "";
        config = TextUtils.isEmpty(orderDishEntity.getDishSpecify()) ? "" : "(" + orderDishEntity.getDishSpecify();
        config = TextUtils.isEmpty(orderDishEntity.getDishPractice()) ? (config.length() > 0 ? config + ")" : "") : ((config.length() > 0 ? config + "," + orderDishEntity.getDishPractice() + ")" : "(" + orderDishEntity.getDishPractice() + ")"));
        if (orderDishEntity.getDishNote() != null && orderDishEntity.getDishNote().length() > 0) {
            mark = orderDishEntity.getDishNote().replace("`","、");
        }
        this.dishConfig = config;
        this.dishCount = orderDishEntity.getDishCount();
        this.dishPrice = AmountUtils.multiply(orderDishEntity.getDishPrice(), 1);
        this.type = 0;
        this.parentBean = null;
        this.dishUnitPrice = uniPrice;
        this.discountMoney = AmountUtils.multiply(discountMoney, 1);
        this.discountRate = discountRate;
        this.status = orderDishEntity.getIsOrdered();
        this.unitName = unitName;
        if (dishSelectedMaterialEntities != null && dishSelectedMaterialEntities.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (DishSelectedMaterialEntity dishSelectMaterial :
                    dishSelectedMaterialEntities) {
                sb.append(dishSelectMaterial.getMaterialName() + "X" + dishSelectMaterial.getSelectedCount() + "、");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            mDishSelectedMaterialEntities = sb.toString();
        } else {
            this.mDishSelectedMaterialEntities = null;
        }
    }

    public String getDishSelectedMaterialEntities() {
        return mDishSelectedMaterialEntities;
    }

    public void setDishSelectedMaterialEntities(String dishSelectedMaterialEntities) {
        mDishSelectedMaterialEntities = dishSelectedMaterialEntities;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getDishUnitPrice() {
        return dishUnitPrice;
    }

    public void setDishUnitPrice(double dishUnitPrice) {
        this.dishUnitPrice = dishUnitPrice;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public int[] getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int[] discountRate) {
        this.discountRate = discountRate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PrintDishBean getParentBean() {
        return parentBean;
    }

    public void setParentBean(PrintDishBean parentBean) {
        this.parentBean = parentBean;
    }

    public String getOrderDishId() {
        return orderDishId;
    }

    public void setOrderDishId(String orderDishId) {
        this.orderDishId = orderDishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(String dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    public String getDishConfig() {
        return dishConfig;
    }

    public void setDishConfig(String dishConfig) {
        this.dishConfig = dishConfig;
    }

    public double getDishCount() {
        return dishCount;
    }

    public void setDishCount(double dishCount) {
        this.dishCount = dishCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }
}
