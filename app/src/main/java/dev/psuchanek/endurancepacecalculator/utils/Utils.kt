package dev.psuchanek.endurancepacecalculator.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun handleSoftKeyboard(context: Context, view: View) {
    val imm: InputMethodManager =
        ContextCompat.getSystemService(
            context,
            InputMethodManager::class.java
        ) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

