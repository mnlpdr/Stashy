package com.mnlpdr.stashy.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

import android.util.Log

class FuturesLogbookRepository {

    private val db = FirebaseFirestore.getInstance()
    private val futuresLogbookCollection = db.collection("futures_logbook")

    suspend fun addLogEntry(logEntry: FuturesLogEntry): String {
        val documentReference = futuresLogbookCollection.add(logEntry).await()
        val documentId = documentReference.id
        futuresLogbookCollection.document(documentId).update("id", documentId).await()
        return documentId
    }

    suspend fun updateLogEntry(logEntry: FuturesLogEntry) {
        logEntry.id?.let { id ->
            val documentReference = futuresLogbookCollection.document(id)
            Log.d("FuturesLogbookRepository", "Updating log entry with ID: $id")
            documentReference.set(logEntry).await()
        } ?: Log.e("FuturesLogbookRepository", "Cannot update log entry without an ID")
    }


    suspend fun deleteLogEntry(logEntryId: String) {
        futuresLogbookCollection.document(logEntryId).delete().await()
    }

    suspend fun getAllLogEntries(): List<FuturesLogEntry> {
        val snapshot = futuresLogbookCollection.get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(FuturesLogEntry::class.java)?.copy(id = document.id)
        }
    }
}