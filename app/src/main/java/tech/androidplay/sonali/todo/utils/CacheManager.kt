package tech.androidplay.sonali.todo.utils

import android.content.Context
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import java.io.File

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/26/2020, 4:29 AM
 */

class CacheManager {

    fun clearCache(context: Context) {
        try {
            val dir: File? = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)
//                logMessage("$dir")
            }
        } catch (e: Exception) {
//            logMessage("${e.message}")
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()!!
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}