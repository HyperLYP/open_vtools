package com.omarea.shell_utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.widget.Toast
import com.omarea.common.shell.KeepShellPublic
import com.omarea.common.shell.RootFile
import com.omarea.common.ui.ProgressBarDialog
import com.omarea.utils.CommonCmds

/**
 * Created by Hello on 2017/11/01.
 */

class BackupRestoreUtils(var context: Activity) {
    val dialog: ProgressBarDialog = ProgressBarDialog(context)
    internal var myHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        private var bootPartPath = "/dev/block/bootdevice/by-name/boot"
        private var recPartPath = "/dev/block/bootdevice/by-name/recovery"
        private var dtboPartPath = "/dev/block/bootdevice/by-name/dtbo"
        private var persistPartPath = "/dev/block/bootdevice/by-name/persist"
        private var modemPartPath = "/dev/block/bootdevice/by-name/modem"
        private var vabBootAPartPath = "/dev/block/bootdevice/by-name/boot_a"
        private var vabBootBPartPath = "/dev/block/bootdevice/by-name/boot_b"
        private var vabVendorBootAPartPath = "/dev/block/bootdevice/by-name/vendor_boot_a"
        private var vabVendorBootBPartPath = "/dev/block/bootdevice/by-name/vendor_boot_b"
        private var vabModemAPartPath = "/dev/block/bootdevice/by-name/modem_a"
        private var vabModemBPartPath = "/dev/block/bootdevice/by-name/modem_b"

        fun isSupport(): Boolean {
            if (
                RootFile.itemExists(bootPartPath) ||
                RootFile.itemExists(recPartPath) ||
                RootFile.itemExists(modemPartPath) ||
                RootFile.itemExists(vabBootAPartPath) || RootFile.itemExists(vabBootBPartPath) ||
                RootFile.itemExists(vabVendorBootAPartPath) || RootFile.itemExists(vabVendorBootBPartPath) ||
                RootFile.itemExists(vabModemAPartPath) || RootFile.itemExists(vabModemBPartPath)
            ) {
                return true
            }

            var r = false
            val boots = KeepShellPublic.doCmdSync("ls /dev/block/platform/*/by-name/BOOT").split("\n")
            if (boots.isNotEmpty() && boots.first().startsWith("/dev/block/platform") && boots.first().endsWith("/by-name/BOOT")) {
                bootPartPath = boots.first()
                r = true
            }
            val recs = KeepShellPublic.doCmdSync("ls /dev/block/platform/*/by-name/RECOVERY").split("\n")
            if (recs.isNotEmpty() && recs.first().startsWith("/dev/block/platform") && recs.first().endsWith("/by-name/RECOVERY")) {
                recPartPath = recs.first()
                r = true
            }
            val dtbos = KeepShellPublic.doCmdSync("ls /dev/block/platform/*/by-name/DTBO").split("\n")
            if (dtbos.isNotEmpty() && dtbos.first().startsWith("/dev/block/platform") && dtbos.first().endsWith("/by-name/DTBO")) {
                dtboPartPath = dtbos.first()
                r = true
            }
            val persists = KeepShellPublic.doCmdSync("ls /dev/block/platform/*/by-name/PERSIST").split("\n")
            if (persists.isNotEmpty() && persists.first().startsWith("/dev/block/platform") && persists.first().endsWith("/by-name/PERSIST")) {
                persistPartPath = persists.first()
                r = true
            }
            return r
        }
    }

    //???????????????
    fun showProgressBar() {
        myHandler.post {
            dialog.showDialog("??????????????????...")
        }
    }

    //???????????????
    fun hideProgressBar() {
        myHandler.post {
            dialog.hideDialog()
        }
    }

    //??????????????????
    fun showMsg(msg: String, longMsg: Boolean) {
        myHandler.post {
            Toast.makeText(context, msg, if (longMsg) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
        }
    }

    //??????Boot
    fun flashBoot(path: String) {
        FlashImgThread(path, bootPartPath).start()
    }

    fun flashRecovery(path: String) {
        FlashImgThread(path, recPartPath).start()
    }

    fun flashDTBO(path: String) {
        FlashImgThread(path, dtboPartPath).start()
    }

    fun flashPersist(path: String) {
        FlashImgThread(path, persistPartPath).start()
    }

    fun flashModem(path: String) {
        FlashImgThread(path, modemPartPath).start()
    }

    fun flashBootA(path: String) {
        FlashImgThread(path, vabBootAPartPath).start()
    }

    fun flashBootB(path: String) {
        FlashImgThread(path, vabBootBPartPath).start()
    }

    fun flashVendorBootA(path: String) {
        FlashImgThread(path, vabVendorBootAPartPath).start()
    }

    fun flashVendorBootB(path: String) {
        FlashImgThread(path, vabVendorBootBPartPath).start()
    }

    fun flashModemA(path: String) {
        FlashImgThread(path, vabModemAPartPath).start()
    }

    fun flashModemB(path: String) {
        FlashImgThread(path, vabModemBPartPath).start()
    }

    internal inner class FlashImgThread(var inputPath: String, var outputPath: String) : Thread() {
        override fun run() {
            if (!isSupport() || !RootFile.itemExists(outputPath)) {
                showMsg("???????????????????????????", true)
                return
            }
            showMsg("????????????\n$inputPath\n?????????????????????", true)
            showProgressBar()
            if (KeepShellPublic.doCmdSync("dd if=\"$inputPath\" of=$outputPath") != "error") {
                showMsg("???????????????", true)
            } else {
                showMsg("??????????????????", true)
            }
            hideProgressBar()
        }
    }


    fun saveBoot() {
        SaveImgThread(bootPartPath, "boot").start()
    }

    fun saveRecovery() {
        SaveImgThread(recPartPath, "recovery").start()
    }

    fun saveDTBO() {
        SaveImgThread(dtboPartPath, "dtbo").start()
    }

    fun savePersist() {
        SaveImgThread(persistPartPath, "persist").start()
    }

    fun saveModem() {
        SaveImgThread(modemPartPath, "modem").start()
    }

    fun saveBootA() {
        SaveImgThread(vabBootAPartPath, "boot_a").start()
    }

    fun saveBootB() {
        SaveImgThread(vabBootBPartPath, "boot_b").start()
    }

    fun saveVendorBootA() {
        SaveImgThread(vabVendorBootAPartPath, "vendor_boot_a").start()
    }

    fun saveVendorBootB() {
        SaveImgThread(vabVendorBootBPartPath, "vendor_boot_b").start()
    }

    fun saveModemA() {
        SaveImgThread(vabModemAPartPath, "modem_a").start()
    }

    fun saveModemB() {
        SaveImgThread(vabModemBPartPath, "modem_b").start()
    }

    internal inner class SaveImgThread(var inputPath: String, var outputName: String) : Thread() {
        override fun run() {
            val outPath = "${CommonCmds.SDCardDir}/$outputName.img"

            if (!isSupport() || !RootFile.itemExists(inputPath)) {
                showMsg("???????????????????????????", true)
                return
            }
            showProgressBar()
            if (KeepShellPublic.doCmdSync("dd if=$inputPath of=$outPath\n") != "error") {
                showMsg("???????????????????????????????????????$outPath ???", true)
            } else {
                showMsg("???????????????????????????", true)
            }
            hideProgressBar()
        }
    }
}
