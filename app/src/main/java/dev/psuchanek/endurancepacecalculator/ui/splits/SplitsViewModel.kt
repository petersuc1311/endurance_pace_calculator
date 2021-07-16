package dev.psuchanek.endurancepacecalculator.ui.splits

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.SplitsCalculatorHelper
import dev.psuchanek.endurancepacecalculator.models.Split
import dev.psuchanek.endurancepacecalculator.models.UIModel
import dev.psuchanek.endurancepacecalculator.utils.DEFAULT_SPLITS_DURATION
import dev.psuchanek.endurancepacecalculator.utils.DEFAULT_SPLITS_SLIDER_VALUES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplitsViewModel @Inject constructor() : ViewModel() {

    private val splitsCalculatorHelper = SplitsCalculatorHelper()

    private val _distance = MutableStateFlow(CalculatorHelper.RUN_1500)
    private val _frequency = MutableStateFlow(SplitsCalculatorHelper.SPLIT_FREQUENCY_100M)
    private val _duration = MutableStateFlow(DEFAULT_SPLITS_DURATION)

    private val _durationValuesList = MutableStateFlow<List<String>>(emptyList())
    val durationValuesList: StateFlow<List<String>> = _durationValuesList

    private val _sliderValues = MutableStateFlow(DEFAULT_SPLITS_SLIDER_VALUES)
    val sliderValues: StateFlow<List<Float>> = _sliderValues

    private val _splits = MutableStateFlow<List<UIModel.SplitsModel>>(emptyList())
    val splits: StateFlow<List<UIModel.SplitsModel>> = _splits

    fun setDistance(distance: Float) {
        setNewSliderValues(distance)
        _distance.value = distance
        updateSplits()
    }

    private fun setNewSliderValues(distance: Float) {
        _sliderValues.value = getNewSliderBounds(distance)
    }

    private fun getNewSliderBounds(distance: Float): List<Float> {
        val listOfValues = splitsCalculatorHelper.getDurationMapValues(distance)
        listOfValues?.let {
            val sliderStartValue = getStartValue(listOfValues[0], listOfValues[1])
            return listOf(listOfValues[0], listOfValues[1], sliderStartValue)
        }

        return emptyList()
    }

    private fun getStartValue(durationFastest: Float, durationSlowest: Float): Float {
        return (durationFastest + durationSlowest) / 2
    }

    fun setFrequency(frequency: Float) {
        _frequency.value = frequency
        updateSplits()
    }

    fun submitDuration(duration: Float) {
        _duration.value = duration
        _durationValuesList.value = splitsCalculatorHelper.generateDurationListOfStrings(duration)
        updateSplits()

    }

    private fun updateSplits() {
        splitsCalculatorHelper.generateSplits(_distance.value, _frequency.value, _duration.value)
        checkAndHandleWhenListIsEqual()
        val splitsInUIModel = mutableListOf<UIModel.SplitsModel>()
        splitsCalculatorHelper.getSplitsList().map { split ->
            splitsInUIModel.add(UIModel.SplitsModel(split))
        }
        _splits.value = splitsInUIModel

    }

    private fun checkAndHandleWhenListIsEqual() {
        if (_splits.value == splitsCalculatorHelper.getSplitsList()) {
            _splits.value = emptyList()
        }
    }
}