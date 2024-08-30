package com.mnlpdr.stashy.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mnlpdr.stashy.data.FuturesLogEntry
import com.mnlpdr.stashy.viewmodel.FuturesLogbookViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FuturesLogbookScreen(viewModel: FuturesLogbookViewModel = viewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    var logToEdit by remember { mutableStateOf<FuturesLogEntry?>(null) }
    var logToDelete by remember { mutableStateOf<FuturesLogEntry?>(null) }

    val logs by viewModel.logEntries.collectAsState()

    // Calcula o PnL total
    val totalPnl = logs.sumOf { it.pnl }

    Column(modifier = Modifier.padding(16.dp)) {

// Estiliza o PnL total
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp,
            color = if (totalPnl >= 0) Color(0xFFDFF0D8) else Color(0xFFF2DEDE)
        ) {
            Text(
                text = "Total PnL: $totalPnl",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (totalPnl >= 0) Color(0xFF3C763D) else Color(0xFFA94442)
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Futures Logbook", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = {
                    logToEdit = null // Novo log
                    showDialog = true
                }
            ) {
                Text("Add Log")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                items = logs,
                key = { log -> log.id ?: log.hashCode() } // Use a chave única aqui, como o ID do log
            ) { log ->
                LogEntryCard(
                    log = log,
                    onEdit = {
                        logToEdit = it
                        showDialog = true
                    },
                    onDelete = {
                        logToDelete = log // Define o log a ser deletado
                    }
                )
            }
        }

        if (logToDelete != null) {
            ConfirmDeleteDialog(
                onConfirm = {
                    viewModel.deleteLogEntry(logToDelete!!.id ?: "")
                    logToDelete = null
                },
                onDismiss = {
                    logToDelete = null
                }
            )
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
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Are you sure you want to delete this log?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("No, keep it")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onConfirm) {
                        Text("Yes, I want to delete")
                    }
                }
            }
        }
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
    var entryPrice by remember { mutableStateOf(logToEdit?.entryValue?.toString() ?: "") }
    var exitPrice by remember { mutableStateOf(logToEdit?.exitValue?.toString() ?: "") }
    var openDate by remember { mutableStateOf(logToEdit?.openDate ?: "") }
    var closeDate by remember { mutableStateOf(logToEdit?.closeDate ?: "") }
    var broker by remember { mutableStateOf(logToEdit?.platform ?: "") }
    var positionType by remember { mutableStateOf(logToEdit?.positionType ?: "") }
    var pnl by remember { mutableStateOf(logToEdit?.pnl?.toString() ?: "") }

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
//                TextField(
//                    value = openDate,
//                    onValueChange = { openDate = it },
//                    label = { Text("Open Date") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp)
//                )
//                TextField(
//                    value = closeDate,
//                    onValueChange = { closeDate = it },
//                    label = { Text("Close Date (optional)") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp)
//                )
                TextField(
                    value = broker,
                    onValueChange = { broker = it },
                    label = { Text("Broker") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = positionType,
                    onValueChange = { positionType = it },
                    label = { Text("Position (Long/Short)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = pnl,
                    onValueChange = { pnl = it },
                    label = { Text("Profits and Losses (PnL)") },
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
                                exitValue = exitPrice.toDoubleOrNull(),
//                                openDate = openDate,
//                                closeDate = closeDate,
                                platform = broker,
                                positionType = positionType,
                                pnl = pnl.toDoubleOrNull() ?: 0.0
                            ) ?: FuturesLogEntry(
                                pair = pair,
                                margin = marginValue.toDoubleOrNull() ?: 0.0,
                                leverage = leverage.toIntOrNull() ?: 1,
                                entryValue = entryPrice.toDoubleOrNull() ?: 0.0,
                                exitValue = exitPrice.toDoubleOrNull(),
//                                openDate = openDate,
//                                closeDate = closeDate,
                                platform = broker,
                                positionType = positionType,
                                pnl = pnl.toDoubleOrNull() ?: 0.0
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
    onDelete: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Pair and Leverage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pair: ${log.pair}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Leverage: ${log.leverage}x",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Operation Type (Long or Short)
                Text(
                    text = "Operation: ${log.positionType}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        log.positionType == "Long" -> Color(0xFF4CAF50)
                        log.positionType == "Short" -> Color(0xFFF44336)
                        else -> Color.Gray
                    },
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Entry and Exit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Entry: ${log.entryValue}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Exit: ${log.exitValue ?: "In progress"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // PnL
                Text(
                    text = "PnL: ${log.pnl}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (log.pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336), // Green for profit, Red for loss
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Edit and Delete buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onEdit(log) }) {
                        Text("Edit")
                    }
                    TextButton(
                        onClick = {
                            onDelete() // Inicia a animação
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    LaunchedEffect(visible) {
        if (!visible) {
            onDelete() // Chama o onDelete quando a animação é concluída
        }
    }
}
