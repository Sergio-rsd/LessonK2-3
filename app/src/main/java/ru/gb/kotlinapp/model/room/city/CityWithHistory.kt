package ru.gb.kotlinapp.model.room.city

data class CityWithHistory(
    val city: String = "",
    val temperature: Int = 0,
    val condition: String = "",
    val favorite: Boolean = false,
    val note : String = ""
)