package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/15.
 */

public class SnackRefreshDishMenuItemEvent {
    private String dishId;

    public SnackRefreshDishMenuItemEvent(String dishId) {
        this.dishId = dishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }
}
