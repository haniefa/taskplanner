package com.example.tahubulatz

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier,
                    title = { Text(text = "TASK PLANNER") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(20.dp),
                    onClick = { navigateToCreateTask() },
                    containerColor = Color(0xFF5B3A1B) ){
                    Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color(0xFFFAE0C9))
                }
            }
        ) {

            Column (
                modifier = Modifier
                    .padding(20.dp)
            ){
                Spacer(modifier = Modifier.height(30.dp))
                TaskList(
                    tasks = tasks, taskViewModel, onTaskClicked = onTaskClicked)
            }
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
    val mContext = LocalContext.current
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
                Row(modifier = Modifier.padding(8.dp)) {
                    Button(modifier = Modifier.padding(8.dp), onClick = {
                        taskViewModel.deleteTask(task.id)
                        Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                    Button(modifier = Modifier.padding(8.dp), onClick = {
                        onTaskClicked(task.id)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
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
