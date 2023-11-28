package com.example.tahubulatz

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskPage(
    taskViewModel: TaskViewModel,
    onTaskCreated: (Task) -> Unit,
    onNavigateUp: () -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var taskDeadline by remember { mutableStateOf("") }
    var taskDetails by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        TopAppBar(
            title = { Text(text = "Create Task") },
            navigationIcon = {
                TextButton(onClick = { onNavigateUp() }) {
                    Text("Cancel")
                }
            },
            actions = {
                TextButton(onClick = {
                    val newTask = Task(
                        id = System.currentTimeMillis().toInt(),
                        name = taskName,
                        deadline = convertToDateMillis(taskDeadline),
                        isCompleted = false,
                        details = taskDetails
                    )
                    taskViewModel.addTask(newTask)
                    Log.d("CreateTaskPage", "taskId: ${newTask.id}")
                    onNavigateUp()
                }) {
                    Text("Create")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Task Name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for choosing date
        OutlinedTextField(
            value = taskDeadline,
            onValueChange = { taskDeadline = it },
            label = { Text("Deadline Date (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskDetails,
            onValueChange = { taskDetails = it },
            label = { Text("Details") },
            maxLines = 5
        )
    }
}

// Convert the date string to milliseconds
private fun convertToDateMillis(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val date = dateFormat.parse(dateString)
        date?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}
