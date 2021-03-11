package dev.erictruong.fizzbuzz.android.dashboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.erictruong.fizzbuzz.android.CoroutineContextProvider
import dev.erictruong.fizzbuzz.android.dashboard.ui.DashboardFragment
import dev.erictruong.fizzbuzz.android.dashboard.ui.model.RequestItem
import dev.erictruong.fizzbuzz.android.data.RequestRepository
import dev.erictruong.fizzbuzz.android.data.model.Request
import kotlinx.coroutines.launch

/**
 * ViewModel for [DashboardFragment].
 */
class DashboardViewModel(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val app: Application,
    private val requestRepository: RequestRepository
) : AndroidViewModel(app) {

    private val _requestItemsLive = MutableLiveData<List<RequestItem>>()
    val requestItemsLive: LiveData<List<RequestItem>> = _requestItemsLive

    fun loadRequests() {
        viewModelScope.launch(coroutineContextProvider.IO) {
            val requestItems = requestRepository.all()
                .map { it.toRequestItem() }
                .sortedByDescending { it.requestCount }
            _requestItemsLive.postValue(requestItems)
        }
    }

    private fun Request.toRequestItem(): RequestItem {
        val count = upperLimit - lowerLimit + 1
        return RequestItem(
            limits = "[ $lowerLimit - $upperLimit ]",
            count = count,
            numberOne = numberOne,
            numberTwo = numberTwo,
            wordOne = wordOne,
            wordOneHitsText = "$wordOneHits / $count",
            wordOneHits = wordOneHits,
            wordTwo = wordTwo,
            wordTwoHitsText = "$wordTwoHits / $count",
            wordTwoHits = wordTwoHits,
            both = wordOne + wordTwo,
            bothHitsText = "$bothHits / $count",
            bothHits = bothHits,
            requestCount = requestCount
        )
    }
}