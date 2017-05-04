package jufeng.juyancash.myinterface;

import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;

/**
 * Created by Administrator102 on 2017/2/11.
 */

public interface SelectTaocanFragmentListener {
    void onTaocanDetailClose();

    void onTaocanDishCancle(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

    void onTaocanDishDelete(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

    void onTaocanDishConfirm(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

    void onTaocanItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);

    void onTaocanCanle();
    void onTaocanDelete();
    void onTaocanConfirm();
    void onTaocanDishItemClick(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);
    void onTaocanDishAdd(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);
    void onTaocanDishReduce(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity);
}
