package com.example.alzapp.data.games.sudoku

import com.example.alzapp.data.games.sudoku.local.SudokuEntity
import javax.inject.Inject

// original source: https://github.com/katarinakovacova/neurodoku
// UseCases were deleted and put into one file SudokuGenerator
class SudokuGenerator @Inject constructor() {

    fun generateNewGame(difficulty: SudokuDifficulty): SudokuEntity {
        val completeGrid = generateSudokuGrid()
        val maskedGrid = generateMaskedGrid(difficulty)

        val userGrid: Array<Array<Int?>> = Array(9) { row ->
            Array(9) { col ->
                if (maskedGrid[row][col]) completeGrid[row][col] else null
            }
        }

        return SudokuEntity(
            completeGrid = completeGrid,
            maskedGrid = maskedGrid,
            userGrid = userGrid,
            difficulty = difficulty,
            timeSpent = 0L,
            isCompleted = false,
            createdAt = System.currentTimeMillis(),
            completedAt = null
        )
    }

    private fun generateSudokuGrid(): Array<IntArray> {
        val sudokuGrid = Array(9) { IntArray(9) }
        fillSudokuGrid(sudokuGrid)
        return sudokuGrid
    }

    private fun fillSudokuGrid(sudokuGrid: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (column in 0..8) {
                if (sudokuGrid[row][column] == 0) {
                    val numbers = (1..9).shuffled()
                    for (number in numbers) {
                        if (isSudokuGridValid(sudokuGrid, row, column, number)) {
                            sudokuGrid[row][column] = number
                            if (fillSudokuGrid(sudokuGrid)) return true
                            sudokuGrid[row][column] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isSudokuGridValid(sudokuGrid: Array<IntArray>, row: Int, column: Int, number: Int): Boolean {
        return !isNumberInRow(sudokuGrid, row, number) &&
                !isNumberInColumn(sudokuGrid, column, number) &&
                !isNumberInBox(sudokuGrid, row, column, number)
    }

    private fun isNumberInRow(sudokuGrid: Array<IntArray>, row: Int, number: Int): Boolean {
        return sudokuGrid[row].contains(number)
    }

    private fun isNumberInColumn(sudokuGrid: Array<IntArray>, column: Int, number: Int): Boolean {
        return sudokuGrid.any { it[column] == number }
    }

    private fun isNumberInBox(sudokuGrid: Array<IntArray>, row: Int, column: Int, number: Int): Boolean {
        val startRow = row - row % 3
        val startColumn = column - column % 3
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (sudokuGrid[startRow + i][startColumn + j] == number) return true
            }
        }
        return false
    }

    private fun generateMaskedGrid(sudokuDifficulty: SudokuDifficulty): Array<Array<Boolean>> {
        val visibleCount = sudokuDifficulty.visibleCount
        val maskForSudoku = Array(9) { Array(9) { false } }
        val positions = mutableListOf<Pair<Int, Int>>()

        for (row in 0..8) {
            for (column in 0..8) {
                positions.add(Pair(row, column))
            }
        }

        positions.shuffle()

        val visibleCells = positions.take(visibleCount)

        for ((row, column) in visibleCells) {
            maskForSudoku[row][column] = true
        }

        return maskForSudoku
    }
}