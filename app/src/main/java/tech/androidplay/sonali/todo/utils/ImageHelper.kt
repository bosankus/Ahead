@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package tech.androidplay.sonali.todo.utils

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.github.dhaval2404.imagepicker.ImagePicker
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Constants.PLAY_STORE_LINK
import java.io.File

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

/** Holds all the image related extension methods to [selectImage], [loadImage], [compressImage]
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

suspend fun Uri.compressImage(context: Context): Uri {
    return Compressor.compress(context, File(this.path)) {
        quality(80)
    }.toUri()
}

fun ImageView.setTint(colorId: Int) {
    ImageViewCompat.setImageTintList(
        this, ColorStateList.valueOf(
            ContextCompat.getColor(this.context, colorId)
        )
    )
}
