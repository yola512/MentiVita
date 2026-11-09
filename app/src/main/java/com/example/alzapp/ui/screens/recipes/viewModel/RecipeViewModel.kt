package com.example.alzapp.ui.screens.recipes.viewModel

import androidx.lifecycle.ViewModel
import com.example.alzapp.data.recipes.MealCategory
import com.example.alzapp.data.recipes.local.RecipeEntity
import com.example.alzapp.data.recipes.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allRecipesFiltered: Flow<List<RecipeEntity>> =
        repository.allRecipes.combine(_searchQuery) { recipes, query ->
            filterRecipes(recipes, query)
        }

    fun getRecipesByCategory(category: MealCategory): Flow<List<RecipeEntity>> {
        return repository.getRecipesByCategory(category)
            .combine(_searchQuery) { recipes, query ->
                filterRecipes(recipes, query)
            }
    }

    private fun filterRecipes(recipes: List<RecipeEntity>, query: String): List<RecipeEntity> {
        return if (query.isBlank()) {
            recipes
        } else {
            recipes.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    fun getRecipeById(id: Int): Flow<RecipeEntity?> {
        return repository.getRecipeById(id)
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
