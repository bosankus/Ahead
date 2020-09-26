package tech.androidplay.sonali.todo.utils

import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions
import tech.androidplay.sonali.todo.utils.Constants.READ_EXTERNAL_STORAGE
import tech.androidplay.sonali.todo.utils.Constants.STORAGE_PERMISSION_REQUEST_CODE
import tech.androidplay.sonali.todo.utils.Constants.STORAGE_PERMISSION_REQUEST_RATIONAL
import tech.androidplay.sonali.todo.utils.Constants.WRITE_EXTERNAL_STORAGE

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 6/15/2020, 11:23 AM
 */
object PermissionManager {

    fun askStoragePermission(fragment: Fragment) {
        if (hasStoragePermission(fragment.requireContext())) return
        else {
            EasyPermissions.requestPermissions(
                fragment,
                STORAGE_PERMISSION_REQUEST_RATIONAL,
                STORAGE_PERMISSION_REQUEST_CODE,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun hasStoragePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )

}