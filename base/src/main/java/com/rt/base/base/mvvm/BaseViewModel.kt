package com.rt.base.base.mvvm


import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rt.base.bean.HttpWrapper
import com.rt.base.util.ToastUtil
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel(), LifecycleObserver {

    val mException: SafeMutableLiveData<Exception> = SafeMutableLiveData()

    val errMsg: SafeMutableLiveData<ErrorMessage> = SafeMutableLiveData()
    val errorMsgList = ArrayList<SafeMutableLiveData<ErrorMessage>>()
    val mExceptionMsgList = ArrayList<SafeMutableLiveData<Exception>>()
    private val mRequestErrorLinser = ArrayList<OnNetWorkCallLinsener>()

    override fun onCleared() {
        super.onCleared()
        mRequestErrorLinser.clear()
    }

    /**
     * 添加网络请求错误问题
     */
    fun regNetWorkRequestLinsener(mNetWorkRequestLinsener: OnNetWorkCallLinsener) {
        mRequestErrorLinser.add(mNetWorkRequestLinsener)
    }

    /**
     * 注册错误回调
     */
    fun registerToListen(errMsg: SafeMutableLiveData<ErrorMessage>) {
        errorMsgList.add(errMsg)
    }

    /**
     * 异常回调
     */
    fun registerExceptionList(errMsg: SafeMutableLiveData<java.lang.Exception>) {
        mExceptionMsgList.add(errMsg)
    }

    init {
        errorMsgList.add(errMsg)
        mExceptionMsgList.add(mException)
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
//        viewModelScope.launch { block() }
        GlobalScope.launch(Dispatchers.Main) { block() }

    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
        }
    }

    fun newLaunch(tryBlock: suspend CoroutineScope.() -> Unit, tag: String = "") {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true, tag)
        }
    }

    /**
     * 返回异常监听
     */
    fun getException(): MutableLiveData<Exception> {
        return mException
    }

    private fun sendRequstError(exe: Exception, tag: String = "") {
        if (TextUtils.isEmpty(tag)) {
            return
        }
        mRequestErrorLinser.forEach {
            it.onNewWorkErrorCall(tag, exe)
        }


    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false, tag: String = ""
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Exception) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    if (!e.toString().contains("JobCancellationException")) {
                        //提示报错信息
                        ToastUtil.showMiddleToast(e.toString())
                    }

                    sendRequstError(e, tag)
                    traverseExpMsg(e)
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

    suspend fun executeResponse(
        response: HttpWrapper<Any>,
        successBlock: suspend CoroutineScope.() -> Unit,
        errorBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            if (response.status == 0) {
                successBlock()
            } else {
                errorBlock()
            }
        }
    }

    suspend fun executeResponse(
        response: String,
        successBlock: suspend CoroutineScope.() -> Unit,
        errorBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            if (TextUtils.isEmpty(response)) errorBlock()
            else successBlock()
        }
    }

    /**
     * 多处需要回调的时候
     */
    fun traverseErrorMsg(mErrorMessage: ErrorMessage) {
        Log.i("ErrorMessage", mErrorMessage.toString())
        errorMsgList.forEach {
            it.value = mErrorMessage
        }
    }

    /**
     * 多出需要
     */
    fun traverseExpMsg(mException: Exception) {
        Log.i("Exception", mException.toString())
        mExceptionMsgList.forEach {
            it.value = mException
        }
    }

    suspend fun <T> executeResponse(
        response: T,
        successBlock: suspend CoroutineScope.() -> Unit,
        errorBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            if (response == null) errorBlock()
            else successBlock()
        }
    }
}
