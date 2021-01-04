@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package tech.androidplay.sonali.todo.utils

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
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
        .start()
}

fun ImageView.loadImageCircleCropped(url: String?) {
    url.let {
        this.load(it) {
            crossfade(true)
            transformations(
                RoundedCornersTransformation(
                    20.0F, 20.0F, 20.0F, 20.0F
                )
            )
        }
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
