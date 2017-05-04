package jufeng.juyancash.eventbus;

import java.util.Objects;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class OnDialogConfirmEvent {
    private Objects[] mObjectses;

    public OnDialogConfirmEvent(Objects[] objectses) {
        mObjectses = objectses;
    }

    public Objects[] getObjectses() {
        return mObjectses;
    }

    public void setObjectses(Objects[] objectses) {
        mObjectses = objectses;
    }
}
