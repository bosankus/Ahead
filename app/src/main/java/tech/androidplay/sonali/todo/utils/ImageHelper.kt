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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import java.io.File

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

fun Fragment.selectImage() {
    ImagePicker.with(this)
        .crop()
        .compress(500)
        .maxResultSize(500, 500)
        .start(1000)
}

fun ImageView.loadImage(url: String?) {
    url.let {
        Glide.with(this.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterInside())
            .into(this)
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

fun Fragment.shareApp() {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    val shareText =
        "Manage projects and personal tasks on the go. Download now ${Constants.PLAY_STORE_LINK}"
    val shareSubText = "Think Ahead - a better way"
    sharingIntent.apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, shareSubText)
        putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(this, "Share via"))
    }
}
