package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.models.Split
import java.lang.IllegalArgumentException
import java.math.RoundingMode
import java.text.DecimalFormat

class SplitsCalculatorHelper : CalculatorHelper() {

    private var _splitList: List<Split> = emptyList()

    fun getSplitsList() = _splitList


    fun generateSplits(distance: Float, frequency: Float, durationInSeconds: Float) {

        val splitsList = mutableListOf<Split>()
        val multiplier = getMultiplier(distance, frequency)

        val baseSplitTime = getBaseSplitTime(durationInSeconds, multiplier)
        val splitDistanceRem = getSplitDistanceRemainder(distance, frequency, multiplier)
        val splitTimeRem = getSplitTimeRemainder(durationInSeconds, baseSplitTime, multiplier)


        for (i in 0 until multiplier.toInt()) {
            splitsList.add(
                i, Split(
                    splitDistance = frequency,
                    splitTime = generateDurationStringWithMilliseconds(baseSplitTime),
                    splitTotalDistance = frequency + (frequency * i),
                    totalTime = generateDurationStringWithMilliseconds(baseSplitTime + (baseSplitTime * i))
                )
            )
        }
        if (!multiplier.rem(1).equals(0.00f)) {
            splitsList.add(
                splitsList.size, Split(
                    splitDistance = splitDistanceRem,
                    splitTime = generateDurationStringWithMilliseconds(splitTimeRem),
                    splitTotalDistance = distance,
                    totalTime = generateDurationStringWithMilliseconds(durationInSeconds)
                )
            )
        }

        _splitList = splitsList
    }

    private fun getMultiplier(distance: Float, frequency: Float) =
        (distance / frequency).roundUpDecimals(2)

    private fun getBaseSplitTime(durationInSeconds: Float, multiplier: Float) =
        (durationInSeconds / multiplier).roundUpDecimals(2)

    private fun getSplitDistanceRemainder(
        distance: Float,
        frequency: Float,
        multiplier: Float
    ): Float {

        return (distance - (frequency * multiplier.withoutDecimals())).roundUpDecimals(3)
    }

    private fun getSplitTimeRemainder(
        durationInSeconds: Float,
        baseSplitTime: Float,
        multiplier: Float
    ): Float {
        val multiplierWithoutDecimals = multiplier.toInt()
        return (durationInSeconds - (baseSplitTime * multiplier.withoutDecimals())).roundUpDecimals(
            2
        )
    }

    private fun Float.withoutDecimals() = this.toInt()

    companion object {
        const val SPLIT_FREQUENCY_100M = 0.1f
        const val SPLIT_FREQUENCY_400M = 0.4f
        const val SPLIT_FREQUENCY_1KM = 1f
        const val SPLIT_FREQUENCY_3KM = 3f
        const val SPLIT_FREQUENCY_5KM = 5f
        const val SPLIT_FREQUENCY_10KM = 10f
    }
}