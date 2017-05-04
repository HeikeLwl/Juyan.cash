package jufeng.juyancash.eventbus;

import android.widget.EditText;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class SnackSetKeyboardEdittextEvent {
    private EditText mEditText;

    public SnackSetKeyboardEdittextEvent(EditText editText) {
        mEditText = editText;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }
}
