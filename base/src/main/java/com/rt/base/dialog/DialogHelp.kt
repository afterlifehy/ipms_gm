package com.rt.base.dialog

import android.content.Context
import com.rt.base.R
import com.rt.base.ext.i18n

class DialogHelp {
    var title: String = ""
    var contentMsg: String = ""
    var leftMsg: String = ""
    var rightMsg: String = ""
    var mOnButtonClickLinsener: OnButtonClickLinsener? = null
    var cancelable = true
    var mGlobalDialog: GlobalDialog? = null
    private var context: Context? = null
    var isAloneButton = false
    var mBuilder = Builder()
    fun initDailog() {
        mGlobalDialog = GlobalDialog(context!!, this)
    }

    /**
     * 开始弹窗
     */
    fun showDailog(): GlobalDialog? {
        mGlobalDialog?.show()
        return mGlobalDialog
    }

    private constructor(mBuilder: Builder, context: Context?) {
        this.title = mBuilder.getTitle()
        this.contentMsg = mBuilder.getContentMsg()
        this.leftMsg = mBuilder.getLeftMsg()
        this.rightMsg = mBuilder.getRightMsg()
        this.mOnButtonClickLinsener = mBuilder.getOnButtonClickLinsener()
        this.cancelable = mBuilder.getCancelable()
        this.context = context
        this.isAloneButton = mBuilder.getisAloneButton()
        this.mBuilder = mBuilder
        initDailog()
    }

    class Builder {
        private var title: String = ""
        private var contentMsg: String = ""
        private var leftMsg: String = i18n(R.string.Cancel)
        private var rightMsg: String = i18n(R.string.Ok)
        private var isAloneButton = false
        private var mOnButtonClickLinsener: OnButtonClickLinsener = object : OnButtonClickLinsener {
            override fun onLeftClickLinsener(msg: String) {
            }

            override fun onRightClickLinsener(msg: String) {
            }
        }
        private var cancelable = true

        fun getTitle(): String {
            return title
        }

        fun getContentMsg(): String {
            return contentMsg
        }

        fun getLeftMsg(): String {
            return leftMsg
        }

        fun getOnButtonClickLinsener(): OnButtonClickLinsener {
            return mOnButtonClickLinsener
        }

        fun getisAloneButton(): Boolean {
            return isAloneButton
        }

        fun isAloneButton(isAloneButton: Boolean): Builder {
            this.isAloneButton = isAloneButton
            return this
        }

        fun setOnButtonClickLinsener(mOnButtonClickLinsener: OnButtonClickLinsener): Builder {
            this.mOnButtonClickLinsener = mOnButtonClickLinsener
            return this
        }

        fun getRightMsg(): String {
            return rightMsg
        }

        fun getCancelable(): Boolean {
            return cancelable
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setContentMsg(contentMsg: String): Builder {
            this.contentMsg = contentMsg
            return this
        }

        fun setLeftMsg(leftMsg: String): Builder {
            this.leftMsg = leftMsg
            return this
        }

        fun setRightMsg(rightMsg: String): Builder {
            this.rightMsg = rightMsg
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(context: Context?): DialogHelp {
            return DialogHelp(this, context)
        }

    }

    interface OnButtonClickLinsener {
        fun onLeftClickLinsener(msg: String = "")
        fun onRightClickLinsener(msg: String = "")
    }
}