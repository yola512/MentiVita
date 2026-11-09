package com.example.alzapp.ui.screens.games.cryptogram.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AlphabetKeyboard(
    keyboardLetters: List<Char>,
    completedLetters: Set<Char>,
    onKeyClick: (Char) -> Unit,
    modifier: Modifier = Modifier,
    highlightedLetter: Char? = null,
    pulseAlpha: Float = 1f
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 48.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 350.dp)
        ) {
            items(keyboardLetters) { letter ->
                val isCompleted = completedLetters.contains(letter)
                val isHighlighted = letter == highlightedLetter
                
                Button(
                    enabled = !isCompleted,
                    onClick = { onKeyClick(letter) },
                    modifier = Modifier
                        .sizeIn(48.dp)
                        .then(
                            if (isHighlighted) {
                                Modifier.border(
                                    width = (4 * pulseAlpha).dp + 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                    shape = RoundedCornerShape(4.dp)
                                )
                            } else Modifier
                        ),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = letter.toString(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
