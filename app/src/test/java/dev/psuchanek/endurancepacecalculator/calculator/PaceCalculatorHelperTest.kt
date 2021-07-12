package dev.psuchanek.endurancepacecalculator.calculator

import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.BIKE
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.FULL_DISTANCE_TRI
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.HALF_DISTANCE_TRI
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.OLYMPIC_TRI
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.RUN
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.RUN_10K
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.RUN_5K
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.RUN_FULL_MARATHON
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.RUN_HALF_MARATHON
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.SPRINT_TRI
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.SWIM
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.T1
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.T2
import org.junit.Before
import org.junit.Test

class PaceCalculatorHelperTest {

    //Subject under test
    private lateinit var paceCalculator: PaceCalculatorHelper

    @Before
    fun init() {
        paceCalculator = PaceCalculatorHelper()
    }

    @Test
    fun `submit run pace value in float and generate pace string`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(runPaceInFloat, RUN)

        //Then
        assertThat(result).isEqualTo("5:30")
    }

    @Test
    fun `submit pace and run distance and generate duration string for five kilometer run`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.convertRunPaceValueToDuration(runPaceInFloat, RUN_5K)

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("00:27:30")
    }

    @Test
    fun `submit pace and run distance and generate duration string for ten kilometer run`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.convertRunPaceValueToDuration(runPaceInFloat, RUN_10K)

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("00:55:00")
    }

    @Test
    fun `submit pace and run distance and generate duration string for half marathon run`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.convertRunPaceValueToDuration(runPaceInFloat, RUN_HALF_MARATHON)

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("01:56:02")
    }

    @Test
    fun `submit pace and run distance and generate duration string for full marathon run`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.convertRunPaceValueToDuration(runPaceInFloat, RUN_FULL_MARATHON)

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("03:52:04")
    }

    @Test
    fun `submit swim pace value in float and generate pace string`() {
        //Given
        val swimPaceInFloat = 120.0f
        //When
        val result = paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(swimPaceInFloat, SWIM)

        //Then
        assertThat(result).isEqualTo("2:00")
    }

    @Test
    fun `submit sprint paces and transition values and get total duration string`() {
        //Given
        val swimPace = 120f
        val transitionTime = 120f
        val bikeSpeed = 25f
        val runPace = 300f
        val triathlonDistance =SPRINT_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T2)

        val result = paceCalculator.getTriathlonTotalDurationListOfValues()

    //Then
    assertThat("${result[0][1]}:${result[1]}:${result[2]}").isEqualTo("1:31:00")

    }

    @Test
    fun `submit half distance paces and transition values and get total duration string`() {
        //Given
        val swimPace = 120f
        val transitionTime = 120f
        val bikeSpeed = 25f
        val runPace = 300f
        val triathlonDistance = HALF_DISTANCE_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T2)

        val result = paceCalculator.getTriathlonTotalDurationListOfValues()

        //Then
        assertThat("${result[0][1]}:${result[1]}:${result[2]}").isEqualTo("6:03:30")

    }

    @Test
    fun `submit olympic paces and transition values and get total duration string`() {
        //Given
        val swimPace = 120f
        val transitionTime = 120f
        val bikeSpeed = 25f
        val runPace = 300f
        val triathlonDistance = OLYMPIC_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T2)

        val result = paceCalculator.getTriathlonTotalDurationListOfValues()

        //Then
        assertThat("${result[0][1]}:${result[1]}:${result[2]}").isEqualTo("3:00:00")

    }

    @Test
    fun `submit full distance paces and transition values and get total duration string`() {
        //Given
        val swimPace = 120f
        val transitionTime = 120f
        val bikeSpeed = 25f
        val runPace = 300f
        val triathlonDistance = FULL_DISTANCE_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, T2)

        val result = paceCalculator.getTriathlonTotalDurationListOfValues()

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("12:03:00")

    }

}