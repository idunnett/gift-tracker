package com.example.gifttracker

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE=200
    val TAG="jcy-MainActivity"
    private val ACTION_REQUEST_PERMISSIONS = 0x001


    private val NEEDED_PERMISSIONS = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }



    private fun afterRequestPermission(requestCode: Int, isAllGranted: Boolean) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
            } else {
                val dg = AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("The application is not authorized and cannot be used temporarily, please reopen it after authorization")
                    .setNegativeButton("Confirm") { dialog, i ->
                        dialog.dismiss()
                        finish()
                    }.create()
                dg.setCancelable(false)
                dg.setCanceledOnTouchOutside(false)
                dg.show()
            }
        }
    }
    private fun init() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                this,
                NEEDED_PERMISSIONS,
                ACTION_REQUEST_PERMISSIONS
            )
        }
    }


    protected fun checkPermissions(neededPermissions: Array<String>?): Boolean {
        if (neededPermissions == null || neededPermissions.size == 0) {
            return true
        }
        var allGranted = true
        for (neededPermission in neededPermissions) {
            Log.d(TAG,
                "checkPermissions: neededPermission " + neededPermission + " check " + ContextCompat.checkSelfPermission(
                    this,
                    neededPermission
                )
            )
            allGranted = allGranted && ContextCompat.checkSelfPermission(
                this,
                neededPermission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return allGranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isAllGranted = true
        for (grantResult in grantResults) {
            isAllGranted = isAllGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
        }
        afterRequestPermission(requestCode, isAllGranted)
    }
}