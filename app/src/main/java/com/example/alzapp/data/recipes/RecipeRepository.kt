package com.example.alzapp.data.recipes

import com.example.alzapp.data.recipes.local.RecipeDao
import com.example.alzapp.data.recipes.local.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val dao: RecipeDao
) {
    val allRecipes: Flow<List<RecipeEntity>> = dao.getAllRecipes()

    fun getRecipesByCategory(category: MealCategory): Flow<List<RecipeEntity>> {
        return dao.getRecipesByCategory(category)
    }

    fun getRecipeById(id: Int): Flow<RecipeEntity?> {
        return dao.getRecipeById(id)
    }
}