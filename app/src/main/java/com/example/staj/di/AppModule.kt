package com.example.staj.di

import android.content.Context
import androidx.room.Room
import com.example.staj.data.local.AppDatabase
import com.example.staj.data.local.MovieDao
import com.example.staj.data.local.TVDao
import com.example.staj.data.remote.MovieAPI
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.data.remote.MovieSearchAPI
import com.example.staj.data.remote.TvAPI
import com.example.staj.data.remote.TvListAPI
import com.example.staj.data.remote.TvSearchAPI
import com.example.staj.util.Constants.BASE_URL
import com.example.staj.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule {
    @Module
    @InstallIn(SingletonComponent::class)
    internal object MovieModule {

        @Provides
        fun provideDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun provideMovieDao(database: AppDatabase): MovieDao {
            return database.MovieDao()
        }

        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideMovieListAPI(retrofit: Retrofit): MovieListAPI {
            return retrofit.create(MovieListAPI::class.java)
        }

        @Provides
        fun provideMovieAPI(retrofit: Retrofit): MovieAPI {
            return retrofit.create(MovieAPI::class.java)
        }

        @Provides
        fun provideMovieSearchAPI(retrofit: Retrofit): MovieSearchAPI {
            return retrofit.create(MovieSearchAPI::class.java)
        }

        @Provides
        fun provideExceptionHandler(): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    internal object TVModule {

        @Provides
        fun provideTVDao(database: AppDatabase): TVDao {
            return database.TVDao()
        }

        @Provides
        fun provideTvListAPI(retrofit: Retrofit): TvListAPI {
            return retrofit.create(TvListAPI::class.java)
        }

        @Provides
        fun provideTvAPI(retrofit: Retrofit): TvAPI {
            return retrofit.create(TvAPI::class.java)
        }

        @Provides
        fun provideTvSearchAPI(retrofit: Retrofit): TvSearchAPI {
            return retrofit.create(TvSearchAPI::class.java)
        }
    }
}
