package com.mnlpdr.stashy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnlpdr.stashy.data.FuturesLogEntry
import com.mnlpdr.stashy.data.FuturesLogbookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import android.util.Log

class FuturesLogbookViewModel : ViewModel() {

    private val repository = FuturesLogbookRepository()

    private val _logEntries = MutableStateFlow<List<FuturesLogEntry>>(emptyList())
    val logEntries: StateFlow<List<FuturesLogEntry>> = _logEntries

    init {
        fetchLogEntries()
    }

    private fun fetchLogEntries() {
        viewModelScope.launch {
            Log.d("FuturesLogbookViewModel", "Fetching log entries")
            _logEntries.value = repository.getAllLogEntries()
            Log.d("FuturesLogbookViewModel", "Log entries fetched: ${_logEntries.value}")
        }
    }

    fun addLogEntry(logEntry: FuturesLogEntry) {
        viewModelScope.launch {
            Log.d("FuturesLogbookViewModel", "Adding log entry: $logEntry")
            repository.addLogEntry(logEntry)
            fetchLogEntries()
        }
    }

    fun updateLogEntry(logEntry: FuturesLogEntry) {
        viewModelScope.launch {
            logEntry.id?.let { id ->
                Log.d("FuturesLogbookViewModel", "Updating log entry: $logEntry")
                repository.updateLogEntry(logEntry) // Passa diretamente o objeto FuturesLogEntry
                fetchLogEntries()
            } ?: Log.e("FuturesLogbookViewModel", "Log entry ID is null, cannot update")
        }
    }


    fun deleteLogEntry(logEntryId: String) {
        viewModelScope.launch {
            Log.d("FuturesLogbookViewModel", "Deleting log entry with ID: $logEntryId")
            repository.deleteLogEntry(logEntryId)
            fetchLogEntries()
        }
    }
}
