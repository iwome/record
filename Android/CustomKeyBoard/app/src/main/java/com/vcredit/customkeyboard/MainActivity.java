package com.vcredit.customkeyboard;

import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private KeyboardView kv;
    private KeyBoardViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(et, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        kv = (KeyboardView) findViewById(R.id.keyboard);
        helper = KeyBoardViewHelper.initHelper(this, et, kv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(kv.getVisibility()== View.VISIBLE){
            helper.hideKeyboard();
        }
        return super.onTouchEvent(event);
    }
}
