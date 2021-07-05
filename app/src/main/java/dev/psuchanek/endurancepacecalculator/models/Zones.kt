package dev.psuchanek.endurancepacecalculator.models

open class Zones(
    open val zone: Int,
    open val lowerZoneBound: Int,
    open val upperZoneBound: Int,
    open val lowerRange: Any,
    open val upperRange: Any
)
