package dev.psuchanek.endurancepacecalculator.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.security.InvalidParameterException

class PaceCalculatorHelperTest {

    //Subject under test
    private lateinit var paceCalculator: PaceCalculatorHelper

    @Before
    fun init() {
        paceCalculator = PaceCalculatorHelper()
    }

    //TODO: Write tests for all functions

    @Test
    fun `submit swim pace value in float and generate pace string`() {
        //Given
        val swimPaceInFloat = 120.0f
        //When
        val result = paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(swimPaceInFloat, PaceCalculatorHelper.SWIM)

        //Then
        assertThat(result).isEqualTo("2:00")
    }

    @Test
    fun `submit run pace value in float and generate pace string`() {
        //Given
        val runPaceInFloat = 330.0f
        //When
        val result = paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(runPaceInFloat, PaceCalculatorHelper.RUN)

        //Then
        assertThat(result).isEqualTo("5:30")
    }

    @Test
    fun `submit sprint paces and transition values and get total duration string`() {
        //Given
        val swimPace = 120f
        val transitionTime = 120f
        val bikeSpeed = 25f
        val runPace = 300f
        val triathlonDistance = PaceCalculatorHelper.SPRINT_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T2)

        val result = paceCalculator.getTriathlonTotalDurationValues()

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
        val triathlonDistance = PaceCalculatorHelper.HALF_DISTANCE_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T2)

        val result = paceCalculator.getTriathlonTotalDurationValues()

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
        val triathlonDistance = PaceCalculatorHelper.OLYMPIC_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T2)

        val result = paceCalculator.getTriathlonTotalDurationValues()

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
        val triathlonDistance = PaceCalculatorHelper.FULL_DISTANCE_TRI

        //When
        paceCalculator.generateDurationInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateDurationInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T2)

        val result = paceCalculator.getTriathlonTotalDurationValues()

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("12:03:00")

    }

}