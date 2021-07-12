package dev.psuchanek.endurancepacecalculator.ui.splits

import androidx.lifecycle.ViewModel
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.SplitsCalculatorHelper
import dev.psuchanek.endurancepacecalculator.models.Split
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplitsViewModel : ViewModel() {

    private val splitsCalculatorHelper = SplitsCalculatorHelper()

    private val _distance = MutableStateFlow(CalculatorHelper.RUN_1500)
    private val _frequency = MutableStateFlow(SplitsCalculatorHelper.SPLIT_FREQUENCY_100M)

    private val _duration = MutableStateFlow<List<String>>(emptyList())
    val duration: StateFlow<List<String>> = _duration

    private val _splits = MutableStateFlow<List<Split>>(emptyList())
    val splits: StateFlow<List<Split>> = _splits

    fun setDistance(distance: Float) {
        _distance.value = distance
    }

    fun setFrequency(frequency: Float) {
        _frequency.value = frequency
    }

    fun submitDuration(duration: Float) {
        _duration.value = splitsCalculatorHelper.generateDurationListOfStrings(duration)

        splitsCalculatorHelper.generateSplits(_distance.value, _frequency.value, duration)
        _splits.value = splitsCalculatorHelper.getSplitsList()
    }
}