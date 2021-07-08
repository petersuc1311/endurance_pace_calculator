package dev.psuchanek.endurancepacecalculator.ui.zones

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.ZonesListAdapter
import dev.psuchanek.endurancepacecalculator.databinding.LayoutMinutesSecondsBinding
import dev.psuchanek.endurancepacecalculator.databinding.LayoutZonesCalculatorBinding
import dev.psuchanek.endurancepacecalculator.models.HeartRateZones
import dev.psuchanek.endurancepacecalculator.models.Zones
import dev.psuchanek.endurancepacecalculator.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ZonesCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: LayoutZonesCalculatorBinding
    private lateinit var zonesAdapter: ZonesListAdapter
    private val zonesViewModel: ZonesViewModel by viewModels()

    private lateinit var sliderSwimPace400: Slider
    private lateinit var sliderSwimPace200: Slider

    private lateinit var textInputBPM: TextInputEditText
    private lateinit var textInputFTP: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutZonesCalculatorBinding.inflate(layoutInflater, container, false)
        setupListAdapter()
        return binding.root
    }

    private fun setupListAdapter() {
        zonesAdapter = ZonesListAdapter()
        binding.layoutZonesChart.zonesRecyclerView.apply {
            adapter = zonesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupSliderListeners()
        initAdapter()
        setupSpinnerListener()
        setupTextInputListeners()
        setupObservers()
    }


    private fun initUI() {
        textInputBPM = binding.layoutHeartRateZones.tiBPM
        textInputFTP = binding.layoutPowerZones.tiFTP
        binding.layoutSwimPaceZones.apply {
            this@ZonesCalculatorFragment.sliderSwimPace400 = sliderSwimPace400
            this@ZonesCalculatorFragment.sliderSwimPace200 = sliderSwimPace200
        }
    }

    private fun setupSliderListeners() {
        sliderSwimPace400.addOnChangeListener(addSliderOnChangeValueListener())
        sliderSwimPace200.addOnChangeListener(addSliderOnChangeValueListener())

    }

    private fun addSliderOnChangeValueListener() = Slider.OnChangeListener { slider, value, _ ->
        when (slider.id) {
            sliderSwimPace400.id -> {
                submitSwimPaceValuesToViewModel(value, sliderSwimPace200.value)
            }
            sliderSwimPace200.id -> {
                submitSwimPaceValuesToViewModel(sliderSwimPace400.value, value)
            }
        }
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
        binding.dropDownZonesSpinner.apply {
            setAdapter(zoneMethodAdapter)
            setText(ZONES_PRESET_VALUE, false)
        }
        binding.tiLayoutDropDownZones.hint = resources.getString(R.string.zones_label)
    }

    private fun initSportAdapter() {
        val sportsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sports)
        )
        binding.layoutPowerZones.dropDownSportChoiceSpinner.apply {
            setAdapter(
                sportsAdapter
            )
            setText(SPORTS_PRESET_VALUE, false)
        }
    }

    private fun setupSpinnerListener() {
        binding.dropDownZonesSpinner.onItemClickListener = methodSpinnerOnItemClickListener()
        binding.layoutPowerZones.dropDownSportChoiceSpinner.onItemClickListener =
            zoneActivitySpinnerOnItemClickListener()

    }

    private fun methodSpinnerOnItemClickListener() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    clearAdapterList()
                    zonesViewModel.setZoneMethodType(ZoneMethodType.LTHR)

                }
                1 -> {
                    clearAdapterList()
                    zonesViewModel.setZoneMethodType(ZoneMethodType.POWER)
                }
                2 -> {
                    clearAdapterList()
                    zonesViewModel.setZoneMethodType(ZoneMethodType.RUN_PACE)
                }
                3 -> {
                    zonesViewModel.setZoneMethodType(ZoneMethodType.SWIM_PACE)
                    submitSwimPaceValuesToViewModel(
                        sliderSwimPace400.value,
                        sliderSwimPace200.value
                    )
                }
            }
        }

    private fun submitSwimPaceValuesToViewModel(paceValue400: Float, paceValue200: Float) {
        zonesViewModel.submitSwimPaceValue(
            paceValue400,
            paceValue200
        )
    }

    private fun zoneActivitySpinnerOnItemClickListener() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    zonesViewModel.setPowerZoneActivity(ZoneActivity.BIKE)

                }
                1 -> {
                    zonesViewModel.setPowerZoneActivity(ZoneActivity.RUN)
                }
            }
            submitPowerToViewModel(textInputFTP.text.toString())
        }

    private fun submitPowerToViewModel(ftp: String) {
        zonesViewModel.submitFTP(ftp)
    }

    private fun setupTextInputListeners() {
        textInputBPM.apply {
            addTextChangedListener(zonesTextWatcher(this.id))
        }
        textInputFTP.apply {
            addTextChangedListener(zonesTextWatcher(this.id))
        }
    }

    private fun zonesTextWatcher(id: Int) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (id) {
                textInputBPM.id -> {
                    zonesViewModel.submitBPM(textInputBPM.text.toString())

                }
                textInputFTP.id -> {
                    submitPowerToViewModel(textInputFTP.text.toString())
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            zonesViewModel.inputStatus.collect{ inputStatus ->
                when(inputStatus) {
                    InputCheckStatus.LENGTH_ERROR -> {
                        binding.layoutPowerZones.tiLayoutFTP.error = "Min. 3 digits"
                    }
                    InputCheckStatus.PASS -> binding.layoutPowerZones.tiLayoutFTP.error = null
                    InputCheckStatus.VALUE_ERROR -> {

                    }
                }
            }
        }

        lifecycleScope.launch {
            zonesViewModel.zoneMethodType.collect { zoneType ->
                setLayoutForZoneType(zoneType)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.lthrZones.collect { zones ->
                submitListToZonesAdapter(zones)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.powerZones.collect { zones ->
                submitListToZonesAdapter(zones)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.swimCSS.collect { listOfPaceValues ->
                if (listOfPaceValues.isNotEmpty()) {
                    val cssPaceValue = "${listOfPaceValues[0]}:${listOfPaceValues[1]}"
                    binding.layoutSwimPaceZones.tvSwimCSS.text = cssPaceValue
                }


            }
        }

        lifecycleScope.launch {
            zonesViewModel.sliderSwimPace400.collect { paceList ->
                if (paceList.isNotEmpty()) {
                    binding.layoutSwimPaceZones.layoutSwimPace400.updateMinutesSecondsUI(paceList)
                }

            }
        }

        lifecycleScope.launch {
            zonesViewModel.sliderSwimPace200.collect { paceList ->
                if (paceList.isNotEmpty()) {
                    binding.layoutSwimPaceZones.layoutSwimPace200.updateMinutesSecondsUI(paceList)
                }

            }
        }

        lifecycleScope.launch {
            zonesViewModel.swimZones.collect { zones ->
                submitListToZonesAdapter(zones)
            }
        }

    }

    private fun <T : Zones> submitListToZonesAdapter(zones: List<T>) {
        zonesAdapter.submitList(zones)
    }

    private fun LayoutMinutesSecondsBinding.updateMinutesSecondsUI(list: List<String>) {
        this.tvPaceMinutes.text = list[0]
        this.tvPaceSeconds.text = list[1]
    }

    private fun setLayoutForZoneType(zoneType: ZoneMethodType) {
        when (zoneType) {
            ZoneMethodType.LTHR -> {
                changeLayoutVisibility(lthr = true, power = false, swimPace = false)
            }
            ZoneMethodType.POWER -> {
                changeLayoutVisibility(lthr = false, power = true, swimPace = false)
            }
            ZoneMethodType.SWIM_PACE -> {
                changeLayoutVisibility(lthr = false, power = false, swimPace = true)
            }
            ZoneMethodType.RUN_PACE -> {

            }
        }

    }

    private fun clearAdapterList() {
        zonesAdapter.submitList(emptyList())
    }

    private fun changeLayoutVisibility(lthr: Boolean, power: Boolean, swimPace: Boolean) {
        binding.layoutHeartRateZones.root.isVisible = lthr
        binding.layoutPowerZones.root.isVisible = power
        binding.layoutSwimPaceZones.root.isVisible = swimPace
    }
}