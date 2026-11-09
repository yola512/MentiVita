package com.example.alzapp.ui.screens.games.sudoku.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.ui.screens.games.components.ActionButtonGame
import com.example.alzapp.ui.theme.AlzAppTheme

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun LowerActionBar(
    onErase: () -> Unit,
    onHint: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButtonGame(
            text = "Usuń",
            icon = ImageVector.vectorResource(R.drawable.icon_delete),
            onClick = onErase
        )
        Spacer(modifier = Modifier.width(12.dp))
        ActionButtonGame(
            text = "Podpowiedź",
            icon = ImageVector.vectorResource(R.drawable.icon_hint),
            onClick = onHint
        )
    }
}

@Preview
@Composable
fun LowerActionBarPreview() {
    AlzAppTheme {
        LowerActionBar({}, {}, Modifier)
    }
}