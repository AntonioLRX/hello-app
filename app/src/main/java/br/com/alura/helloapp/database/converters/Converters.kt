package br.com.alura.helloapp.database.converters

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun dateToLong(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}