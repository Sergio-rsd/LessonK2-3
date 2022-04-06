package ru.gb.kotlinapp.model.room.city

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "city")
@Entity
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
//    val cityId: Long = 0,
    val city: String = ""
//    val favorite: Int = 0
)