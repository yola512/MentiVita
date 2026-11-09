package com.example.alzapp.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.ui.theme.AlzAppTheme

@Composable
fun CustomBackButton(
    verticalPadding: Dp = 0.dp,
    iconSize: Dp,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(iconSize)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back_button),
                contentDescription = "Back button",
                modifier = Modifier.size(iconSize),
                tint = color
            )
        }
        Text(
            text = "POWRÓT",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}

@Preview
@Composable
private fun CustomBackButtonPreview() {
    AlzAppTheme() {
        CustomBackButton(verticalPadding = 10.dp, iconSize = 60.dp, onClick = {})
    }
}