import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tahubulatz.Task
import com.example.tahubulatz.TaskViewModel
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
    val mContext = LocalContext.current
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
                Toast.makeText(mContext, "Task Created", Toast.LENGTH_LONG).show()
            }) {
                Text("Create")
            }
        }
    )
    // Wrapper composable with padding
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Column for the content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // OutlinedTextField for Task Name
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Adjust the width as needed
                    .border(40.dp, Color.Transparent),
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // OutlinedTextField for Deadline Date
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Adjust the width as needed
                    .border(40.dp, Color.Transparent),
                value = taskDeadline,
                onValueChange = { taskDeadline = it },
                label = { Text("Deadline Date (YYYY-MM-DD)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // OutlinedTextField for Details
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Adjust the width as needed
                    .border(40.dp, Color.Transparent),
                value = taskDetails,
                onValueChange = { taskDetails = it },
                label = { Text("Details") },
                maxLines = 5
            )
        }
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
