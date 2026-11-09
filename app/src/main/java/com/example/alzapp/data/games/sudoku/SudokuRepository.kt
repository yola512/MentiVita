package com.example.alzapp.data.games.sudoku

import com.example.alzapp.data.games.sudoku.local.SudokuDao
import com.example.alzapp.data.games.sudoku.local.SudokuEntity
import javax.inject.Inject
import javax.inject.Singleton

// original source: https://github.com/katarinakovacova/neurodoku
@Singleton
class SudokuRepository @Inject constructor(
    private val sudokuDao: SudokuDao
) {
    suspend fun getUnfinishedSudoku(): SudokuEntity? {
        return sudokuDao.getUnfinishedSudoku()
    }

    suspend fun insertSudoku(sudoku: SudokuEntity) {
        sudokuDao.insertSudoku(sudoku)
    }
}