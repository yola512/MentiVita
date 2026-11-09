package com.example.alzapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.alzapp.core.AppDatabase
import com.example.alzapp.data.games.cryptogram.ProverbLibrary
import com.example.alzapp.data.user.local.AchievementDao
import com.example.alzapp.data.user.AchievementLibrary
import com.example.alzapp.data.games.cryptogram.local.ProverbDao
import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectDao
import com.example.alzapp.data.games.sudoku.local.SudokuDao
import com.example.alzapp.data.medicationReminder.local.MedicationDao
import com.example.alzapp.data.notifications.local.SnoozeDao
import com.example.alzapp.data.recipes.local.RecipeDao
import com.example.alzapp.data.recipes.RecipeLibrary
import com.example.alzapp.data.todo.local.TodoDao
import com.example.alzapp.data.settings.local.SettingsDao
import com.example.alzapp.data.user.local.UserStatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseProvider: Provider<AppDatabase>
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val database = databaseProvider.get()
                    populateInitialData(database)
                }
            }
        })
        .fallbackToDestructiveMigration()
        .build()
    }

    private suspend fun populateInitialData(db: AppDatabase) {
        val recipeDao = db.recipeDao()
        // all recipes available in the app
        val initialRecipes = RecipeLibrary.getInitialRecipes()
        recipeDao.insertRecipes(initialRecipes)

        // initial achievements
        val achievementDao = db.achievementDao()
        val initialAchievements = AchievementLibrary.getInitialAchievements()
        achievementDao.insertAchievements(initialAchievements)

        // initial proverbs (cryptogram game)
        val proverbDao = db.proverbDao()
        val initialProverbs = ProverbLibrary.getInitialProverbs()
        proverbDao.insertProverbs(initialProverbs)
    }

    // DAOs
    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()
    @Provides
    fun provideMedicationDao(db: AppDatabase): MedicationDao = db.medicationDao()
    @Provides
    fun provideRecipeDao(db: AppDatabase): RecipeDao = db.recipeDao()
    @Provides
    fun provideSudokuDao(db: AppDatabase): SudokuDao = db.sudokuDao()
    @Provides
    fun provideProverbDao(db: AppDatabase): ProverbDao = db.proverbDao()
    @Provides
    fun provideSettingsDao(db: AppDatabase): SettingsDao = db.settingsDao()
    @Provides
    fun provideUserStatsDao(db: AppDatabase): UserStatsDao = db.userStatsDao()
    @Provides
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()
    @Provides
    fun provideSnoozeDao(db: AppDatabase): SnoozeDao = db.snoozeDao()
    @Provides
    fun provideHiddenObjectDao(db: AppDatabase): HiddenObjectDao = db.hiddenObjectDao()
}
