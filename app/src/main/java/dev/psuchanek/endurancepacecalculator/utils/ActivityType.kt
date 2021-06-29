package dev.psuchanek.endurancepacecalculator.utils

enum class ActivityType(val distance: Float) {
    FIVE_KM(5f),
    TEN_KM(10f),
    HALF_MARATHON(21.097f),
    FULL_MARATHON(42.195f),
    SPRINT(25.75f),
    OLYMPIC(51.5f),
    HALF_TRIATHLON(113f),
    FULL_TRIATHLON(226f)
}