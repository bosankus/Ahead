package tech.androidplay.sonali.todo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tech.androidplay.sonali.todo.BuildConfig
import tech.androidplay.sonali.todo.data.model.PixabayImageResponse

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Nov/2020
 * Email: ankush@androidplay.in
 */
interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.PXB_API_KEY
    ): Response<PixabayImageResponse>
}