package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.models.HeartRateZones
import dev.psuchanek.endurancepacecalculator.models.PaceZones
import dev.psuchanek.endurancepacecalculator.models.PowerZones
import dev.psuchanek.endurancepacecalculator.models.Zones
import java.security.InvalidParameterException


class ZonesCalculatorHelper : CalculatorHelper() {

    private var swimCSSValue: Float = 0f
    private var criticalSwimSpeedPaceValues = emptyList<String>()
    private var bikeFTP: Int = 0
    private var runFTP: Int = 0
    private var lthr: Int = 0

    private var zones = emptyList<Zones>()

    fun getCSSPace() = criticalSwimSpeedPaceValues

    @Suppress("UNCHECKED_CAST")
    fun getCSSZones() = zones as List<PaceZones>

    @Suppress("UNCHECKED_CAST")
    fun getBikePowerZones() = zones as List<PowerZones>

    @Suppress("UNCHECKED_CAST")
    fun getRunPowerZones() = zones as List<PowerZones>

    @Suppress("UNCHECKED_CAST")
    fun getHeartRateZones() = zones as List<HeartRateZones>


    fun generateCriticalSwimSpeedPaceZones(
        paceValue400: Float,
        paceValue200: Float
    ) {
        calculateCriticalSwimSpeed(paceValue400, paceValue200)
        criticalSwimSpeedPaceValues = generatePaceListOfStrings(swimCSSValue.toInt())
        zones = calculateZones(boundsList = LIST_OF_SWIM_ZONE_BOUNDS, methodType = SWIM_PACE)
    }

    private fun calculateCriticalSwimSpeed(paceValue400: Float, paceValue200: Float) {
        swimCSSValue = (paceValue400 - paceValue200) / 2
    }


    fun generatePowerZones(ftp: Int, activity: Int) {
        when (activity) {
            BIKE_POWER -> {
                bikeFTP = ftp
                zones = calculateZones(
                    boundsList = LIST_OF_BIKE_POWER_ZONE_BOUNDS,
                    methodType = activity
                )
            }
            RUN_POWER -> {
                runFTP = ftp
                zones = calculateZones(
                    boundsList = LIST_OF_RUN_POWER_ZONE_BOUNDS,
                    methodType = activity
                )
            }
        }
    }


    fun generateHeartRateZones(lthr: Int) {
        this.lthr = lthr
        zones = calculateZones(boundsList = LIST_OF_HR_ZONE_BOUNDS, LTHR)
    }


    private fun calculateZones(boundsList: List<Int>, methodType: Int): List<Zones> {

        val zonesList = mutableListOf<Zones>()

        for (i in boundsList.indices) {
            val lowerBoundValue = calculateZoneBound(
                boundsList[i],
                methodType
            )

            val upperBoundValue: Int


            when {
                i != (boundsList.size - 1) -> {
                    upperBoundValue = calculateZoneBound(
                        boundsList[i + 1],
                        methodType
                    )
                    zonesList.add(
                        i, getZoneModel(
                            methodType = methodType,
                            zone = i+1,
                            lowerZoneBound = boundsList[i],
                            upperZoneBound = boundsList[i + 1],
                            lowerRange = lowerBoundValue,
                            upperRange = upperBoundValue
                        )
                    )

                }
                else -> {
                    zonesList.add(
                        i, getZoneModel(
                            methodType = methodType,
                            zone = i+1,
                            lowerZoneBound = boundsList[i],
                            upperZoneBound = ZONE_MAX_UPPER_ZONE,
                            lowerRange = lowerBoundValue,
                            upperRange = ZONE_MAX_UPPER_ZONE
                        )
                    )
                }
            }


        }
        return zonesList
    }


    private fun getZoneModel(
        methodType: Int,
        zone: Int,
        lowerZoneBound: Int,
        upperZoneBound: Int,
        lowerRange: Int,
        upperRange: Int
    ): Zones {
        return when (methodType) {
            SWIM_PACE -> {
                PaceZones(
                    zone = zone,
                    lowerZoneBound = lowerZoneBound,
                    upperZoneBound = upperZoneBound,
                    lowerPaceRange = generatePaceListOfStrings(lowerRange),
                    upperPaceRange = generatePaceListOfStrings(upperRange)
                )
            }
            BIKE_POWER, RUN_POWER -> {
                PowerZones(
                    zone = zone,
                    lowerZoneBound = lowerZoneBound,
                    upperZoneBound = upperZoneBound,
                    lowerPowerRange = lowerRange,
                    upperPowerRange = upperRange
                )
            }
            LTHR -> {
                HeartRateZones(
                    zone = zone,
                    lowerZoneBound = lowerZoneBound,
                    upperZoneBound = upperZoneBound,
                    lowerHeartRateRange = lowerRange,
                    upperHeartRateRange = upperRange
                )
            }

            else -> throw IllegalArgumentException("The method type: $methodType, is not supported.")
        }
    }


    private fun calculateZoneBound(zoneBound: Int, activity: Int): Int {
        return when (activity) {
            SWIM_PACE -> {
                zoneBoundCalculationHelper(swimCSSValue.toInt(), zoneBound, true)
            }
            BIKE_POWER -> {
                zoneBoundCalculationHelper(bikeFTP, zoneBound)
            }
            RUN_POWER -> {
                zoneBoundCalculationHelper(runFTP, zoneBound)
            }
            LTHR -> {
                zoneBoundCalculationHelper(lthr, zoneBound)
            }
            else -> throw InvalidParameterException("The activity: $activity, is not supported.")
        }

    }

    private fun zoneBoundCalculationHelper(
        metric: Int,
        zoneBound: Int,
        isSwim: Boolean = false
    ): Int {
        return when (isSwim) {
            true -> {
                (metric / (zoneBound / 100f)).toInt()
            }
            false -> {
                (metric * (zoneBound / 100f)).toInt()
            }
        }
    }


    companion object {

        private const val SWIM_ZONE_ONE_LOWER_ZONE_BOUND = 75
        private const val SWIM_ZONE_ONE_UPPER_ZONE_BOUND = 84
        private const val SWIM_ZONE_TWO_UPPER_ZONE_BOUND = 91
        private const val SWIM_ZONE_THREE_UPPER_ZONE_BOUND = 96
        private const val SWIM_ZONE_FOUR_UPPER_ZONE_BOUND = 100
        private const val SWIM_ZONE_FIVE_UPPER_ZONE_BOUND = 102
        private const val SWIM_ZONE_SIX_UPPER_ZONE_BOUND = 106

        private const val ZONE_MAX_UPPER_ZONE = 1000

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
        private const val BIKE_POWER_ZONE_ONE_UPPER_ZONE_BOUND = 55
        private const val BIKE_POWER_ZONE_TWO_UPPER_ZONE_BOUND = 75
        private const val BIKE_POWER_ZONE_THREE_UPPER_ZONE_BOUND = 90
        private const val BIKE_POWER_ZONE_FOUR_UPPER_ZONE_BOUND = 105
        private const val BIKE_POWER_ZONE_FIVE_UPPER_ZONE_BOUND = 120
        private const val BIKE_POWER_ZONE_SIX_UPPER_ZONE_BOUND = 125

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
        private const val RUN_POWER_ZONE_ONE_UPPER_ZONE_BOUND = 78
        private const val RUN_POWER_ZONE_TWO_UPPER_ZONE_BOUND = 88
        private const val RUN_POWER_ZONE_THREE_UPPER_ZONE_BOUND = 95
        private const val RUN_POWER_ZONE_FOUR_UPPER_ZONE_BOUND = 102
        private const val RUN_POWER_ZONE_FIVE_UPPER_ZONE_BOUND = 115
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

        private const val HR_ZONE_ONE_LOWER_ZONE_BOUND = 72
        private const val HR_ZONE_ONE_UPPER_ZONE_BOUND = 85
        private const val HR_ZONE_TWO_UPPER_ZONE_BOUND = 90
        private const val HR_ZONE_THREE_UPPER_ZONE_BOUND = 95
        private const val HR_ZONE_FOUR_UPPER_ZONE_BOUND = 100

        private val LIST_OF_HR_ZONE_BOUNDS =
            listOf(
                HR_ZONE_ONE_LOWER_ZONE_BOUND,
                HR_ZONE_ONE_UPPER_ZONE_BOUND,
                HR_ZONE_TWO_UPPER_ZONE_BOUND,
                HR_ZONE_THREE_UPPER_ZONE_BOUND,
                HR_ZONE_FOUR_UPPER_ZONE_BOUND
            )

    }
}