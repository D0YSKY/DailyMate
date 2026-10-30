package com.example.dailymate.screens.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.model.TaskCategory
import com.example.dailymate.data.model.TaskEntity
import com.example.dailymate.data.model.TaskPriority
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

data class AddTaskUiState(
    val title: String = "",
    val description: String = "",
    val dueDate: Long = System.currentTimeMillis(),
    val timeString: String = "09:00",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.PERSONAL,
    val isEditing: Boolean = false,
    val taskId: Long? = null,
    val titleError: String? = null,
    val isLoading: Boolean = false
)

class AddTaskViewModel(
    private val repository: TaskRepository,
    private val initialTaskId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState

    private val _saveSuccessEvent = MutableSharedFlow<Unit>()
    val saveSuccessEvent: SharedFlow<Unit> = _saveSuccessEvent.asSharedFlow()

    init {
        if (initialTaskId != null && initialTaskId > 0) {
            loadExistingTask(initialTaskId)
        }
    }

    private fun loadExistingTask(taskId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val task = repository.getTaskById(taskId).firstOrNull()
            if (task != null) {
                _uiState.value = AddTaskUiState(
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    timeString = task.timeString,
                    priority = task.priority,
                    category = task.category,
                    isEditing = true,
                    taskId = task.id
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, titleError = null)
    }

    fun onDescriptionChange(newDesc: String) {
        _uiState.value = _uiState.value.copy(description = newDesc)
    }

    fun onDateChange(newDateMillis: Long) {
        _uiState.value = _uiState.value.copy(dueDate = newDateMillis)
    }

    fun onTimeChange(newTime: String) {
        _uiState.value = _uiState.value.copy(timeString = newTime)
    }

    fun onPriorityChange(newPriority: TaskPriority) {
        _uiState.value = _uiState.value.copy(priority = newPriority)
    }

    fun onCategoryChange(newCategory: TaskCategory) {
        _uiState.value = _uiState.value.copy(category = newCategory)
    }

    fun saveTask() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(titleError = "Title cannot be empty")
            return
        }

        viewModelScope.launch {
            val taskEntity = TaskEntity(
                id = currentState.taskId ?: 0,
                title = currentState.title.trim(),
                description = currentState.description.trim(),
                dueDate = currentState.dueDate,
                timeString = currentState.timeString,
                priority = currentState.priority,
                category = currentState.category
            )

            if (currentState.isEditing) {
                repository.updateTask(taskEntity)
            } else {
                repository.insertTask(taskEntity)
            }

            _saveSuccessEvent.emit(Unit)
        }
    }
}
