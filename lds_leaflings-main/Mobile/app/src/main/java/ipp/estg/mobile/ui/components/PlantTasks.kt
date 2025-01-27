package ipp.estg.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PlantTasks(
    tasks: List<ipp.estg.mobile.data.retrofit.models.task.TaskResponse>
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Care Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        if(tasks.isEmpty()) {
            Text(
                text = "No tasks avaiable for this plant",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        tasks.forEach { task ->
            TaskItem(
                title = task.taskName,
                description = task.taskDescription,
                )
        }


//        // List of tasks
//        TaskItem(
//            title = "Water the plant",
//            description = "Keep soil moist but not waterlogged",
//        )
//
//        TaskItem(
//            title = "Check sunlight exposure",
//            description = "Ensure proper light conditions",
//        )
//
//        TaskItem(
//            title = "Prune dead leaves",
//            description = "Remove any yellowing or dead foliage",
//        )
//
//        TaskItem(
//            title = "Monitor soil",
//            description = "Check soil moisture and condition",
//        )
//
//        TaskItem(
//            title = "Check for pests",
//            description = "Inspect leaves and stems for insects",
//        )
    }
}

@Composable
private fun TaskItem(
    title: String,
    description: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Task,
            contentDescription = title,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

//@Preview
//@Composable
//fun PlantTasksPreview() {
//    PlantTasks(plantViewModel = PlantViewModel())
//}