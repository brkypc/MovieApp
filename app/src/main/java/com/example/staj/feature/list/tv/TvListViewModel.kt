package com.example.staj.feature.list.tv

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staj.BuildConfig
import com.example.staj.data.local.TVDao
import com.example.staj.data.remote.TvListAPI
import com.example.staj.data.remote.TvSearchAPI
import com.example.staj.extensions.toTV
import com.example.staj.models.TV
import com.example.staj.models.TVData
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
class TvListViewModel @Inject constructor(
    private val database: TVDao,
    private val tvListAPI: TvListAPI,
    private val tvSearchAPI: TvSearchAPI,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    var tvLiveData = MutableLiveData<List<TV>>()
    val searchLiveData = MutableLiveData<List<TV>>()
    val errorLiveData = MutableLiveData<String>()
    private val allTvList = mutableListOf<TV>()
    private var maxPage = MAX_PAGE
    private var page = 1
    var tvPos = -1

    init {
        getTvFromDatabase()
    }

    private fun getTvFromDatabase() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tvList = database.getAllTV((page - 1) * PAGE_LIMIT)

            if (tvList.size != PAGE_LIMIT) {
                fetchTvList(INSERT)
            } else {
                allTvList.addAll(tvList)
                tvLiveData.postValue(allTvList)
                fetchTvList(UPDATE)
            }
        }
    }

    private fun fetchTvList(insertUpdate: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tvListAPI.listTV(BuildConfig.API_KEY, page)?.let {
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

    private fun updateDatabase(tvList: List<TVData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            tvList.forEach {
                database.updateTv(it.id, it.popularity, it.voteAverage, it.voteCount)
            }
        }
    }

    private fun saveToDatabase(tvList: List<TVData>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tvListMap = tvList.map { it.toTV() }
            database.insert(tvListMap)
            allTvList.addAll(tvListMap)
            tvLiveData.postValue(allTvList)
        }
    }

    fun searchTv(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tvSearchAPI.listTV(BuildConfig.API_KEY, query)?.let {
                    val tvList = it.results.map { tvData ->
                        tvData.toTV()
                    }
                    searchLiveData.postValue(tvList)
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

    fun checkUpdate() {
        if (tvPos != -1 && allTvList.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                val tv = database.getTV(allTvList[tvPos].tvID)

                tv?.let {
                    allTvList[tvPos] = it
                    tvLiveData.postValue(allTvList)
                }
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
