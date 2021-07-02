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
    fun `submit pace value from slider and get pace string`() = runBlocking {
        //Given
        val paceValue = 330f

        //When
        paceViewModel.submitPaceValue(paceValue)
        val result = paceViewModel.runPaceValues.first()

        //Then
        assertThat(result.size).isEqualTo(2)
        assertThat("${result[0]}:${result[1]}").isEqualTo("05:30")
    }
}