package com.example.dailymate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailymate.data.repository.TaskRepository
import com.example.dailymate.ui.components.DailyMateBottomBar
import com.example.dailymate.ui.components.bottomNavItems
import com.example.dailymate.ui.navigation.DailyMateNavGraph
import com.example.dailymate.ui.theme.DailyMateTheme

@Composable
fun MainAppScreen(repository: TaskRepository) {
    var isDarkMode by remember { mutableStateOf(false) }

    DailyMateTheme(darkTheme = isDarkMode) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomBar = bottomNavItems.any { item ->
            currentRoute?.startsWith(item.route) == true
        }

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    DailyMateBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            DailyMateNavGraph(
                navController = navController,
                repository = repository,
                onDarkModeToggled = { enabled -> isDarkMode = enabled },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
