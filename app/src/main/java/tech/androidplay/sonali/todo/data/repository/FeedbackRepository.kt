package tech.androidplay.sonali.todo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Feb/2021
 * Email: ankush@androidplay.in
 */
class FeedbackRepository @Inject constructor(
    fireStore: FirebaseFirestore,
    firebaseAuth: FirebaseAuth
) {

    val currentUser = firebaseAuth.currentUser
    private val feedbackReference = fireStore.collection(Constants.FEEDBACK_COLLECTION)

    suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String> {
        return try {
            val feedback = feedbackReference
                .add(hashMap)
                .await()
            ResultData.Success(feedback.id)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }
}