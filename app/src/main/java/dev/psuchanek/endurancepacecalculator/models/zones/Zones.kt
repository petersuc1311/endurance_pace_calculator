package dev.psuchanek.endurancepacecalculator.models.zones

open class Zones(
    open val zone: Int,
    open val lowerZoneBound: Int,
    open val upperZoneBound: Int,
    open val lowerRange: Any,
    open val upperRange: Any
) {

    val getZone: String
        get() = zone.toString()

    val getZoneBounds: String
        get() = getZoneBoundsAsString()

    val getZoneRange: String
        get() = getZoneRangesAsString()

    private fun getZoneBoundsAsString(): String {
        return if (checkUpperZoneBoundValue()) {
            "$lowerZoneBound ${Char(CHAR_MORE_THAN)}"
        } else {
            "$lowerZoneBound - $upperZoneBound"
        }
    }

    private fun getZoneRangesAsString(): String {
        return when (lowerRange) {
            is Int -> {
                if (checkUpperRangeValue()) {
                    "$lowerRange ${Char(CHAR_PLUS_SIGN)}"
                } else {
                    "$lowerRange - $upperRange"
                }
            }
            else -> {
                if (zone > 6) {
                    "${getRangeValuesAsString(lowerRange as List<*>)} ${Char(CHAR_PLUS_SIGN)}"
                } else {
                    "${getRangeValuesAsString(lowerRange as List<*>)} - ${
                        getRangeValuesAsString(
                            upperRange as List<*>
                        )
                    }"
                }

            }
        }
    }

    private fun checkUpperZoneBoundValue() = upperZoneBound == ZONE_MAX_UPPER_ZONE_VALUE
    private fun checkUpperRangeValue() = (upperRange as Int) >= ZONE_MAX_UPPER_ZONE_VALUE

    private fun getRangeValuesAsString(range: List<*>): String {
        return "${range[0]}:${range[1]}"
    }


    companion object {
        private const val ZONE_MAX_UPPER_ZONE_VALUE = 1000
        private const val CHAR_MORE_THAN = 60
        private const val CHAR_PLUS_SIGN = 43
    }
}
