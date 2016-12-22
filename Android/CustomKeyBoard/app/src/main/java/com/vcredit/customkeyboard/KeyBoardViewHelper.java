package com.vcredit.customkeyboard;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;

/**
 * Created by qiubangbang on 2016/11/18.
 */

public class KeyBoardViewHelper implements KeyboardView.OnKeyboardActionListener {

    private Activity mActivity;
    private EditText mEt;
    private KeyboardView mkbv;
    private Keyboard mkb;
    public static final String TAG = "key_helper";
    private StringBuffer sb;
    private PopupWindow popWnd;
    private Button mBtnRecharge;
    private Button mBtnClose;
    private Button mBtnConfirm;
    private final RelativeLayout mRl;


    public KeyBoardViewHelper(Activity activity) {
        mActivity = activity;
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.popu_soft_input, null);
        mEt = (EditText) contentView.findViewById(R.id.et_money);
        mkbv = (KeyboardView) contentView.findViewById(R.id.keyboard);
        mBtnRecharge = (Button) contentView.findViewById(R.id.btn_recharge);
        mBtnClose = (Button) contentView.findViewById(R.id.btn_close);
        mBtnConfirm = (Button) contentView.findViewById(R.id.btn_confirm);
        mRl = (RelativeLayout) contentView.findViewById(R.id.rl);
        hideAndroSoftInput(activity);
        popWnd = new PopupWindow(mActivity);
        popWnd.setContentView(contentView);
        popWnd.setOutsideTouchable(true);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setClippingEnabled(true);
        popWnd.setBackgroundDrawable(new ColorDrawable(0x000000));
        popWnd.setAnimationStyle(R.style.contextMenuAnim);
        initKeyBoardView();
    }

    private void setBackground() {
        mRl.setBackgroundColor(0x66000000);
    }

    private void initKeyBoardView() {
        mkb = new Keyboard(mActivity, R.xml.keyboard_numbers);
        mkbv.setKeyboard(mkb);
        mkbv.setPreviewEnabled(false);
        mkbv.setEnabled(true);
        mkbv.setOnKeyboardActionListener(this);
        sb = new StringBuffer();
        setEtListenner();
    }

    /**
     * 弹出输入框
     *
     * @param view 展示在view的下方
     */
    public void showKeyboard(View view) {
        if (null != popWnd) {
            //设置activity背景
//            setBackground();
            popWnd.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.6f, 1000);
        }
    }

    /**
     * 关闭输入框
     */
    public void closeKeyboard() {
        if (null != popWnd) {
            popWnd.dismiss();
            backgroundAlpha(0f,0);
        }
    }

    /**
     * 绑定事件
     */

    public void eventBind(View.OnClickListener onClickListener) {
        mBtnClose.setOnClickListener(onClickListener);
        mBtnConfirm.setOnClickListener(onClickListener);
        mBtnRecharge.setOnClickListener(onClickListener);
    }

    private void hideAndroSoftInput(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEt.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(mEt, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void showKeyBoard() {
        if (sb == null) {
            sb = new StringBuffer();
        }

        mkbv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(final float bgAlpha, int Duration) {
        final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(bgAlpha);
        animator.setDuration(Duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + f);
                lp.alpha = 1-f; //0.0-1.0
                mActivity.getWindow().setAttributes(lp);
                if (f == bgAlpha) {
                    animation.removeAllUpdateListeners();
                }
            }
        });
        animator.start();
    }

}
