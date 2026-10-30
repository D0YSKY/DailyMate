package com.example.dailymate.data.repository

import com.example.dailymate.data.local.TaskDao
import com.example.dailymate.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksForDate(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDate(startOfDay, endOfDay)
    }

    fun getTaskById(id: Long): Flow<TaskEntity?> {
        return taskDao.getTaskById(id)
    }

    suspend fun insertTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }

    suspend fun clearCompletedTasks() {
        taskDao.clearCompletedTasks()
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}
