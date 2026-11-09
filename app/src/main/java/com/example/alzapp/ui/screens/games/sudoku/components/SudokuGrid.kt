package com.example.alzapp.ui.screens.games.sudoku.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun SudokuGrid(
    modifier: Modifier = Modifier,
    sudoku: Array<Array<Int?>>,
    originalCells: Array<Array<Boolean>>,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (row: Int, col: Int) -> Unit,
    isResultVisible: Boolean = false,
    showSolutions: Boolean = false,
    completeGrid: Array<IntArray>? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(3.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            .padding(1.dp)
    ) {
        for (rowIndex in sudoku.indices) {
            Row (modifier = modifier.weight(1f)) {
                for (colIndex in sudoku[rowIndex].indices) {
                    val userValue = sudoku[rowIndex][colIndex]
                    val isOriginal = originalCells[rowIndex][colIndex]
                    val correctValue = completeGrid?.get(rowIndex)?.get(colIndex)
                    
                    val displayValue = if (showSolutions && !isOriginal) correctValue else userValue

                    // colors on the board are changed after the user finished the game and the "ok" button is clicked on the end-game pop-up
                    var statusColor: Color? = null
                    if (isResultVisible && !isOriginal) {
                        if (showSolutions) {
                            // board showing correct solution (all cells that could've been modified by the user (!isOriginal) are marked as green)
                            statusColor = Color.Green.copy(alpha = 0.3f)
                        } else if (userValue != null && correctValue != null) {
                            // board showing user's solution (cells that were filled incorrectly are marked as red and those filled correctly - marked as green)
                            statusColor = if (userValue == correctValue) {
                                Color.Green.copy(alpha = 0.3f)
                            } else {
                                Color.Red.copy(alpha = 0.3f)
                            }
                        }
                    }

                    SudokuCell(
                        modifier = modifier.weight(1f).fillMaxHeight(),
                        number = displayValue,
                        isSelected = selectedCell == Pair(rowIndex, colIndex),
                        isOriginal = isOriginal,
                        hasRightBorder = (colIndex + 1) % 3 == 0 && colIndex != sudoku[rowIndex].lastIndex,
                        hasBottomBorder = (rowIndex + 1) % 3 == 0 && rowIndex != sudoku.lastIndex,
                        onClick = { onCellClick(rowIndex, colIndex) },
                        statusBackgroundColor = statusColor,
                        isBlocked = isResultVisible
                    )
                }
            }
        }
    }
}