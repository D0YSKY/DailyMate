package com.example.dailymate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dailymate.data.repository.TaskRepository
import com.example.dailymate.screens.addtask.AddTaskScreen
import com.example.dailymate.screens.addtask.AddTaskViewModel
import com.example.dailymate.screens.calendar.CalendarScreen
import com.example.dailymate.screens.calendar.CalendarViewModel
import com.example.dailymate.screens.checklist.ChecklistScreen
import com.example.dailymate.screens.checklist.ChecklistViewModel
import com.example.dailymate.screens.focustimer.FocusTimerScreen
import com.example.dailymate.screens.focustimer.FocusTimerViewModel
import com.example.dailymate.screens.home.HomeScreen
import com.example.dailymate.screens.home.HomeViewModel
import com.example.dailymate.screens.profile.ProfileScreen
import com.example.dailymate.screens.profile.ProfileViewModel
import com.example.dailymate.ui.ViewModelFactory

@Composable
fun DailyMateNavGraph(
    navController: NavHostController,
    repository: TaskRepository,
    onDarkModeToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(repository))
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddTask = { taskId ->
                    navController.navigate(Screen.AddTask.createRoute(taskId))
                }
            )
        }

        composable(Screen.Calendar.route) {
            val viewModel: CalendarViewModel = viewModel(factory = ViewModelFactory(repository))
            CalendarScreen(
                viewModel = viewModel,
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.AddTask.createRoute(taskId))
                }
            )
        }

        composable(Screen.Checklist.route) {
            val viewModel: ChecklistViewModel = viewModel(factory = ViewModelFactory(repository))
            ChecklistScreen(
                viewModel = viewModel,
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.AddTask.createRoute(taskId))
                }
            )
        }

        composable(Screen.FocusTimer.route) {
            val viewModel: FocusTimerViewModel = viewModel(factory = ViewModelFactory(repository))
            FocusTimerScreen(
                viewModel = viewModel
            )
        }

        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(repository))
            ProfileScreen(
                viewModel = viewModel,
                onDarkModeToggled = onDarkModeToggled
            )
        }

        composable(
            route = Screen.AddTask.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val taskIdString = backStackEntry.arguments?.getString("taskId")
            val taskId = taskIdString?.toLongOrNull()
            val viewModel: AddTaskViewModel = viewModel(factory = ViewModelFactory(repository, taskId))

            AddTaskScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
