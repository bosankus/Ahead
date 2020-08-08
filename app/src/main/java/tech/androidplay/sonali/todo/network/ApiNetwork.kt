package tech.androidplay.sonali.todo.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import tech.androidplay.sonali.todo.network.model.Model

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 6/14/2020, 9:52 AM
 */

const val BASE_URL = "https://api.on-track.in/api/"

interface ApiNetwork {

    @Multipart
    @POST("uploadCustomerDoc")
    suspend fun uploadImage(
        @Part("id") id: RequestBody,
        @Part("key") key: RequestBody,
        @Part("old") old: RequestBody,
        @Part image: MultipartBody.Part
    ): Model

}