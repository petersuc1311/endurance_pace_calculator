package dev.psuchanek.endurancepacecalculator.utils

enum class ActivityType(val distance: Float) {
    RUN_FIVE_KM(5f),
    RUN_TEN_KM(10f),
    RUN_HALF_MARATHON(21.097f),
    RUN_FULL_MARATHON(42.195f),
    SPRINT(25.75f),
    OLYMPIC(51.5f),
    HALF_TRIATHLON(113f),
    FULL_TRIATHLON(226f)
}