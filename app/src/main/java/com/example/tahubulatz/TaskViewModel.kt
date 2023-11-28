package com.example.tahubulatz

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class Task(
    val id: Int,
    var name: String,
    var deadline: Long,
    var isCompleted: Boolean = false,
    var details: String = ""
)

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks
    private val _taskId = mutableStateOf<Int?>(null)
    val taskId: State<Int?> = _taskId

    fun setTaskId(id: Int) {
        _taskId.value = id
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value + task.copy()  // Create a new Task instance
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map {
                if (it.id == updatedTask.id) updatedTask else it  // Replace the task if IDs match
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.filterNot { it.id == taskId }
        }
    }
    fun setTaskCompleted(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedTasks = _tasks.value.map {
                if (it.id == taskId) {
                    it.copy(isCompleted = isCompleted)
                } else {
                    it
                }
            }
            _tasks.value = updatedTasks
        }
    }
}
