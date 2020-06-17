package tech.androidplay.sonali.todo.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 6/16/2020, 11:35 PM
 */

object RequestBodyParser {

    fun parseText(string: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), string)
    }

    fun parseImage(uri: String): MultipartBody.Part {
        val file = File(uri)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}