package dev.psuchanek.endurancepacecalculator.models

data class PaceZones(
    override val zone: Int,
    override val lowerZoneBound: String,
    override val upperZoneBound: String,
    val lowerPaceRange: List<String>,
    val upperPaceRange: List<String>
): Zones(zone, lowerZoneBound, upperZoneBound){
}