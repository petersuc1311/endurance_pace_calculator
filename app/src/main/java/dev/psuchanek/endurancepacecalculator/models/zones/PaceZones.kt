package dev.psuchanek.endurancepacecalculator.models.zones

data class PaceZones(
    override var zone: Int = 0,
    override var lowerZoneBound: Int = 0,
    override var upperZoneBound: Int = 0,
    val lowerPaceRange: List<String> = emptyList(),
    val upperPaceRange: List<String> = emptyList()
) : Zones(zone, lowerZoneBound, upperZoneBound, lowerPaceRange, upperPaceRange) {
}