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
import dev.psuchanek.endurancepacecalculator.databinding.LayoutSplitsCalculatorBinding
import dev.psuchanek.endurancepacecalculator.utils.FREQUENCY_PRESET_VALUE

@AndroidEntryPoint
class SplitsCalculatorFragment : Fragment(R.layout.layout_splits_calculator) {

    private lateinit var binding: LayoutSplitsCalculatorBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSplitsCalculatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initAdapters()
    }

    private fun initUI() {

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
        binding.dropDownSplitDistanceSpinner.setAdapter(distanceAdapter)
    }

    private fun setupDistanceAdapter() {
        val frequencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.splits_frequency)
        )
        binding.dropDownFrequencySpinner.apply {
            setAdapter(frequencyAdapter)
            setText(FREQUENCY_PRESET_VALUE, false)
        }
    }
}