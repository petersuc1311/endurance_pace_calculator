package dev.psuchanek.endurancepacecalculator.ui.zones

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.ZonesCalculatorHelper
import dev.psuchanek.endurancepacecalculator.models.HeartRateZones
import dev.psuchanek.endurancepacecalculator.models.PaceZones
import dev.psuchanek.endurancepacecalculator.models.PowerZones
import dev.psuchanek.endurancepacecalculator.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ZonesViewModel @Inject constructor() : ViewModel() {

    private val zonesCalculatorHelper = ZonesCalculatorHelper()

    private val _zoneMethodType = MutableStateFlow(ZoneMethodType.LTHR)
    val zoneMethodType: StateFlow<ZoneMethodType> = _zoneMethodType

    private val _powerZoneActivity = MutableStateFlow(ZoneActivity.BIKE)

    private val _inputStatus = MutableStateFlow(InputCheckStatus.PASS)
    val inputStatus: StateFlow<InputCheckStatus> = _inputStatus

    private val _lthrZones = MutableStateFlow<List<HeartRateZones>>(emptyList())
    val lthrZones: StateFlow<List<HeartRateZones>> = _lthrZones

    private val _powerZones = MutableStateFlow<List<PowerZones>>(emptyList())
    val powerZones: StateFlow<List<PowerZones>> = _powerZones

    private val _sliderSwimPace400 = MutableStateFlow<List<String>>(emptyList())
    val sliderSwimPace400: StateFlow<List<String>> = _sliderSwimPace400

    private val _sliderSwimPace200 = MutableStateFlow<List<String>>(emptyList())
    val sliderSwimPace200: StateFlow<List<String>> = _sliderSwimPace200

    private val _swimCSS = MutableStateFlow<List<String>>(emptyList())
    val swimCSS: StateFlow<List<String>> = _swimCSS

    private val _swimZones = MutableStateFlow<List<PaceZones>>(emptyList())
    val swimZones: StateFlow<List<PaceZones>> = _swimZones

    fun setZoneMethodType(zoneMethodType: ZoneMethodType) {
        _zoneMethodType.value = zoneMethodType
    }

    fun setPowerZoneActivity(powerZoneActivity: ZoneActivity) {
        _powerZoneActivity.value = powerZoneActivity
    }

    fun submitBPM(bpm: String) {
        when {
            bpm.length > 3 || bpm.length <= 2 -> {
                _inputStatus.value = InputCheckStatus.LENGTH_ERROR
                return
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

        when (_powerZoneActivity.value) {
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
        _sliderSwimPace400.value = zonesCalculatorHelper.generatePaceListOfStrings(paceValue400.toInt())
        _sliderSwimPace200.value = zonesCalculatorHelper.generatePaceListOfStrings(paceValue200.toInt())
        _swimCSS.value = zonesCalculatorHelper.getCSSPace()
        _swimZones.value = zonesCalculatorHelper.getCSSZones()
    }
}