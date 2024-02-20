package com.rt.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.max

object ImageUtil {
    var space = 100f
    fun addWaterMark3(src: Bitmap, waterBerthId: String?, waterArrivedTime: String?, context: Context): Bitmap {
        println("aaa " + src.getWidth() + " {} " + src.getHeight())
        val layoutWidth = src.getWidth()
        val layoutHeight = src.getHeight()
        if (layoutHeight < layoutWidth) {
            // 横屏状态下
        }
        val newb = Bitmap.createBitmap(layoutWidth, (layoutHeight + space).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newb)
        canvas.drawColor(Color.BLACK)
        canvas.drawBitmap(src, 0f, space, null)

        //启用抗锯齿和使用设备的文本字距
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DEV_KERN_TEXT_FLAG)
        //字体的相关设置
        textPaint.textSize = 22f //字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD)
        textPaint.setColor(Color.RED)
        textPaint.setShadowLayer(3f, 1f, 1f, context.resources.getColor(com.rt.base.R.color.black))
        val layout = StaticLayout(waterBerthId, textPaint, layoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)
        canvas.save()
        canvas.translate(20f, 20f) //从20，20开始画
        layout.draw(canvas)
        canvas.save()
        val layout2 = StaticLayout(
            waterArrivedTime, textPaint, layoutWidth,
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true
        )
        canvas.save()
        canvas.translate(0f, 40f)
        layout2.draw(canvas)
        canvas.save()
        return newb
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    fun getCompressedImage(srcPath: String?, targetWidth: Float, targetHeight: Float): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(srcPath, options)
        val originalWidth = options.outWidth
        val originalHeight = options.outHeight

        // 计算缩放比例
        val widthScale = targetWidth / originalWidth
        val heightScale = targetHeight / originalHeight
        val scaleFactor = max(widthScale.toDouble(), heightScale.toDouble()).toFloat()

        // 设置缩放参数
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateInSampleSize(options, scaleFactor)

        // 重新加载图片
        var bitmap = BitmapFactory.decodeFile(srcPath, options)

        // 进行缩放
        if (bitmap != null) {
            val scaleWidth = targetWidth / bitmap.getWidth()
            val scaleHeight = targetHeight / bitmap.getHeight()
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
        }
        return compressImageByQuality(bitmap) // 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    fun compressImageByQuality(image: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos) // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 95
        while (baos.toByteArray().size / 1024 > 50 && options > 5) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
            baos.reset() // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos) // 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5 // 每次都减少5
        }
        val isBm = ByteArrayInputStream(baos.toByteArray()) // 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null) // 把ByteArrayInputStream数据生成图片
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, scaleFactor: Float): Int {
        var inSampleSize = 1
        val reqWidth = (options.outWidth * scaleFactor).toInt()
        val reqHeight = (options.outHeight * scaleFactor).toInt()
        while (reqWidth / inSampleSize > 1140 || reqHeight / inSampleSize > 945) {
            inSampleSize *= 2
        }
        return inSampleSize
    }

}