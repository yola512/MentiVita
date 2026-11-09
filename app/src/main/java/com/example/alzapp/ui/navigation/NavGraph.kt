package com.example.alzapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.alzapp.ui.screens.games.HomeScreen
import com.example.alzapp.ui.screens.games.InGameScreen
import com.example.alzapp.ui.screens.medicationReminder.form.MedicationForm
import com.example.alzapp.ui.screens.medicationReminder.MedicationScreen
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationViewModel
import com.example.alzapp.ui.screens.recipes.RecipeCategory
import com.example.alzapp.ui.screens.recipes.RecipeDetails
import com.example.alzapp.ui.screens.recipes.RecipesScreen
import com.example.alzapp.ui.screens.settings.SettingsScreen
import com.example.alzapp.ui.screens.todo.TaskForm
import com.example.alzapp.ui.screens.todo.TodoScreen
import com.example.alzapp.utils.FormMode
import java.time.LocalDate
import java.time.LocalTime

// NavGraph - data structure that contains each destination within the app and the connections between them
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    highlightedReminders: List<Pair<Int, String?>>,
    highlightedTodoId: Int?,
    navigateToTodo: Boolean,
    onTodoNavigated: () -> Unit
) {
    NavHost(navController = navController, startDestination = Home, modifier = modifier) {
        composable<Home> {
            HomeScreen(
                onGameClick = { gameId ->
                    navController.navigate(InGameScreenRoute(gameId))
                }
            )
        }
        composable<InGameScreenRoute> { backStackEntry ->
            val route: InGameScreenRoute = backStackEntry.toRoute()
            InGameScreen(gameId = route.gameId, onBackClick = { navController.popBackStack() })
        }
        composable<Recipes> {
            RecipesScreen(
                onCategoryClick = { navController.navigate(RecipeCategoryRoute(it)) },
                onRecipeClick = { navController.navigate(RecipeDetailsRoute(it)) }
            )
        }
        composable<RecipeCategoryRoute> { backStackEntry ->
            val route: RecipeCategoryRoute = backStackEntry.toRoute()
            RecipeCategory(
                category = route.category,
                onRecipeClick = { navController.navigate(RecipeDetailsRoute(it)) },
                onBackClick = { navController.popBackStack() })
        }
        composable<RecipeDetailsRoute> { backStackEntry ->
            val route: RecipeDetailsRoute = backStackEntry.toRoute()
            RecipeDetails(recipeId = route.recipeId, onBackClick = { navController.popBackStack() })
        }
        composable<MedicationReminder> {
            val medicationViewModel: MedicationViewModel = hiltViewModel()
            LaunchedEffect(highlightedReminders) {
                if (highlightedReminders.isNotEmpty()) {
                    medicationViewModel.onDateSelected(LocalDate.now())
                    highlightedReminders.forEach { (id, timeStr) ->
                        timeStr?.let { LocalTime.parse(it) }
                            ?.let { medicationViewModel.addHighlightedMedication(id, it) }
                    }
                }
            }
            MedicationScreen(
                viewModel = medicationViewModel,
                onAddMedicationClick = { date ->
                    navController.navigate(
                        MedicationFormRoute(
                            FormMode.ADD,
                            selectedDate = date.toString()
                        )
                    )
                },
                onMedicationClick = { med ->
                    navController.navigate(
                        MedicationFormRoute(
                            FormMode.INFO,
                            medicationId = med.id
                        )
                    )
                },
            )
        }
        composable<MedicationFormRoute> { backStackEntry ->
            val route: MedicationFormRoute = backStackEntry.toRoute()
            MedicationForm(
                viewModel = hiltViewModel(),
                mode = route.mode,
                medicationId = route.medicationId,
                initialDate = route.selectedDate,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(
                        MedicationFormRoute(
                            FormMode.EDIT,
                            medicationId = route.medicationId
                        )
                    )
                })
        }
        composable<Todo> {
            LaunchedEffect(highlightedTodoId, navigateToTodo) {
                if (highlightedTodoId != null || navigateToTodo) {
                    onTodoNavigated()
                }
            }
            TodoScreen(
                viewModel = hiltViewModel(),
                onAddTaskClick = { date ->
                    navController.navigate(
                        TaskFormRoute(
                            FormMode.ADD,
                            selectedDate = date.toString()
                        )
                    )
                },
                onTaskClick = { task ->
                    navController.navigate(
                        TaskFormRoute(
                            FormMode.INFO,
                            taskId = task.id
                        )
                    )
                },
                highlightedTodoId = highlightedTodoId
            )
        }
        composable<TaskFormRoute> { backStackEntry ->
            val route: TaskFormRoute = backStackEntry.toRoute()
            TaskForm(
                viewModel = hiltViewModel(),
                mode = route.mode,
                taskId = route.taskId,
                initialDate = route.selectedDate,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(
                        TaskFormRoute(
                            FormMode.EDIT,
                            taskId = route.taskId
                        )
                    )
                })
        }
        composable<Settings> { SettingsScreen() }
    }
}