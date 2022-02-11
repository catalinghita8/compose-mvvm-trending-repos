package com.codingtroops.repositoriesapp.di

import android.content.Context
import androidx.room.Room
import com.codingtroops.repositoriesapp.data.ReposRepository
import com.codingtroops.repositoriesapp.data.local.ReposDao
import com.codingtroops.repositoriesapp.data.local.ReposDb
import com.codingtroops.repositoriesapp.data.remote.ReposApiService
import com.codingtroops.repositoriesapp.domain.PredefinedContent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun providePredefinedContent() = PredefinedContent

    @Provides
    @Singleton
    fun provideReposRepository(
        apiService: ReposApiService,
        roomDao: ReposDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ReposRepository {
        return ReposRepository(apiService, roomDao, dispatcher)
    }

    @Provides
    @Singleton
    fun provideRoomDao(@ApplicationContext context: Context): ReposDao {
        return Room.databaseBuilder(
            context.applicationContext,
            ReposDb::class.java,
            "repos_database"
        )
            .fallbackToDestructiveMigration()
            .build()
            .dao
    }

    @Provides
    @Singleton
    fun provideRemoteService(): ReposApiService {
        return Retrofit
            .Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://trendings.herokuapp.com/")
            .build()
            .create(ReposApiService::class.java)
    }
}

