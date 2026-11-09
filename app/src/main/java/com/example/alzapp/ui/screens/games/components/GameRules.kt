package com.example.alzapp.ui.screens.games.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.alzapp.ui.theme.AlzAppTheme

@Composable
fun GameRules(
    gameName: String,
    rules: List<String>,
    tips: List<String>,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Jak grać w $gameName?",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 40.sp
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Zasady",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                rules.forEach { rule ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = rule,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 1.5.em
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Text(
                    text = "Wskazówki",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                tips.forEachIndexed { index, tip ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "${index + 1}.",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .width(30.dp)
                                .alignBy(FirstBaseline)
                        )
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 1.5.em,
                            modifier = Modifier
                                .weight(1f)
                                .alignBy(FirstBaseline)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    "Rozumiem",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GameRulesPreview() {
    AlzAppTheme {
        GameRules(
            gameName = "Sudoku",
            rules = listOf("• Każdy wiersz musi zawierać cyfry od 1 do 9 bez powtórzeń.",
                "• Każda kolumna musi zawierać cyfry od 1 do 9 bez powtórzeń.",
                "• Każdy z dziewięciu kwadratów 3x3 musi zawierać cyfry od 1 do 9 bez powtórzeń."
            ),
            tips = listOf(
                "Wybierz pole na planszy, aby je zaznaczyć.",
                "Wybierz cyfrę z dolnego panelu, aby wpisać ją w pole.",
                "Użyj przycisku 'Usuń', aby wyczyścić błędnie wpisaną cyfrę.",
                "Jeśli utkniesz, użyj przycisku 'Podpowiedź'."
            ),
            onDismissRequest = {}
        )
    }
}
