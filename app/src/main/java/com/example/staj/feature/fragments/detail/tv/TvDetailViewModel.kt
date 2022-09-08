package com.example.staj.feature.fragments.detail.tv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.TVDao
import com.example.staj.data.remote.TvAPI
import com.example.staj.models.TV
import com.example.staj.toTV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel @Inject constructor(
    private val database: TVDao,
    private val tvAPI: TvAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val tvLiveData = MutableLiveData<TV?>()

    fun getTv(tvID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tv = database.getTV(tvID)
            if (tv != null) {
                if (tv.episodeRunTime != -1) {
                    tvLiveData.postValue(tv)
                } else fetchTV(tv)
            } else fetchTV(tvID)
        }
    }

    private fun fetchTV(tv: TV) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvAPI.getTV(tv.tvID, BuildConfig.API_KEY, "credits,reviews")?.let {
                tv.apply {
                    lastAirDate = it.lastAirDate
                    episodeRunTime = it.episodeRunTime[0]
                    numberOfEpisodes = it.numberOfEpisodes
                    numberOfSeasons = it.numberOfSeasons
                    inProduction = it.inProduction
                    status = it.status
                    type = it.type
                    adult = it.adult
                    tagline = it.tagline
                    cast = it.credits.cast
                    crew = it.credits.crew
                    genres = it.genres
                    reviews = it.reviews.reviewList
                }
                tvLiveData.postValue(tv)
                updateTV(tv)
            }
        }
    }

    private fun fetchTV(tvID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvAPI.getTV(tvID, BuildConfig.API_KEY, "credits,reviews")?.let {
                database.insert(it.toTV())
                tvLiveData.postValue(it.toTV())
            }
        }
    }

    fun updateTvFavourite(tv: TV, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tv.isFavourite = status
            database.update(tv)
        }
    }

    private fun updateTV(tv: TV) {
        viewModelScope.launch(Dispatchers.IO) {
            database.update(tv)
        }
    }
}
