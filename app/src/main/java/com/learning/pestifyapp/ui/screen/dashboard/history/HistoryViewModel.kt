package com.learning.pestifyapp.ui.screen.dashboard.history

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.pestifyapp.data.model.historydata.HistoryData
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private val _history = MutableStateFlow<List<HistoryData>>(emptyList())
    val history: StateFlow<List<HistoryData>> get() = _history

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _selectedHistory = MutableStateFlow<HistoryData?>(null)
    val selectedHistory: StateFlow<HistoryData?> get() = _selectedHistory

    private val _historyImage = MutableStateFlow<Bitmap?>(null)
    val historyImage: StateFlow<Bitmap?> get() = _historyImage

    init {
        fetchHistory()
    }

    fun refreshHistory() {
        fetchHistory()
    }

    private fun fetchHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _history.value = historyRepository.fetchHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchHistoryById(historyId: String) {
        viewModelScope.launch {
            try {
                val response = historyRepository.getHistoryById(historyId)
                if (response.isSuccessful) {
                    _selectedHistory.value = response.body()?.data
                } else {
                    throw Exception("Failed to fetch history: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deleteHistoryById(historyId: String) {
        viewModelScope.launch {
            try {
                val response = historyRepository.deleteHistoryById(historyId)
                if (response.isSuccessful) {
                    _selectedHistory.value = response.body()?.data
                    historyRepository.deleteHistoryImage(historyId)
                } else {
                    throw Exception("Failed to fetch history: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getHistoryImage(historyId: String, callback: (HistoryImageEntity?) -> Unit) {
        viewModelScope.launch {
            try {
                val image = historyRepository.getHistoryImage(historyId)
                callback(image)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}
