package ipp.estg.mobile.ui.screens.diary

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.mobile.ui.components.cards.LogCard
import ipp.estg.mobile.ui.components.forms.LogForm
import ipp.estg.mobile.ui.components.navigation.CustomTopAppBar
import ipp.estg.mobile.ui.components.utils.Loading
import ipp.estg.mobile.viewModels.DiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    diaryId: Int,
    diaryViewModel: DiaryViewModel = viewModel(),
    onLogDetailClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val logs by diaryViewModel.logs.collectAsState()
    val isLoading by diaryViewModel.isLoading.collectAsState()
    val error by diaryViewModel.error.collectAsState()

    // State to trigger data reload
    var reloadTrigger by remember { mutableStateOf(false) }

    // Fetch logs when screen loads or reloadTrigger changes
    LaunchedEffect(diaryId, reloadTrigger) {
        diaryViewModel.getLogs(diaryId)
    }

    // Handle errors
    LaunchedEffect(error) {
        if(error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Diary Details",
                onBackClick = { onBackClick() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // New Log Input Section
            var newLogDescription by remember { mutableStateOf("") }
            LogForm(
                newLogDescription = newLogDescription,
                onLogDescriptionChange = { newLogDescription = it },
                onAddLog = {
                    diaryViewModel.addLog(
                        diaryId = diaryId,
                        description = newLogDescription,
                        onSuccess = {
                            newLogDescription = "" // Clear input after adding
                            reloadTrigger = !reloadTrigger // Trigger reload
                        }
                    )
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )

            // Logs List Section
            Text(
                text = "Existing Logs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Loading State
            if (isLoading) {
                Loading()
            }

            // Logs List
            LogsContent(
                logs = logs,
                onLogDetailClick = onLogDetailClick
            )
        }

    }
}

@Composable
private fun LogsContent(
    logs: List<ipp.estg.mobile.data.retrofit.models.diaryLogs.LogResponse>,
    onLogDetailClick: (Int) -> Unit,
) {
    // Logs List
    if (logs.isEmpty()) {
        Text(
            text = "No logs available",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(logs, key = { it.id }) { log ->
                LogCard(
                    log = log,
                    onDetailClick = { onLogDetailClick(log.id) }
                )
            }
        }
    }
}

