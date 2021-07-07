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
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.LayoutMinutesSecondsBinding
import dev.psuchanek.endurancepacecalculator.databinding.LayoutZonesCalculatorBinding
import dev.psuchanek.endurancepacecalculator.utils.SPORTS_PRESET_VALUE
import dev.psuchanek.endurancepacecalculator.utils.ZONES_PRESET_VALUE
import dev.psuchanek.endurancepacecalculator.utils.ZoneActivity
import dev.psuchanek.endurancepacecalculator.utils.ZoneMethodType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ZonesCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: LayoutZonesCalculatorBinding
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
        return binding.root
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
        zonesViewModel.submitSwimPaceValue(sliderSwimPace400.value, sliderSwimPace200.value)
    }

    private fun addSliderOnChangeValueListener() = Slider.OnChangeListener { slider, value, _ ->
        when (slider.id) {
            sliderSwimPace400.id -> {
                zonesViewModel.submitSwimPaceValue(value, sliderSwimPace200.value)
            }
            sliderSwimPace200.id -> {
                zonesViewModel.submitSwimPaceValue(sliderSwimPace400.value, value)
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
                    zonesViewModel.setZoneMethodType(ZoneMethodType.LTHR)
                }
                1 -> {
                    zonesViewModel.setZoneMethodType(ZoneMethodType.POWER)
                }
                2 -> {
                    zonesViewModel.setZoneMethodType(ZoneMethodType.RUN_PACE)
                }
                3 -> {
                    zonesViewModel.setZoneMethodType(ZoneMethodType.SWIM_PACE)
                }
            }
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
            zonesViewModel.zoneMethodType.collect { zoneType ->
                setLayoutForZoneType(zoneType)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.lthrZones.collect { zones ->
                Timber.d("DEBUG: lthr zones: $zones")
            }
        }

        lifecycleScope.launch {
            zonesViewModel.powerZones.collect { zones ->
                Timber.d("DEBUG: power zones: $zones")
            }
        }

        lifecycleScope.launch {
            zonesViewModel.swimCSS.collect { listOfPaceValues ->
                val cssPaceValue = "${listOfPaceValues[0]}:${listOfPaceValues[1]}"
                binding.layoutSwimPaceZones.tvSwimCSS.text = cssPaceValue
            }
        }

        lifecycleScope.launch {
            zonesViewModel.sliderSwimPace400.collect { paceList ->
                binding.layoutSwimPaceZones.layoutSwimPace400.updateMinutesSecondsUI(paceList)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.sliderSwimPace200.collect { paceList ->
                binding.layoutSwimPaceZones.layoutSwimPace200.updateMinutesSecondsUI(paceList)
            }
        }

        lifecycleScope.launch {
            zonesViewModel.swimZones.collect { zones ->
                Timber.d("DEBUG: swim zones: $zones")
            }
        }

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

    private fun changeLayoutVisibility(lthr: Boolean, power: Boolean, swimPace: Boolean) {
        binding.layoutHeartRateZones.root.isVisible = lthr
        binding.layoutPowerZones.root.isVisible = power
        binding.layoutSwimPaceZones.root.isVisible = swimPace
    }
}