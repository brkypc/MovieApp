package com.example.staj.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.staj.BuildConfig
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.models.Movie
import com.example.staj.toMovie
import com.example.staj.util.Constants.PAGE_START
import retrofit2.HttpException
import java.io.IOException

class MyPagingSource(
    private val service: MovieListAPI,
    private val favourites: List<Movie>
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageIndex = params.key ?: PAGE_START
        Log.d("myLog", "pageIndex: ".plus(pageIndex))
        return try {
            val response = service.listMovies(
                apiKey = BuildConfig.API_KEY,
                sortBy = "popularity.desc",
                page = pageIndex
            )
            val results = response.results
            val movies = mutableListOf<Movie>()

            results.forEach { movieData ->
                val mMovie = movieData.toMovie()
                val fMovie: Movie? = favourites.find { it.movieID == movieData.id }

                fMovie?.let {
                    mMovie.isFavourite = it.isFavourite
                }
                movies.add(mMovie)
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == PAGE_START) null else pageIndex - 1,
                nextKey = if (results.isEmpty()) null else pageIndex + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
