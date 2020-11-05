package tech.androidplay.sonali.todo.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

object Extensions {

    fun Fragment.selectImage(context: Fragment) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }


}