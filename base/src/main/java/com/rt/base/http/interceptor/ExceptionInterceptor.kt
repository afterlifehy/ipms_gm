package com.rt.base.http.interceptor

import android.os.CountDownTimer
import android.util.Log
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.util.NetTimeUtil
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.promisesBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class ExceptionInterceptor : Interceptor {
    private val UTF8 = Charset.forName("UTF-8")
    private var countDownTimer: CountDownTimer? = null
    var timeOn = true
    val log: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    fun start() {
        countDownTimer = object : CountDownTimer(1 * 60 * 1000L, 10 * 1000L) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                timeOn = true
                countDownTimer?.cancel()
                countDownTimer = null
            }
        }.start()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (timeOn) {
            val netTime = NetTimeUtil.getNtpTime()
            if (abs(netTime) > 0 && abs(netTime - System.currentTimeMillis()) > 1000 * 60) {
                throw IOException("本机时间有误，请联系后台客服人员处理")
            } else {
                timeOn = false
                runOnUiThread {
                    start()
                }
            }
        }
        val maxRetryCount = 3 // 最大重试次数
        var retryCount = 0 // 当前重试次数
        val retryDelayMs = 3000L // 重试间隔时间，单位毫秒
        var response: Response? = null
        var startNs: Long

        while (retryCount <= maxRetryCount) {
            startNs = System.nanoTime()
            try {
                response = chain.proceed(request)
                val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
                // 响应日志拦截
                return logForResponse(response, tookMs)
            } catch (e: SocketTimeoutException) {
                retryCount++
                log("<-- HTTP FAILED: $e, retrying ($retryCount/$maxRetryCount)")
                if (retryCount > maxRetryCount) {
                    val errorMsg = "连接超时，请检查网络"
                    log.info("--- HTTP FAILED---: retryCount: $retryCount$errorMsg${request.url}")
                    throw IOException(errorMsg, e)
                }
                Thread.sleep(retryDelayMs)
            } catch (e: ConnectException) {
                log("<-- HTTP FAILED: $e")
                val errorMsg = "连接服务器错误，请检查网络"
                throw IOException(errorMsg, e)
            } catch (e: UnknownHostException) {
                log("<-- HTTP FAILED: $e")
                val errorMsg = "网络错误，请检查网络"
                throw IOException(errorMsg, e)
            } catch (e: Exception) {
                log("<-- HTTP FAILED: $e")
                throw e
            }
        }
        // 若重试后仍然失败，抛出最后一个异常
        throw IOException("已达到最大尝试次数")
    }

    private fun log(message: String) {
        Log.v("G0", message)
    }

    private fun logForResponse(response: Response, tookMs: Long): Response {
        val builder: Response.Builder = response.newBuilder()
        val clone: Response = builder.build()
        var responseBody = clone.body
        try {
            log("<-- " + clone.code + ' ' + clone.message + ' ' + clone.request.url + " (" + tookMs + "ms)")
            val headers = clone.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                log(
                    """
                        
                        ${headers.name(i)}: ${headers.value(i)}
                        """.trimIndent()
                )
                i++
            }
            log(" ")
            if (clone.promisesBody()) {
                if (responseBody == null) return response
                if (isPlaintext(responseBody.contentType())) {
                    val bytes = ConvertUtils.inputStream2Bytes(responseBody.byteStream())
                    val contentType = responseBody.contentType()
                    val body = java.lang.String(bytes, getCharset(contentType)!!)
                    log("\nbody:$body")
                    responseBody = ResponseBody.create(responseBody.contentType(), bytes)
                    return response.newBuilder().body(responseBody).build()
                } else {
                    log("\nbody: maybe [binary body], omitted!")
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            log("<-- END HTTP")
        }
        return response
    }

    private fun isPlaintext(mediaType: MediaType?): Boolean {
        if (mediaType == null) return false
        if (mediaType.type == "text") {
            return true
        }
        var subtype = mediaType.subtype
        if (subtype != null) {
            subtype = subtype.lowercase(Locale.getDefault())
            if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) //
                return true
        }
        return false
    }

    private fun getCharset(contentType: MediaType?): Charset? {
        var charset = if (contentType != null) contentType.charset(UTF8) else UTF8
        if (charset == null) charset = UTF8
        return charset
    }

    fun getCurrentTime(): String {
        val time = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")
        return time
    }
}