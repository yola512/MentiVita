package com.example.alzapp.data.games.sudoku.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alzapp.data.games.GameCategory
import com.example.alzapp.data.games.sudoku.SudokuDifficulty

// original source: https://github.com/katarinakovacova/neurodoku
@Entity(tableName = "sudoku")
data class SudokuEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val completeGrid: Array<IntArray>,
    val maskedGrid: Array<Array<Boolean>>,
    val userGrid: Array<Array<Int?>>,
    val difficulty: SudokuDifficulty,
    val category: GameCategory = GameCategory.LOGIC,
    val timeSpent: Long,
    val isCompleted: Boolean = false,
    val createdAt: Long,
    val completedAt: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuEntity

        if (id != other.id) return false
        if (!completeGrid.contentDeepEquals(other.completeGrid)) return false
        if (!maskedGrid.contentDeepEquals(other.maskedGrid)) return false
        if (!userGrid.contentDeepEquals(other.userGrid)) return false
        if (difficulty != other.difficulty) return false
        if (category != other.category) return false
        if (timeSpent != other.timeSpent) return false
        if (isCompleted != other.isCompleted) return false
        if (createdAt != other.createdAt) return false
        if (completedAt != other.completedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + completeGrid.contentDeepHashCode()
        result = 31 * result + maskedGrid.contentDeepHashCode()
        result = 31 * result + userGrid.contentDeepHashCode()
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + timeSpent.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (completedAt?.hashCode() ?: 0)
        return result
    }
}