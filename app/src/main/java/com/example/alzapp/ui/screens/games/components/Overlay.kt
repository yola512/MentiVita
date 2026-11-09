package com.example.alzapp.ui.screens.games.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.ui.theme.AlzAppTheme

@Composable
fun Overlay(
    onDismiss: () -> Unit,
    onTutorial: () -> Unit,
    onRules: () -> Unit = {},
    onExit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceBright),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(24.dp)
                .wrapContentSize(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(min = 280.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Gra wstrzymana",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )

                val explanations = listOf(
                    "Naciśnij KONTYNUUJ, aby wrócić do gry.",
                    "Naciśnij ZASADY GRY, aby poznać zasady gry.",
                    "Naciśnij SAMOUCZEK, aby przejść trening.",
                    "Naciśnij WYJŚCIE, aby wyjść z gry.",
                )

                explanations.forEach { expl ->
                    Text(
                        text = "• $expl",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        lineHeight = 30.sp
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "KONTYNUUJ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Button(
                    onClick = onRules,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "ZASADY GRY",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Button(
                    onClick = onTutorial,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "SAMOUCZEK",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Button(
                    onClick = onExit,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "WYJŚCIE",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OverlayPreview() {
    AlzAppTheme {
        Overlay({}, {}, {}, {})
    }
}