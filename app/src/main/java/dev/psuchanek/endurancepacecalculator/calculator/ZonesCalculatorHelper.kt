package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.models.CriticalSwimSpeed

class ZonesCalculatorHelper : CalculatorHelper() {

    private var swimCSSValue: Float = 0f
    private var criticalSwimSpeedPaceValues = emptyList<String>()
    private var criticalSwimSpeedPaceZones = emptyList<CriticalSwimSpeed>()

    private var bikeFTP: Int = 0
    private var runFTP: Int = 0

    fun getCSSPace() = criticalSwimSpeedPaceValues
    fun getCSSZones() = criticalSwimSpeedPaceZones


    fun generateCriticalSwimSpeedPace(
        paceValue400: Float,
        paceValue200: Float
    ) {
        calculateCriticalSwimSpeed(paceValue400, paceValue200)
        criticalSwimSpeedPaceValues = generatePaceListOfStrings(swimCSSValue.toInt())
    }

    private fun calculateCriticalSwimSpeed(paceValue400: Float, paceValue200: Float) {
        swimCSSValue = (paceValue400 - paceValue200) / 2
    }

    fun generateZonesForCriticalSwimSpeed() {
        val swimSpeedZonesList = mutableListOf<CriticalSwimSpeed>()

        for (i in LIST_OF_SWIM_ZONE_BOUNDS.indices) {
            val lowerPaceBoundValue = calculateZoneBound(
                LIST_OF_SWIM_ZONE_BOUNDS[i]
            )

            val upperPaceBoundValue: Int

            when {
                i != (LIST_OF_SWIM_ZONE_BOUNDS.size - 1) -> {
                    upperPaceBoundValue = calculateZoneBound(
                        LIST_OF_SWIM_ZONE_BOUNDS[i + 1]
                    )
                    swimSpeedZonesList.add(
                        i, CriticalSwimSpeed(
                            zone = i,
                            lowerZoneBound = LIST_OF_SWIM_ZONE_BOUNDS[i].toString(),
                            upperZoneBound = LIST_OF_SWIM_ZONE_BOUNDS[i + 1].toString(),
                            lowerPaceRange = generatePaceListOfStrings(lowerPaceBoundValue),
                            upperPaceRange = generatePaceListOfStrings(upperPaceBoundValue)
                        )
                    )

                }
                else -> {
                    swimSpeedZonesList.add(
                        i, CriticalSwimSpeed(
                            zone = i,
                            lowerZoneBound = LIST_OF_SWIM_ZONE_BOUNDS[i].toString(),
                            upperZoneBound = ZONE_SEVEN_UPPER_ZONE_RANGE,
                            lowerPaceRange = generatePaceListOfStrings(lowerPaceBoundValue),
                            upperPaceRange = listOf(ZONE_SEVEN_UPPER_ZONE_BOUND)
                        )
                    )
                }

            }
        }

        criticalSwimSpeedPaceZones = swimSpeedZonesList
    }

    private fun calculateZoneBound(zoneBound: Int): Int {
        return (swimCSSValue / (zoneBound / 100f)).toInt()
    }

    companion object {

        private const val SWIM_ZONE_ONE_LOWER_ZONE_BOUND = 75
        private const val SWIM_ZONE_ONE_UPPER_ZONE_BOUND = 84
        private const val SWIM_ZONE_TWO_UPPER_ZONE_BOUND = 91
        private const val SWIM_ZONE_THREE_UPPER_ZONE_BOUND = 96
        private const val SWIM_ZONE_FOUR_UPPER_ZONE_BOUND = 100
        private const val SWIM_ZONE_FIVE_UPPER_ZONE_BOUND = 102
        private const val SWIM_ZONE_SIX_UPPER_ZONE_BOUND = 106

        private const val ZONE_SEVEN_UPPER_ZONE_BOUND = "<"
        private const val ZONE_SEVEN_UPPER_ZONE_RANGE = "+"

        private val LIST_OF_SWIM_ZONE_BOUNDS =
            listOf(
                SWIM_ZONE_ONE_LOWER_ZONE_BOUND,
                SWIM_ZONE_ONE_UPPER_ZONE_BOUND,
                SWIM_ZONE_TWO_UPPER_ZONE_BOUND,
                SWIM_ZONE_THREE_UPPER_ZONE_BOUND,
                SWIM_ZONE_FOUR_UPPER_ZONE_BOUND,
                SWIM_ZONE_FIVE_UPPER_ZONE_BOUND,
                SWIM_ZONE_SIX_UPPER_ZONE_BOUND
            )

        private const val BIKE_POWER_ZONE_ONE_LOWER_ZONE_BOUND = 50
        private const val BIKE_POWER_ZONE_ONE_UPPER_ZONE_BOUND = 70
        private const val BIKE_POWER_ZONE_TWO_UPPER_ZONE_BOUND = 83
        private const val BIKE_POWER_ZONE_THREE_UPPER_ZONE_BOUND = 91
        private const val BIKE_POWER_ZONE_FOUR_UPPER_ZONE_BOUND = 102
        private const val BIKE_POWER_ZONE_FIVE_UPPER_ZONE_BOUND = 110
        private const val BIKE_POWER_ZONE_SIX_UPPER_ZONE_BOUND = 115

        private val LIST_OF_BIKE_POWER_ZONE_BOUNDS =
            listOf(
                BIKE_POWER_ZONE_ONE_LOWER_ZONE_BOUND,
                BIKE_POWER_ZONE_ONE_UPPER_ZONE_BOUND,
                BIKE_POWER_ZONE_TWO_UPPER_ZONE_BOUND,
                BIKE_POWER_ZONE_THREE_UPPER_ZONE_BOUND,
                BIKE_POWER_ZONE_FOUR_UPPER_ZONE_BOUND,
                BIKE_POWER_ZONE_FIVE_UPPER_ZONE_BOUND,
                BIKE_POWER_ZONE_SIX_UPPER_ZONE_BOUND
            )

        private const val RUN_POWER_ZONE_ONE_LOWER_ZONE_BOUND = 50
        private const val RUN_POWER_ZONE_ONE_UPPER_ZONE_BOUND = 76
        private const val RUN_POWER_ZONE_TWO_UPPER_ZONE_BOUND = 88
        private const val RUN_POWER_ZONE_THREE_UPPER_ZONE_BOUND = 94
        private const val RUN_POWER_ZONE_FOUR_UPPER_ZONE_BOUND = 103
        private const val RUN_POWER_ZONE_FIVE_UPPER_ZONE_BOUND = 120
        private const val RUN_POWER_ZONE_SIX_UPPER_ZONE_BOUND = 125

        private val LIST_OF_RUN_POWER_ZONE_BOUNDS =
            listOf(
                RUN_POWER_ZONE_ONE_LOWER_ZONE_BOUND,
                RUN_POWER_ZONE_ONE_UPPER_ZONE_BOUND,
                RUN_POWER_ZONE_TWO_UPPER_ZONE_BOUND,
                RUN_POWER_ZONE_THREE_UPPER_ZONE_BOUND,
                RUN_POWER_ZONE_FOUR_UPPER_ZONE_BOUND,
                RUN_POWER_ZONE_FIVE_UPPER_ZONE_BOUND,
                RUN_POWER_ZONE_SIX_UPPER_ZONE_BOUND
            )

    }
}