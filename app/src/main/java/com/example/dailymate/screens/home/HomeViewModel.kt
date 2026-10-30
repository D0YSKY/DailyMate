package com.example.dailymate.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymate.data.model.TaskCategory
import com.example.dailymate.data.model.TaskEntity
import com.example.dailymate.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class TaskFilter {
    ALL, TODAY, PENDING, COMPLETED
}

data class HomeUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val totalTasksCount: Int = 0,
    val completedTasksCount: Int = 0,
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val selectedCategory: TaskCategory? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class HomeViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter.ALL)
    private val _category = MutableStateFlow<TaskCategory?>(null)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        repository.allTasks,
        _filter,
        _category,
        _searchQuery
    ) { allTasks, filter, category, query ->
        val todayStart = getTodayStartMillis()
        val todayEnd = getTodayEndMillis()

        val filteredTasks = allTasks.filter { task ->
            val matchesFilter = when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.TODAY -> task.dueDate in todayStart..todayEnd
                TaskFilter.PENDING -> !task.isCompleted
                TaskFilter.COMPLETED -> task.isCompleted
            }

            val matchesCategory = category == null || task.category == category

            val matchesQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)

            matchesFilter && matchesCategory && matchesQuery
        }

        val todayTasks = allTasks.filter { it.dueDate in todayStart..todayEnd }
        val todayTotal = todayTasks.size
        val todayCompleted = todayTasks.count { it.isCompleted }

        HomeUiState(
            tasks = filteredTasks,
            totalTasksCount = if (todayTotal > 0) todayTotal else allTasks.size,
            completedTasksCount = if (todayTotal > 0) todayCompleted else allTasks.count { it.isCompleted },
            selectedFilter = filter,
            selectedCategory = category,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun setFilter(filter: TaskFilter) {
        _filter.value = filter
    }

    fun setCategory(category: TaskCategory?) {
        _category.value = if (_category.value == category) null else category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getTodayEndMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}
