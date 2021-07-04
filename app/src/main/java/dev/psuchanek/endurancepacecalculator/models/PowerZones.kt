package dev.psuchanek.endurancepacecalculator.models

data class PowerZones(
    override val zone: Int,
    override val lowerZoneBound: String,
    override val upperZoneBound: String,
    val lowerPowerRange: Int,
    val upperPowerRange: Int
): Zones(zone, lowerZoneBound, upperZoneBound) {
}