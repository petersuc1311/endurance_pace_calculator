package dev.psuchanek.endurancepacecalculator.ui.pace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.utils.*
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_DURATION
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_PACE
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.MAX_MINUTES_SECONDS_VALUE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaceViewModel @Inject constructor() :
    ViewModel() {

    private val paceCalculatorHelper: PaceCalculatorHelper = PaceCalculatorHelper()

    private val _inputCheckStatus = MutableStateFlow(InputCheckStatus.PASS)
    val inputCheckStatus: StateFlow<InputCheckStatus> = _inputCheckStatus

    private val _paceValues = MutableStateFlow(DEFAULT_PACE)
    val paceValues: StateFlow<List<String>> = _paceValues

    private val _durationValues = MutableStateFlow(DEFAULT_DURATION)
    val durationValues: StateFlow<List<String>> = _durationValues

    private val _unitsType = MutableStateFlow(Units.METRIC)

    private val _activityType = MutableStateFlow(ActivityType.FIVE_KM)

    private val _swimPaceValue = MutableStateFlow("2:00")
    val swimPaceValue: StateFlow<String> = _swimPaceValue

    fun setActivityType(activityType: ActivityType) {
        _activityType.value = activityType
    }

    fun submitPace(
        minutes: String = DEFAULT_TIME_VALUE,
        seconds: String = DEFAULT_TIME_VALUE,
        units: Units = Units.METRIC
    ) {
        when {
            minutes.length > 2 || seconds.length > 2 -> {
                _inputCheckStatus.value = InputCheckStatus.LENGTH_ERROR
            }

            minutes.toInt() > MAX_MINUTES_SECONDS_VALUE || seconds.toInt() > MAX_MINUTES_SECONDS_VALUE -> {
                _inputCheckStatus.value = InputCheckStatus.VALUE_ERROR
            }
        }

        _durationValues.value =
            paceCalculatorHelper.convertPaceToDuration(
                minutes.toInt(),
                seconds.toInt(),
                _activityType.value
            )

    }

    fun submitDuration(
        hours: String = DEFAULT_TIME_VALUE,
        minutes: String = DEFAULT_TIME_VALUE,
        seconds: String = DEFAULT_TIME_VALUE,
        units: Units = Units.METRIC
    ) {
        when {
            hours.length > 2 || minutes.length > 2 || seconds.length > 2 -> {
                _inputCheckStatus.value = InputCheckStatus.LENGTH_ERROR
            }
            minutes.toInt() > MAX_MINUTES_SECONDS_VALUE || seconds.toInt() > MAX_MINUTES_SECONDS_VALUE -> {
                _inputCheckStatus.value = InputCheckStatus.VALUE_ERROR
            }
        }

        _paceValues.value =
            paceCalculatorHelper.convertDurationToPace(
                hours.toInt(),
                minutes.toInt(),
                seconds.toInt(),
                _activityType.value
            )


    }

    fun submitTriathlonStagePace(sliderValue: Float, triStage: TriStage) {
        when(triStage) {
            TriStage.SWIM -> {
                _swimPaceValue.value = sliderValue.toString()
            }

            TriStage.T1 -> {

            }

            TriStage.BIKE -> {

            }

            TriStage.T2 -> {

            }

            TriStage.RUN -> {

            }
        }
    }
}