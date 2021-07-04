package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.utils.Units
import java.security.InvalidParameterException

class PaceCalculatorHelper : CalculatorHelper() {

    private var swimPaceInSeconds: Int = 0
    private var runPaceInSeconds: Int = 0
    private var runDurationInSeconds: Float = 0f

    private var transitionOneInSeconds: Int = 0
    private var transitionTwoInSeconds: Int = 0
    private var swimDurationInSeconds: Float = 0f
    private var bikeDurationInSeconds: Float = 0f
    private var triathlonRunDurationInSeconds: Float = 0f

    private var triathlonTotalDurationListOfValues: List<String> = emptyList()

    fun getTriathlonTotalDurationListOfValues(): List<String> {
        setTriathlonTotalDurationValues()
        return triathlonTotalDurationListOfValues
    }


    private var _unitInUse: Units = Units.METRIC
        get() = field

    fun setUnit(unit: Units) {
        _unitInUse = unit
    }

    fun convertRunPaceValueToDuration(paceValue: Float, runDistance: Float): List<String> {
        runDurationInSeconds = paceValue * runDistance
        return generateDurationListOfStrings(runDurationInSeconds)
    }

    fun generateRunPaceListOfValuesFromFloatPaceValue(value: Float): List<String> {
        return generatePaceListOfStrings(value.toInt())
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
                    ((SPRINT_BIKE_METRIC / paceValue) * SECONDS_IN_ONE_HOUR)
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
                    ((OLYMPIC_BIKE_METRIC / paceValue) * SECONDS_IN_ONE_HOUR)
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
                    ((HALF_DISTANCE_BIKE_METRIC / paceValue) * SECONDS_IN_ONE_HOUR)
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
                    ((FULL_DISTANCE_BIKE_METRIC / paceValue) * SECONDS_IN_ONE_HOUR)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (FULL_DISTANCE_RUN_METRIC * paceValue)
            }

        }
    }

    private fun setTriathlonTotalDurationValues() {
        triathlonTotalDurationListOfValues =
            generateDurationListOfStrings(getTriDurationInSeconds())
    }


    private fun getTriDurationInSeconds() =
        swimDurationInSeconds + transitionOneInSeconds + bikeDurationInSeconds + transitionTwoInSeconds + triathlonRunDurationInSeconds

}




