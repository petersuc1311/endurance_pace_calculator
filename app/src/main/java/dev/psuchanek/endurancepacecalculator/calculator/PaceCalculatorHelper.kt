package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.utils.TriStage
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


//    private var _unitInUse: Units = Units.METRIC
//        get() = field
//
//    fun setUnit(unit: Units) {
//        _unitInUse = unit
//    }

    fun getRunDurationListOfStringsFromPaceAndDistanceValue(
        paceValue: Float,
        runDistance: Float
    ): List<String> {
        calculateRunDurationInSeconds(paceValue, runDistance)
        return generateDurationListOfStringsInHoursMinutesSeconds(runDurationInSeconds)
    }

    private fun calculateRunDurationInSeconds(paceValue: Float, runDistance: Float) {
        runDurationInSeconds = paceValue * runDistance
    }

    fun getRunPaceListOfValuesFromFloatPaceValue(value: Float): List<String> {
        return generatePaceListInMinutesSeconds(value.toInt())
    }


    fun getTriathlonSwimOrTransitionTimePaceString(
        paceValue: Float,
        activityType: Int
    ): String {
        setPaceOfActivityInSeconds(paceValue, activityType)

        return when(activityType) {
            SWIM -> {
                generatePaceString(swimPaceInSeconds)
            }
            T1 -> {
                generatePaceString(transitionOneInSeconds)
            }
            T2 -> {
                generatePaceString(transitionTwoInSeconds)
            }
            RUN -> {
                generatePaceString(runPaceInSeconds)
            }
            else -> throw IllegalArgumentException("Invalid activity type submitted.")
        }
    }

    private fun setPaceOfActivityInSeconds(paceValue: Float, activityType: Int) {
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
            SPRINT_TRI_ID -> {
                calculateTriathlonTimeInSeconds(paceValue, activityType, sprintMetrics)
            }
            OLYMPIC_TRI_ID -> {
                calculateTriathlonTimeInSeconds(paceValue, activityType, olympicMetrics)
            }
            HALF_DISTANCE_TRI_ID -> {
                calculateTriathlonTimeInSeconds(paceValue, activityType, halfDistanceMetrics)
            }
            FULL_DISTANCE_TRI_ID -> {
                calculateTriathlonTimeInSeconds(paceValue, activityType, fullDistanceMetrics)
            }
            else -> throw IllegalArgumentException("Invalid triathlon distance submitted.")
        }
    }

    private fun calculateTriathlonTimeInSeconds(
        paceValue: Float,
        activityType: Int,
        metricsList: List<Float>
    ) {
        when (activityType) {
            SWIM -> {
                swimDurationInSeconds = ((metricsList[0] / SWIM_UNIT_COEF) * paceValue)
            }
            BIKE -> {
                bikeDurationInSeconds = ((metricsList[1] / paceValue) * SECONDS_IN_ONE_HOUR)
            }
            RUN -> {
                triathlonRunDurationInSeconds = (metricsList[2] * paceValue)
            }

        }
    }

    private fun setTriathlonTotalDurationValues() {
        triathlonTotalDurationListOfValues =
            generateDurationListOfStringsInHoursMinutesSeconds(getTriDurationInSeconds())
    }


    private fun getTriDurationInSeconds() =
        swimDurationInSeconds + transitionOneInSeconds + bikeDurationInSeconds + transitionTwoInSeconds + triathlonRunDurationInSeconds


    companion object {
        const val SWIM = 111
        const val T1 = 222
        const val BIKE = 333
        const val T2 = 444
        const val RUN = 555

        const val SPRINT_TRI_ID = 11
        const val OLYMPIC_TRI_ID = 22
        const val HALF_DISTANCE_TRI_ID = 33
        const val FULL_DISTANCE_TRI_ID = 44


        private const val SPRINT_SWIM_METRIC = 750f
        private const val SPRINT_BIKE_METRIC = 20f
        private const val SPRINT_RUN_METRIC = 5f

        private val sprintMetrics =
            listOf(SPRINT_SWIM_METRIC, SPRINT_BIKE_METRIC, SPRINT_RUN_METRIC)

        private const val OLYMPIC_SWIM_METRIC = 1500f
        private const val OLYMPIC_BIKE_METRIC = 40f
        private const val OLYMPIC_RUN_METRIC = 10f

        private val olympicMetrics =
            listOf(OLYMPIC_SWIM_METRIC, OLYMPIC_BIKE_METRIC, OLYMPIC_RUN_METRIC)

        private const val HALF_DISTANCE_SWIM_METRIC = 1900f
        private const val HALF_DISTANCE_BIKE_METRIC = 90f
        private const val HALF_DISTANCE_RUN_METRIC = 21.1f

        private val halfDistanceMetrics =
            listOf(HALF_DISTANCE_SWIM_METRIC, HALF_DISTANCE_BIKE_METRIC, HALF_DISTANCE_RUN_METRIC)

        private const val FULL_DISTANCE_SWIM_METRIC = 3800f
        private const val FULL_DISTANCE_BIKE_METRIC = 180f
        private const val FULL_DISTANCE_RUN_METRIC = 42.2f

        private val fullDistanceMetrics =
            listOf(FULL_DISTANCE_SWIM_METRIC, FULL_DISTANCE_BIKE_METRIC, FULL_DISTANCE_RUN_METRIC)
    }
}




