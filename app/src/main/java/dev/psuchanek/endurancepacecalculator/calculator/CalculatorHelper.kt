package dev.psuchanek.endurancepacecalculator.calculator

import java.lang.IllegalArgumentException
import java.math.RoundingMode
import java.text.DecimalFormat

sealed class CalculatorHelper {


    fun generateDurationListOfStringsInHoursMinutesSeconds(durationInSeconds: Float): List<String> {
        val hoursOutput = (durationInSeconds / SECONDS_IN_ONE_HOUR).toInt()
        val minutesOutput =
            ((durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR) / SECONDS_IN_ONE_MINUTE).toInt()
        val secondsOutput =
            (durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR - minutesOutput * SECONDS_IN_ONE_MINUTE).toInt()
        return listOf(
            hoursOutput.convertToDoubleDigitStringFormat(),
            minutesOutput.convertToDoubleDigitStringFormat(),
            secondsOutput.convertToDoubleDigitStringFormat()
        )
    }

    fun generateDurationStringsInHoursMinutesSeconds(durationInSeconds: Float): String {
        val hoursOutput = (durationInSeconds / SECONDS_IN_ONE_HOUR).toInt()
        val minutesOutput =
            ((durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR) / SECONDS_IN_ONE_MINUTE).toInt()
        val secondsOutput =
            (durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR - minutesOutput * SECONDS_IN_ONE_MINUTE).toInt()
        return "${hoursOutput.convertToDoubleDigitStringFormat()}:${minutesOutput.convertToDoubleDigitStringFormat()}:${secondsOutput.convertToDoubleDigitStringFormat()}"


    }

    fun generateDurationStringInHoursMinutesSecondsWithMilliseconds(durationInSeconds: Float): String {
        val hoursOutput = (durationInSeconds / SECONDS_IN_ONE_HOUR).toInt()
        val minutesOutput =
            ((durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR) / SECONDS_IN_ONE_MINUTE).toInt()
        val secondsOutput =
            (durationInSeconds - hoursOutput * SECONDS_IN_ONE_HOUR - minutesOutput * SECONDS_IN_ONE_MINUTE)

        return when (durationInSeconds > SECONDS_IN_ONE_HOUR) {
            true -> {
                "${hoursOutput.convertToDoubleDigitStringFormat()}:${minutesOutput.convertToDoubleDigitStringFormat()}:${
                    checkMillisecondsInSecondsAndConvertToString(
                        secondsOutput.roundUpDecimals(2)
                    )
                }"
            }
            false -> "${minutesOutput.convertToDoubleDigitStringFormat()}:${
                checkMillisecondsInSecondsAndConvertToString(
                    secondsOutput.roundUpDecimals(2)
                )
            }"
        }
    }

    private fun checkMillisecondsInSecondsAndConvertToString(seconds: Float): String {
        return if (seconds.rem(1).equals(0.00f)) {
            seconds.toInt().convertToDoubleDigitStringFormat()
        } else {
            if (seconds > 9) "$seconds" else "0${seconds}"
        }
    }

    fun generatePaceListInMinutesSeconds(paceInSeconds: Int): List<String> {
        val minutes = paceInSeconds / SECONDS_IN_ONE_MINUTE
        val seconds = paceInSeconds - minutes * SECONDS_IN_ONE_MINUTE
        return listOf(
            minutes.convertToDoubleDigitStringFormat(),
            seconds.convertToDoubleDigitStringFormat()
        )
    }

    fun generatePaceString(paceInSeconds: Int): String {
        val minutes = paceInSeconds / SECONDS_IN_ONE_MINUTE
        val seconds = paceInSeconds - minutes * SECONDS_IN_ONE_MINUTE
        return "${minutes.convertToDoubleDigitStringFormat()}:${seconds.convertToDoubleDigitStringFormat()}"
    }

    private fun Int.convertToDoubleDigitStringFormat(): String = when (this.toString().length) {
        1 -> {
            "0${this}"
        }
        else -> {
            this.toString()
        }
    }

    protected fun Float.roundUpDecimals(decimalPoint: Int): Float {
        val decimalFormat = when (decimalPoint) {
            2 -> {
                DecimalFormat("#.##")
            }
            3 -> {
                DecimalFormat("#.###")
            }
            else -> throw IllegalArgumentException("This function supports only 2 and 3 decimal rounding")
        }
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(this).toFloat()

    }


    companion object {
        val DEFAULT_PACE = listOf("00", "00")
        val DEFAULT_DURATION = listOf("00", "00", "00")

        const val SWIM_UNIT_COEF = 100
        const val SECONDS_IN_ONE_HOUR = 3600
        const val SECONDS_IN_ONE_MINUTE = 60

        const val RUN_1500 = 1.5f
        const val RUN_3K = 3f
        const val RUN_5K = 5f
        const val RUN_10K = 10f
        const val RUN_HALF_MARATHON = 21.097f
        const val RUN_FULL_MARATHON = 42.195f

        val LIST_OF_RUN_DISTANCES =
            listOf(RUN_1500, RUN_3K, RUN_5K, RUN_10K, RUN_HALF_MARATHON, RUN_FULL_MARATHON)


    }
}