package dev.psuchanek.endurancepacecalculator.utils

import java.security.InvalidParameterException

class PaceCalculatorHelper {


    private var _unitInUse: Units = Units.METRIC
        get() = field

    fun setUnit(unit: Units) {
        _unitInUse = unit
    }

    fun convertPaceToDuration(
        minutes: Int = 0,
        seconds: Int = 0,
        activityType: ActivityType
    ): List<String> {
        val distance = activityType.getDistance()
        val minutesInSeconds = minutes * MINUTES_TO_SECONDS_COEF
        val secondsByDistance = (minutesInSeconds + seconds) * distance
        return generateDurationListOfStrings(secondsByDistance)
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


    fun convertDurationToPace(
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0,
        activityType: ActivityType
    ): List<String> {
        val distance = activityType.getDistance()
        val durationInSeconds = getDurationInSeconds(hours, minutes, seconds)
        val paceInSeconds = (durationInSeconds / distance).toInt()

        return generatePaceListOfStrings(paceInSeconds)
    }

    private fun generatePaceListOfStrings(paceInSeconds: Int): List<String> {
        val minutes = paceInSeconds / MINUTES_TO_SECONDS_COEF
        val seconds = paceInSeconds - minutes * MINUTES_TO_SECONDS_COEF
        return listOf(minutes.convertToDoubleDigitStringFormat(), seconds.convertToDoubleDigitStringFormat())
    }

    private fun getDurationInSeconds(hours: Int, minutes: Int, seconds: Int): Int{
        return hours * HOURS_TO_SECONDS_COEF + minutes * MINUTES_TO_SECONDS_COEF + seconds
    }

    companion object {
        val DEFAULT_PACE = listOf("00", "00")
        val DEFAULT_DURATION = listOf("00", "00", "00")
        const val MAX_MINUTES_SECONDS_VALUE = 59
    }
}




