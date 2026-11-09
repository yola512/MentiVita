package com.example.alzapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.utils.AppConfig.PL_LOCALE
import com.example.alzapp.ui.theme.AlzAppTheme
import com.example.alzapp.utils.AppConfig.MONTH_YEAR_FORMATTER
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarSlider(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    locale: Locale
) {
    // data needed for the slider to show all days of the selected month
    val currentMonth = remember(selectedDate) { YearMonth.from(selectedDate)}
    val daysInMonth =  remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
    }

    // scrolling state + screen measurements used for centering DateCard in the LazyRow()
    val listState = rememberLazyListState()
    // "translator" between dp and px; px is needed for offSet
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val itemWidth = 100.dp // dayCard width + padding


    // result of this is the distance from the left edge to the beginning of the DateCard on the center
    val centerOffsetPx = with(density) {
        (screenWidth / 2 - itemWidth / 2 ).toPx().toInt()
    }

    // every time selectedDate changes (when the user clicks on different date or changes the month)
    // the code will be executed
    LaunchedEffect(selectedDate, daysInMonth) {
        // selectedDate index
        val index = daysInMonth.indexOf(selectedDate)
        if (index != -1) {
            // -centerOffsetPx cause animateScrollToItem normally pushes the item to the left edge (offset 0).
            // list scrolls to the item leaving as many pixels on the left as the centerOffsetPx
            listState.animateScrollToItem(index, -centerOffsetPx)
        }
    }
    // month-year clickable header
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // header, e.g. Marzec 2026 (clickable)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // left-pointing arrow; if clicked -> month -1
            IconButton(
                onClick = { onDateSelected(selectedDate.minusMonths(1)) },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.rounded_arrow_left_alt_24),
                    contentDescription = "Poprzedni miesiąc",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectedDate.format(MONTH_YEAR_FORMATTER)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            // right-pointing arrow; if clicked -> month +1
            IconButton(
                onClick = { onDateSelected(selectedDate.plusMonths(1)) },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.rounded_arrow_forward_24),
                    contentDescription = "Następny miesiąc",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // slider with days
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(count = daysInMonth.size, key = { daysInMonth[it].toString()} ) { day ->
                val date = daysInMonth[day]
                val isSelected = date == selectedDate

                DateCard(
                    date = date,
                    isSelected = isSelected,
                    onDateSelected = { onDateSelected(date) },
                    locale = locale,
                    cardWidth = itemWidth
                )
            }
        }
    }
}

@Composable
fun DateCard(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    locale: Locale,
    cardWidth: Dp
) {
    val dayName = date.format(DateTimeFormatter.ofPattern("EEE", locale)).uppercase()

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(120.dp)
            .clickable { onDateSelected(date) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.dayOfMonth.toString(),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarSliderPreview() {
    AlzAppTheme {
        CalendarSlider(
            selectedDate = LocalDate.now(),
            onDateSelected= {},
            locale= PL_LOCALE)
    }
}
