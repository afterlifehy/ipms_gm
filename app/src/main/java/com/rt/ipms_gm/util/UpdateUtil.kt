package com.rt.ipms_gm.util

import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PathUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.rt.base.bean.UpdateBean
import com.rt.base.help.ActivityCacheManager
import com.rt.base.util.ToastUtil
import com.rt.ipms_gm.dialog.UpdateDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateUtil {
    private var updateDialog: UpdateDialog? = null
    private var updateBean: UpdateBean? = null

    companion object {
        private var updateUtil: UpdateUtil? = null
        val instance: UpdateUtil?
            get() {
                if (updateUtil == null) {
                    updateUtil = UpdateUtil()
                }
                return updateUtil
            }
    }

    fun checkNewVersion(updateBean: UpdateBean, inter: UpdateInterface) {
        this.updateBean = updateBean
        if (updateBean.state == "0") {
            updateDialog = UpdateDialog(updateBean, object : UpdateDialog.updateCallBack {
                override fun confirm() {
                    inter.requestionPermission()
                }

            })
            updateDialog?.show()
        }
    }

    fun downloadFileAndInstall() {
        updateDialog?.downLoadUI()
        ToastUtil.showMiddleToast("开始下载更新")
        GlobalScope.launch(Dispatchers.IO) {
            FileDownloader.setup(ActivityCacheManager.instance().getCurrentActivity())
            val path = "${PathUtils.getExternalDownloadsPath()}/${FileDownloadUtils.generateFileName(updateBean?.url)}.apk"
            FileDownloader.getImpl().create(updateBean?.url)
                .setPath(path)
                .setListener(object : FileDownloadListener() {
                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            updateDialog?.updateProgress((soFarBytes * 100f / totalBytes.toFloat()).toInt())
                        }
                    }

                    override fun completed(task: BaseDownloadTask?) {
                        AppUtils.installApp(path)
                        if (updateBean?.force == "1") {
                            updateDialog?.updateUI()
                        } else if (updateBean?.force == "0") {
                            updateDialog?.dismiss()
                        }
                    }

                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun error(task: BaseDownloadTask?, e: Throwable?) {
                    }

                    override fun warn(task: BaseDownloadTask?) {
                    }

                }).start()
        }
    }

    interface UpdateInterface {
        fun requestionPermission()
    }
}
