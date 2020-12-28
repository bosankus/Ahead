package tech.androidplay.sonali.todo.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Dec/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ServiceComponent::class)
@ExperimentalCoroutinesApi
class ServiceModule {

    @ServiceScoped
    @Provides
    fun providesFirebaseRepository(
        firebaseCrashReport: FirebaseCrashlytics,
        firebaseAuth: FirebaseAuth,
        storageReference: StorageReference,
        fireStore: FirebaseFirestore
    ) = FirebaseRepository(firebaseCrashReport, firebaseAuth, storageReference, fireStore)
}