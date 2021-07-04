package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.models.HeartRateZones
import dev.psuchanek.endurancepacecalculator.models.PowerZones
import dev.psuchanek.endurancepacecalculator.models.PaceZones
import java.security.InvalidParameterException
import kotlin.math.ceil

class ZonesCalculatorHelper : CalculatorHelper() {

    private var swimCSSValue: Float = 0f
    private var criticalSwimSpeedPaceValues = emptyList<String>()
    private var criticalSwimSpeedPaceZones = emptyList<PaceZones>()

    private var bikeFTP: Int = 0
    private var bikePowerZones = emptyList<PowerZones>()
    private var runFTP: Int = 0
    private var runPowerZones = emptyList<PowerZones>()

    private var lthr: Int = 0
    private var heartRateZones = emptyList<HeartRateZones>()

    fun getCSSPace() = criticalSwimSpeedPaceValues
    fun getCSSZones() = criticalSwimSpeedPaceZones

    fun getBikePowerZones() = bikePowerZones
    fun getRunPowerZones() = runPowerZones

    fun getHeartRateZones() = heartRateZones


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
        val swimSpeedZonesList = mutableListOf<PaceZones>()

        for (i in LIST_OF_SWIM_ZONE_BOUNDS.indices) {
            val lowerPaceBoundValue = calculateZoneBound(
                LIST_OF_SWIM_ZONE_BOUNDS[i],
                SWIM
            )

            val upperPaceBoundValue: Int

            when {
                i != (LIST_OF_SWIM_ZONE_BOUNDS.size - 1) -> {
                    upperPaceBoundValue = calculateZoneBound(
                        LIST_OF_SWIM_ZONE_BOUNDS[i + 1],
                        SWIM
                    )
                    swimSpeedZonesList.add(
                        i, PaceZones(
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
                        i, PaceZones(
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

    fun generatePowerZones(ftp: Int, activity: Int) {
        when (activity) {
            BIKE -> {
                bikeFTP = ftp
                bikePowerZones = calculatePowerZones(activity, LIST_OF_BIKE_POWER_ZONE_BOUNDS)
            }
            RUN -> {
                runFTP = ftp
                runPowerZones = calculatePowerZones(activity, LIST_OF_RUN_POWER_ZONE_BOUNDS)
            }
        }
    }

    private fun calculatePowerZones(activity: Int, zoneBounds: List<Int>): List<PowerZones> {
        val powerZones = mutableListOf<PowerZones>()

        for (i in zoneBounds.indices) {
            val lowerPaceBoundValue = calculateZoneBound(
                zoneBounds[i],
                activity
            )

            val upperPaceBoundValue: Int

            when {
                i != (zoneBounds.size - 1) -> {
                    upperPaceBoundValue = calculateZoneBound(
                        zoneBounds[i + 1],
                        activity
                    )
                    powerZones.add(
                        i, PowerZones(
                            zone = i,
                            lowerZoneBound = zoneBounds[i].toString(),
                            upperZoneBound = zoneBounds[i + 1].toString(),
                            lowerPowerRange = lowerPaceBoundValue,
                            upperPowerRange = upperPaceBoundValue
                        )
                    )

                }
                else -> {
                    powerZones.add(
                        i, PowerZones(
                            zone = i,
                            lowerZoneBound = zoneBounds[i].toString(),
                            upperZoneBound = ZONE_SEVEN_UPPER_ZONE_RANGE,
                            lowerPowerRange = lowerPaceBoundValue,
                            upperPowerRange = ZONE_SEVEN_POWER_UPPER_ZONE_RANGE
                        )
                    )
                }

            }
        }
        return powerZones
    }

    fun generateHeartRateZones(lthr: Int) {
        this.lthr = lthr
        heartRateZones = calculateHeartRateZones()
    }

    private fun calculateHeartRateZones(): List<HeartRateZones> {
        val heartRateZones = mutableListOf<HeartRateZones>()

        for (i in LIST_OF_HR_ZONE_BOUNDS.indices) {
            val lowerPaceBoundValue = calculateZoneBound(
                LIST_OF_HR_ZONE_BOUNDS[i],
                RUN_BIKE_HR
            )

            val upperPaceBoundValue: Int

            when {
                i != (LIST_OF_HR_ZONE_BOUNDS.size - 1) -> {
                    upperPaceBoundValue = calculateZoneBound(
                        LIST_OF_HR_ZONE_BOUNDS[i + 1],
                        RUN_BIKE_HR
                    )
                    heartRateZones.add(
                        i, HeartRateZones(
                            zone = i,
                            lowerZoneBound = LIST_OF_HR_ZONE_BOUNDS[i].toString(),
                            upperZoneBound = LIST_OF_HR_ZONE_BOUNDS[i + 1].toString(),
                            lowerHeartRateRange = lowerPaceBoundValue,
                            upperHeartRateRange = upperPaceBoundValue
                        )
                    )

                }
                else -> {
                    heartRateZones.add(
                        i, HeartRateZones(
                            zone = i,
                            lowerZoneBound = LIST_OF_HR_ZONE_BOUNDS[i].toString(),
                            upperZoneBound = ZONE_SEVEN_UPPER_ZONE_RANGE,
                            lowerHeartRateRange = lowerPaceBoundValue,
                            upperHeartRateRange = ZONE_SEVEN_POWER_UPPER_ZONE_RANGE
                        )
                    )
                }

            }
        }
        return heartRateZones
    }


    private fun calculateZoneBound(zoneBound: Int, activity: Int): Int {
        return when (activity) {
            SWIM -> {
                zoneBoundCalcHelper(swimCSSValue.toInt(), zoneBound, true)
            }
            BIKE -> {
                zoneBoundCalcHelper(bikeFTP, zoneBound)
            }
            RUN -> {
                zoneBoundCalcHelper(runFTP, zoneBound)
            }
            RUN_BIKE_HR -> {
                zoneBoundCalcHelper(lthr, zoneBound)
            }
            else -> throw InvalidParameterException("The activity $activity is not supported.")
        }

    }

    private fun zoneBoundCalcHelper(metric: Int, zoneBound: Int, isSwim: Boolean = false): Int {
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

        private const val ZONE_SEVEN_UPPER_ZONE_BOUND = "<"
        private const val ZONE_SEVEN_UPPER_ZONE_RANGE = "+"
        private const val ZONE_SEVEN_POWER_UPPER_ZONE_RANGE = 999

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

        private const val HR_ZONE_ONE_LOWER_ZONE_BOUND = 72
        private const val HR_ZONE_ONE_UPPER_ZONE_BOUND = 81
        private const val HR_ZONE_TWO_UPPER_ZONE_BOUND = 90
        private const val HR_ZONE_THREE_UPPER_ZONE_BOUND = 100
        private const val HR_ZONE_FOUR_UPPER_ZONE_BOUND = 105

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