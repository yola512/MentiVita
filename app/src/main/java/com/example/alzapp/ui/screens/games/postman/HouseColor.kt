package com.example.alzapp.ui.screens.games.postman

import androidx.compose.ui.graphics.Color

// all available colors of the house and name of each color displayed in feedback message in Postman game
enum class HouseColor(val color: Color, val displayName: String) {
    RED(Color(0xFFD32F2F), "czerwony"),
    BLUE(Color(0xFF1976D2), "niebieski"),
    GREEN(Color(0xFF388E3C), "zielony"),
    YELLOW(Color(0xFFFBC02D), "żółty"),
    ORANGE(Color(0xFFF57C00), "pomarańczowy"),
    PURPLE(Color(0xFF7B1FA2), "fioletowy")
}