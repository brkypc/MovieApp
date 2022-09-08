package com.example.staj.feature.favourite.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.data.local.TVDao
import com.example.staj.models.TV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteTvViewModel @Inject constructor(
    private val database: TVDao,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    fun getTv() = database.getFavouriteTV()

    fun updateTvFavourite(tv: TV) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tv.isFavourite = false
            database.update(tv)
        }
    }
}
