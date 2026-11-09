package com.example.alzapp.data.games.hiddenObjects.model

import androidx.annotation.DrawableRes

// level of the game; includes objects that needs to be found, level id and displayed image
data class HiddenObjectLevel(
    val id: Int,
    @field:DrawableRes val imageResId: Int,
    val objectsToFind: List<HiddenObject>
)