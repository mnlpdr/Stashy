package com.mnlpdr.stashy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mnlpdr.stashy.data.FuturesLogEntry
import com.mnlpdr.stashy.viewmodel.FuturesLogbookViewModel

@Composable
fun FuturesLogbookScreen(viewModel: FuturesLogbookViewModel = viewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    var logToEdit by remember { mutableStateOf<FuturesLogEntry?>(null) }

    val logs by viewModel.logEntries.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            onClick = {
                logToEdit = null // Novo log
                showDialog = true
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Log")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Futures Logbook", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(logs) { log ->
                LogEntryCard(
                    log = log,
                    onEdit = {
                        logToEdit = it
                        showDialog = true
                    },
                    onDelete = {
                        viewModel.deleteLogEntry(it.id ?: "")
                    }
                )
            }
        }
    }

    if (showDialog) {
        AddEditLogEntryDialog(
            logToEdit = logToEdit,
            onDismiss = { showDialog = false },
            onAddLogEntry = { logEntry ->
                if (logToEdit == null) {
                    viewModel.addLogEntry(logEntry)
                } else {
                    viewModel.updateLogEntry(logEntry)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun AddEditLogEntryDialog(
    logToEdit: FuturesLogEntry?,
    onDismiss: () -> Unit,
    onAddLogEntry: (FuturesLogEntry) -> Unit
) {
    var pair by remember { mutableStateOf(logToEdit?.pair ?: "") }
    var marginValue by remember { mutableStateOf(logToEdit?.margin?.toString() ?: "") }
    var leverage by remember { mutableStateOf(logToEdit?.leverage?.toString() ?: "") }
    var entryPrice by remember { mutableStateOf(logToEdit?.entryValue?.toString() ?:"") }
    var exitPrice by remember { mutableStateOf(logToEdit?.exitValue?.toString() ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = pair,
                    onValueChange = { pair = it },
                    label = { Text("Pair") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = marginValue,
                    onValueChange = { marginValue = it },
                    label = { Text("Margin Value") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = leverage,
                    onValueChange = { leverage = it },
                    label = { Text("Leverage") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = entryPrice,
                    onValueChange = { entryPrice = it },
                    label = { Text("Entry Price") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = exitPrice,
                    onValueChange = { exitPrice = it },
                    label = { Text("Exit Price (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val logEntry = logToEdit?.copy(
                                pair = pair,
                                margin = marginValue.toDoubleOrNull() ?: 0.0,
                                leverage = leverage.toIntOrNull() ?: 1,
                                entryValue = entryPrice.toDoubleOrNull() ?: 0.0,
                                exitValue = exitPrice.toDoubleOrNull()
                            ) ?: FuturesLogEntry(
                                pair = pair,
                                margin = marginValue.toDoubleOrNull() ?: 0.0,
                                leverage = leverage.toIntOrNull() ?: 1,
                                entryValue = entryPrice.toDoubleOrNull() ?: 0.0,
                                exitValue = exitPrice.toDoubleOrNull()
                            )
                            onAddLogEntry(logEntry)
                        }
                    ) {
                        Text(if (logToEdit == null) "Add" else "Save")
                    }
                }
            }
        }
    }
}

@Composable
fun LogEntryCard(
    log: FuturesLogEntry,
    onEdit: (FuturesLogEntry) -> Unit,
    onDelete: (FuturesLogEntry) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Pair: ${log.pair}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Entry: ${log.entryValue}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Exit: ${log.exitValue ?: "In progress"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Leverage: ${log.leverage}x", style = MaterialTheme.typography.bodyMedium)
            Text(text = "PnL: ${log.pnl}", style = MaterialTheme.typography.bodyMedium)

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onEdit(log) }) {
                    Text("Edit")
                }
                TextButton(onClick = { onDelete(log) }) {
                    Text("Delete")
                }
            }
        }
    }
}
