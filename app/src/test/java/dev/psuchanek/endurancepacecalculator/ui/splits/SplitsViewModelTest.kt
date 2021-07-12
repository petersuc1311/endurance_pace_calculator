package dev.psuchanek.endurancepacecalculator.ui.splits

import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.SplitsCalculatorHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SplitsViewModelTest {

    //Subject under test
    private lateinit var splitsViewModel: SplitsViewModel

    @Before
    fun init() {
        splitsViewModel = SplitsViewModel()
    }

    @Test
    fun `submit duration from slider then observe and receive correct time values`() = runBlocking {
        //Given
        val duration = 450f
        val expectedResult = listOf("00", "07", "30")

        //When
        splitsViewModel.submitDuration(duration)
        val result = splitsViewModel.durationValuesList.first()

        //Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `submit duration from slider then observe and receive a split list`() = runBlocking {
        //Given
        val duration = 1500f //25 minutes in seconds
        val expectedResult = "25:00"

        //When
        splitsViewModel.submitDuration(duration)
        val result = splitsViewModel.splits.first()

        //Then
        println(result.size)
        println(result)
        assertThat(result[result.size - 1].totalTime).isEqualTo(expectedResult)
    }

    @Test
    fun `submit distance, frequency, duration then observe and receive a split list`() = runBlocking {
        //Given
        val distance = CalculatorHelper.RUN_5K
        val frequency = SplitsCalculatorHelper.SPLIT_FREQUENCY_400M
        val duration = 1500f //25 minutes in seconds
        val expectedResult = 0.2f

        //When
        splitsViewModel.setDistance(distance)
        splitsViewModel.setFrequency(frequency)
        splitsViewModel.submitDuration(duration)
        val result = splitsViewModel.splits.first()

        //Then
        println(result.size)
        println(result)
        assertThat(result[result.size - 1].splitDistance).isEqualTo(expectedResult)
    }

    @Test
    fun`set new distance then observe and collect new slider values`() = runBlocking {
        //Given
        val distance = CalculatorHelper.LIST_OF_RUN_DISTANCES[2]
        val expectedValues = listOf(600f, 2700f, 1650f)

    //When
        println(splitsViewModel.sliderValues.first())
        splitsViewModel.setDistance(distance)
        val result = splitsViewModel.sliderValues.first()
        println(splitsViewModel.sliderValues.first())

        //Then
        assertThat(result).isEqualTo(expectedValues)
    }
}