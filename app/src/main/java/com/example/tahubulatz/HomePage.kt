package com.example.tahubulatz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    taskViewModel: TaskViewModel,
    navigateToCreateTask: () -> Unit,
    onTaskClicked: (Int) -> Unit
) {
    val tasks by taskViewModel.tasks.collectAsState()

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToCreateTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) {
        Column {
            TaskList(tasks = tasks, taskViewModel ,onTaskClicked = onTaskClicked)
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, taskViewModel: TaskViewModel ,onTaskClicked: (Int) -> Unit) {
    Log.d("TaskList", "Recomposing with ${tasks.size} tasks")
    LazyColumn {
        items(tasks) { task ->
            TaskListItem(task = task, taskViewModel ,onTaskClicked = { onTaskClicked(task.id) })
        }
    }
}

@Composable
fun TaskListItem(task: Task, taskViewModel: TaskViewModel, onTaskClicked: (Int) -> Unit) {
    // Check if the task deadline is today or has passed
    val isDeadlinePassed = isTaskDeadlinePast(task.deadline)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onTaskClicked(task.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked -> taskViewModel.setTaskCompleted(task.id, isChecked) }
            )

            // Display both task name and deadline
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = task.name,
                    style = if (task.isCompleted) TextStyle.Default.copy(textDecoration = TextDecoration.LineThrough) else TextStyle.Default
                )
                Text(
                    text = "Deadline: ${convertMillisToDateString(task.deadline)}", // Convert milliseconds to date string
                    modifier = Modifier.alpha(if (task.isCompleted) 0.5f else 1f)
                        .apply {
                            if (isDeadlinePassed) {
                                this.then(
                                    Modifier
                                        .alpha(1f)
                                        .then(Modifier.alpha(0.5f)))
                            }
                        },
                    style = if (task.isCompleted) TextStyle.Default.copy(textDecoration = TextDecoration.LineThrough) else TextStyle.Default
                )
                Button(onClick = {
                    taskViewModel.deleteTask(task.id)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
                Button(onClick = {
                    onTaskClicked(task.id)
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
            }

            if (task.isCompleted) {
                Spacer(modifier = Modifier.width(8.dp))
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                )
            }
        }
    }
}



fun isTaskDeadlinePast(deadline: Long): Boolean {
    val currentMillis = System.currentTimeMillis()
    // Check if the deadline is today or has passed
    return deadline <= currentMillis + 24 * 60 * 60 * 1000
}
