package com.example.dailymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dailymate.data.local.AppDatabase
import com.example.dailymate.data.repository.TaskRepository
import com.example.dailymate.ui.MainAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = TaskRepository(database.taskDao())

        setContent {
            MainAppScreen(repository = repository)
        }
    }
}
