package com.rt.base.http.interceptor

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.EncryptUtils
import com.rt.base.BaseApplication
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.util.Constant
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.util.*


class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var sortParam = ""

        val body = chain.request().body
        val buffer = Buffer()
        body?.writeTo(buffer)
        var charset = Charset.forName("UTF-8")
        val contentType = body?.contentType()
        if (contentType == null) {
            sortParam = ""
        } else if (TextUtils.equals(contentType.type + contentType.subtype, "applicationx-www-form-urlencoded")) {
            charset = contentType.charset(charset)
            val requestParams = buffer.readString(charset)
            sortParam = getSortForm(requestParams).toString()
        } else {
            charset = contentType.charset(charset)
            val requestParams = buffer.readString(charset)
            if (TextUtils.isEmpty(requestParams)) {//防止参数为空的时候，导致闪退
                sortParam = requestParams
            } else {
                sortParam = getSortJson(JSONObject.parseObject(requestParams))
            }
        }

        val addHeader = chain.request().newBuilder()
        val timeStamp = System.currentTimeMillis().toString()
        runBlocking {
            val token = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            addHeader.addHeader("Content-Type", "application/json")
                .addHeader("timestamp", timeStamp)
                .addHeader("token", token)
                .addHeader("version", AppUtils.getAppVersionName())
                .addHeader("versionCode", AppUtils.getAppVersionCode().toString())
                .addHeader("sign", EncryptUtils.encryptMD5ToString(sortParam + timeStamp + Constant.secret))
        }
        val request = addHeader.build()
        return chain.proceed(request)
    }

    fun getSortJson(json: JSONObject): String {
        val map: Map<String, Any> = JSON.parseObject(json.toString(), Map::class.java) as Map<String, Any>
        val keys: List<String> = ArrayList(map.keys)
        Collections.sort(keys)
        val sb = StringBuilder()
        for (key in keys) {
            sb.append(key).append(map[key].toString())
        }
        return sb.toString()
//        val iteratorKeys: Iterator<String> = json.keys().iterator()
//        val map: TreeMap<String?, String?> = TreeMap()
//        while (iteratorKeys.hasNext()) {
//            val key = iteratorKeys.next()
//            val value = json.getString(key)
//            map[key] = value
//        }
//        var sort = ""
//        val keySets: List<String> = ArrayList<String>(map.keys)
//        for (i in 0 until map.size) {
//            val key = keySets[i]
//            val value = map[key].toString()
//            sort += key + value
//        }
//        return sort
    }

    fun getSortForm(form: String): String? {
        val list = ArrayList<String>()
        if (form.contains("&")) {
            val temp = form.split("&")
            list.addAll(temp)
        } else {
            list.add(form)
        }
        Collections.sort(list)
        var sort = ""
        for (i in list.indices) {
            list[i].replace("=", "")
            sort += list[i]
        }
        return sort
    }
}
