package dev.psuchanek.endurancepacecalculator.calculator

import dev.psuchanek.endurancepacecalculator.models.Split

class SplitsCalculatorHelper : CalculatorHelper() {

    private var _splitList: List<Split> = emptyList()

    private val _durationsFastestSlowestBoundaries: HashMap<Float, List<Float>> = hashMapOf(
        Pair(RUN_1500, listOf(DURATION_FASTEST_1500, DURATION_SLOWEST_1500)),
        Pair(RUN_3K, listOf(DURATION_FASTEST_3K, DURATION_SLOWEST_3K)),
        Pair(RUN_5K, listOf(DURATION_FASTEST_5K, DURATION_SLOWEST_5K)),
        Pair(RUN_10K, listOf(DURATION_FASTEST_10K, DURATION_SLOWEST_10K)),
        Pair(
            RUN_HALF_MARATHON,
            listOf(DURATION_FASTEST_HALF_MARATHON, DURATION_SLOWEST_HALF_MARATHON)
        ),
        Pair(
            RUN_FULL_MARATHON,
            listOf(DURATION_FASTEST_FULL_MARATHON, DURATION_SLOWEST_FULL_MARATHON)
        )
    )

    fun getSplitsList() = _splitList

    fun getDurationMapValues(distance: Float) = _durationsFastestSlowestBoundaries[distance]


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
                    splitTotalDistance = (frequency + (frequency * i)).roundUpDecimals(2),
                    totalTime = generateDurationStringWithMilliseconds(baseSplitTime + (baseSplitTime * i))
                )
            )
        }
        if (!multiplier.rem(1).equals(0.00f)) {
            splitsList.add(
                splitsList.size, Split(
                    splitDistance = splitDistanceRem,
                    splitTime = generateDurationStringWithMilliseconds(splitTimeRem),
                    splitTotalDistance = distance.roundUpDecimals(2),
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

        val LIST_OF_SPLIT_FREQUENCIES = listOf(
            SPLIT_FREQUENCY_100M,
            SPLIT_FREQUENCY_400M,
            SPLIT_FREQUENCY_1KM,
            SPLIT_FREQUENCY_3KM,
            SPLIT_FREQUENCY_5KM,
            SPLIT_FREQUENCY_10KM
        )

        private const val DURATION_SLOWEST_1500 = 810f
        private const val DURATION_FASTEST_1500 = 180f

        private const val DURATION_SLOWEST_3K = 1620f
        private const val DURATION_FASTEST_3K = 360f

        private const val DURATION_SLOWEST_5K = 2700f
        private const val DURATION_FASTEST_5K = 600f

        private const val DURATION_SLOWEST_10K = 5400f
        private const val DURATION_FASTEST_10K = 1200f

        private const val DURATION_SLOWEST_HALF_MARATHON = 11400f
        private const val DURATION_FASTEST_HALF_MARATHON = 2530f

        private const val DURATION_SLOWEST_FULL_MARATHON = 22790f
        private const val DURATION_FASTEST_FULL_MARATHON = 5060f
    }
}