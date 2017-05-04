package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/12/16.
 */

public class CashierKeyboardUtil2 {
    public static final int KEYCODE_ZDDZ = 119;
    public static final int KEYCODE_BFDZ = 116;
    public static final int KEYCODE_FADZ = 115;
    public static final int KEYCODE_QKSY = 104;
    public static final int KEYCODE_XJ = 101;
    public static final int KEYCODE_YHK = 114;
    public static final int KEYCODE_WXZF = 121;
    public static final int KEYCODE_ZFB = 117;
    public static final int KEYCODE_TG = 100;
    public static final int KEYCODE_GZ = 102;
    public static final int KEYCODE_HYZF = 106;
    public static final int KEYCODE_HYYH = 107;
    public static final int KEYCODE_AC = 108;
    public static final int KEYCODE_FH = 105;
    public static final int KEYCODE_JZWB = 103;
    private Context ctx;
    private MyKeyboardView3 keyboardView;
    private Keyboard k1;// 收银键盘
    private EditText ed;
    private OnCashierKeyBoardClickListener mOnCashierKeyBoardClickListener;

    public CashierKeyboardUtil2(Context ctx, Window view, EditText edit) {
        this.ctx = ctx;
        this.ed = edit;
        k1 = new Keyboard(ctx, R.xml.symbols_1);
        keyboardView = (MyKeyboardView3) view.findViewById(R.id.keyboard_view);
        keyboardView.setContext(ctx);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public void setEdittext(EditText et) {
        this.ed = et;
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if(ed != null) {
                Editable editable = ed.getText();
                int start = ed.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                }else if(primaryCode == KEYCODE_AC){
                    //清空所有字符串
                    if (editable != null && editable.length() > 0) {
                        editable.clear();
                        ed.requestFocus();
                    }
                }else if(primaryCode < 100) {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
            if(primaryCode >= 100 && mOnCashierKeyBoardClickListener != null){
                mOnCashierKeyBoardClickListener.onCashierKeyClick(primaryCode);
            }
        }
    };

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public void setCashierKeyBoardClickListener(OnCashierKeyBoardClickListener listener){
        mOnCashierKeyBoardClickListener = listener;
    }

    public interface OnCashierKeyBoardClickListener{
        void onCashierKeyClick(int keyCode);
    }
}
