package dev.psuchanek.endurancepacecalculator.models

data class HeartRateZones(
    override val zone: Int,
    override val lowerZoneBound: String,
    override val upperZoneBound: String,
    val lowerHeartRateRange: Int,
    val upperHeartRateRange: Int
): Zones(zone, lowerZoneBound, upperZoneBound) {
}