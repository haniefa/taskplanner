import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tahubulatz.R
import com.example.tahubulatz.Task
import com.example.tahubulatz.ui.theme.Pink40
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
        modifier = Modifier.shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(0.dp)),
        navigationIcon = {
//            Icon(Icons.Default.Home, contentDescription = "Delete", tint = Color.Black, modifier = Modifier
//                .size(36.dp))
        },
        actions = {
//            TextButton(onClick = {
//                val newTask = Task(
//                    id = System.currentTimeMillis().toInt(),
//                    name = taskName,
//                    deadline = convertToDateMillis(taskDeadline),
//                    isCompleted = false,
//                    details = taskDetails
//                )
//                taskViewModel.addTask(newTask)
//                Log.d("CreateTaskPage", "taskId: ${newTask.id}")
//                onNavigateUp()
//                Toast.makeText(mContext, "Task Created", Toast.LENGTH_LONG).show()
//            }) {
//                Text("Create")
//            }
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
                    .border(40.dp, Color.Transparent)
                ,
                shape = RoundedCornerShape(18.dp),
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
                shape = RoundedCornerShape(18.dp),
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
                shape = RoundedCornerShape(18.dp),
                value = taskDetails,
                onValueChange = { taskDetails = it },
                label = { Text("Details") },
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(40.dp))


            Row() {
                // Create Button
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
                    Text(text ="Create",
//                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }


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
