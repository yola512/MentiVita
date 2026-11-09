package com.example.alzapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

// model for each item in bottom navigation bar
data class NavBarItem<T: Any>(
    val label: String,
    val route: T,
    val icon: ImageVector,
    val iconScale: Float = 1f // manual scale to normalize icons with different blank spaces
)