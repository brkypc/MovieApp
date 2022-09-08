package com.example.staj.feature.detail.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieAPI
import com.example.staj.extensions.toMovie
import com.example.staj.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val database: MovieDao,
    private val movieAPI: MovieAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val movieLiveData = MutableLiveData<Movie?>()
    val errorLiveData = MutableLiveData<String>()

    fun getMovie(movieID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val movie = database.getMovie(movieID)

            if (movie != null) {
                if (movie.budget != -1) {
                    movieLiveData.postValue(movie)
                } else {
                    fetchMovie(movie)
                }
            } else {
                fetchMovie(movieID)
            }
        }
    }

    private fun fetchMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieAPI.getMovie(movie.movieID, BuildConfig.API_KEY, "credits,reviews,recommendations,videos")?.let {
                    movie.apply {
                        budget = it.budget
                        revenue = it.revenue
                        runtime = it.runtime
                        status = it.status
                        tagline = it.tagline
                        cast = it.credits.cast
                        crew = it.credits.crew
                        genres = it.genres
                        reviews = it.reviews.reviewList
                        similar = it.similar.results
                        videos = it.videos.videos
                    }
                    movieLiveData.postValue(movie)
                    updateMovie(movie)
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

    private fun fetchMovie(movieID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieAPI.getMovie(movieID, BuildConfig.API_KEY, "credits,reviews,recommendations,videos")?.let {
                    database.insert(it.toMovie())
                    movieLiveData.postValue(it.toMovie())
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
        viewModelScope.launch(Dispatchers.IO) {
            movie.isFavourite = status
            database.update(movie)
        }
    }

    private fun updateMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            database.update(movie)
        }
    }
}
