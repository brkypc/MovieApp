package com.example.staj.feature.detail.tv

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.TVDao
import com.example.staj.data.remote.TvAPI
import com.example.staj.extensions.toTV
import com.example.staj.models.TV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel @Inject constructor(
    private val database: TVDao,
    private val tvAPI: TvAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    val tvLiveData = MutableLiveData<TV?>()
    val errorLiveData = MutableLiveData<String>()

    fun getTv(tvID: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tv = database.getTV(tvID)

            if (tv != null) {
                if (tv.episodeRunTime != -1) {
                    tvLiveData.postValue(tv)
                } else {
                    fetchTV(tv)
                }
            } else {
                fetchTV(tvID)
            }
        }
    }

    private fun fetchTV(tv: TV) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tvAPI.getTV(tv.tvID, BuildConfig.API_KEY, "credits,reviews,recommendations,videos")?.let {
                    tv.apply {
                        lastAirDate = it.lastAirDate
                        episodeRunTime = it.episodeRunTime?.get(0) ?: -1
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
                        similar = it.similar.results
                        videos = it.videos.videos
                    }
                    tvLiveData.postValue(tv)
                    updateTV(tv)
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

    private fun fetchTV(tvID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tvAPI.getTV(tvID, BuildConfig.API_KEY, "credits,reviews,recommendations,videos")?.let {
                    database.insert(it.toTV())
                    tvLiveData.postValue(it.toTV())
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
