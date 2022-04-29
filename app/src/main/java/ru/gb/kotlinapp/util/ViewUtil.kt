package ru.gb.kotlinapp.util

import android.view.View
import com.google.android.material.snackbar.Snackbar


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