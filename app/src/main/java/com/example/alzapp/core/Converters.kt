package com.example.alzapp.core

import androidx.room.TypeConverter
import com.example.alzapp.data.games.hiddenObjects.model.HiddenObject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, dateFormatter) }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    @TypeConverter
    fun fromLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it, timeFormatter) }
    }

    @TypeConverter
    fun localTimeToTimestamp(time: LocalTime?): String? {
        return time?.format(timeFormatter)
    }

    @TypeConverter
    fun fromList(list: List<LocalDateTime>?): String? {
        return list?.map { it.format(formatter) }?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toList(value: String?): List<LocalDateTime>? {
        return value?.let { Json.decodeFromString<List<String>>(it).map { LocalDateTime.parse(it, formatter) } }
    }

    @TypeConverter
    fun fromStringSet(set: Set<String>?): String? {
        return set?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringSet(value: String?): Set<String>? {
        return value?.let { Json.decodeFromString<Set<String>>(it) }
    }

    @TypeConverter
    fun fromLocalDateSet(set: Set<LocalDate>?): String? {
        return set?.map { it.format(dateFormatter) }?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toLocalDateSet(value: String?): Set<LocalDate>? {
        return value?.let { Json.decodeFromString<Set<String>>(it).map { LocalDate.parse(it, dateFormatter) }.toSet() }
    }

    @TypeConverter
    fun fromLocalDateTimeSet(set: Set<LocalDateTime>?): String? {
        return set?.map { it.format(formatter) }?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toLocalDateTimeSet(value: String?): Set<LocalDateTime>? {
        return value?.let { Json.decodeFromString<Set<String>>(it).map { LocalDateTime.parse(it, formatter) }.toSet() }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { Json.decodeFromString<List<String>>(it) }
    }

    // sudoku
    @TypeConverter
    fun fromIntArray2D(value: Array<IntArray>): String = Json.encodeToString(value)

    @TypeConverter
    fun toIntArray2D(value: String): Array<IntArray> = Json.decodeFromString(value)

    @TypeConverter
    fun fromBooleanArray2D(value: Array<Array<Boolean>>): String = Json.encodeToString(value)

    @TypeConverter
    fun toBooleanArray2D(value: String): Array<Array<Boolean>> = Json.decodeFromString(value)

    @TypeConverter
    fun fromNullableIntArray2D(value: Array<Array<Int?>>): String = Json.encodeToString(value)

    @TypeConverter
    fun toNullableIntArray2D(value: String): Array<Array<Int?>> = Json.decodeFromString(value)

    // hiddenObjects
    @TypeConverter
    fun fromHiddenObjectList(value: List<HiddenObject>): String = Json.encodeToString(value)

    @TypeConverter
    fun toHiddenObjectList(value: String): List<HiddenObject> = Json.decodeFromString(value)

    @TypeConverter
    fun fromIntSet(value: Set<Int>): String = Json.encodeToString(value)

    @TypeConverter
    fun toIntSet(value: String): Set<Int> = Json.decodeFromString(value)
}