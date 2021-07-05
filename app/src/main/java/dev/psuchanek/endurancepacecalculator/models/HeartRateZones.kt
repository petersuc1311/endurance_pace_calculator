package dev.psuchanek.endurancepacecalculator.models

data class HeartRateZones(
    override val zone: Int = 1,
    override val lowerZoneBound: Int = 0,
    override val upperZoneBound: Int = 0,
    val lowerHeartRateRange: Int = 0,
    val upperHeartRateRange: Int = 0
): Zones(zone, lowerZoneBound, upperZoneBound, lowerHeartRateRange, upperHeartRateRange) {
}