package com.example.dailymate.screens.focustimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.model.TaskEntity
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PomodoroMode(val title: String, val defaultMinutes: Int) {
    FOCUS("Focus", 25),
    SHORT_BREAK("Short Break", 5),
    LONG_BREAK("Long Break", 15)
}

data class TimerCoreState(
    val mode: PomodoroMode,
    val remainingSeconds: Int,
    val totalSeconds: Int,
    val isRunning: Boolean
)

data class FocusTimerUiState(
    val mode: PomodoroMode = PomodoroMode.FOCUS,
    val remainingSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val completedSessionsCount: Int = 0,
    val totalFocusMinutes: Int = 0,
    val selectedTask: TaskEntity? = null,
    val availableTasks: List<TaskEntity> = emptyList()
)

class FocusTimerViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _mode = MutableStateFlow(PomodoroMode.FOCUS)
    private val _remainingSeconds = MutableStateFlow(25 * 60)
    private val _totalSeconds = MutableStateFlow(25 * 60)
    private val _isRunning = MutableStateFlow(false)

    private val _completedSessions = MutableStateFlow(0)
    private val _totalFocusMinutes = MutableStateFlow(0)
    private val _selectedTaskId = MutableStateFlow<Long?>(null)

    private var timerJob: Job? = null

    private val timerCoreFlow = combine(_mode, _remainingSeconds, _totalSeconds, _isRunning) { mode, rem, tot, running ->
        TimerCoreState(mode, rem, tot, running)
    }

    private val statsFlow = combine(_completedSessions, _totalFocusMinutes) { sess, mins ->
        sess to mins
    }

    val uiState: StateFlow<FocusTimerUiState> = combine(
        timerCoreFlow,
        statsFlow,
        _selectedTaskId,
        repository.allTasks
    ) { core, stats, selectedTaskId, tasks ->
        val pendingTasks = tasks.filter { !it.isCompleted }
        val selected = pendingTasks.find { it.id == selectedTaskId }

        FocusTimerUiState(
            mode = core.mode,
            remainingSeconds = core.remainingSeconds,
            totalSeconds = core.totalSeconds,
            isRunning = core.isRunning,
            completedSessionsCount = stats.first,
            totalFocusMinutes = stats.second,
            selectedTask = selected,
            availableTasks = pendingTasks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FocusTimerUiState()
    )

    fun setMode(newMode: PomodoroMode) {
        pauseTimer()
        _mode.value = newMode
        val secs = newMode.defaultMinutes * 60
        _totalSeconds.value = secs
        _remainingSeconds.value = secs
    }

    fun startTimer() {
        if (_isRunning.value) return
        _isRunning.value = true

        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _isRunning.value) {
                delay(1000L)
                _remainingSeconds.value -= 1
            }

            if (_remainingSeconds.value <= 0) {
                _isRunning.value = false
                if (_mode.value == PomodoroMode.FOCUS) {
                    _completedSessions.value += 1
                    _totalFocusMinutes.value += _mode.value.defaultMinutes
                }
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        pauseTimer()
        _remainingSeconds.value = _totalSeconds.value
    }

    fun selectTask(taskId: Long?) {
        _selectedTaskId.value = taskId
    }
}
