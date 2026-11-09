package com.example.alzapp.ui.screens.games.hiddenobject.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.ui.screens.games.hiddenobject.HiddenObjectTutorialStep

@Composable
fun TutorialInfoBubble(step: HiddenObjectTutorialStep) {
    val message = when (step) {
        HiddenObjectTutorialStep.WELCOME -> "Witaj w grze Oko Szpiega! Twoim zadaniem jest odnalezienie ukrytych przedmiotów."
        HiddenObjectTutorialStep.EXPLAIN_LIST -> "Na dole widzisz listę rzeczy, które musisz znaleźć na obrazku."
        HiddenObjectTutorialStep.EXPLAIN_SEARCH -> "Przyjrzyj się uważnie ilustracji i spróbuj wypatrzeć te przedmioty."
        HiddenObjectTutorialStep.FIND_FIRST -> "Spróbuj znaleźć ŁOPATĘ na obrazku i kliknij ją!"
        HiddenObjectTutorialStep.SUCCESS -> "Świetnie! Przedmiot został zaznaczony czerwonym kółkiem, a na liście pojawiło się zielone obramowanie."
        HiddenObjectTutorialStep.COMPLETED -> "Teraz już wiesz jak grać. Powodzenia w szukaniu reszty przedmiotów!"
        else -> ""
    }

    Box(
        modifier = Modifier
            .heightIn(min = 120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp),
            shadowElevation = 6.dp,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp),
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 30.sp
            )
        }
    }
}