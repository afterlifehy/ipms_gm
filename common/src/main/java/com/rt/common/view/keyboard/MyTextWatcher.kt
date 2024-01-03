package com.rt.common.view.keyboard

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MyTextWatcher(
    private val beforeEditText: EditText?, private val nextEditText: EditText?,
    private val isNumber: Boolean, private val keyboardUtil: KeyboardUtil
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            if (it.isNotEmpty()) {
                nextEditText?.let {
                    it.requestFocus()
                    it.setSelection(it.text.length)
                    keyboardUtil.changeKeyboard(isNumber)
                    keyboardUtil.setEditText(nextEditText)
                }
            } else {
                beforeEditText?.let {
                    it?.requestFocus()
                    keyboardUtil.changeKeyboard(isNumber)
                    keyboardUtil.setEditText(it)
                }
            }
        }
    }

}