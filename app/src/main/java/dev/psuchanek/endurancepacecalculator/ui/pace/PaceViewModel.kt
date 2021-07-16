package dev.psuchanek.endurancepacecalculator.ui.pace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.utils.*
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.DEFAULT_DURATION
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper.Companion.DEFAULT_PACE
import dev.psuchanek.endurancepacecalculator.calculator.PaceCalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.PaceCalculatorHelper.Companion.FULL_DISTANCE_TRI_ID
import dev.psuchanek.endurancepacecalculator.calculator.PaceCalculatorHelper.Companion.HALF_DISTANCE_TRI_ID
import dev.psuchanek.endurancepacecalculator.calculator.PaceCalculatorHelper.Companion.OLYMPIC_TRI_ID
import dev.psuchanek.endurancepacecalculator.calculator.PaceCalculatorHelper.Companion.SPRINT_TRI_ID
import dev.psuchanek.endurancepacecalculator.calculator.ZonesCalculatorHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaceViewModel @Inject constructor() :
    ViewModel() {

    private val paceCalculatorHelper: PaceCalculatorHelper = PaceCalculatorHelper()

    private val _unitsType = MutableStateFlow(Units.METRIC)

    private val _activityType = MutableStateFlow(ActivityType.RUN_FIVE_KM)

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

    fun submitRunPaceValue(value: Float) {
        setRunDurationValue(value)
        setRunPaceValue(value)
    }

    private fun setRunDurationValue(value: Float) {
        _runDurationValues.value = paceCalculatorHelper.getRunDurationListOfStringsFromPaceAndDistanceValue(
            value,
            getRunDistance()
        )
    }

    private fun setRunPaceValue(value: Float) {
        _runPaceValues.value =
            paceCalculatorHelper.getRunPaceListOfValuesFromFloatPaceValue(value)
    }

    fun submitTriathlonStagePace(sliderValue: Float, triStage: TriStage) {
        val triathlonDistance = when (_activityType.value) {
            ActivityType.SPRINT -> SPRINT_TRI_ID
            ActivityType.OLYMPIC -> OLYMPIC_TRI_ID
            ActivityType.HALF_TRIATHLON -> HALF_DISTANCE_TRI_ID
            ActivityType.FULL_TRIATHLON -> FULL_DISTANCE_TRI_ID
            else -> -1
        }
        when (triStage) {
            TriStage.SWIM -> {
                _triSwimPaceValue.value =
                    paceCalculatorHelper.getTriathlonSwimOrTransitionTimePaceString(
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
                    paceCalculatorHelper.getTriathlonSwimOrTransitionTimePaceString(
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
                    paceCalculatorHelper.getTriathlonSwimOrTransitionTimePaceString(
                        sliderValue,
                        PaceCalculatorHelper.T2
                    )
            }

            TriStage.RUN -> {
                _triRunPaceValue.value =
                    paceCalculatorHelper.getTriathlonSwimOrTransitionTimePaceString(
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
        _triDurationValue.value = paceCalculatorHelper.getTriathlonTotalDurationListOfValues()
    }

    private fun getRunDistance(): Float = when (_activityType.value) {
        ActivityType.RUN_FIVE_KM -> CalculatorHelper.RUN_5K
        ActivityType.RUN_TEN_KM -> CalculatorHelper.RUN_10K
        ActivityType.RUN_HALF_MARATHON -> CalculatorHelper.RUN_HALF_MARATHON
        ActivityType.RUN_FULL_MARATHON -> CalculatorHelper.RUN_FULL_MARATHON
        else -> -1f
    }
}