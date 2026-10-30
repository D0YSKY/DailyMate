package com.example.dailymate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskPriority(val label: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

enum class TaskCategory(val label: String) {
    WORK("Work"),
    PERSONAL("Personal"),
    STUDY("Study"),
    HEALTH("Health"),
    OTHER("Other")
}

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDate: Long, // epoch millis representing date
    val timeString: String = "09:00",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: TaskCategory = TaskCategory.PERSONAL,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
