package tech.androidplay.sonali.todo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tech.androidplay.sonali.todo.data.room.TaskDatabase
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.SHARED_PREFERENCE_NAME
import tech.androidplay.sonali.todo.utils.Constants.TASK_TABLE
import tech.androidplay.sonali.todo.utils.Constants.USER_DISPLAY_IMAGE
import tech.androidplay.sonali.todo.utils.DatePickerFragment
import tech.androidplay.sonali.todo.utils.TimePickerFragment
import java.util.*
import javax.inject.Singleton

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Aug/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun providesTaskDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TASK_TABLE
        ).build()

    @Singleton
    @Provides
    fun getTaskDao(db: TaskDatabase) = db.getTaskDao()

    @Singleton
    @Provides
    fun provideFirebaseInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun providesFireStoreReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.FIRESTORE_COLLECTION)
    }

    @Singleton
    @Provides
    fun providesStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Singleton
    @Provides
    fun providesCacheManager(): CacheManager {
        return CacheManager()
    }

    @Singleton
    @Provides
    fun providesWorkManager(
        @ApplicationContext app: Context
    ): WorkManager {
        return WorkManager.getInstance(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideUserDisplayImage(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(USER_DISPLAY_IMAGE, "") ?: ""

    @Singleton
    @Provides
    fun providesCalender(): Calendar = Calendar.getInstance()

    @Singleton
    @Provides
    fun providesDatePickerFragment() = DatePickerFragment()

    @Singleton
    @Provides
    fun providesTimePickerFragment() = TimePickerFragment()
}
