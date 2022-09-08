package com.example.staj.paging

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.staj.BuildConfig
import com.example.staj.data.local.AppDatabase
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.models.Movie
import com.example.staj.models.RemoteKeys
import com.example.staj.toMovie
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val service: MovieListAPI,
    private val database: AppDatabase,
) : RemoteMediator<Int, Movie>() {

    private val movieDao = database.MovieDao()
    private val remoteKeysDao = database.RemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = service.listMovies(
                apiKey = BuildConfig.API_KEY,
                sortBy = "popularity.desc",
                page = currentPage
            )
            val endOfPaginationReached = response.results.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.deleteAllPaging()
                    remoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.results.map { movieData ->
                    RemoteKeys(
                        id = movieData.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                remoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                movieDao.addMoviesPaging(movies = response.results.map { it.toMovie() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }  catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieID?.let { id ->
                remoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                remoteKeysDao.getRemoteKeys(id = unsplashImage.movieID)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                remoteKeysDao.getRemoteKeys(id = unsplashImage.movieID)
            }
    }

}