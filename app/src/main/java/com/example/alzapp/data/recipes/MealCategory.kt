package com.example.alzapp.data.recipes

import com.example.alzapp.R

enum class MealCategory(val displayName: String, val imageRes: Int) {
    VEGETARIAN("Bez mięsa", R.drawable.vegetarian),
    MEAT("Dania mięsne", R.drawable.meat),
    SEAFOOD("Ryby i owoce morza", R.drawable.seafood),
    SALADS_AND_SNACKS("Sałatki i przystawki", R.drawable.salads_snacks),
    DESSERTS("Na słodko", R.drawable.sweet)
}