package com.example.dailymate.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.model.TaskEntity
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class CalendarUiState(
    val selectedDateMillis: Long = System.currentTimeMillis(),
    val tasksForDate: List<TaskEntity> = emptyList(),
    val totalCount: Int = 0,
    val completedCount: Int = 0
)

class CalendarViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(getTodayStartMillis())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = _selectedDate.flatMapLatest { selectedMillis ->
        val startOfDay = getStartOfDay(selectedMillis)
        val endOfDay = getEndOfDay(selectedMillis)

        repository.getTasksForDate(startOfDay, endOfDay).map { tasks ->
            CalendarUiState(
                selectedDateMillis = selectedMillis,
                tasksForDate = tasks,
                totalCount = tasks.size,
                completedCount = tasks.count { it.isCompleted }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun selectDate(millis: Long) {
        _selectedDate.value = millis
    }

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

    private fun getTodayStartMillis(): Long {
        return getStartOfDay(System.currentTimeMillis())
    }

    private fun getStartOfDay(timeMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getEndOfDay(timeMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
}
