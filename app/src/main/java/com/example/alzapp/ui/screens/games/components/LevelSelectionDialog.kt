package com.example.alzapp.ui.screens.games.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.data.games.GameDifficulty

@Composable
fun LevelSelectionDialog(
    onDismissRequest: () -> Unit,
    onLevelSelected: (GameDifficulty) -> Unit,
    modifier: Modifier = Modifier,
    labelProvider: (GameDifficulty) -> String = { level ->
        when(level) {
            GameDifficulty.EASY -> "Łatwy"
            GameDifficulty.MEDIUM -> "Średni"
            GameDifficulty.HARD -> "Trudny"
            GameDifficulty.EXTREME -> "Ekspert"
        }
    }
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Wybierz poziom trudności",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 40.sp
            )
        },
        text = {
            Column {
                GameDifficulty.entries.forEach { level ->
                    Button(
                        onClick = {
                            onLevelSelected(level)
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(labelProvider(level), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = { },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Anuluj", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
