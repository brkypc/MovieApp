package com.example.staj.feature.fragments.detail.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.MovieDao
import com.example.staj.data.remote.MovieAPI
import com.example.staj.models.Movie
import com.example.staj.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val database: MovieDao,
    private val movieAPI: MovieAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val movieLiveData = MutableLiveData<Movie?>()

    fun getMovie(movieID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val movie = database.getMovie(movieID)
            if (movie != null) {
                if (movie.budget != -1) {
                    movieLiveData.postValue(movie)
                } else fetchMovie(movie)
            } else fetchMovie(movieID)
        }
    }

    private fun fetchMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movieAPI.getMovie(movie.movieID, BuildConfig.API_KEY, "credits,reviews")?.let {
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
                }
                movieLiveData.postValue(movie)
                updateMovie(movie)
            }
        }
    }

    private fun fetchMovie(movieID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movieAPI.getMovie(movieID, BuildConfig.API_KEY, "credits,reviews")?.let {
                database.insert(it.toMovie())
                movieLiveData.postValue(it.toMovie())
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
