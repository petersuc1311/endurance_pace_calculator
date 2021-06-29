package dev.psuchanek.endurancepacecalculator.ui.pace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.CalculatorBaseLayoutBinding
import dev.psuchanek.endurancepacecalculator.utils.ACTIVITY_PRESET_VALUE
import dev.psuchanek.endurancepacecalculator.utils.UNIT_PRESET_VALUE

@AndroidEntryPoint
class PaceCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: CalculatorBaseLayoutBinding
    private val paceViewModel: PaceViewModel by viewModels()


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
        setupSpinnerListeners()
    }

    private fun initUI() {

    }

    private fun initAdapters() {
        val activityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.activities_for_pace_calculator)
        )
        binding.dropDownBaseSpinner.apply {
            setAdapter(activityAdapter)
            setText(ACTIVITY_PRESET_VALUE, false)
        }
        binding.tiLayoutDropDownBase.hint = resources.getString(R.string.activities_label)

        val unitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.units)
        )
        binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.unitsDropDown.apply {
            setAdapter(unitAdapter)
            setText(UNIT_PRESET_VALUE, false)
        }

    }

    private fun setupSpinnerListeners() {
       binding.dropDownBaseSpinner.onItemClickListener = activitySpinnerOnItemClickListener()
        binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.unitsDropDown.onItemClickListener = runUnitSpinnerOnItemClickListener()
    }

    private fun activitySpinnerOnItemClickListener() = AdapterView.OnItemClickListener { _, _, position, _ ->
        when(position) {
            0, 1, 2, 3 -> {
                binding.layoutPaceCalculator.layoutRunning.root.isVisible = true
                binding.layoutPaceCalculator.layoutTriathlon.root.isVisible = false
            }
            4,5,6,7 -> {
                binding.layoutPaceCalculator.layoutRunning.root.isVisible = false
                binding.layoutPaceCalculator.layoutTriathlon.root.isVisible = true

            }
        }
    }

    private fun runUnitSpinnerOnItemClickListener() = AdapterView.OnItemClickListener { _, _, position, _ ->
        when(position) {
            0 -> {
                //TODO: Implement unit change to pace calculator for both options
            }
            1 -> {

            }
        }
    }
}