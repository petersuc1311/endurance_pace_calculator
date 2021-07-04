package dev.psuchanek.endurancepacecalculator.models

data class CriticalSwimSpeed(
    val zone: Int,
    val lowerZoneBound: String,
    val upperZoneBound: String,
    val lowerPaceRange: List<String>,
    val upperPaceRange: List<String>
)
