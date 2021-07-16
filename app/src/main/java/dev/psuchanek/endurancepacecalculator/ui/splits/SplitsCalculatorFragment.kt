package dev.psuchanek.endurancepacecalculator.ui.splits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.EnduranceListAdapter
import dev.psuchanek.endurancepacecalculator.calculator.CalculatorHelper
import dev.psuchanek.endurancepacecalculator.calculator.SplitsCalculatorHelper
import dev.psuchanek.endurancepacecalculator.databinding.LayoutSplitsCalculatorBinding
import dev.psuchanek.endurancepacecalculator.utils.DISTANCE_PRESET_VALUE
import dev.psuchanek.endurancepacecalculator.utils.FREQUENCY_PRESET_VALUE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplitsCalculatorFragment : Fragment(R.layout.layout_splits_calculator) {

    private lateinit var binding: LayoutSplitsCalculatorBinding
    private val splitsViewModel: SplitsViewModel by viewModels()
    private lateinit var enduranceAdapter: EnduranceListAdapter

    private lateinit var sliderDuration: Slider


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSplitsCalculatorBinding.inflate(layoutInflater, container, false)
        initEnduranceAdapter()
        return binding.root
    }

    private fun initEnduranceAdapter() {
        enduranceAdapter = EnduranceListAdapter()
        binding.layoutSplitRecyclerView.splitsRecyclerView.apply {
            adapter = enduranceAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initAdapters()
        setupSpinnerListeners()
        setupObservers()
    }


    private fun initUI() {
        sliderDuration = binding.splitsDurationSlider
        setupSliderListener()
    }

    private fun setupSliderListener() {
        sliderDuration.addOnChangeListener(sliderOnChangeListener)
    }

    private val sliderOnChangeListener =
        Slider.OnChangeListener { _, value, _ -> submitDurationToViewModel(value) }

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
        binding.dropDownSplitDistanceSpinner.apply {
            setAdapter(distanceAdapter)
            setText(DISTANCE_PRESET_VALUE, false)
        }
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

    private fun setupSpinnerListeners() {
        binding.dropDownSplitDistanceSpinner.apply {
            onItemClickListener = itemClickListener(this.id)
        }
        binding.dropDownFrequencySpinner.apply {
            onItemClickListener = itemClickListener(this.id)
        }
    }

    private fun itemClickListener(id: Int) =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (id) {
                binding.dropDownSplitDistanceSpinner.id -> {
                    handleDistanceSpinner(position)
                }
                binding.dropDownFrequencySpinner.id -> {
                    handleFrequencySpinner(position)
                }

            }
        }


    private fun handleDistanceSpinner(position: Int) {
        submitDistanceToViewModel(CalculatorHelper.LIST_OF_RUN_DISTANCES[position])
    }

    private fun handleFrequencySpinner(position: Int) {
        submitFrequencyToViewModel(SplitsCalculatorHelper.LIST_OF_SPLIT_FREQUENCIES[position])
    }

    private fun submitDistanceToViewModel(distance: Float) {
        splitsViewModel.setDistance(distance)
    }

    private fun submitFrequencyToViewModel(frequency: Float) {
        splitsViewModel.setFrequency(frequency)
    }

    private fun submitDurationToViewModel(duration: Float) {
        splitsViewModel.submitDuration(duration)
    }

    private fun setupObservers() {

        lifecycleScope.launch {
            splitsViewModel.sliderValues.collect { valuesList ->
                setSliderValues(valuesList)
            }
        }

        lifecycleScope.launch {
            splitsViewModel.durationValuesList.collect { durationList ->
                binding.layoutSplitsDuration.tvDurationHours.text = durationList[0]
                binding.layoutSplitsDuration.tvDurationMinutes.text = durationList[1]
                binding.layoutSplitsDuration.tvDurationSeconds.text = durationList[2]
            }
        }

        lifecycleScope.launch {
            splitsViewModel.splits.collect { splitsList ->
                enduranceAdapter.submitList(splitsList)
            }
        }

    }

    private fun setSliderValues(valuesList: List<Float>) {
        sliderDuration.valueFrom = valuesList[0]
        sliderDuration.valueTo = valuesList[1]
        sliderDuration.value = valuesList[2]
        submitDurationToViewModel(sliderDuration.value)
    }


}