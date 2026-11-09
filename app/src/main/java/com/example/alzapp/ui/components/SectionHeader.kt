package com.example.alzapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionHeader(verticalPadding: Dp = 0.dp, title: String) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .padding(horizontal = 18.dp, vertical = verticalPadding)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(end = 28.dp, top = 14.dp),
            lineHeight = 30.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .clip(CircleShape),
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
