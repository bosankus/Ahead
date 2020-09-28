package tech.androidplay.sonali.todo.di

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.FIRESTORE_COLLECTION
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Aug/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ActivityComponent::class)
class AppModule {

    @Provides
    fun provideFirebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun providesFireStoreReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(FIRESTORE_COLLECTION)
    }

    @Provides
    fun providesStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Provides
    fun providesCacheManager(): CacheManager {
        return CacheManager()
    }

    @Provides
    fun providesAlertDialog(@ActivityContext context: Context): AlertDialog.Builder {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Do you want to delete the task")
        alertDialog.setCancelable(true)
        return alertDialog
    }

    @Provides
    fun providesDatePickerDialog() =
        MaterialDatePicker.Builder.datePicker().build()

    @Provides
    fun providesCalender(): Calendar = Calendar.getInstance()

    @Provides
    fun providesTimePickerDialogTimeSetListener(calendar: Calendar): TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
        }


    @Provides
    fun providesTaskRepository(
        firebaseAuth: FirebaseAuth,
        collectionReference: CollectionReference,
    ): TaskRepository {
        return TaskRepository(firebaseAuth, collectionReference)
    }

    @Provides
    fun provideTaskViewModel(
        taskRepository: TaskRepository
    ): TaskViewModel {
        return TaskViewModel(taskRepository)
    }

    @Provides
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepository(firebaseAuth)
    }

    @Provides
    fun providesTodoAdapter(
        alertDialog: AlertDialog.Builder,
        viewModel: TaskViewModel
    ): TodoAdapter {
        return TodoAdapter(viewModel, alertDialog)
    }

}