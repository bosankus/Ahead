@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package tech.androidplay.sonali.todo.utils

import android.content.res.*
import android.widget.*
import androidx.core.content.*
import androidx.core.widget.*
import androidx.fragment.app.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.bitmap.*
import com.github.dhaval2404.imagepicker.*
import tech.androidplay.sonali.todo.R

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

/** Holds all the image related extension methods to [selectImage], [loadImage]
 * & [setTint] of an image.
 * */

fun Fragment.selectImage() {
    ImagePicker.with(this)
        .crop()
        .compress(500)
        .maxResultSize(500, 500)
        .start(1000)
}

fun ImageView.loadImage(url: String?) {
    val imagePlaceHolder = this
    url.let {
        Glide.with(imagePlaceHolder.context)
            .load(url)
            .placeholder(R.drawable.ic_image_loading)
            .transform(CenterCrop())
            .into(imagePlaceHolder)
    }
}


fun ImageView.setTint(colorId: Int) {
    ImageViewCompat.setImageTintList(
        this, ColorStateList.valueOf(
            ContextCompat.getColor(this.context, colorId)
        )
    )
}
