package com.example.alzapp.ui.screens.games

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alzapp.ui.screens.games.cryptogram.CryptogramScreen
import com.example.alzapp.ui.screens.games.hiddenobject.HiddenObjectScreen
import com.example.alzapp.ui.screens.games.postman.PostmanScreen
import com.example.alzapp.ui.screens.games.sudoku.SudokuScreen
import com.example.alzapp.utils.GameIds

@Composable
fun InGameScreen(
    gameId: Int,
    onBackClick: () -> Unit
) {
    when (gameId) {
        GameIds.SUDOKU -> SudokuScreen(
            viewModel = hiltViewModel(),
            onBackClick = onBackClick
        )

        GameIds.POSTMAN -> PostmanScreen(
            viewModel = hiltViewModel(),
            onBackClick = onBackClick
        )

        GameIds.PROVERB -> CryptogramScreen(
            viewModel = hiltViewModel(),
            onBackClick = onBackClick
        )

        GameIds.HIDDEN_OBJECTS -> HiddenObjectScreen(
            viewModel = hiltViewModel(),
            onBackClick = onBackClick
        )
    }
}
