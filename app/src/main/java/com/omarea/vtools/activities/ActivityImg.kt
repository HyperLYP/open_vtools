package com.omarea.vtools.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.omarea.Scene
import com.omarea.common.shared.FilePathResolver
import com.omarea.common.ui.DialogHelper
import com.omarea.shell_utils.BackupRestoreUtils
import com.omarea.utils.CommonCmds
import com.omarea.vtools.R
import kotlinx.android.synthetic.main.activity_img.*
import java.io.File
import java.util.*


class ActivityImg : ActivityBase() {
    private fun createItem(title: String, desc: String, key: String): HashMap<String, Any> {
        val item = HashMap<String, Any>()
        item.put("Title", title)
        item.put("Desc", desc)
        item.put("Key", key)
        return item
    }

    //获取SD卡可用空间
    fun getSDFreeSizeMB(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.availableBytes / 1024 / 1024 //剩余空间
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img)

        setBackArrow()

        onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        title = getString(R.string.menu_img)
    }

    fun onViewCreated() {
        val listItem = ArrayList<HashMap<String, Any>>()/*在数组中存放数据*/

        listItem.add(createItem(getString(R.string.backup_action_title_boot), getString(R.string.backup_action_desc_boot), "dump-boot"))
        listItem.add(createItem(getString(R.string.restore_action_title_boot), getString(R.string.restore_action_desc_boot), "flash-boot"))

        listItem.add(createItem(getString(R.string.backup_action_title_rec), getString(R.string.backup_action_desc_rec), "dump-rec"))
        listItem.add(createItem(getString(R.string.restore_action_title_rec), getString(R.string.restore_action_desc_rec), "flash-rec"))

        listItem.add(createItem(getString(R.string.backup_action_title_dtbo), getString(R.string.backup_action_desc_dtbo), "dump-dtbo"))
        listItem.add(createItem(getString(R.string.restore_action_title_dtbo), getString(R.string.restore_action_desc_dtbo), "flash-dtbo"))

        listItem.add(createItem(getString(R.string.backup_action_title_persist), getString(R.string.backup_action_desc_persist), "dump-persist"))
        listItem.add(createItem(getString(R.string.restore_action_title_persist), getString(R.string.restore_action_desc_persist), "flash-persist"))

        listItem.add(createItem(getString(R.string.backup_action_title_modem), getString(R.string.backup_action_desc_modem), "dump-modem"))
        listItem.add(createItem(getString(R.string.restore_action_title_modem), getString(R.string.restore_action_desc_modem), "flash-modem"))

        listItem.add(createItem(getString(R.string.backup_action_title_boot_a), getString(R.string.backup_action_desc_boot_a), "dump-boot-a"))
        listItem.add(createItem(getString(R.string.restore_action_title_boot_a), getString(R.string.restore_action_desc_boot_a), "flash-boot-a"))

        listItem.add(createItem(getString(R.string.backup_action_title_boot_b), getString(R.string.backup_action_desc_boot_b), "dump-boot-b"))
        listItem.add(createItem(getString(R.string.restore_action_title_boot_b), getString(R.string.restore_action_desc_boot_b), "flash-boot-b"))

        listItem.add(createItem(getString(R.string.backup_action_title_vendor_boot_a), getString(R.string.backup_action_desc_vendor_boot_a), "dump-vendor-boot-a"))
        listItem.add(createItem(getString(R.string.restore_action_title_vendor_boot_a), getString(R.string.restore_action_desc_vendor_boot_a), "flash-vendor-boot-a"))

        listItem.add(createItem(getString(R.string.backup_action_title_vendor_boot_b), getString(R.string.backup_action_desc_vendor_boot_b), "dump-vendor-boot-b"))
        listItem.add(createItem(getString(R.string.restore_action_title_vendor_boot_b), getString(R.string.restore_action_desc_vendor_boot_b), "flash-vendor-boot-b"))

        listItem.add(createItem(getString(R.string.backup_action_title_modem_a), getString(R.string.backup_action_desc_modem_a), "dump-modem-a"))
        listItem.add(createItem(getString(R.string.restore_action_title_modem_a), getString(R.string.restore_action_desc_modem_a), "flash-modem-a"))

        listItem.add(createItem(getString(R.string.backup_action_title_modem_b), getString(R.string.backup_action_desc_modem_b), "dump-modem-b"))
        listItem.add(createItem(getString(R.string.restore_action_title_modem_b), getString(R.string.restore_action_desc_modem_b), "flash-modem-b"))

        val mSimpleAdapter = SimpleAdapter(
                this,
                listItem,
                R.layout.list_item_action,
                arrayOf("Title", "Desc"),
                intArrayOf(R.id.Title, R.id.Desc)
        )
        img_action_listview.adapter = mSimpleAdapter


        img_action_listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val key = listItem.get(position).get("Key")
            when (key) {
                "dump-boot" -> backupImg(BOOT_IMG)
                "flash-boot" -> chooseImgToFlash(BOOT_IMG)

                "dump-rec" -> backupImg(RECOVERY_IMG)
                "flash-rec" -> chooseImgToFlash(RECOVERY_IMG)

                "dump-dtbo" -> backupImg(DTBO_IMG)
                "flash-dtbo" -> chooseImgToFlash(DTBO_IMG)

                "dump-persist" -> backupImg(PERSIST_IMG)
                "flash-persist" -> chooseImgToFlash(PERSIST_IMG)

                "dump-modem" -> backupImg(MODEM_IMG)
                "flash-modem" -> chooseImgToFlash(MODEM_IMG)

                "dump-boot-a" -> backupImg(BOOT_A_IMG)
                "flash-boot-a" -> chooseImgToFlash(BOOT_A_IMG)

                "dump-boot-b" -> backupImg(BOOT_B_IMG)
                "flash-boot-b" -> chooseImgToFlash(BOOT_B_IMG)

                "dump-vendor-boot-a" -> backupImg(VENDOR_BOOT_A_IMG)
                "flash-vendor-boot-a" -> chooseImgToFlash(VENDOR_BOOT_A_IMG)

                "dump-vendor-boot-b" -> backupImg(VENDOR_BOOT_B_IMG)
                "flash-vendor-boot-b" -> chooseImgToFlash(VENDOR_BOOT_B_IMG)

                "dump-modem-a" -> backupImg(MODEM_A_IMG)
                "flash-modem-a" -> chooseImgToFlash(MODEM_A_IMG)

                "dump-modem-b" -> backupImg(MODEM_B_IMG)
                "flash-modem-b" -> chooseImgToFlash(MODEM_B_IMG)
            }
        }
    }

    private fun backupImg(action: Int) {
        if (getSDFreeSizeMB() < 200) {
            Scene.toast(getString(R.string.backup_space_small), Toast.LENGTH_LONG)
            return
        } else {
            var fileName = ""
            when (action) {
                BOOT_IMG -> fileName = "boot"
                RECOVERY_IMG -> fileName = "recovery"
                DTBO_IMG -> fileName = "dtbo"
                PERSIST_IMG -> fileName = "persist"
                MODEM_IMG -> fileName = "modem"
                BOOT_A_IMG -> fileName = "boot_a"
                BOOT_B_IMG -> fileName = "boot_b"
                VENDOR_BOOT_A_IMG -> fileName = "vendor_boot_a"
                VENDOR_BOOT_B_IMG -> fileName = "vendor_boot_b"
                MODEM_A_IMG -> fileName = "modem_a"
                MODEM_B_IMG -> fileName = "modem_b"
            }
            if (File("${CommonCmds.SDCardDir}/$fileName.img").exists()) {
                DialogHelper.confirm(this,
                        getString(R.string.backup_file_exists),
                        String.format(getString(R.string.backup_img_exists), fileName), {
                    backupImgConfirm(action)
                })
            } else {
                backupImgConfirm(action)
            }
        }
    }

    private fun backupImgConfirm(action: Int) {
        when (action) {
            BOOT_IMG -> BackupRestoreUtils(this).saveBoot()
            RECOVERY_IMG -> BackupRestoreUtils(this).saveRecovery()
            DTBO_IMG -> BackupRestoreUtils(this).saveDTBO()
            PERSIST_IMG -> BackupRestoreUtils(this).savePersist()
            MODEM_IMG -> BackupRestoreUtils(this).saveModem()
            BOOT_A_IMG -> BackupRestoreUtils(this).saveBootA()
            BOOT_B_IMG -> BackupRestoreUtils(this).saveBootB()
            VENDOR_BOOT_A_IMG -> BackupRestoreUtils(this).saveVendorBootA()
            VENDOR_BOOT_B_IMG -> BackupRestoreUtils(this).saveVendorBootB()
            MODEM_A_IMG -> BackupRestoreUtils(this).saveModemA()
            MODEM_B_IMG -> BackupRestoreUtils(this).saveModemB()
        }
    }

    private val BOOT_IMG = 0
    private val RECOVERY_IMG = 1
    private val DTBO_IMG = 2
    private val PERSIST_IMG = 3
    private val MODEM_IMG = 4
    private val BOOT_A_IMG = 5
    private val BOOT_B_IMG = 6
    private val VENDOR_BOOT_A_IMG = 7
    private val VENDOR_BOOT_B_IMG = 8
    private val MODEM_A_IMG = 9
    private val MODEM_B_IMG = 10

    private fun chooseImgToFlash(type: Int) {
        if (Build.VERSION.SDK_INT >= 30) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, type)
        } else {
            val intent = Intent(this, ActivityFileSelector::class.java)
            intent.putExtra("extension", "img")
            startActivityForResult(intent, type)
        }
    }

    private fun flash(imgPath: String, requestCode: Int) {
        //刷入recovery
        if (File(imgPath).exists()) {
            var partition = ""
            when (requestCode) {
                BOOT_IMG -> partition = "Boot"
                RECOVERY_IMG -> partition = "Recovery"
                DTBO_IMG -> partition = "DTBO"
                PERSIST_IMG -> partition = "Persist"
                MODEM_IMG -> partition = "Modem"
                BOOT_A_IMG -> partition = "Boot_A"
                BOOT_B_IMG -> partition = "Boot_B"
                VENDOR_BOOT_A_IMG -> partition = "Vendor_Boot_A"
                VENDOR_BOOT_B_IMG -> partition = "Vendor_Boot_B"
                MODEM_A_IMG -> partition = "Modem_A"
                MODEM_B_IMG -> partition = "Modem_B"
            }
            DialogHelper.confirm(this,
                    getString(R.string.flash_confirm),
                    "此操作将刷入${imgPath}到系统${partition}分区，如果你选择了错误的文件，刷入后可能导致手机无法开机！",
                    {
                        when (requestCode) {
                            BOOT_IMG -> BackupRestoreUtils(this).flashBoot(imgPath)
                            RECOVERY_IMG -> BackupRestoreUtils(this).flashRecovery(imgPath)
                            DTBO_IMG -> BackupRestoreUtils(this).flashDTBO(imgPath)
                            PERSIST_IMG -> BackupRestoreUtils(this).flashPersist(imgPath)
                            MODEM_IMG -> BackupRestoreUtils(this).flashModem(imgPath)
                            BOOT_A_IMG -> BackupRestoreUtils(this).flashBootA(imgPath)
                            BOOT_B_IMG -> BackupRestoreUtils(this).flashBootB(imgPath)
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (Build.VERSION.SDK_INT >= 30) {
                val absPath = FilePathResolver().getPath(this, data.data)
                if (absPath != null) {
                    if (absPath.endsWith(".img")) {
                        flash(absPath, requestCode)
                    } else {
                        Toast.makeText(this, "选择的文件无效（应当是.img文件）！", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "所选的文件没找到！", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (data.extras?.containsKey("file") == true) {
                    val path = data.extras!!.getString("file")!!
                    flash(path, requestCode)
                } else {
                    Toast.makeText(this, "所选的文件没找到！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
