package tech.androidplay.sonali.todo.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.appsflyer.AppsFlyerLib
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.TodoApplication
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_ID
import tech.androidplay.sonali.todo.utils.Constants.SHARED_PREFERENCE_NAME
import tech.androidplay.sonali.todo.utils.Notify
import tech.androidplay.sonali.todo.view.activity.MainActivity
import java.util.*
import javax.inject.Singleton

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Aug/2020
 * Email: ankush@androidplay.in
 */

/** [ApplicationContext] provides dependencies through application level injections */

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideAppsFlyerLib(): AppsFlyerLib = AppsFlyerLib.getInstance()

    @Provides
    fun providesApplication(): TodoApplication = TodoApplication()

    @Singleton
    @Provides
    fun provideFirebaseInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirestoreInstance() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseCrashlytics() = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesCalender(): Calendar = Calendar.getInstance()

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

    @ExperimentalCoroutinesApi
    @Provides
    fun providesPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TASK_FRAGMENT
        }
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentIntent(pendingIntent)

    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun providesNotify(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder
    ) = Notify(notificationManager, notificationBuilder)

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) =
        WorkManager.getInstance(context)
}
