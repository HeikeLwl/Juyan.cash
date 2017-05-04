package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class SnackDishDetailEvent {
    private String orderDishId;

    public SnackDishDetailEvent(String orderDishId) {
        this.orderDishId = orderDishId;
    }

    public String getOrderDishId() {
        return orderDishId;
    }

    public void setOrderDishId(String orderDishId) {
        this.orderDishId = orderDishId;
    }
}
