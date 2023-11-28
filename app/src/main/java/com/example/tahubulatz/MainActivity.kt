package com.example.tahubulatz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.example.tahubulatz.ui.theme.TahubulatzTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            TahubulatzTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen") {
                        val taskId = remember {taskViewModel.taskId.value}
                        HomePage(
                            taskViewModel = taskViewModel,
                            navigateToCreateTask = { navController.navigate("create_task_screen") },
                            onTaskClicked = { taskId -> navController.navigate("edit_task_screen/${taskId}") }
                        )
                    }

                    composable("edit_task_screen/{taskId}") {NavBackStackEntry ->
                        val taskId = NavBackStackEntry.arguments?.getString("taskId")?.toInt()
//                        val taskId by rememberUpdatedState(taskViewModel.taskId.value)
//                        val task = remember { taskViewModel.getTaskById(taskId)}
                        if (taskId != null) {
                            EditTaskPage(
                                taskId = taskId,
                                taskViewModel = taskViewModel,
                                onTaskUpdated = { updatedTask -> taskViewModel.updateTask(updatedTask) },
                                onTaskDeleted = { deletedTaskId -> taskViewModel.deleteTask(deletedTaskId) },
                                onNavigateUp = { navController.popBackStack() }
                            ) //{ navController.popBackStack() }
                        } else {
                            navController.popBackStack()
                        }
                    }

                    composable("create_task_screen") {
                        CreateTaskPage(
                            taskViewModel = taskViewModel,
                            onTaskCreated = { createdTask -> taskViewModel.addTask(createdTask) },
                            onNavigateUp = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}