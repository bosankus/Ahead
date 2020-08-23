package tech.androidplay.sonali.todo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.github.dhaval2404.imagepicker.ImagePicker

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 23/Aug/2020
 * Email: ankush@androidplay.in
 */
object ImageHelper {

    // Opens pop up window to pick or click image
    fun selectImage(context: Context) {
        ImagePicker.with(context as Activity)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    // To convert Intent to string path
    fun parseData(data: Intent?): String {
        return ImagePicker.getFilePath(data).toString()
    }
}
