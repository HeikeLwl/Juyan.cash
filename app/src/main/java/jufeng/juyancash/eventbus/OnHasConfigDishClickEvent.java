package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/17.
 */

public class OnHasConfigDishClickEvent {
    private String dishId;

    public OnHasConfigDishClickEvent(String dishId) {
        this.dishId = dishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }
}
