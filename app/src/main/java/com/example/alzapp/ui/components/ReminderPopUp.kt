package com.example.alzapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.alzapp.data.notifications.ActiveNotificationManager
import com.example.alzapp.utils.declineDose

@Composable
fun ReminderPopUp(
    title: String,
    reminders: List<ActiveNotificationManager.ReminderData>,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val hasTodo = reminders.any { it.isTodo }
                val hasMed = reminders.any { !it.isTodo }
                
                val displayTitle = if (hasTodo && hasMed) "Nadchodzące wydarzenia" else title

                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.height(12.dp))

                val annotatedString = buildAnnotatedString {
                    val todoReminders = reminders.filter { it.isTodo }
                    val medReminders = reminders.filter { !it.isTodo }

                    if (todoReminders.isNotEmpty()) {
                        val mins = todoReminders.first().reminderMins ?: 0
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                            if (mins > 0) append("Zadania do zrobienia za $mins minut:\n") else append("Zadania na teraz:\n")
                        }
                        todoReminders.forEach { reminder ->
                            append("- ${reminder.name}")
                            if (reminder.dose.isNotEmpty()) append(" (${reminder.dose})")
                            append("\n")
                        }
                        if (medReminders.isNotEmpty()) append("\n")
                    }

                    if (medReminders.isNotEmpty()) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                            if (medReminders.size == 1) append("Lek do przyjęcia:\n") else append("Leki do przyjęcia:\n")
                        }
                        medReminders.forEach { reminder ->
                            append("- ${reminder.name}")
                            if (reminder.dose.isNotEmpty()) append(" (${declineDose(reminder.dose)})")
                            append("\n")
                        }
                    }
                }
                
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.5,
                    softWrap = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 20.dp),
                    textAlign = TextAlign.Start
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismissRequest,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
