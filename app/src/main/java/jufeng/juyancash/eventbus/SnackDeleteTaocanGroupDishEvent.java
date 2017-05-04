package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/21.
 */

public class SnackDeleteTaocanGroupDishEvent {
    private String orderedTaocanGroupDishId;

    public SnackDeleteTaocanGroupDishEvent(String orderedTaocanGroupDishId) {
        this.orderedTaocanGroupDishId = orderedTaocanGroupDishId;
    }

    public String getOrderedTaocanGroupDishId() {
        return orderedTaocanGroupDishId;
    }

    public void setOrderedTaocanGroupDishId(String orderedTaocanGroupDishId) {
        this.orderedTaocanGroupDishId = orderedTaocanGroupDishId;
    }
}
