package com.example.alzapp.ui.screens.games.cryptogram

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

@Composable
fun CryptogramTutorialBubble(step: CryptogramTutorialStep) {
    val message = when (step) {
        CryptogramTutorialStep.WELCOME -> "Witaj w samouczku! Pokażę Ci, jak odgadnąć ukryte poniżej hasło. Kliknij 'Idź dalej', aby rozpocząć"
        CryptogramTutorialStep.EXPLAIN_TILES -> "Pusty kafelek z numerem to jedna litera. Te same numery to te same litery."
        CryptogramTutorialStep.EXPLAIN_KEYBOARD -> "Na dole masz klawiaturę z literami. Niektóre z nich mogą być zmyłką!"
        CryptogramTutorialStep.GUESS_LETTER -> "Aby wstawić literę kliknij pole z numerem 1 (podświetlone na pomarańczowo) i wybierz literę 'S' na klawiaturze."
        CryptogramTutorialStep.EXPLAIN_KEYBOARD_DISABLED -> "Litery 'S' wskoczyły na swoje miejsca. Szary kolor klawisza na klawiaturze oznacza, że ta litera nie jest już nam potrzebna."
        CryptogramTutorialStep.EXPLAIN_HINT -> "Jeśli utkniesz, wybierz pole i kliknij 'Podpowiedź', aby je odkryć."
        CryptogramTutorialStep.SUCCESS -> "Super! Teraz spróbuj samodzielnie uzupełnić resztę hasła. Jeśli się pomylisz - pole podświetli się na czerwono i poczujesz wibracje. Jeśli zgadniesz - pole podświetli się na zielono."
        CryptogramTutorialStep.COMPLETED -> "Teraz już wiesz wszystko. Czas na prawdziwe przysłowie. Powodzenia!"
        else -> ""
    }

    Box(
        modifier = Modifier
            .heightIn(min = 120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
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