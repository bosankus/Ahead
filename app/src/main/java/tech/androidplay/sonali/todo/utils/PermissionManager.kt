package tech.androidplay.sonali.todo.utils

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

@SuppressLint("InlinedApi")
fun Context.checkNotificationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}
