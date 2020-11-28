package tech.androidplay.sonali.todo.data.pixabay

import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.data.remote.PixabayAPI
import tech.androidplay.sonali.todo.utils.Resource
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */
class PixabayRepository @Inject constructor(private val pixabayApi: PixabayAPI) : PixabayImageApi {

    override suspend fun searchForImage(query: String): Resource<PixabayImageResponse> {
        return try {
            val result = pixabayApi.searchForImage(query)
            if (result.isSuccessful) {
                result.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Something went wrong", null)
            } else Resource.error("Something went wrong", null)
        } catch (e: Exception) {
            Resource.error("Oops! ${e.message}", null)
        }
    }
}