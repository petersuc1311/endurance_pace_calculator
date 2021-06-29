package dev.psuchanek.endurancepacecalculator.ui.pace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.utils.InputCheckStatus
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.ACTIVITY_5K
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_DURATION
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.DEFAULT_PACE
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.MAX_MINUTES_SECONDS_VALUE
import dev.psuchanek.endurancepacecalculator.utils.PaceCalculatorHelper.Companion.UNITS_METRIC
import dev.psuchanek.endurancepacecalculator.utils.Units
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaceViewModel @Inject constructor() : ViewModel() {

    private val _inputCheckStatus = MutableStateFlow(InputCheckStatus.PASS)
    val inputCheckStatus: StateFlow<InputCheckStatus> = _inputCheckStatus

    private val _paceValues = MutableStateFlow(DEFAULT_PACE)
    val paceValues: StateFlow<List<String>> = _paceValues

    private val _durationValues = MutableStateFlow(DEFAULT_DURATION)
    val durationValues: StateFlow<List<String>> = _paceValues

    private val _unitsType = MutableStateFlow(UNITS_METRIC)

    private val _activityType = MutableStateFlow(ACTIVITY_5K)

    fun submitPace(minutes: String, seconds: String, units: Units = Units.METRIC) {
        when {
            minutes.length > 2 || seconds.length > 2 -> {
                _inputCheckStatus.value = InputCheckStatus.LENGTH_ERROR
            }
            minutes.toInt() > MAX_MINUTES_SECONDS_VALUE || seconds.toInt() > MAX_MINUTES_SECONDS_VALUE -> {
                _inputCheckStatus.value = InputCheckStatus.VALUE_ERROR
            }
        }
    }

    fun submitDuration(hours: String, minutes: String, seconds: String, units: Units = Units.METRIC) {

    }
}