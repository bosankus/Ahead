package tech.androidplay.sonali.todo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 6/15/2020, 11:23 AM
 */
class PermissionManager {

    fun requestPermission(context: Context): Boolean {
        var permissionStatus: Boolean = false

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!(ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            ) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    Companion.PERMISSION_CODE
                )
                permissionStatus = true
            }
        } else {
            permissionStatus = true
        }
        return permissionStatus
    }

    companion object {
        const val PERMISSION_CODE: Int = 1001
    }
}