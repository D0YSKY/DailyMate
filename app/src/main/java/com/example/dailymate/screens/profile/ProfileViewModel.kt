package com.example.dailymate.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "DailyMate User",
    val userTitle: String = "Productivity Ninja ⚡",
    val totalTasksCount: Int = 0,
    val completedTasksCount: Int = 0,
    val isDarkMode: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val defaultPomodoroMinutes: Int = 25
)

class ProfileViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    private val _isSoundEnabled = MutableStateFlow(true)
    private val _defaultPomodoroMinutes = MutableStateFlow(25)

    val uiState: StateFlow<ProfileUiState> = combine(
        _isDarkMode,
        _isSoundEnabled,
        _defaultPomodoroMinutes,
        repository.allTasks
    ) { darkMode, sound, pomodoroMins, tasks ->
        ProfileUiState(
            totalTasksCount = tasks.size,
            completedTasksCount = tasks.count { it.isCompleted },
            isDarkMode = darkMode,
            isSoundEnabled = sound,
            defaultPomodoroMinutes = pomodoroMins
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun toggleSound(enabled: Boolean) {
        _isSoundEnabled.value = enabled
    }

    fun setPomodoroMinutes(minutes: Int) {
        _defaultPomodoroMinutes.value = minutes
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.clearCompletedTasks()
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }
}
