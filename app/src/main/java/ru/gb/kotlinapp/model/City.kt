package ru.gb.kotlinapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val city: String,
    val lat: Double,
    val lon: Double,
    val favorite: Boolean,
    val note: String,
    val region: String
) : Parcelable