package dev.psuchanek.endurancepacecalculator.utils

import java.lang.IllegalArgumentException
import java.security.InvalidParameterException

class PaceCalculatorHelper {

    private var swimPaceInSeconds: Int = 0
    private var transitionOneInSeconds: Int = 0
    private var transitionTwoInSeconds: Int = 0
    private var runPaceInSeconds: Int = 0
    private var runDurationInSeconds: Float = 0f


    private var swimDurationInSeconds: Float = 0f
    private var bikeDurationInSeconds: Float = 0f
    private var triathlonRunDurationInSeconds: Float = 0f


    private var _unitInUse: Units = Units.METRIC
        get() = field

    fun setUnit(unit: Units) {
        _unitInUse = unit
    }

    fun convertRunPaceValueToDuration(paceValue: Float, runDistance: Float): List<String> {
        runDurationInSeconds = paceValue * runDistance
        return generateDurationListOfStrings(runDurationInSeconds)
    }

    private fun ActivityType.getDistance() = when (this) {
        ActivityType.FIVE_KM -> {
            distance
        }
        ActivityType.TEN_KM -> {
            distance
        }
        ActivityType.HALF_MARATHON -> {
            distance
        }
        ActivityType.FULL_MARATHON -> {
            distance
        }
        else -> throw InvalidParameterException("This function is for run activities only")
    }

    private fun generateDurationListOfStrings(secondsByDistance: Float): List<String> {
        val hoursOutput = (secondsByDistance / HOURS_TO_SECONDS_COEF).toInt()
        val minutesOutput =
            ((secondsByDistance - hoursOutput * HOURS_TO_SECONDS_COEF) / MINUTES_TO_SECONDS_COEF).toInt()
        val secondsOutput =
            (secondsByDistance - hoursOutput * HOURS_TO_SECONDS_COEF - minutesOutput * MINUTES_TO_SECONDS_COEF).toInt()
        return listOf(
            hoursOutput.convertToDoubleDigitStringFormat(),
            minutesOutput.convertToDoubleDigitStringFormat(),
            secondsOutput.convertToDoubleDigitStringFormat()
        )
    }
    fun generateRunPaceListOfValuesFromFloatPaceValue(value: Float):List<String> {
        return generatePaceListOfStrings(value.toInt())
    }

    private fun generatePaceListOfStrings(paceInSeconds: Int): List<String> {
        val minutes = paceInSeconds / MINUTES_TO_SECONDS_COEF
        val seconds = paceInSeconds - minutes * MINUTES_TO_SECONDS_COEF
        return listOf(
            minutes.convertToDoubleDigitStringFormat(),
            seconds.convertToDoubleDigitStringFormat()
        )
    }

    fun generateTriathlonSwimOrTransitionTimePaceString(
        paceValue: Float,
        activityType: Int
    ): String {
        setSwimTransitionOrRunPaceValue(paceValue, activityType)
        val paceOrTimeList: List<String> = when (activityType) {
            SWIM -> {
                generatePaceListOfStrings(swimPaceInSeconds)
            }
            T1 -> {
                generatePaceListOfStrings(transitionOneInSeconds)
            }
            T2 -> {
                generatePaceListOfStrings(transitionTwoInSeconds)
            }
            RUN -> {
                generatePaceListOfStrings(runPaceInSeconds)
            }
            else -> throw IllegalArgumentException("Invalid activity type submitted.")
        }

        return "${paceOrTimeList[0][1]}:${paceOrTimeList[1]}"
    }

    private fun setSwimTransitionOrRunPaceValue(paceValue: Float, activityType: Int) {
        when (activityType) {
            SWIM -> {
                swimPaceInSeconds = paceValue.toInt()
            }
            T1 -> {
                transitionOneInSeconds = paceValue.toInt()
            }
            T2 -> {
                transitionTwoInSeconds = paceValue.toInt()
            }
            RUN -> {
                runPaceInSeconds = paceValue.toInt()
            }
            else -> throw InvalidParameterException("Only swim and transition activities allowed in this function")
        }
    }

    fun generateDurationInSecondsFromPaceValue(
        paceValue: Float,
        activityType: Int,
        triathlonDistance: Int
    ) {
        when (triathlonDistance) {
            SPRINT_TRI -> {
                calculateSprintTimeInSeconds(paceValue, activityType)
            }
            OLYMPIC_TRI -> {
                calculateOlympicTimeInSeconds(paceValue, activityType)
            }
            HALF_DISTANCE_TRI -> {
                calculateHalfDistanceTimeInSeconds(paceValue, activityType)
            }
            FULL_DISTANCE_TRI -> {
                calculateFullDistanceTimeInSeconds(paceValue, activityType)
            }
            else -> throw IllegalArgumentException("Invalid triathlon distance submitted.")
        }
    }

    private fun calculateSprintTimeInSeconds(paceValue: Float, activityType: Int) {
        when (activityType) {
            SWIM -> {
                swimDurationInSeconds = ((SPRINT_SWIM_METRIC / SWIM_UNIT_COEF) * paceValue)
            }
            BIKE -> {
                bikeDurationInSeconds =
                    ((SPRINT_BIKE_METRIC / paceValue) * HOURS_TO_SECONDS_COEF)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (SPRINT_RUN_METRIC * paceValue)
            }

        }
    }

    private fun calculateOlympicTimeInSeconds(paceValue: Float, activityType: Int) {
        when (activityType) {
            SWIM -> {
                swimDurationInSeconds = ((OLYMPIC_SWIM_METRIC / SWIM_UNIT_COEF) * paceValue)
            }
            BIKE -> {
                bikeDurationInSeconds =
                    ((OLYMPIC_BIKE_METRIC / paceValue) * HOURS_TO_SECONDS_COEF)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (OLYMPIC_RUN_METRIC * paceValue)
            }

        }
    }

    private fun calculateHalfDistanceTimeInSeconds(paceValue: Float, activityType: Int) {
        when (activityType) {
            SWIM -> {
                swimDurationInSeconds =
                    ((HALF_DISTANCE_SWIM_METRIC / SWIM_UNIT_COEF) * paceValue)
            }
            BIKE -> {
                bikeDurationInSeconds =
                    ((HALF_DISTANCE_BIKE_METRIC / paceValue) * HOURS_TO_SECONDS_COEF)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (HALF_DISTANCE_RUN_METRIC * paceValue)
            }

        }
    }

    private fun calculateFullDistanceTimeInSeconds(paceValue: Float, activityType: Int) {
        when (activityType) {
            SWIM -> {
                swimDurationInSeconds =
                    ((FULL_DISTANCE_SWIM_METRIC / SWIM_UNIT_COEF) * paceValue)
            }
            BIKE -> {
                bikeDurationInSeconds =
                    ((FULL_DISTANCE_BIKE_METRIC / paceValue) * HOURS_TO_SECONDS_COEF)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (FULL_DISTANCE_RUN_METRIC * paceValue)
            }

        }
    }

    fun getTriathlonTotalDurationValues(): List<String> {
        val totalDurationInSeconds =
            swimDurationInSeconds + transitionOneInSeconds + bikeDurationInSeconds + transitionTwoInSeconds + triathlonRunDurationInSeconds
        return generateDurationListOfStrings(totalDurationInSeconds)
    }


    fun getTriDurationInSeconds() =
        swimPaceInSeconds + transitionOneInSeconds + bikeDurationInSeconds + transitionTwoInSeconds + triathlonRunDurationInSeconds




    companion object {
        val DEFAULT_PACE = listOf("00", "00")
        val DEFAULT_DURATION = listOf("00", "00", "00")

        private const val SWIM_UNIT_COEF = 100
        private const val HOURS_TO_SECONDS_COEF = 3600
        private const val MINUTES_TO_SECONDS_COEF = 60

        const val RUN_5K = 5f
        const val RUN_10K = 10f
        const val RUN_HALF_MARATHON = 21.097f
        const val RUN_FULL_MARATHON = 42.195f

        const val SWIM = 1
        const val T1 = 2
        const val BIKE = 3
        const val T2 = 4
        const val RUN = 5

        const val SPRINT_TRI = 1
        const val OLYMPIC_TRI = 2
        const val HALF_DISTANCE_TRI = 3
        const val FULL_DISTANCE_TRI = 4

        private const val SPRINT_SWIM_METRIC = 750
        private const val SPRINT_BIKE_METRIC = 20
        private const val SPRINT_RUN_METRIC = 5

        private const val OLYMPIC_SWIM_METRIC = 1500
        private const val OLYMPIC_BIKE_METRIC = 40
        private const val OLYMPIC_RUN_METRIC = 10

        private const val HALF_DISTANCE_SWIM_METRIC = 1900
        private const val HALF_DISTANCE_BIKE_METRIC = 90f
        private const val HALF_DISTANCE_RUN_METRIC = 21.1f

        private const val FULL_DISTANCE_SWIM_METRIC = 3800
        private const val FULL_DISTANCE_BIKE_METRIC = 180f
        private const val FULL_DISTANCE_RUN_METRIC = 42.2f
    }
}




