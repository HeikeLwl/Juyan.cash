package jufeng.juyancash.bean;

import jufeng.juyancash.dao.DishEntity;

/**
 * Created by Administrator102 on 2016/11/10.
 */

public class DishBean{
    private boolean isHasConfig;
    private double dishCount;
    private boolean isChing;
    private DishEntity mDishEntity;

    public DishBean(){
        isHasConfig = false;
        dishCount = 0;
        isChing = false;
    }

    public DishEntity getDishEntity() {
        return mDishEntity;
    }

    public void setDishEntity(DishEntity dishEntity) {
        mDishEntity = dishEntity;
    }

    public boolean isHasConfig() {
        return isHasConfig;
    }

    public void setHasConfig(boolean hasConfig) {
        isHasConfig = hasConfig;
    }

    public double getDishCount() {
        return dishCount;
    }

    public void setDishCount(double dishCount) {
        this.dishCount = dishCount;
    }

    public boolean isChing() {
        return isChing;
    }

    public void setChing(boolean ching) {
        isChing = ching;
    }
}
