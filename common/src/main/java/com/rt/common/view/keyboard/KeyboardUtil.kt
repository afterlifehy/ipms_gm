package com.rt.common.view.keyboard

import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.rt.base.help.ActivityCacheManager
import com.rt.common.R
import java.lang.reflect.Method

class KeyboardUtil(val keyboardView: MyKeyboardView, var requestEditAct: (() -> Unit)? = null) {

    private var mKeyboardView: MyKeyboardView
    private val provinceKeyboard: Keyboard
    private var abcKeyboard: Keyboard
    private var editText: EditText? = null

    private var hideAction: (() -> Unit)? = null
    private var clickPosition = 0
    private var callback: KeyInputCallBack? = null

    // private var hideEdit:(()->Unit)? = null
    init {
        // 省份键盘
        provinceKeyboard = Keyboard(ActivityCacheManager.instance().getCurrentActivity(), R.xml.province)
        // abc键盘
        abcKeyboard = Keyboard(ActivityCacheManager.instance().getCurrentActivity(), R.xml.abc)

        mKeyboardView = keyboardView
        mKeyboardView.apply {
            keyboard = abcKeyboard
            isEnabled = true
            // 设置按键没有点击放大镜显示的效果
            isPreviewEnabled = false
            setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
                override fun onPress(primaryCode: Int) {

                }

                override fun onRelease(primaryCode: Int) {
                }

                override fun onText(text: CharSequence?) {
                }

                override fun swipeLeft() {
                }

                override fun swipeRight() {
                }

                override fun swipeDown() {
                }

                override fun swipeUp() {
                }

                override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                    val editable = editText?.text
                    when (primaryCode) {
                        -1 -> changeKeyboard(true)
                        -2 -> changeKeyboard(false)
                        -3 -> {
                            val etValue = editText?.text.toString()
                            if (etValue != null) {
                                if (etValue.isNotEmpty()) {
                                    val stringBuilder: StringBuilder = StringBuilder(etValue)
                                    if (clickPosition > 0) {
                                        if (callback != null) {
                                            callback?.keyDelete()
                                        }
                                        editText?.setText(stringBuilder.deleteCharAt(clickPosition - 1))
                                        clickPosition -= 1
                                        editText?.setSelection(clickPosition)
                                    }
                                }
                            } else {
                                if (callback != null) {
                                    callback?.keyDelete()
                                }
                            }
                        }

                        -4 -> {
                            hideKeyboard()
                        }

                        else -> {
                            if (editText != null && editable != null && editable.length < 8) {
                                editable.insert(clickPosition, primaryCode.toChar().toString())
                                clickPosition += 1
                            }
                            if (editText == null) {
                                clickPosition += 1
                            }
                            if (callback != null) {
                                callback?.keyInput(primaryCode.toChar().toString())
                            }
                        }
                    }
                }

            })
        }
    }

    fun setEditText(editText: EditText, clickPosition: Int = -1) {
        this.editText = editText
        if (clickPosition == -1) {
            this.editText!!.setSelection(this.editText!!.length())
            this.clickPosition = this.editText!!.length()
        } else {
            this.editText!!.setSelection(clickPosition)
            this.clickPosition = clickPosition
        }
    }

    /**
     * 指定切换软键盘
     * isNumber false 省份软键盘， true 数字字母软键盘
     */
    fun changeKeyboard(isNumber: Boolean) {
        if (isNumber) {
            mKeyboardView.keyboard = abcKeyboard
        } else {
            mKeyboardView.keyboard = provinceKeyboard
        }
    }

    /**
     * 软键盘展示状态
     */
    fun isShow() = mKeyboardView.visibility == View.VISIBLE

    /**
     * 显示软键盘
     */
    fun showKeyboard(show: (() -> Unit)? = null, hide: (() -> Unit)? = null) {
        hideAction = hide
        show?.invoke()
        val visibility = mKeyboardView.visibility
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val visibility = mKeyboardView.visibility
        if (visibility == View.VISIBLE) {
            mKeyboardView.visibility = View.INVISIBLE
        }
        hideAction?.invoke()
    }

    /**
     * 禁掉系统软键盘
     */
    fun hideSoftInputMethod(editText: EditText) {
//        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        editText.inputType = InputType.TYPE_NULL

        ActivityCacheManager.instance().getCurrentActivity()?.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        try {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus: Method
            setShowSoftInputOnFocus = cls.getMethod(
                "setShowSoftInputOnFocus",
                Boolean::class.javaPrimitiveType
            )
            setShowSoftInputOnFocus.setAccessible(true)
            setShowSoftInputOnFocus.invoke(editText, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun setCallBack(callBack: KeyInputCallBack) {
        this.callback = callBack
    }

    interface KeyInputCallBack {
        fun keyInput(value: String)
        fun keyDelete()
    }
}