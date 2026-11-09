package com.example.alzapp.ui.navigation

import com.example.alzapp.utils.FormMode
import com.example.alzapp.data.recipes.MealCategory
import kotlinx.serialization.Serializable

// bottom nav bar routes
@Serializable
object Recipes
@Serializable
object MedicationReminder
@Serializable
object Home
@Serializable
object Todo
@Serializable
object Settings

// --------------------------------------------------------
@Serializable
data class RecipeCategoryRoute(
    val category: MealCategory
)

@Serializable
data class RecipeDetailsRoute(
    val recipeId: Int
)
@Serializable
data class MedicationFormRoute(
    val mode: FormMode,
    val medicationId: Int? = null,
    val selectedDate: String? = null
)

@Serializable
data class InGameScreenRoute(
    val gameId: Int
)

@Serializable
data class TaskFormRoute(
    val mode: FormMode,
    val taskId: Int? = null,
    val selectedDate: String? = null
)
