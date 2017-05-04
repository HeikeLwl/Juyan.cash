package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackDishConfigDeleteEvent {
    private String dishId;

    public SnackDishConfigDeleteEvent(String dishId) {
        this.dishId = dishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }
}
