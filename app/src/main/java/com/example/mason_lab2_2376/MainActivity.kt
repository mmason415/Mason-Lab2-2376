package com.example.mason_lab2_2376
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

data class Task(val description: String, var isCompleted: Boolean = false)

@Composable
fun MainScreen() {
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<Task>() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Task Manager",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TaskInputField(taskText, onTextChange = { taskText = it }) {
            if (taskText.isNotBlank()) {
                tasks.add(Task(taskText))
                taskText = ""
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TaskList(tasks, onTaskCheckedChange = { task, isChecked ->
            task.isCompleted = isChecked
        }, onTaskDelete = { task ->
            tasks.remove(task)
        })
    }
}

@Composable
fun TaskInputField(text: String, onTextChange: (String) -> Unit, onAddTask: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            label = { Text("Enter task") }
        )
        Button(onClick = onAddTask, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onTaskCheckedChange: (Task, Boolean) -> Unit, onTaskDelete: (Task) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks) { task ->
            TaskItem(task, onTaskCheckedChange, onTaskDelete)
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskCheckedChange: (Task, Boolean) -> Unit, onTaskDelete: (Task) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked -> onTaskCheckedChange(task, isChecked) },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EE))
            )
            Text(
                text = task.description,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = if (task.isCompleted) Color.Gray else Color.Black,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            )
        }
        IconButton(onClick = { onTaskDelete(task) }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
