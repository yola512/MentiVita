package com.example.alzapp.utils

import kotlin.math.abs

private val unitForms = mapOf(
    "tabletka" to listOf("tabletka", "tabletki", "tabletek"),
    "kropla" to listOf("kropla", "krople", "kropli")
)
fun getPolishUnit(amountStr: String, unitType: String): String {
    val amount = amountStr.replace(',', '.').toDoubleOrNull() ?: return unitType
    val normalizedUnit = when (unitType.lowercase()) {
        "tabletka", "tabletki", "tabletek" -> "tabletka"
        "kropla", "krople", "kropli" -> "kropla"
        else -> unitType
    }

    val forms = unitForms[normalizedUnit] ?: return unitType

    // fractional dose
    if (amount % 1.0 != 0.0) {
        return if (normalizedUnit == "tabletka") forms[1] else forms [2]
    }

    // integer (whole number) dose
    val n = abs(amount.toInt())
    return when {
        n == 1 -> forms[0] // 1 tabletka, 1 kropla
        n % 10 in 2..4 && n % 100 !in 12..14 -> forms[1] // 2, 3, 4, 23 tabletki
        else -> forms[2] // 5, 12, 30 tabletek
    }
}

//a full dose string e.g. instead of "1 tabletek" -> "1 tabletka"
fun declineDose(dose: String): String {
    val parts = dose.split(" ")
    if (parts.size < 2) return dose
    // parts[0] = amount; parts[1] = unit
    return "${parts[0]} ${getPolishUnit(parts[0], parts[1])}"
}
