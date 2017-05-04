package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackConfigDishEvent {
    private String dishId;
    private String orderDishId;

    public SnackConfigDishEvent(String dishId, String orderDishId) {
        this.dishId = dishId;
        this.orderDishId = orderDishId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getOrderDishId() {
        return orderDishId;
    }

    public void setOrderDishId(String orderDishId) {
        this.orderDishId = orderDishId;
    }
}
