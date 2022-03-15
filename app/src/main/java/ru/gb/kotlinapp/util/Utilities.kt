package ru.gb.kotlinapp.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.time.Duration
import java.time.LocalTime

fun longitudeSunDay(sunrise: String?, sunset: String?): String? {
    val duration = Duration.between(LocalTime.parse(sunrise), LocalTime.parse(sunset))
    val hour = duration.toMinutes() / 60
    val minute = duration.toMinutes() % 60
    return String.format("%02d:%02d", hour, minute)
}

fun plusMinusTemperature(temp: Int?): String {
    return if (temp ?: 404 > 0) "+$temp" else temp.toString()
}

fun cloudPercent(cloud: Double?): String {
    return (cloud?.times(100) ?: 404).toInt().toString()
}

// random access
fun getRandomSuccess(): Boolean {
    return (Math.random() * 2).toInt() != 0
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

// ДЗ 4-2
fun View.showSnackBarNoAction(
    text: String,
    actionText: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar
        .make(this, text, length)
        .setAction(actionText, null)
        .show()
}

fun View.showSnackBarStringText(
    text: Int,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar
        .make(this, text, length)
        .setAction(actionText, action)
        .show()
}
