package com.example.staj.feature.fragments.list.tv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.TVDao
import com.example.staj.data.remote.TvListAPI
import com.example.staj.data.remote.TvSearchAPI
import com.example.staj.models.TV
import com.example.staj.models.TVData
import com.example.staj.toTV
import com.example.staj.util.Constants.MAX_PAGE
import com.example.staj.util.Constants.PAGE_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvListViewModel @Inject constructor(
    private val database: TVDao,
    private val tvListAPI: TvListAPI,
    private val tvSearchAPI: TvSearchAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    var tvLiveData = MutableLiveData<List<TV>>()
    val searchLiveData = MutableLiveData<List<TV>>()
    private var maxPage = MAX_PAGE
    private var page = 1

    init {
        getTvFromDatabase()
    }

    private fun getTvFromDatabase() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tvList = database.getAllTV(page * PAGE_LIMIT)
            tvLiveData.postValue(tvList)
            if (tvList.size != page * PAGE_LIMIT) {
                fetchTvList(1)
            } else {
                fetchTvList(2)
            }
        }
    }

    private fun fetchTvList(insertUpdate: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvListAPI.listTV(BuildConfig.API_KEY, "popularity.desc", page)?.let {
                maxPage = it.totalPages
                when (insertUpdate) {
                    1 -> saveToDatabase(it.results)
                    2 -> updateDatabase(it.results)
                }
            }
        }
    }

    private fun updateDatabase(tvList: List<TVData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvList.forEach {
                database.updateTv(it.id, it.popularity, it.voteAverage, it.voteCount)
            }
            tvLiveData.postValue(database.getAllTV(page * PAGE_LIMIT))
        }
    }

    private fun saveToDatabase(tvList: List<TVData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvList.forEach {
                database.insert(it.toTV())
            }
            tvLiveData.postValue(database.getAllTV(page * PAGE_LIMIT))
        }
    }

    fun searchTv(query: String) {
        val tvList = mutableListOf<TV>()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvSearchAPI.listTV(BuildConfig.API_KEY, query)?.let {
                it.results.forEach { tvData ->
                    tvList.add(tvData.toTV())
                }
                searchLiveData.postValue(tvList)
            }
        }
    }

    fun updateTvFavourite(tv: TV, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tv.isFavourite = status

            val tvDB = database.getTV(tv.tvID)
            if (tvDB != null) {
                database.update(tv)
            } else {
                database.insert(tv)
            }
        }
    }

    fun onNextPage() {
        if (page < maxPage) {
            page++
            getTvFromDatabase()
        }
    }
}
