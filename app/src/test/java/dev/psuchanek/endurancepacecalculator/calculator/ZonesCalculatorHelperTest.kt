package dev.psuchanek.endurancepacecalculator.calculator


import com.google.common.truth.Truth.assertThat
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
        zonesCalculatorHelper.generateCriticalSwimSpeedPaceZones(
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
        zonesCalculatorHelper.generateCriticalSwimSpeedPaceZones(
            swimPaceValueFor400Meters,
            swimPaceValueFor200Meters
        )
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
        zonesCalculatorHelper.generateCriticalSwimSpeedPaceZones(
            swimPaceValueFor400Meters,
            swimPaceValueFor200Meters
        )
        val result = zonesCalculatorHelper.getCSSZones()

        val lowerBound = "${result[1].lowerPaceRange[0]}:${result[1].lowerPaceRange[1]}"
        val upperBound = "${result[1].upperPaceRange[0]}:${result[1].upperPaceRange[1]}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate bike power zones from functional threshold power and return zone one`() {
        //Given
        val bikeFTP = 250
        val expectedResult = "125->137"
        //When
        zonesCalculatorHelper.generatePowerZones(bikeFTP, CalculatorHelper.BIKE_POWER_ID)
        val result = zonesCalculatorHelper.getBikePowerZones()

        //Then
        val lowerBound = "${result[0].lowerPowerRange}"
        val upperBound = "${result[0].upperPowerRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate bike power zones from functional threshold power and return zone seven`() {
        //Given
        val bikeFTP = 250
        val expectedResult = "312->1000"
        //When
        zonesCalculatorHelper.generatePowerZones(bikeFTP, CalculatorHelper.BIKE_POWER_ID)
        val result = zonesCalculatorHelper.getBikePowerZones()

        //Then
        val lowerBound = "${result[6].lowerPowerRange}"
        val upperBound = "${result[6].upperPowerRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate run power zones from functional threshold power and return zone one`() {
        //Given
        val runFTP = 250
        val expectedResult = "125->195"
        //When
        zonesCalculatorHelper.generatePowerZones(runFTP, CalculatorHelper.RUN_POWER_ID)
        val result = zonesCalculatorHelper.getRunPowerZones()

        //Then
        val lowerBound = "${result[0].lowerPowerRange}"
        val upperBound = "${result[0].upperPowerRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate run power zones from functional threshold power and return zone seven`() {
        //Given
        val runFTP = 250
        val expectedResult = "312->1000"
        //When
        zonesCalculatorHelper.generatePowerZones(runFTP, CalculatorHelper.RUN_POWER_ID)
        val result = zonesCalculatorHelper.getRunPowerZones()

        //Then
        val lowerBound = "${result[6].lowerPowerRange}"
        val upperBound = "${result[6].upperPowerRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate heart rate zones from lactate threshold and return zone one`() {
        //Given
        val lthr = 170
        val expectedResult = "122->144"
        //When
        zonesCalculatorHelper.generateHeartRateZones(lthr)
        val result = zonesCalculatorHelper.getHeartRateZones()

        //Then
        val lowerBound = "${result[0].lowerRange}"
        val upperBound = "${result[0].upperRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }

    @Test
    fun`generate heart rate zones from lactate threshold and return zone five`() {
        //Given
        val lthr = 170
        val expectedResult = "170->1000"
        //When
        zonesCalculatorHelper.generateHeartRateZones(lthr)
        val result = zonesCalculatorHelper.getHeartRateZones()

        //Then
        val lowerBound = "${result[4].lowerRange}"
        val upperBound = "${result[4].upperRange}"

        //Then
        assertThat("$lowerBound->$upperBound").isEqualTo(
            expectedResult
        )
    }
}