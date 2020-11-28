package tech.androidplay.sonali.todo.data.pixabay

import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.utils.Resource
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Nov/2020
 * Email: ankush@androidplay.in
 */
interface PixabayImageApi {

    suspend fun searchForImage(query: String): Resource<PixabayImageResponse>
}