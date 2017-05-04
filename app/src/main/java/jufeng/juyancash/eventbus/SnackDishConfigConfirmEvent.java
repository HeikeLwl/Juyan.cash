package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.dao.DishSelectedMaterialEntity;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackDishConfigConfirmEvent {
    //dishId, specifyId, practiceId, orderDishId, etNote.getText().toString(), AmountUtils.multiply1(mEtCount.getText().toString(),"1"
    private String orderDishId;
    private String dishId;
    private String specifyId;
    private String practiceId;
    private String note;
    private double count;
    private ArrayList<DishSelectedMaterialEntity> mDishSelectedMaterialEntities;

    public SnackDishConfigConfirmEvent(String orderDishId, String dishId, String specifyId, String practiceId, String note, double count,ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        this.orderDishId = orderDishId;
        this.dishId = dishId;
        this.specifyId = specifyId;
        this.practiceId = practiceId;
        this.note = note;
        this.count = count;
        this.mDishSelectedMaterialEntities = dishSelectedMaterialEntities;
    }

    public SnackDishConfigConfirmEvent(String orderDishId, String dishId, String specifyId, String practiceId, String note, double count) {
        this.orderDishId = orderDishId;
        this.dishId = dishId;
        this.specifyId = specifyId;
        this.practiceId = practiceId;
        this.note = note;
        this.count = count;
        this.mDishSelectedMaterialEntities = null;
    }

    public ArrayList<DishSelectedMaterialEntity> getDishSelectedMaterialEntities() {
        return mDishSelectedMaterialEntities;
    }

    public void setDishSelectedMaterialEntities(ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        mDishSelectedMaterialEntities = dishSelectedMaterialEntities;
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

    public String getSpecifyId() {
        return specifyId;
    }

    public void setSpecifyId(String specifyId) {
        this.specifyId = specifyId;
    }

    public String getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(String practiceId) {
        this.practiceId = practiceId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
