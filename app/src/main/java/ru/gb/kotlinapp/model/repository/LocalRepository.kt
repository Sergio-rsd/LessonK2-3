package ru.gb.kotlinapp.model.repository

import ru.gb.kotlinapp.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}