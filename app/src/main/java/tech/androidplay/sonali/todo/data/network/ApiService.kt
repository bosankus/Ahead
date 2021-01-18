package tech.androidplay.sonali.todo.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import tech.androidplay.sonali.todo.data.model.Quote

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 15/Jan/2021
 * Email: ankush@androidplay.in
 */
interface ApiService {

    @Headers("x-rapidapi-key:1bf5a89813msh56794ecdecf4fddp147016jsnf16656420e1a")
    @GET("quote")
    suspend fun getQuote(): Quote
}