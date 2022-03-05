package ru.gb.kotlinapp.util

import java.time.Duration
import java.time.LocalTime

fun longitudeSunDay(sunrise: String, sunset: String): String {
    val duration = Duration.between(LocalTime.parse(sunrise), LocalTime.parse(sunset))
    val hour = duration.toMinutes() / 60
    val minute = duration.toMinutes() % 60
    return String.format("%02d:%02d", hour, minute)
}
fun plusMinusTemperature(temp: Int): String {
    return if (temp > 0) "+$temp" else temp.toString()
}