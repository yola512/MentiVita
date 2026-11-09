package com.example.alzapp.data.games.hiddenObjects.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

// object that needs to be found
@Serializable
data class HiddenObject(
    val id: Int,
    val name: String,
    val xPercent: Float, // 0.0 to 100.0
    val yPercent: Float, // 0.0 to 100.0
    val radiusPercent: Float, // radius in percentage of the smaller dimension
    @field:DrawableRes val iconResId: Int? = null
)
