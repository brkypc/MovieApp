package com.example.staj.feature.fragments.list.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.staj.BuildConfig
import com.example.staj.data.local.AppDatabase
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.data.remote.MovieSearchAPI
import com.example.staj.models.Movie
import com.example.staj.models.MovieData
import com.example.staj.paging.MovieRemoteMediator
import com.example.staj.paging.MyPagingSource
import com.example.staj.repository.PagingRepo
import com.example.staj.toMovie
import com.example.staj.util.Constants
import com.example.staj.util.Constants.MAX_PAGE
import com.example.staj.util.Constants.PAGE_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MovieListViewModel @Inject constructor( //Viewmodel inject?
    private val appDatabase: AppDatabase,
    private val database: MovieDao,
    private val movieListAPI: MovieListAPI,
    private val movieSearchAPI: MovieSearchAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val movieLiveData = MutableLiveData<List<Movie>>()
    val searchLiveData = MutableLiveData<List<Movie>>()
    private val movieList = mutableListOf<Movie>()
    private var favourites = listOf<Movie>()
    private var maxPage = MAX_PAGE
    private var page = 1

    init {
        getFavourites()
    }

    fun getAllMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { database.getAllMoviesPaging() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                service = movieListAPI,
                database = appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_LIMIT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MyPagingSource(service = movieListAPI, favourites)
            }
        ).flow.cachedIn(viewModelScope)
    }

    private fun getFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            favourites = database.getFavourites()
        }
    }

    private fun getMoviesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val movies = database.getAllMovies((page - 1) * PAGE_LIMIT)

            if (movies.size != PAGE_LIMIT) {
                fetchMovies(1)
            } else {
                movieList.addAll(movies)
                movieLiveData.postValue(movieList)
                fetchMovies(2)
            }
        }
    }

    private fun fetchMovies(insertUpdate: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieListAPI.listMovies(BuildConfig.API_KEY, "popularity.desc", page)?.let {
                    maxPage = it.totalPages
                    when (insertUpdate) {
                        1 -> saveToDatabase(it.results)
                        2 -> updateDatabase(it.results)
                    }
                }
            } catch (e: UnknownHostException) {
                if (page > 1) {
                    page--
                }
                Log.d("myLog", e.message.toString())
                Log.d("myLog", e.stackTraceToString())
            }
        }
    }

    private fun updateDatabase(movies: List<MovieData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movies.forEach {
                database.updateMovie(it.id, it.popularity, it.voteAverage, it.voteCount)
            }
            val moviesDB = database.getAllMovies((page - 1) * PAGE_LIMIT)

//            movieLiveData.postValue(movieList)
        }
    }

    private fun saveToDatabase(movies: List<MovieData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movies.forEach {
                database.insert(it.toMovie())
            }
            val moviesDB = database.getAllMovies((page - 1) * PAGE_LIMIT)
            movieList.addAll(moviesDB)
            movieLiveData.postValue(movieList)
        }
    }

    fun searchMovies(query: String) {
        val movies = mutableListOf<Movie>()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movieSearchAPI.listMovies(BuildConfig.API_KEY, query)?.let {
                it.results.forEach { movieData ->
                    movies.add(movieData.toMovie())
                }
                searchLiveData.postValue(movies)
            }
        }
    }

    fun updateMovieFavourite(movie: Movie, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movie.isFavourite = status

            val movieDB = database.getMovie(movie.movieID)
            if (movieDB != null) {
                database.update(movie)
            } else {
                database.insert(movie)
            }
        }
    }

    fun onNextPage() {
        if (page < maxPage) {
//            page++
//            getMoviesFromDatabase()
        }
    }
}
