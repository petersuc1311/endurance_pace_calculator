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

    @Test
    fun `convert pace to duration for five kilometer distance`() {
        //Given
        val minutes = 10
        val seconds = 40
        val activityType = ActivityType.FIVE_KM

        //When
        val result = paceCalculator.convertPaceToDuration(minutes, seconds, activityType)

        //Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0]).isEqualTo("00")
        assertThat(result[1]).isEqualTo("53")
        assertThat(result[2]).isEqualTo("20")
    }

    @Test
    fun `convert pace to duration for ten kilometer distance`() {
        //Given
        val minutes = 10
        val seconds = 40
        val activityType = ActivityType.TEN_KM

        //When
        val result = paceCalculator.convertPaceToDuration(minutes, seconds, activityType)

        //Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0]).isEqualTo("01")
        assertThat(result[1]).isEqualTo("46")
        assertThat(result[2]).isEqualTo("40")
    }

    @Test
    fun `convert pace to duration for half marathon distance`() {
        //Given
        val minutes = 5
        val seconds = 40
        val activityType = ActivityType.HALF_MARATHON

        //When
        val result = paceCalculator.convertPaceToDuration(minutes, seconds, activityType)

        //Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0]).isEqualTo("01")
        assertThat(result[1]).isEqualTo("59")
        assertThat(result[2]).isEqualTo("32")
    }

    @Test
    @Throws(InvalidParameterException::class)
    fun `convert pace to duration for triathlon and throw exception`() {
        //Given
        val minutes = 5
        val seconds = 40
        val activityType = ActivityType.SPRINT

        //When
        val result = try {
            paceCalculator.convertPaceToDuration(minutes, seconds, activityType)
        }catch (e: InvalidParameterException) {
            e.message
        }

        //Then
        assertThat(result).isEqualTo("This function is for run activities only")
    }

    @Test
    fun `convert duration to pace for five kilometer distance`() {
        //Given
        val hours = 0
        val minutes = 20
        val seconds = 40
        val activityType = ActivityType.FIVE_KM

        //When
        val result = paceCalculator.convertDurationToPace( hours, minutes, seconds, activityType)

        //Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0]).isEqualTo("04")
        assertThat(result[1]).isEqualTo("08")
    }

    @Test
    fun `convert duration to pace for full marathon distance`() {
        //Given
        val hours = 4
        val minutes = 30
        val seconds = 47
        val activityType = ActivityType.FULL_MARATHON

        //When
        val result = paceCalculator.convertDurationToPace( hours, minutes, seconds, activityType)

        //Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0]).isEqualTo("06")
        assertThat(result[1]).isEqualTo("25")
    }

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
        paceCalculator.generateTimeInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

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
        paceCalculator.generateTimeInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

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
        paceCalculator.generateTimeInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

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
        paceCalculator.generateTimeInSecondsFromPaceValue(swimPace, PaceCalculatorHelper.SWIM, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(bikeSpeed, PaceCalculatorHelper.BIKE, triathlonDistance)
        paceCalculator.generateTimeInSecondsFromPaceValue(runPace, PaceCalculatorHelper.RUN, triathlonDistance)

        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T1)
        paceCalculator.generateTriathlonSwimOrTransitionTimePaceString(transitionTime, PaceCalculatorHelper.T2)

        val result = paceCalculator.getTriathlonTotalDurationValues()

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("12:03:00")

    }

}