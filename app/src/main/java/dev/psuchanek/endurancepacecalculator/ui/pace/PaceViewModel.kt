package dev.psuchanek.endurancepacecalculator.ui.pace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.utils.*
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_DURATION
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_PACE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaceViewModel @Inject constructor() :
    ViewModel() {

    private val paceCalculatorHelper: PaceCalculatorHelper = PaceCalculatorHelper()

    private val _unitsType = MutableStateFlow(Units.METRIC)

    private val _activityType = MutableStateFlow(ActivityType.FIVE_KM)

    private val _inputCheckStatus = MutableStateFlow(InputCheckStatus.PASS)
    val inputCheckStatus: StateFlow<InputCheckStatus> = _inputCheckStatus

    private val _runPaceValues = MutableStateFlow(DEFAULT_PACE)
    val runPaceValues: StateFlow<List<String>> = _runPaceValues

    private val _runDurationValues = MutableStateFlow(DEFAULT_DURATION)
    val runDurationValues: StateFlow<List<String>> = _runDurationValues

    private val _triDurationValue = MutableStateFlow(DEFAULT_DURATION)
    val triDurationValue: StateFlow<List<String>> = _triDurationValue

    private val _triSwimPaceValue = MutableStateFlow("2:00")
    val triSwimPaceValue: StateFlow<String> = _triSwimPaceValue

    private val _transitionOneValue = MutableStateFlow("5:00")
    val transitionOneValue: StateFlow<String> = _transitionOneValue

    private val _triBikePaceValue = MutableStateFlow("25")
    val triBikePaceValue: StateFlow<String> = _triBikePaceValue

    private val _transitionTwoValue = MutableStateFlow("5:00")
    val transitionTwoValue: StateFlow<String> = _transitionTwoValue

    private val _triRunPaceValue = MutableStateFlow("5:30")
    val triRunPaceValue: StateFlow<String> = _triRunPaceValue

    fun setActivityType(activityType: ActivityType) {
        _activityType.value = activityType
    }

    fun submitPaceValue(value: Float) {
        setRunDurationValues(value)
        setRunPaceValues(value)
    }

    private fun setRunDurationValues(value: Float) {
        _runDurationValues.value = paceCalculatorHelper.convertRunPaceValueToDuration(
            value,
            getRunDistance()
        )
    }

    private fun setRunPaceValues(value: Float) {
        _runPaceValues.value =
            paceCalculatorHelper.generateRunPaceListOfValuesFromFloatPaceValue(value)
    }

    fun submitTriathlonStagePace(sliderValue: Float, triStage: TriStage) {
        val triathlonDistance = when (_activityType.value) {
            ActivityType.SPRINT -> PaceCalculatorHelper.SPRINT_TRI
            ActivityType.OLYMPIC -> PaceCalculatorHelper.OLYMPIC_TRI
            ActivityType.HALF_TRIATHLON -> PaceCalculatorHelper.HALF_DISTANCE_TRI
            ActivityType.FULL_TRIATHLON -> PaceCalculatorHelper.FULL_DISTANCE_TRI
            else -> -1
        }
        when (triStage) {
            TriStage.SWIM -> {
                _triSwimPaceValue.value =
                    paceCalculatorHelper.generateTriathlonSwimOrTransitionTimePaceString(
                        sliderValue,
                        PaceCalculatorHelper.SWIM
                    )
                paceCalculatorHelper.generateDurationInSecondsFromPaceValue(
                    sliderValue,
                    PaceCalculatorHelper.SWIM,
                    triathlonDistance
                )
            }

            TriStage.T1 -> {
                _transitionOneValue.value =
                    paceCalculatorHelper.generateTriathlonSwimOrTransitionTimePaceString(
                        sliderValue,
                        PaceCalculatorHelper.T1
                    )
            }

            TriStage.BIKE -> {

                _triBikePaceValue.value = sliderValue.toString()
                paceCalculatorHelper.generateDurationInSecondsFromPaceValue(
                    sliderValue,
                    PaceCalculatorHelper.BIKE,
                    triathlonDistance
                )
            }

            TriStage.T2 -> {
                _transitionTwoValue.value =
                    paceCalculatorHelper.generateTriathlonSwimOrTransitionTimePaceString(
                        sliderValue,
                        PaceCalculatorHelper.T2
                    )
            }

            TriStage.RUN -> {
                _triRunPaceValue.value =
                    paceCalculatorHelper.generateTriathlonSwimOrTransitionTimePaceString(
                        sliderValue,
                        PaceCalculatorHelper.RUN
                    )
                paceCalculatorHelper.generateDurationInSecondsFromPaceValue(
                    sliderValue,
                    PaceCalculatorHelper.RUN,
                    triathlonDistance
                )
            }
        }
        _triDurationValue.value = paceCalculatorHelper.getTriathlonTotalDurationValues()
    }

    private fun getRunDistance(): Float = when (_activityType.value) {
        ActivityType.FIVE_KM -> PaceCalculatorHelper.RUN_5K
        ActivityType.TEN_KM -> PaceCalculatorHelper.RUN_10K
        ActivityType.HALF_MARATHON -> PaceCalculatorHelper.RUN_HALF_MARATHON
        ActivityType.FULL_MARATHON -> PaceCalculatorHelper.RUN_FULL_MARATHON
        else -> -1f
    }
}