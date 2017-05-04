package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackDishConfigEvent {
    private String orderDishId;
    private String dishId;

    public SnackDishConfigEvent(String orderDishId,String dishId) {
        this.orderDishId = orderDishId;
        this.dishId = dishId;
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
