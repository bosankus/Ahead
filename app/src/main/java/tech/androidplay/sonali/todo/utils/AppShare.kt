package tech.androidplay.sonali.todo.utils

import android.content.Intent
import androidx.fragment.app.Fragment
import tech.androidplay.sonali.todo.utils.Constants.PLAY_STORE_LINK

/**Created by
Author: Ankush Bose
Date: 11,June,2021
 **/

/**
 * This AppShare object holds [shareApp] method. When called creates a global share intent.
 * */

fun Fragment.shareApp() {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    val shareText =
        "Manage projects and personal tasks on the go collaboratively. Get it now $PLAY_STORE_LINK"
    val shareSubText = "Ahead - choose cleaner way"
    sharingIntent.apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, shareSubText)
        putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(this, "Spread via"))
    }
}