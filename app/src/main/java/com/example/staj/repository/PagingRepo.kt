package com.example.staj.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.staj.data.local.AppDatabase
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.models.Movie
import com.example.staj.paging.MovieRemoteMediator
import com.example.staj.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PagingRepo @Inject constructor(
    private val movieListAPI: MovieListAPI,
    private val database: AppDatabase
) {
    fun getAllMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { database.MovieDao().getAllMoviesPaging() }
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                service = movieListAPI,
                database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


}