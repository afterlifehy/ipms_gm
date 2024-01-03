package com.rt.base.dialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.rt.base.viewbase.BaseViewAddManagers;
import com.rt.base.viewbase.BaseViewAddManagersImpl;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 对话框
 */


public abstract class BaseLibDialog extends BackgroundDarkDialog {

    private AtomicBoolean hideInput = new AtomicBoolean(false);

    /**
     * 是否有输入框
     *
     * @return
     */
    protected abstract boolean getHideInput();


    /**
     * 对话框宽度
     *
     * @return
     */
    protected abstract float getWidth();

    /**
     * 对话框高度
     *
     * @return
     */
    protected abstract float getHeight();

    /**
     * 布局
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutResID();

    /**
     * 点击外围对话框消失
     *
     * @return
     */
    protected abstract boolean getCanceledOnTouchOutside();

    protected abstract int getGravity();
    protected abstract View getBindingView();

    protected View mRoot;
    protected BaseViewAddManagers mViewAddManager = new BaseViewAddManagersImpl();

    /**
     * 对话框
     *
     * @param context 上下文
     */
    public BaseLibDialog(@NonNull Context context) {
        super(context);
        hideInput.set(getHideInput());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mBinView = getBindingView();
        if (mBinView == null) {
            ViewGroup parent = (ViewGroup) inflater.inflate(getLayoutResID(), null, false);
            setContentView(parent);
        } else {
//            mRoot = mViewAddManager.getRootView(context, mBinView);
            setContentView(mBinView);
        }
        setLayout(getWidth(), getHeight());
    }

    public BaseLibDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        hideInput.set(getHideInput());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mBinView = getBindingView();
        if (mBinView == null) {
            ViewGroup parent = (ViewGroup) inflater.inflate(getLayoutResID(), null, false);
            setContentView(parent);
        } else {
            setContentView(mBinView);
        }
        setLayout(getWidth(), getHeight());
    }

//    public BaseLibDialog(@NonNull Context context, @StyleRes int themeResId) {
//        super(context, themeResId);
//        hideInput.set(getHideInput());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        ViewGroup parent = (ViewGroup) inflater.inflate(getLayoutResID(), null, false);
//        setContentView(parent);
//        setLayout(getWidth(), getHeight());
//    }

    public void setLayout(float width, float height) {
        setCanceledOnTouchOutside(getCanceledOnTouchOutside());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
//        View decorView = dialogWindow.getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        dialogWindow.setGravity(getGravity());
        p.height = (int) height;
        p.width = (int) width;
        dialogWindow.setAttributes(p);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (hideInput.get()) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View view = getCurrentFocus();
                if (isHideInput(view, ev)) {
                    HideSoftInput(view.getWindowToken());
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom);
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
