package tech.androidplay.sonali.todo.di

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.TodoApplication
import tech.androidplay.sonali.todo.ui.activity.MainActivity
import tech.androidplay.sonali.todo.ui.picker.DatePickerFragment
import tech.androidplay.sonali.todo.ui.picker.TimePickerFragment
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.SHARED_PREFERENCE_NAME
import tech.androidplay.sonali.todo.utils.Constants.USER_DISPLAY_IMAGE
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

    @Provides
    fun providesApplication() = TodoApplication()

    @Singleton
    @Provides
    fun provideFirebaseInstance() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFireStoreReference() =
        FirebaseFirestore.getInstance().collection(Constants.FIRESTORE_COLLECTION)

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

    @Provides
    fun providesAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context,
    ): NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(true)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_time)


    @Provides
    fun providesPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TASK_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}
