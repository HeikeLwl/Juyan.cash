package jufeng.juyancash.eventbus;

import jufeng.juyancash.dao.TurnoverHistoryEntity;

/**
 * Created by Administrator102 on 2017/3/14.
 */

public class TurnoverHistoryPrintAgainEvent {
    private TurnoverHistoryEntity mTurnoverHistoryEntity;

    public TurnoverHistoryPrintAgainEvent(TurnoverHistoryEntity turnoverHistoryEntity) {
        mTurnoverHistoryEntity = turnoverHistoryEntity;
    }

    public TurnoverHistoryEntity getTurnoverHistoryEntity() {
        return mTurnoverHistoryEntity;
    }

    public void setTurnoverHistoryEntity(TurnoverHistoryEntity turnoverHistoryEntity) {
        mTurnoverHistoryEntity = turnoverHistoryEntity;
    }
}
