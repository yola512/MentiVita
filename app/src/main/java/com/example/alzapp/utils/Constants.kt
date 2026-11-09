package com.example.alzapp.utils

import java.time.format.DateTimeFormatter
import java.util.Locale

// game IDs
object GameIds {
    const val SUDOKU = 1
    const val POSTMAN = 2
    const val PROVERB = 3
    const val HIDDEN_OBJECTS = 4
}

// locale & date formatters
object AppConfig {
    val PL_LOCALE: Locale = Locale.Builder()
        .setLanguage("pl")
        .setRegion("PL")
        .build()

    val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", PL_LOCALE)
    val DATE_FORMATTER_1: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    const val DATE_PICKER_FORMATTER = "d MMMM, yyyy" // e.g. 8 czerwca, 2026
    val MONTH_YEAR_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", PL_LOCALE) // LLLL -> Marzec instead of Marca
}