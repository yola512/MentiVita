package com.example.alzapp.data.settings

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class FontSize(
    val bodySize: TextUnit,
    val headlineSize: TextUnit,
    val displaySize: TextUnit,
    val label: String
) {
    SMALL(18.sp, 22.sp, 26.sp, "Mała"),
    MEDIUM(22.sp, 26.sp, 32.sp, "Średnia"),
    LARGE(26.sp, 32.sp, 40.sp, "Duża")
}
