package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/9/27.
 */
public class DishModel{
    private String dishModelId;
    private String dishModelName;
    private String dishModelTypeId;
    private int dishModelType;
    private double count;

    public void setCount(double count) {
        this.count = count;
    }

    public double getCount() {
        return count;
    }

    public String getDishModelId() {
        return dishModelId;
    }

    public void setDishModelId(String dishModelId) {
        this.dishModelId = dishModelId;
    }

    public String getDishModelName() {
        return dishModelName;
    }

    public void setDishModelName(String dishModelName) {
        this.dishModelName = dishModelName;
    }

    public String getDishModelTypeId() {
        return dishModelTypeId;
    }

    public void setDishModelTypeId(String dishModelTypeId) {
        this.dishModelTypeId = dishModelTypeId;
    }

    public int getDishModelType() {
        return dishModelType;
    }

    public void setDishModelType(int dishModelType) {
        this.dishModelType = dishModelType;
    }
}
