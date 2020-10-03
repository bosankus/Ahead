package tech.androidplay.sonali.todo.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.ui.activity.MainActivity
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_ID

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(true)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_time)
        .setContentTitle("Syncing task")
        .setContentText("Task Sync started")
        .setContentIntent(pendingIntent)


    @ServiceScoped
    @Provides
    fun providesPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TASK_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}