package com.mnlpdr.stashy.data

import com.google.firebase.Timestamp

data class FuturesLogEntry(
    val id: String? = null, // Adicionei o campo id
    val pair: String = "",
    val margin: Double = 0.0,
    val marginType: String = "",
    val leverage: Int = 1,
    val positionSize: Double = 0.0,
    val entryValue: Double = 0.0,
    val exitValue: Double? = null,
    val profitTargets: List<Double> = listOf(),
    val pnl: Double = 0.0,
    val positionType: String = "",
    val openDate: Timestamp = Timestamp.now(),
    val closeDate: Timestamp? = null,
    val fees: Double? = null,
    val platform: String = "",
    val notes: String = ""
)