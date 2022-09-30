package com.igoapp.i_go.di

import android.app.Application
import android.app.Notification
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Room
import com.igoapp.i_go.feature_note._constants.Const.RemoteConst.BASE_URL
import com.igoapp.i_go.feature_note.data.notification_source.NotificationDatabase
import com.igoapp.i_go.feature_note.data.remote.PatientAPI
import com.igoapp.i_go.feature_note.data.remote.UserAPI
import com.igoapp.i_go.feature_note.data.repository.NotificationRepositoryImpl
import com.igoapp.i_go.feature_note.data.repository.PatientRepositoryImpl
import com.igoapp.i_go.feature_note.data.repository.UserRepositoryImpl
import com.igoapp.i_go.feature_note.data.storage.IdStore
import com.igoapp.i_go.feature_note.data.storage.TokenStore
import com.igoapp.i_go.feature_note.data.storage.dataStore
import com.igoapp.i_go.feature_note.data.storage.idStore
import com.igoapp.i_go.feature_note.domain.repository.NotificationRepository
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import com.igoapp.i_go.feature_note.domain.use_case.notification.AddNotification
import com.igoapp.i_go.feature_note.domain.use_case.notification.DeleteNotification
import com.igoapp.i_go.feature_note.domain.use_case.notification.GetNotifications
import com.igoapp.i_go.feature_note.domain.use_case.notification.NotificationUseCases
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // 인터셉터 제공
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        @ApplicationContext appContext: Context,
    ): Interceptor = Interceptor { chain ->
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${
                    runBlocking {
                        appContext.dataStore.data.map {
                            it[stringPreferencesKey("user")] }.first()
                        appContext.dataStore.data.map {
                            it[stringPreferencesKey("id")] }.first()
                        appContext.idStore.data.map {
                            it[stringPreferencesKey("user")] }.first()
                        appContext.idStore.data.map {
                            it[stringPreferencesKey("hospital")] }.first()
                        appContext.idStore.data.map {
                            it[stringPreferencesKey("patient")] }.first()
                    }
                } "
            )
            .build()
        return@Interceptor chain.proceed(newRequest)
    }
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor() { it.log() }.setLevel(HttpLoggingInterceptor.Level.BODY)

    // client 제공
    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Retrofit 제공
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
    }

    // patient api 제공
    @Provides
    @Singleton
    fun providePatientsApi(retrofit: Retrofit): PatientAPI {
        return retrofit.create(PatientAPI::class.java)
    }

    // user api 제공
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    // USER repository 제공
    @Provides
    @Singleton
    fun provideUserRepository(store: TokenStore, idStore: IdStore, api: UserAPI): UserRepository {
        return UserRepositoryImpl(store, idStore, api)
    }

    // patient repository 제공
    @Provides
    @Singleton
    fun providePatientRepository(api: PatientAPI): PatientRepository {
        return PatientRepositoryImpl(api)
    }

    // Notification Database 제공
    @Provides
    @Singleton
    fun provideNotificationDatabase(app: Application): NotificationDatabase {
        return Room.databaseBuilder(
            app,
            NotificationDatabase::class.java,
            NotificationDatabase.DATABASE_NAME
        ).build()
    }

    //
    @Provides
    @Singleton
    fun provideNotificationRepository(db: NotificationDatabase): NotificationRepository {
        return NotificationRepositoryImpl(db.notificationDao)
    }

    @Provides
    @Singleton
    fun provideNotificationUseCases(repository: NotificationRepository): NotificationUseCases {
        return NotificationUseCases(
            getNotifications = GetNotifications(repository),
            addNotification = AddNotification(repository),
            deleteNotification = DeleteNotification(repository)
        )
    }
}