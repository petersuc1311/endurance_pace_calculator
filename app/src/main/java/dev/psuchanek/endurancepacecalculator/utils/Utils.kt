package dev.psuchanek.endurancepacecalculator.utils

fun Int.convertToDoubleDigitStringFormat(): String = when(this.toString().length) {
    1 -> {
        "0${this.toString()}"
    }
    else -> { this.toString()}
}