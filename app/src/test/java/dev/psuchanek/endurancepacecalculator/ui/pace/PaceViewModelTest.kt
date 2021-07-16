package dev.psuchanek.endurancepacecalculator.ui.pace


import com.google.common.truth.Truth.assertThat
import dev.psuchanek.endurancepacecalculator.utils.ActivityType
import dev.psuchanek.endurancepacecalculator.utils.TriStage
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
    fun `submit pace value from slider and observe pace string`() = runBlocking {
        //Given
        val paceValue = 330f

        //When
        paceViewModel.submitRunPaceValue(paceValue)
        val result = paceViewModel.runPaceValues.first()

        //Then
        assertThat(result.size).isEqualTo(2)
        assertThat("${result[0]}:${result[1]}").isEqualTo("05:30")
    }

    @Test
    fun `set activity to run 5 kilometers submit pace value from slider and observe duration string`() = runBlocking {
        //Given
        val paceValue = 330f

        //When
        paceViewModel.setActivityType(ActivityType.RUN_FIVE_KM)
        paceViewModel.submitRunPaceValue(paceValue)
        val result = paceViewModel.runDurationValues.first()

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("00:27:30")
    }

    @Test
    fun `set activity to sprint triathlon submit pace values for each activity from slider and observe duration string`() = runBlocking {
        //Given
        val swimPaceValue = 120f
        val transitionDurationValue = 120f
        val bikeSpeedValue = 25f
        val runPaceValue = 330f

        //When
        paceViewModel.setActivityType(ActivityType.SPRINT)
        paceViewModel.submitTriathlonStagePace(swimPaceValue, TriStage.SWIM)
        paceViewModel.submitTriathlonStagePace(transitionDurationValue, TriStage.T1)
        paceViewModel.submitTriathlonStagePace(bikeSpeedValue, TriStage.BIKE)
        paceViewModel.submitTriathlonStagePace(transitionDurationValue, TriStage.T2)
        paceViewModel.submitTriathlonStagePace(runPaceValue, TriStage.RUN)
        val result = paceViewModel.triDurationValue.first()

        //Then
        assertThat("${result[0]}:${result[1]}:${result[2]}").isEqualTo("01:34:30")
    }


}