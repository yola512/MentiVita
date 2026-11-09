package com.example.alzapp.ui.screens.games.sudoku.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.data.games.sudoku.SudokuDifficulty
import com.example.alzapp.ui.screens.games.components.ActionButtonGame
import com.example.alzapp.ui.theme.AlzAppTheme

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun UpperActionBar(
    onNewGame: () -> Unit,
    onPause: () -> Unit,
    difficulty: SudokuDifficulty,
) {
    val difficultyLabel = when(difficulty) {
        SudokuDifficulty.EASY -> "Łatwy"
        SudokuDifficulty.MEDIUM -> "Średni"
        SudokuDifficulty.HARD -> "Trudny"
        SudokuDifficulty.EXTREME -> "Ekspert"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPause,
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_pause),
                contentDescription = "Pauza",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(55.dp)
            )
        }

        Text(
            text = difficultyLabel,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
        )

        ActionButtonGame(
            text = "Nowa",
            icon = ImageVector.vectorResource(R.drawable.icon_add),
            onClick = onNewGame
        )
    }
}

@Preview
@Composable
fun UpperActionBarPreview() {
    AlzAppTheme {
        UpperActionBar({}, {}, SudokuDifficulty.MEDIUM)
    }
}
