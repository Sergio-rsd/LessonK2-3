package ru.gb.kotlinapp.model.room.city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val city: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val favorite: Boolean = false,
    val note: String = "",
    val region: String = ""
)