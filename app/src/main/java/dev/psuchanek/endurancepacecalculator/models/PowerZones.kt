package dev.psuchanek.endurancepacecalculator.models

data class PowerZones(
    override val zone: Int = 0,
    override val lowerZoneBound: Int = 0,
    override val upperZoneBound: Int = 0,
    val lowerPowerRange: Int = 0,
    val upperPowerRange: Int = 0
) : Zones(zone, lowerZoneBound, upperZoneBound, lowerPowerRange, upperPowerRange) {
}