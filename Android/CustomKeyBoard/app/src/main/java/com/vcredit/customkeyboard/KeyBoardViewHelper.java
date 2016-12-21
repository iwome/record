package com.vcredit.customkeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by qiubangbang on 2016/11/18.
 */

public class KeyBoardViewHelper implements KeyboardView.OnKeyboardActionListener {

    Context mContext;
    EditText mEt;
    KeyboardView mkbv;
    private Keyboard mkb;
    private static KeyBoardViewHelper keyBoardHelper;
    public static final String TAG = "key_helper";
    private StringBuffer sb;

    private KeyBoardViewHelper(Context context, EditText editText, KeyboardView keyboardView) {
        mContext = context;
        mEt = editText;
        mkbv = keyboardView;
        initKeyBoardView();
    }

    public static KeyBoardViewHelper initHelper(Context context, EditText editText, KeyboardView keyboardView) {
        if (keyBoardHelper == null) {
            keyBoardHelper = new KeyBoardViewHelper(context, editText, keyboardView);
        }
        return keyBoardHelper;
    }

    private void initKeyBoardView() {
        mkb = new Keyboard(mContext, R.xml.keyboard_numbers);
        mkbv.setKeyboard(mkb);
        mkbv.setPreviewEnabled(false);
        mkbv.setEnabled(true);
        mkbv.setOnKeyboardActionListener(this);
        sb = new StringBuffer();
        setEtListenner();
    }

    private void setEtListenner() {
        mEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mkbv.getVisibility() == View.VISIBLE) {
//                    hideKeyboard();
                } else {
                    showKeyBoard();
                }
            }
        });
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d(TAG, "onKey: primaryCode: " + primaryCode);
        //记录光标的位置
        int cursor = mEt.getSelectionStart();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (cursor != 0) {
                    sb.deleteCharAt(cursor - 1);
                    cursor--;
                }
                break;
            case -1:
                if (cursor != 0 && sb.indexOf(".") == -1) {
                    sb.insert(cursor, '.');
                    cursor++;
                }
                break;
            default:
                if (sb.length() == 0) {
                    cursor = 0;
                }
                sb.insert(cursor, (char) primaryCode);
                cursor++;
                break;
        }
        mEt.setText(sb);
        //处理光标变化
        if (sb.length() > 0) {
            mEt.setSelection(cursor);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void showKeyBoard() {
        if (sb == null) {
            sb = new StringBuffer();
        }

        mkbv.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        mkbv.setVisibility(View.GONE);
    }

}
