package dev.psuchanek.endurancepacecalculator.ui.splits

import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.SplitsCalculatorHelper
import dev.psuchanek.endurancepacecalculator.models.Split
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
        val result = splitsViewModel.duration.first()

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
}