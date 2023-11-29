package com.example.tahubulatz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskPage(
    taskId: Int,
    taskViewModel: TaskViewModel,
    onTaskUpdated: (Task) -> Unit,
    onTaskDeleted: (Int) -> Unit,
    onNavigateUp: () -> Unit
) {
    val task by taskViewModel.tasks.collectAsState()
    val mContext = LocalContext.current
    val editedTask = remember { taskViewModel.getTaskById(taskId) }
    Log.d("EditTaskPage", "taskId: $taskId")
    if (editedTask != null) {
        var editedName by remember { mutableStateOf(editedTask.name) }
        var editedDeadline by remember { mutableStateOf(convertMillisToDateString(editedTask.deadline)) }
        var editedDetails by remember { mutableStateOf(editedTask.details) }
        // Top bar
        TopAppBar(
            title = { Text(text = "Edit Task") },
            modifier = Modifier.shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            ),
            navigationIcon = {
//                TextButton(onClick = { onNavigateUp() }) {
//                    Text("Cancel")
//                }
            },
            actions = {
//                TextButton(onClick = {
//                    val updatedTask = Task(
//                        id = editedTask.id,
//                        name = editedName,
//                        deadline = convertDateStringToMillis(editedDeadline),
//                        isCompleted = editedTask.isCompleted,
//                        details = editedDetails
//                    )
//                    onTaskUpdated(updatedTask)
//                    onNavigateUp()
//                    Toast.makeText(mContext, "Task Updated", Toast.LENGTH_LONG).show()
//                }) {
//                    Text("Save")
//                }
//                TextButton(onClick = {
//                    onTaskDeleted(taskId)
//                    onNavigateUp()
//                    Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_LONG).show()
//                }) {
//                    Text("Delete")
//                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                // Editable fields
                OutlinedTextField(
                    value = editedName,
                    shape = RoundedCornerShape(18.dp),
                    onValueChange = { editedName = it },
                    label = { Text("Task Name") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Deadline Text Field
                OutlinedTextField(
                    value = editedDeadline,
                    shape = RoundedCornerShape(18.dp),
                    onValueChange = { editedDeadline = it },
                    label = { Text("Deadline Date (YYYY-MM-DD)") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedDetails,
                    shape = RoundedCornerShape(18.dp),
                    onValueChange = { editedDetails = it },
                    label = { Text("Details") },
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.padding(0.dp, 20.dp)
                ) {
                    TextButton(
                        modifier = Modifier
                            .height(52.dp)
                            .width(150.dp)
                            .padding(10.dp, 0.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                            ),
                        colors = ButtonDefaults.buttonColors(),
                        shape = RoundedCornerShape(18.dp),
                        onClick = {
                        val updatedTask = Task(
                            id = editedTask.id,
                            name = editedName,
                            deadline = convertDateStringToMillis(editedDeadline),
                            isCompleted = editedTask.isCompleted,
                            details = editedDetails
                        )
                        onTaskUpdated(updatedTask)
                        onNavigateUp()
                        Toast.makeText(mContext, "Task Updated", Toast.LENGTH_LONG).show()
                    }) {
                        Text("Save")
                    }
                    TextButton(
                        modifier = Modifier
                            .height(52.dp)
                            .width(150.dp)
                            .padding(10.dp, 0.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                            ),
                        colors = ButtonDefaults.buttonColors(),
                        shape = RoundedCornerShape(18.dp),
                        onClick = {
                        onTaskDeleted(taskId)
                        onNavigateUp()
                        Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_LONG).show()
                    }) {
                        Text("Delete")
                    }
                }
            }

        }

    }
}

// Convert date string to milliseconds
private fun convertDateStringToMillis(date: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val dateObject = dateFormat.parse(date)
        dateObject?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}

// Convert milliseconds to date string
fun convertMillisToDateString(millis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(millis))
}