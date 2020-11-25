package tech.androidplay.sonali.todo.data.pixabay

import com.google.firebase.auth.FirebaseUser
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Nov/2020
 * Email: ankush@androidplay.in
 */
interface PixabayImpl {

    suspend fun searchForImage(query: String)
}