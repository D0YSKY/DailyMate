package com.example.dailymate.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Checklist : Screen("checklist")
    object FocusTimer : Screen("focustimer")
    object Profile : Screen("profile")
    object AddTask : Screen("addtask?taskId={taskId}") {
        fun createRoute(taskId: Long? = null): String {
            return if (taskId != null) "addtask?taskId=$taskId" else "addtask"
        }
    }
}
