package dev.psuchanek.endurancepacecalculator.ui.pace


import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.utils.InputCheckStatus
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

}