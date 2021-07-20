package dev.psuchanek.endurancepacecalculator.calculator


import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.models.Split
import org.junit.Before
import org.junit.Test

class SplitsCalculatorHelperTest {

    //Subject under test
    private lateinit var splitsCalculatorHelper: SplitsCalculatorHelper

    @Before
    fun init() {
        splitsCalculatorHelper = SplitsCalculatorHelper()
    }

    @Test
    fun `submit distance, split frequency and duration and receive splits as a list`() {
        //Given
        val distance = CalculatorHelper.RUN_5K
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val durationInSeconds = 1500f

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        assertThat(result).isNotEqualTo(emptyList<Split>())
    }

    @Test
    fun `submit distance, split frequency and duration then receive correct amount of splits`() {
        //Given
        val distance = CalculatorHelper.RUN_5K
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val durationInSeconds = 1500f
        val expectedResult = 13

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        assertThat(result.size).isEqualTo(expectedResult)
    }

    @Test
    fun `submit distance, split frequency and duration then receive correct amount of splits for different distance`() {
        //Given
        val distance = 4.8f
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val durationInSeconds = 1500f
        val expectedResult = 12

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        assertThat(result.size).isEqualTo(expectedResult)
    }

    @Test
    fun `submit distance, split frequency and duration and expect last entry split to be correct`() {
        //Given
        val distance = CalculatorHelper.RUN_5K
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val durationInSeconds = 1500f
        val expectedResult = Split(
            splitNumber = 13,
            splitTime = "01:00",
            splitTotalDistance = CalculatorHelper.RUN_5K,
            totalTime = "25:00"
        )

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        assertThat(result[result.size - 1]).isEqualTo(expectedResult)
    }


    @Test
    fun `submit distance 10 kilometers, split frequency and duration and expect last entry split to be correct`() {
        //Given
        val distance = CalculatorHelper.RUN_10K
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val durationInSeconds = 3000f
        val expectedListSize = 25
        val expectedResult = Split(
            splitNumber = 25,
            splitTime = "02:00",
            splitTotalDistance = CalculatorHelper.RUN_10K,
            totalTime = "50:00"
        )

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        assertThat(result.size).isEqualTo(expectedListSize)
        assertThat(result[result.size - 1]).isEqualTo(expectedResult)
    }

    @Test
    fun `submit distance half marathon, split frequency and duration and expect last entry split to be correct`() {
        //Given
        val distance = CalculatorHelper.RUN_HALF_MARATHON
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_3KM
        val durationInSeconds = 7200f

        val expectedResult = Split(
            splitNumber = 8,
            splitTime = "00:40.91",
            splitTotalDistance = 21.1f,
            totalTime = "02:00:00"
        )

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        println(result[result.size - 3])
        println(result[result.size - 2])
        assertThat(result[result.size - 1]).isEqualTo(expectedResult)
    }

    @Test
    fun `submit distance marathon, split frequency and duration and expect last entry split to be correct`() {
        //Given
        val distance = CalculatorHelper.RUN_FULL_MARATHON
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_3KM
        val durationInSeconds = 14400f

        val expectedResult = Split(
            splitNumber = 15,
            splitTime = "01:11.56",
            splitTotalDistance = 42.2f,
            totalTime = "04:00:00"
        )

        //When
        splitsCalculatorHelper.generateSplits(distance, frequency, durationInSeconds)
        val result = splitsCalculatorHelper.getSplitsList()

        //Then
        println(result[result.size - 3])
        println(result[result.size - 2])
        assertThat(result[result.size - 1]).isEqualTo(expectedResult)
    }


}