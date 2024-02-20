package com.rt.common.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.rt.base.BaseApplication
import java.io.*


/**
 * Created by huy  on 2022/8/13.
 */
object FileUtil {

    fun getPath(): String {
        if (Build.VERSION.SDK_INT >= 29) {
            return BaseApplication.instance().getExternalFilesDir("")?.absolutePath.toString()
        } else {
            return Environment.getExternalStorageDirectory().path.toString()
        }
    }

    fun mkdirFile(path: String) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    fun getSandboxPath(): String {
        val externalFilesDir: File = BaseApplication.instance().getExternalFilesDir("")!!
        val customFile = File(externalFilesDir.absolutePath, "Runba")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

    fun saveBitmap(bmp: Bitmap, path: String, fileName: String): File {
        val appDir = File(path)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 通知相册有新图片
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(file)
        intent.data = uri
        BaseApplication.instance().sendBroadcast(intent)
        return file
    }

    fun compressToMaxSize(src: Bitmap, maxSizeKB: Int, recycle: Boolean): Bitmap? {
        val baos = ByteArrayOutputStream()
        var quality = 100 // 初始质量设为100
        src.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        while (baos.toByteArray().size / 1024 > maxSizeKB) {
            baos.reset() // 重置baos即清空baos
            quality -= 10 // 每次减少10
            src.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        }
        val bytes = baos.toByteArray()
        if (recycle && !src.isRecycled) {
            src.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun saveBitmapMediaStore(image: Bitmap) {
        val mImageTime = System.currentTimeMillis()
        val SCREENSHOT_FILE_NAME_TEMPLATE = "rt_%s.png" //图片名称，以"rt"+时间戳命名
        val mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, mImageTime)
        val values = ContentValues()
        values.put(
            MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                    + File.separator + "rt"
        )
        //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, mImageFileName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        values.put(MediaStore.MediaColumns.DATE_ADDED, mImageTime / 1000)
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, mImageTime / 1000)
        values.put(MediaStore.MediaColumns.DATE_EXPIRES, (mImageTime + DateUtils.DAY_IN_MILLIS) / 1000)
        values.put(MediaStore.MediaColumns.IS_PENDING, 1)
        val resolver: ContentResolver = BaseApplication.instance().contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            // First, write the actual data for our screenshot
            resolver.openOutputStream(uri!!).use { out ->
                if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw IOException("Failed to compress")
                }
            }
            // Everything went well above, publish it!
            values.clear()
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            values.putNull(MediaStore.MediaColumns.DATE_EXPIRES)
            resolver.update(uri, values, null, null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageInQ(bitmap: Bitmap): Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = BaseApplication.instance().contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri as Uri
    }

    fun legacySave(file: File, bitmap: Bitmap): Uri {
        val outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        MediaScannerConnection.scanFile(
            BaseApplication.instance(), arrayOf(file.absolutePath),
            null, null
        )
        return FileProvider.getUriForFile(
            BaseApplication.instance(), "${BaseApplication.instance().packageName}.provider",
            file
        )
    }

    /**
     * 保存图片到沙盒目录
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param bitmap   文件
     * @return 路径，为空时表示保存失败
     */
    fun FileSaveToInside(fileName: String?, bitmap: Bitmap): File {
        var fos: FileOutputStream? = null
        var path: String? = null
        var file: File? = null
        try {
            val folder = BaseApplication.instance().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (folder!!.exists() || folder.mkdir()) {
                file = File(folder, fileName.toString())
                fos = FileOutputStream(file)
                //写入文件
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos)
                fos.flush()
                path = file.absolutePath
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //返回路径
        return file!!
    }

    fun fileToBase64(file: File): String? {
        try {
            val inputStream = FileInputStream(file)
            val bytes = ByteArray(file.length().toInt())
            inputStream.read(bytes)
            inputStream.close()

            // 使用Base64编码将字节数组转换为字符串
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}