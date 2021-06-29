package dev.psuchanek.endurancepacecalculator.ui.zones

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
import dev.psuchanek.endurancepacecalculator.utils.SPORTS_PRESET_VALUE
import dev.psuchanek.endurancepacecalculator.utils.ZONES_PRESET_VALUE

@AndroidEntryPoint
class ZonesCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

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
        initAdapter()
    }

    private fun initUI() {
        binding.layoutZonesCalculator.root.isVisible = true
        binding.layoutZonesChartInBase.root.isVisible = true
        binding.layoutPaceCalculator.root.isVisible = false
    }

    private fun initAdapter() {

        initMethodAdapter()
        initSportAdapter()
    }

    private fun initMethodAdapter() {
        val zoneMethodAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.zones_for_zones_calculator)
        )
        binding.dropDownBaseSpinner.apply {
            setAdapter(zoneMethodAdapter)
            setText(ZONES_PRESET_VALUE, false)
        }
        binding.tiLayoutDropDownBase.hint = resources.getString(R.string.zones_label)
    }

    private fun initSportAdapter() {
        val sportsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sports)
        )
        binding.layoutZonesCalculator.layoutHeartRateZones.layoutZonesBase.dropDownSportSpinner.apply {
            setAdapter(
                sportsAdapter
            )
            setText(SPORTS_PRESET_VALUE, false)
        }
    }
}