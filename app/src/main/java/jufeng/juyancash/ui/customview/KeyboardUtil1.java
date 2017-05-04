package jufeng.juyancash.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/12/16.
 */

public class KeyboardUtil1 {
    private MyKeyboardView1 keyboardView;
    private Keyboard k1;// 字母键盘
    private boolean isupper;//大小写切换
    private EditText ed;

    public KeyboardUtil1(Activity act, Context ctx, EditText edit, int type) {
        this.ed = edit;
        k1 = new Keyboard(ctx, R.xml.qwerty_symbols);
        keyboardView = (MyKeyboardView1) act.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public KeyboardUtil1(View view, Context ctx, EditText edit, int type, int keyoardViewId) {
        this.ed = edit;
        switch (type){
            case 1:
                k1 = new Keyboard(ctx, R.xml.qwerty_symbols);
                break;
            case 2:
                k1 = new Keyboard(ctx, R.xml.qwerty_symbols_1);
                break;
        }
        keyboardView = (MyKeyboardView1) view.findViewById(keyoardViewId);
        keyboardView.setContext(ctx);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public KeyboardUtil1(Context ctx, MyKeyboardView1 keyboardView,EditText edit, int type) {
        this.ed = edit;
        switch (type){
            case 1:
                k1 = new Keyboard(ctx, R.xml.qwerty_symbols);
                break;
            case 2:
                k1 = new Keyboard(ctx, R.xml.qwerty_symbols_1);
                break;
        }
        this.keyboardView = keyboardView;
        this.keyboardView.setContext(ctx);
        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(true);
        this.keyboardView.setOnKeyboardActionListener(listener);
    }

    public void setEditText(EditText et){
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
                if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                    changeKey();
                    keyboardView.setKeyboard(k1);
                }else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }else{
                if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                    changeKey();
                    keyboardView.setKeyboard(k1);
                }
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Keyboard.Key> keylist = k1.getKeys();
        if (isupper) {//大写切换小写
            isupper = false;
            for(Keyboard.Key key:keylist){
                if (key.label!=null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0]+32;
                }
            }
        } else {//小写切换大写
            isupper = true;
            for(Keyboard.Key key:keylist){
                if (key.label!=null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0]-32;
                }
            }
        }
    }

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

    private boolean isword(String str){
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        return wordstr.indexOf(str.toLowerCase()) > -1;
    }
}
