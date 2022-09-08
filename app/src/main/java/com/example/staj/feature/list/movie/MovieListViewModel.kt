package com.example.staj.feature.list.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieListAPI
import com.example.staj.data.remote.MovieSearchAPI
import com.example.staj.extensions.toMovie
import com.example.staj.models.Movie
import com.example.staj.models.MovieData
import com.example.staj.util.Constants.INSERT
import com.example.staj.util.Constants.MAX_PAGE
import com.example.staj.util.Constants.PAGE_LIMIT
import com.example.staj.util.Constants.UPDATE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val database: MovieDao,
    private val movieListAPI: MovieListAPI,
    private val movieSearchAPI: MovieSearchAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val movieLiveData = MutableLiveData<List<Movie>>()
    val searchLiveData = MutableLiveData<List<Movie>>()
    val errorLiveData = MutableLiveData<String>()
    private val movieList = mutableListOf<Movie>()
    private var maxPage = MAX_PAGE
    private var page = 1
    var moviePos = -1

    init {
        getMoviesFromDatabase()
    }

    private fun getMoviesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val movies = database.getAllMovies((page - 1) * PAGE_LIMIT)

            if (movies.size != PAGE_LIMIT) {
                fetchMovies(INSERT)
            } else {
                movieList.addAll(movies)
                movieLiveData.postValue(movieList)
                fetchMovies(UPDATE)
            }
        }
    }

    private fun fetchMovies(insertUpdate: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieListAPI.listMovies(BuildConfig.API_KEY, page)?.let {
                    maxPage = it.totalPages
                    when (insertUpdate) {
                        INSERT -> saveToDatabase(it.results)
                        UPDATE -> updateDatabase(it.results)
                    }
                }
            } catch (e: IOException) {
                if (page > 1) {
                    page--
                }
                errorLiveData.postValue("IO")
                Log.e("movieLog", e.message.toString())
            } catch (e: HttpException) {
                if (page > 1) {
                    page--
                }
                errorLiveData.postValue("HTTP")
                Log.e("movieLog", e.message.toString())
            }
        }
    }

    private fun updateDatabase(movies: List<MovieData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movies.forEach {
                database.updateMovie(it.id, it.popularity, it.voteAverage, it.voteCount)
            }
        }
    }

    private fun saveToDatabase(movies: List<MovieData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val moviesMap = movies.map { it.toMovie() }
            database.insert(moviesMap)
            movieList.addAll(moviesMap)
            movieLiveData.postValue(movieList)
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieSearchAPI.listMovies(BuildConfig.API_KEY, query)?.let {
                    val movies = it.results.map { movieData ->
                        movieData.toMovie()
                    }
                    searchLiveData.postValue(movies)
                }
            } catch (e: IOException) {
                errorLiveData.postValue("IO")
                Log.e("movieLog", e.message.toString())
            } catch (e: HttpException) {
                errorLiveData.postValue("HTTP")
                Log.e("movieLog", e.message.toString())
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

    fun checkUpdate() {
        if (moviePos != -1 && movieList.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                val movie = database.getMovie(movieList[moviePos].movieID)

                movie?.let {
                    movieList[moviePos] = it
                    movieLiveData.postValue(movieList)
                }
            }
        }
    }

    fun onNextPage() {
        if (page < maxPage) {
            page++
            getMoviesFromDatabase()
        }
    }
}
