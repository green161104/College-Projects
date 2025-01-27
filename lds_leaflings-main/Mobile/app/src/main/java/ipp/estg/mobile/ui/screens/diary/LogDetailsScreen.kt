package ipp.estg.mobile.ui.screens.diary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.mobile.data.retrofit.models.diaryLogs.LogResponse
import ipp.estg.mobile.ui.components.cards.LogCard
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.viewModels.DiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailScreen(
    logId: Int,
    diaryViewModel: DiaryViewModel = viewModel(),
    onBackClick: () -> Unit,
    onLogDeleted: () -> Unit
) {
    var log by remember { mutableStateOf<LogResponse?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var editedDescription by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var reloadTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(logId, reloadTrigger) {
        diaryViewModel.getLog(
            logId = logId,
            onSuccess = { fetchedLog ->
                log = fetchedLog
                editedDescription = fetchedLog.logDescription
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Log") },
            text = { Text("Are you sure you want to delete this log?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        log?.let { currentLog ->
                            diaryViewModel.deleteLog(
                                logId = currentLog.id,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Log Deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onLogDeleted()
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Delete Button
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Log",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    // Edit Button
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = if (isEditing)
                                "Cancel Edit"
                            else
                                "Edit"
                        )
                    }
                }
            )
        }
    ) { padding ->
        log?.let { currentLog ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (isEditing) {
                    EditLogContent(
                        editedDescription = editedDescription,
                        onDescriptionChange = { editedDescription = it },
                        onSave = {
                            diaryViewModel.updateLog(
                                logId = currentLog.id,
                                diaryId = currentLog.diaryId,
                                newDescription = editedDescription,
                                onSuccess = {
                                    isEditing = false
                                    Toast.makeText(
                                        context,
                                        "Log Updated Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    reloadTrigger = !reloadTrigger
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        },
                        onCancel = { isEditing = false }
                    )
                } else {
                    LogCard(
                        log = currentLog,
                        onDetailClick = {
                            isEditing = true;
                        }
                    )
                }
            }
        } ?: Loading()
    }
}

@Composable
private fun EditLogContent(
    editedDescription: String,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    TextField(
        value = editedDescription,
        onValueChange = onDescriptionChange,
        label = { Text("Log Description") },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        supportingText = {
            Text(
                text = "${editedDescription.length} / 500",
                style = MaterialTheme.typography.bodySmall
            )
        },
        isError = editedDescription.length > 500
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onSave,
            enabled = editedDescription.isNotBlank() && editedDescription.length <= 500
        ) {
            Text("Save")
        }

        OutlinedButton(onClick = onCancel) {
            Text("Cancel")
        }
    }
}