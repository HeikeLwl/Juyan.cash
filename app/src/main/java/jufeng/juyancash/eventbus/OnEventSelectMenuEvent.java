package jufeng.juyancash.eventbus;

import android.os.Bundle;

/**
 * Created by Administrator102 on 2017/3/17.
 */

public class OnEventSelectMenuEvent {
    private int mIndex;
    private Bundle mBundle;

    public OnEventSelectMenuEvent(int index, Bundle bundle) {
        mIndex = index;
        mBundle = bundle;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }
}
