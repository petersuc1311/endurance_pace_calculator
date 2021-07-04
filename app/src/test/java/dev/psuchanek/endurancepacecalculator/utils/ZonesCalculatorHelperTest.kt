package dev.psuchanek.endurancepacecalculator.utils


import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.calculator.ZonesCalculatorHelper
import org.junit.Before
import org.junit.Test

class ZonesCalculatorHelperTest {

    //Subject under test
    private lateinit var zonesCalculatorHelper: ZonesCalculatorHelper

    @Before
    fun init() {
        zonesCalculatorHelper = ZonesCalculatorHelper()
    }


    @Test
    fun `submit critical swim speed pace values and return pace values in list of strings`() {
        //Given
        val swimPaceValueFor400Meters = 360f
        val swimPaceValueFor200Meters = 180f
        val expectedPace = "01:30"
        //When
        zonesCalculatorHelper.generateCriticalSwimSpeedPace(
            swimPaceValueFor400Meters,
            swimPaceValueFor200Meters
        )
        val result = zonesCalculatorHelper.getCSSPace()

        //Then
        assertThat("${result[0]}:${result[1]}").isEqualTo(expectedPace)
    }

    @Test
    fun `generate swim pace zones from critical swim speed value`() {
        //Given
        val swimPaceValueFor400Meters = 480f
        val swimPaceValueFor200Meters = 240f
        val expectedResult = "02:40->02:22"


        //When
        zonesCalculatorHelper.generateCriticalSwimSpeedPace(
            swimPaceValueFor400Meters,
            swimPaceValueFor200Meters
        )
        zonesCalculatorHelper.generateZonesForCriticalSwimSpeed()
        val result = zonesCalculatorHelper.getCSSZones()

        //Then
        assertThat("${result[0].lowerPaceRange[0]}:${result[0].lowerPaceRange[1]}->${result[0].upperPaceRange[0]}:${result[0].upperPaceRange[1]}").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun `generate swim pace zones from critical swim speed value and display zone two`() {
        //Given
        val swimPaceValueFor400Meters = 480f
        val swimPaceValueFor200Meters = 240f
        val expectedResult = "02:22->02:11"


        //When
        zonesCalculatorHelper.generateCriticalSwimSpeedPace(
            swimPaceValueFor400Meters,
            swimPaceValueFor200Meters
        )
        zonesCalculatorHelper.generateZonesForCriticalSwimSpeed()
        val result = zonesCalculatorHelper.getCSSZones()

        val lowerBound = "${result[1].lowerPaceRange[0]}:${result[1].lowerPaceRange[1]}"
        val upperBound = "${result[1].upperPaceRange[0]}:${result[1].upperPaceRange[1]}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }
}