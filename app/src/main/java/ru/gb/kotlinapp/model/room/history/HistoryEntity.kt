package ru.gb.kotlinapp.model.room.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "history")
@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
//    val historyId: Long = 0,
    val id: Long = 0,
//    val city: String = "",
    @ColumnInfo(name = "city_id")
    val cityId : Long = 0,
//    val cityCreateId: Long = 0,
    val temperature: Int = 0,
    val condition: String = ""
)