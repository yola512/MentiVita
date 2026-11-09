package com.example.alzapp.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.alzapp.R
import com.example.alzapp.ui.screens.settings.viewModel.SettingsViewModel
import com.example.alzapp.ui.theme.AlzAppTheme

@SuppressLint("RestrictedApi")
@Composable
fun MainRoot(
    modifier: Modifier,
    highlightedReminders: List<Pair<Int, String?>> = emptyList(),
    highlightedTodoId: Int? = null,
    navigateToTodo: Boolean = false,
    onTodoNavigated: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // navigation logic for highlighted reminders (medication or to-do)
    LaunchedEffect(highlightedReminders, highlightedTodoId, navigateToTodo) {
        if (highlightedReminders.isNotEmpty()) {
            navController.navigate(MedicationReminder) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else if (highlightedTodoId != null || navigateToTodo) {
            navController.navigate(Todo) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val iconRecipes = ImageVector.vectorResource(R.drawable.icon_recipes)
    val iconMedications = ImageVector.vectorResource(R.drawable.icon_medications)
    val iconHome = ImageVector.vectorResource(R.drawable.icon_home)
    val iconTodo = ImageVector.vectorResource(R.drawable.icon_todo)
    val iconSettings = ImageVector.vectorResource(R.drawable.icon_settings)

    // list of items in bottom navbar with individual scales to normalize visual size
    val items = remember {
        listOf(
            NavBarItem(
                label = "Przepisy",
                route = Recipes,
                icon = iconRecipes,
                iconScale = 1.0f
            ),
            NavBarItem(
                label = "Leki",
                route = MedicationReminder,
                icon = iconMedications,
                iconScale = 1.2f
            ),
            NavBarItem(
                label = "Start",
                route = Home,
                icon = iconHome,
                iconScale = 1.2f
            ),
            NavBarItem(
                label = "Zadania",
                route = Todo,
                icon = iconTodo,
                iconScale = 1.0f
            ),
            NavBarItem(
                label = "Ustawienia",
                route = Settings,
                icon = iconSettings,
                iconScale = 1.0f
            ),
        )
    }

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settings by settingsViewModel.settings.collectAsState()

    AlzAppTheme(
        fontSize = settings.fontSize
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            bottomBar = {
                val showBottomBar = currentDestination?.let { dest ->
                    listOf(
                        InGameScreenRoute::class,
                        RecipeCategoryRoute::class,
                        RecipeDetailsRoute::class,
                        MedicationFormRoute::class,
                        TaskFormRoute::class
                    ).none { dest.hasRoute(it) }
                } ?: true
                if (showBottomBar) {
                    CustomNavigationBar(
                        items,
                        currentDestination,
                        onItemClick = { navItem ->
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
            }
        ) { innerPadding ->
            NavGraph(
                navController = navController,
                modifier = modifier.padding(innerPadding),
                highlightedReminders = highlightedReminders,
                highlightedTodoId = highlightedTodoId,
                navigateToTodo = navigateToTodo,
                onTodoNavigated = onTodoNavigated
            )
        }
    }
}
