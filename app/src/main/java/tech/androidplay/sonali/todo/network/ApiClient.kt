package tech.androidplay.sonali.todo.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 6/14/2020, 3:45 PM
 */
object ApiClient {

    private val interceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Authorization", "Basic YWRtaW5BdXRoOnZpc2h1QDE5OTI=")
            .build()
        chain.proceed(request)
    }

    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(interceptor)

    val retrofit: ApiNetwork = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())
        .build().create(ApiNetwork::class.java)
}

enum class JsonParser {
    MOCHI, GSON
}