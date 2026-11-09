package com.example.alzapp.data.recipes.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alzapp.data.recipes.MealCategory

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageRes: Int,
    val category: MealCategory,
    val ingredients: List<String>,
    val steps: List<String>,
    val cookingTime: String = "",
    val difficulty: String = "",
    val tools: List<String> = emptyList()
)