package com.example.alzapp.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alzapp.data.user.local.AchievementDao
import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectDao
import com.example.alzapp.data.medicationReminder.local.MedicationDao
import com.example.alzapp.data.games.cryptogram.local.ProverbDao
import com.example.alzapp.data.recipes.local.RecipeDao
import com.example.alzapp.data.settings.local.SettingsDao
import com.example.alzapp.data.notifications.local.SnoozeDao
import com.example.alzapp.data.games.sudoku.local.SudokuDao
import com.example.alzapp.data.todo.local.TodoDao
import com.example.alzapp.data.user.local.UserStatsDao
import com.example.alzapp.data.user.local.AchievementEntity
import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectEntity
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import com.example.alzapp.data.games.cryptogram.local.ProverbEntity
import com.example.alzapp.data.recipes.local.RecipeEntity
import com.example.alzapp.data.settings.local.SettingsEntity
import com.example.alzapp.data.notifications.local.SnoozeEntity
import com.example.alzapp.data.games.sudoku.local.SudokuEntity
import com.example.alzapp.data.todo.local.TodoTaskEntity
import com.example.alzapp.data.user.local.UserStatsEntity

@Database(entities = [
    TodoTaskEntity::class,
    MedicationEntity::class,
    SudokuEntity::class,
    ProverbEntity::class,
    SettingsEntity::class,
    RecipeEntity::class,
    UserStatsEntity::class,
    AchievementEntity::class,
    HiddenObjectEntity::class,
    SnoozeEntity::class
    ],
    version = 29,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao
    abstract fun medicationDao() : MedicationDao
    abstract fun sudokuDao() : SudokuDao
    abstract fun proverbDao() : ProverbDao
    abstract fun settingsDao() : SettingsDao
    abstract fun recipeDao() : RecipeDao
    abstract fun userStatsDao() : UserStatsDao
    abstract fun achievementDao() : AchievementDao
    abstract fun hiddenObjectDao() : HiddenObjectDao
    abstract fun snoozeDao() : SnoozeDao
}