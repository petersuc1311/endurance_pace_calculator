package dev.psuchanek.endurancepacecalculator.ui.zones

import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.utils.InputCheckStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ZonesViewModelTest {

    //Subject under test
    private lateinit var zonesViewModel: ZonesViewModel

    @Before
    fun init() {
        zonesViewModel = ZonesViewModel()
    }


    @Test
    fun`submit beats per minutes then observe and receive list of size five`() = runBlocking {
        //Given
        val bpm = "170"
        val expectedSize = 5

        //When
        zonesViewModel.submitBPM(bpm)
        val result = zonesViewModel.lthrZones.first()

        //Then
        assertThat(result.size).isEqualTo(expectedSize)
    }

    @Test
    fun`submit beats per minutes with long input then expect error`() = runBlocking {
        //Given
        val bpm = "1700"
        val expectedError = InputCheckStatus.LENGTH_ERROR

        //When
        zonesViewModel.submitBPM(bpm)
        val result = zonesViewModel.inputStatus.first()

        //Then
        assertThat(result).isEqualTo(expectedError)
    }

    @Test
    fun`submit beats per minutes with short input then expect error`() = runBlocking {
        //Given
        val bpm = "1"
        val expectedError = InputCheckStatus.LENGTH_ERROR

        //When
        zonesViewModel.submitBPM(bpm)
        val result = zonesViewModel.inputStatus.first()

        //Then
        assertThat(result).isEqualTo(expectedError)
    }

    @Test
    fun`submit ftp for bike and receive list of size seven`() = runBlocking {
        //Given
        val ftp = "250"
        val expectedSize = 7

        //When
        zonesViewModel.submitFTP(ftp)
        val result = zonesViewModel.powerZones.first()

        //Then
        assertThat(result.size).isEqualTo(expectedSize)
    }

    @Test
    fun`submit swim pace values receive list of size seven`() = runBlocking {
        //Given
        val paceFor400 = 480f
        val paceFor200 = 240f
        val expectedSize = 7

        //When
        zonesViewModel.submitSwimPaceValue(paceFor400, paceFor200)
        val result = zonesViewModel.swimZones.first()

        //Then
        assertThat(result.size).isEqualTo(expectedSize)
    }
}