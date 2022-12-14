package com.example.staj.feature.favourite.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.data.local.MovieDao
import com.example.staj.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteMoviesViewModel @Inject constructor(
    private val database: MovieDao,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    fun getMovies() = database.getFavouriteMovies()

    fun updateMovieFavourite(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            movie.isFavourite = false
            database.update(movie)
        }
    }
}
