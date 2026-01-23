package com.rt.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class LogInterceptor(private val isDebug: Boolean) : Interceptor {
    protected val log: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestContent = request.toString()
        val printContent = requestContent.replace(Regex("""photo":"([^&]*)"""")) { matchResult ->
            val value = matchResult.groupValues[1] // 获取 photo 参数值
            if (value.isEmpty()) {
                "photo:空值"
            } else {
                "photo:${value.take(50)} 总字节数:${value.toByteArray().size}"
            }
        }

        log.info("okhttp3:$printContent")
        val response: Response = chain.proceed(request)
//        if (isDebug) {
        val mediaType = response.body!!.contentType()
        val content = response.body!!.string()
        log.info(response.toString())
        log.info("=============request:{}\n=============response body:{}\n", printContent, content)

        // 返回一个新的response，并保留原始的响应体内容
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content)) // 使用复制的响应体
            .build()
    }
}
