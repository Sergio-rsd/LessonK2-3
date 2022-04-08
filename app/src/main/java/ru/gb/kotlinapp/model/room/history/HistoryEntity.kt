package ru.gb.kotlinapp.model.room.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "city_id")
    val cityId : Long = 0,
    val temperature: Int = 0,
    val condition: String = ""
)