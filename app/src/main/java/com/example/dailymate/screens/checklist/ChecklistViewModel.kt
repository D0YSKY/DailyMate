package com.example.dailymate.screens.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.model.TaskEntity
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChecklistUiState(
    val pendingTasks: List<TaskEntity> = emptyList(),
    val completedTasks: List<TaskEntity> = emptyList(),
    val totalCount: Int = 0,
    val completedCount: Int = 0
)

class ChecklistViewModel(private val repository: TaskRepository) : ViewModel() {

    val uiState: StateFlow<ChecklistUiState> = repository.allTasks.map { tasks ->
        val pending = tasks.filter { !it.isCompleted }
        val completed = tasks.filter { it.isCompleted }

        ChecklistUiState(
            pendingTasks = pending,
            completedTasks = completed,
            totalCount = tasks.size,
            completedCount = completed.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChecklistUiState()
    )

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.clearCompletedTasks()
        }
    }
}
