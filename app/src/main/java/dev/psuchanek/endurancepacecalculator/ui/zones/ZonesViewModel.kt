package dev.psuchanek.endurancepacecalculator.ui.zones

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.ZonesCalculatorHelper
import dev.psuchanek.endurancepacecalculator.models.HeartRateZones
import dev.psuchanek.endurancepacecalculator.models.PaceZones
import dev.psuchanek.endurancepacecalculator.models.PowerZones
import dev.psuchanek.endurancepacecalculator.utils.InputCheckStatus
import dev.psuchanek.endurancepacecalculator.utils.ZoneActivity
import dev.psuchanek.endurancepacecalculator.utils.ZoneMethodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ZonesViewModel @Inject constructor() : ViewModel() {

    private val zonesCalculatorHelper = ZonesCalculatorHelper()

    private val _zoneMethodType = MutableStateFlow(ZoneMethodType.LTHR)

    private val _zoneActivity = MutableStateFlow(ZoneActivity.BIKE)

    private val _inputStatus = MutableStateFlow(InputCheckStatus.PASS)
    val inputStatus: StateFlow<InputCheckStatus> = _inputStatus

    private val _lthrZones = MutableStateFlow<List<HeartRateZones>>(emptyList())
    val lthrZones: StateFlow<List<HeartRateZones>> = _lthrZones

    private val _powerZones = MutableStateFlow<List<PowerZones>>(emptyList())
    val powerZones: StateFlow<List<PowerZones>> = _powerZones

    private val _swimZones = MutableStateFlow<List<PaceZones>>(emptyList())
    val swimZones: StateFlow<List<PaceZones>> = _swimZones

    fun setZoneMethodType(zoneMethodType: ZoneMethodType) {
        _zoneMethodType.value = zoneMethodType
    }

    fun setZoneActivity(zoneActivity: ZoneActivity) {
        _zoneActivity.value = zoneActivity
    }

    fun submitBPM(bpm: String) {
        when {
            bpm.length > 3 || bpm.length < 2 -> {
                _inputStatus.value = InputCheckStatus.LENGTH_ERROR
            }
        }

        zonesCalculatorHelper.generateHeartRateZones(bpm.toInt())
        _lthrZones.value = zonesCalculatorHelper.getHeartRateZones()
    }

    fun submitFTP(ftp: String) {

        when {
            ftp.length > 3 || ftp.length < 2 -> {
                _inputStatus.value = InputCheckStatus.LENGTH_ERROR
            }
        }

        when (_zoneActivity.value) {
            ZoneActivity.BIKE -> {
                zonesCalculatorHelper.generatePowerZones(ftp.toInt(), CalculatorHelper.BIKE_POWER)
                _powerZones.value = zonesCalculatorHelper.getBikePowerZones()
            }
            ZoneActivity.RUN -> {
                zonesCalculatorHelper.generatePowerZones(ftp.toInt(), CalculatorHelper.RUN_POWER)
                _powerZones.value = zonesCalculatorHelper.getRunPowerZones()
            }
        }
    }

    fun submitSwimPaceValue(paceValue400: Float, paceValue200: Float) {
        zonesCalculatorHelper.generateCriticalSwimSpeedPaceZones(paceValue400, paceValue200)
        _swimZones.value = zonesCalculatorHelper.getCSSZones()
    }
}