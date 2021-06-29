package dev.psuchanek.endurancepacecalculator.ui.splits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.CalculatorBaseLayoutBinding
import dev.psuchanek.endurancepacecalculator.utils.FREQUENCY_PRESET_VALUE

@AndroidEntryPoint
class SplitsCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: CalculatorBaseLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalculatorBaseLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initAdapters()
    }

    private fun initUI() {
        binding.layoutPaceCalculator.root.isVisible = false
        binding.layoutSplitsRecyclerViewInBase.root.isVisible = true
        binding.layoutSplitsCalculator.root.isVisible = true

    }

    private fun initAdapters() {
        setupDistanceAdapter()
        setupFrequencyAdapter()

    }

    private fun setupFrequencyAdapter() {
        val distanceAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.distances_for_splits_calculator)
        )
        binding.dropDownBaseSpinner.setAdapter(distanceAdapter)
        binding.tiLayoutDropDownBase.hint = resources.getString(R.string.distance_label)
    }

    private fun setupDistanceAdapter() {
        val frequencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.splits_frequency)
        )
        binding.layoutSplitsCalculator.dropDownFrequencySpinner.apply {
            setAdapter(frequencyAdapter)
            setText(FREQUENCY_PRESET_VALUE, false)
        }
    }
}