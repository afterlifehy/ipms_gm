package com.rt.common.util

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.rt.base.BaseApplication
import com.rt.base.help.ActivityCacheManager
import com.zrq.spanbuilder.Spans
import com.zrq.spanbuilder.TextStyle
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by huy  on 2022/8/5.
 */
object AppUtil {

    /**
     * 跳转到手机浏览器
     */
    fun goBrowser(url: String?) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ActivityCacheManager.instance().getCurrentActivity()?.startActivity(intent)
    }

    /**
     * 截图
     *
     * @param v
     * @return
     */
    fun getViewBp(v: View?): Bitmap? {
        if (null == v) {
            return null
        }
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache()
        v.measure(
            View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(v.height, View.MeasureSpec.EXACTLY)
        )
        v.layout(v.x.toInt(), v.y.toInt(), v.x.toInt() + v.measuredWidth, v.y.toInt() + v.measuredHeight)
        val b = Bitmap.createBitmap(v.drawingCache, 0, 0, v.measuredWidth, v.measuredHeight)
        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
        return b
    }

    fun getGroupSeparator(): Char {
        return DecimalFormatSymbols.getInstance().groupingSeparator
    }

    fun getDecimalSeparator(): Char {
        return DecimalFormatSymbols.getInstance().decimalSeparator
    }

    fun replaceComma(value: String): String {
        return value.replace(getGroupSeparator().toString(), "").replace(getDecimalSeparator().toString(), ".")
    }

    /**
     * 保留N位小数
     */
    fun keepNDecimal(value: Double, N: Int): String {
        var format = "##0."
        for (i in 1..N) {
            format += "0"
        }
        val df = DecimalFormat(format)
        return replaceComma(df.format(value))
    }

    /**
     * 保留N位小数
     */
    fun keepNDecimal(value: String, N: Int): String {
        if (N == 0) {
            val format = "##"
            val df = DecimalFormat(format)
            return replaceComma(df.format(value.toDouble()))
        } else {
            if (value.length - 1 - value.indexOf(".") > N) {
                var format = "##0."
                if (!TextUtils.isEmpty(value)) {
                    for (i in 1..N) {
                        format += "0"
                    }
                } else {
                    return ""
                }
                val df = DecimalFormat(format)
                return replaceComma(df.format(value.toDouble()))
            } else {
                return replaceComma(value)
            }
        }
    }

    fun keepNDecimals(value: String, N: Int): String {
        val number = value.toDoubleOrNull()
        return if (number != null) {
            String.format("%.${N}f", number)
        } else {
            "Invalid Input"
        }
    }

    /**
     * 保留N位小数，最多maxLength位数
     */
    val maxLength = 7
    fun subStringNDecimal(value: String, N: Int): String {
        if (value.contains(".")) {
            val pointIndex = value.indexOf(".")
            if (N == 0) {
                return value.substring(0, pointIndex)
            } else if (N < 0) {
                return replaceComma(value)
            } else {
                if (pointIndex + 1 + N > value.length) {
                    return replaceComma(value)
                } else {
                    val tempValue = value.substring(0, pointIndex + 1 + N)
                    if (tempValue.length < maxLength) {
                        if (value.length <= maxLength) {
                            return replaceComma(value)
                        } else {
                            return replaceComma(value.substring(0, maxLength))
                        }
                    }
                    return replaceComma(value.substring(0, pointIndex + 1 + N))
                }
            }
        } else {
            return value
        }
    }

    /**
     * 将固定格式转化成时间戳（默认 yyyy-MM-dd HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    fun getStringToLong(format: String?, dateString: String): Long {
        var format = format
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return try {
            val date = sdf.parse(dateString)
            date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    //long转String
    fun getLongToString(date: Long, type: String?): String? {
        return SimpleDateFormat(type, Locale.ENGLISH)
            .format(Date(date))
    }

    //防止快速点击
    private var lastClickTime1: Long = 0

    fun isFastClick(interval: Long): Boolean {
        val currentClickTime = System.currentTimeMillis()
        Log.v("lastClickTime3", (currentClickTime - AppUtil.lastClickTime1).toString())
        return if (currentClickTime - lastClickTime1 >= interval) {
            lastClickTime1 = currentClickTime
            false
        } else {
            lastClickTime1 = currentClickTime
            true
        }
    }

    /**
     * 补0
     */
    fun fillZero(value: String): String {
        if (value.length == 1) {
            return "0" + value
        } else {
            return value
        }
    }

    //textview不同字体大小，颜色
    fun getSpan(strings: Array<String>, sizes: IntArray, colors: IntArray): Spannable? {
        val builder: Spans.Builder = Spans.builder()
        for (i in strings.indices) {
            builder.text(strings[i], sizes[i], BaseApplication.instance().resources.getColor(colors[i]))
        }
        return builder.build()
    }

    //textview不同字体大小，颜色
    fun getSpan(strings: Array<String>, sizes: IntArray, colors: IntArray, textStyles: Array<TextStyle>): Spannable? {
        val builder: Spans.Builder = Spans.builder()
        for (i in strings.indices) {
            if (!TextUtils.isEmpty(strings[i])) {
                builder.text(strings[i], sizes[i], BaseApplication.instance().resources.getColor(colors[i]))
                    .style(
                        textStyles[i]
                    )
            }
        }
        return builder.build()
    }

    /**
     * 获取资源文件Uri
     *
     * @param file
     * @param intent
     * @return
     */
    fun getUri(file: File?, intent: Intent): Uri {
        return if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(
                BaseApplication.instance(), "com.rt.ipms_gm.fileprovider",
                file!!
            )
        } else {
            Uri.fromFile(file)
        }
    }

    fun dayHourMin(parkingTime: Int): String {
        val day = parkingTime / (60 * 24)
        val hour = parkingTime / 60 - day * 24
        val minute = parkingTime - day * 24 * 60 - hour * 60
        if (day == 0 && hour == 0) {
            return "${minute}分钟"
        } else if (day == 0) {
            return "${hour}小时${minute}分钟"
        } else {
            return "${day}天${hour}小时${minute}分钟"
        }
        return "${day}天${hour}小时${minute}分钟"
    }
}