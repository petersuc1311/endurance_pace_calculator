package dev.psuchanek.endurancepacecalculator.ui.pace


import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.utils.InputCheckStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class PaceViewModelTest {

    //Subject under test
    private lateinit var paceViewModel: PaceViewModel

    @Before
    fun init() {
        paceViewModel = PaceViewModel()
    }

    @Test
    fun `submit pace with long numeric input and return error`() {
        //Given
        val minutes = "300"
        val seconds = "80"

        //When
        paceViewModel.submitPace(minutes, seconds)
        val result = paceViewModel.inputCheckStatus.value

        //Then
        assertThat(result).isEqualTo(InputCheckStatus.LENGTH_ERROR)

    }

    @Test
    fun `submit pace with wrong numeric input and return error`() {
        //Given
        val minutes = "1"
        val seconds = "80"

        //When
        paceViewModel.submitPace(minutes, seconds)
        val result = paceViewModel.inputCheckStatus.value

        //Then
        assertThat(result).isEqualTo(InputCheckStatus.VALUE_ERROR)

    }

    @Test
    fun `submit pace and get list of duration values of size three`() = runBlocking {
        //Given
        val minutes = "1"
        val seconds = "50"

        //When
        paceViewModel.submitPace(minutes, seconds)
        val result = paceViewModel.durationValues.first()

        //Then
        assertThat(result.size).isEqualTo(3)

    }

    @Test
    fun `submit duration with long numeric input and return error`() {
        //Given
        val hours = "300"
        val minutes = "300"
        val seconds = "80"

        //When
        paceViewModel.submitDuration(hours, minutes, seconds)
        val result = paceViewModel.inputCheckStatus.value

        //Then
        assertThat(result).isEqualTo(InputCheckStatus.LENGTH_ERROR)

    }

    @Test
    fun `submit duration with wrong numeric input and return error`() {
        //Given
        val hours = "48"
        val minutes = "78"
        val seconds = "80"

        //When
        paceViewModel.submitDuration(hours, minutes, seconds)
        val result = paceViewModel.inputCheckStatus.value

        //Then
        assertThat(result).isEqualTo(InputCheckStatus.VALUE_ERROR)

    }

    @Test
    fun `submit duration and get list of pace values of size two`() = runBlocking {
        //Given
        val hours = "1"
        val minutes = "15"
        val seconds = "50"

        //When
        paceViewModel.submitDuration(hours, minutes, seconds)
        val result = paceViewModel.paceValues.first()

        //Then
        assertThat(result.size).isEqualTo(2)

    }

}