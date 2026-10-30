package com.example.dailymate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailymate.data.repository.TaskRepository
import com.example.dailymate.screens.addtask.AddTaskViewModel
import com.example.dailymate.screens.calendar.CalendarViewModel
import com.example.dailymate.screens.checklist.ChecklistViewModel
import com.example.dailymate.screens.focustimer.FocusTimerViewModel
import com.example.dailymate.screens.home.HomeViewModel
import com.example.dailymate.screens.profile.ProfileViewModel

class ViewModelFactory(
    private val repository: TaskRepository,
    private val taskId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddTaskViewModel::class.java) -> {
                AddTaskViewModel(repository, taskId) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChecklistViewModel::class.java) -> {
                ChecklistViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FocusTimerViewModel::class.java) -> {
                FocusTimerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
