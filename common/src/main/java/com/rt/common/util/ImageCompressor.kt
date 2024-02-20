package com.rt.common.util

import android.content.Context
import android.text.TextUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * @desc: 图片压缩
 * @author: Uranus
 * @date: 2023/11/30
 * @version: 1.0v
 */
object ImageCompressor {
    private const val IGNORE_SIZE = 100

    fun compress(context: Context, imageFile: File, compressResult: CompressResult) {

        Luban.with(context)
            .load(imageFile)
            .ignoreBy(IGNORE_SIZE)
            .filter {
                !TextUtils.isEmpty(it)
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(file: File) {
                    compressResult.onSuccess(file)
                }

                override fun onError(e: Throwable) {
                    compressResult.onError(e)
                }
            }).launch()

    }

    interface CompressResult {
        fun onSuccess(file: File)
        fun onError(e: Throwable)
    }
}