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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.CalculatorBaseLayoutBinding
import dev.psuchanek.endurancepacecalculator.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaceCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: CalculatorBaseLayoutBinding
    private val paceViewModel: PaceViewModel by viewModels()


    private lateinit var tvRunDurationHours: MaterialTextView
    private lateinit var tvRunDurationMinutes: MaterialTextView
    private lateinit var tvRunDurationSeconds: MaterialTextView

    private lateinit var tvTriathlonDurationHours: MaterialTextView
    private lateinit var tvTriathlonDurationMinutes: MaterialTextView
    private lateinit var tvTriathlonDurationSeconds: MaterialTextView

    private lateinit var sliderSwim: Slider
    private lateinit var sliderTransitionOne: Slider
    private lateinit var sliderBike: Slider
    private lateinit var sliderTransitionTwo: Slider
    private lateinit var sliderRun: Slider
    private lateinit var sliderRunPace: Slider


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
        setupSlidersTouchListeners()
        setupObservers()
        initAdapters()
        setupSpinnerListeners()
        submitPaceValuesToViewModel(sliderRunPace.value)
    }


    private fun initUI() {
        binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.apply {
            this@PaceCalculatorFragment.tvRunDurationHours = tvDurationHours
            this@PaceCalculatorFragment.tvRunDurationMinutes = tvDurationMinutes
            this@PaceCalculatorFragment.tvRunDurationSeconds = tvDurationSeconds
        }
        sliderRunPace = binding.layoutPaceCalculator.layoutRunning.sliderRunPace

        binding.layoutPaceCalculator.layoutTriathlon.apply {
            sliderSwim = swimSlider
            sliderTransitionOne = transitionOneSlider
            sliderBike = bikeSlider
            sliderTransitionTwo = transitionTwoSlider
            sliderRun = runSlider

            layoutTriathlonTotalDurationTime.apply {
                tvTriathlonDurationHours = tvDurationHours
                tvTriathlonDurationMinutes = tvDurationMinutes
                tvTriathlonDurationSeconds = tvDurationSeconds
            }
        }
    }

    private fun setupSlidersTouchListeners() {
        sliderSwim.addOnChangeListener(addOnSliderValueChangeListener())
        sliderTransitionOne.addOnChangeListener(addOnSliderValueChangeListener())
        sliderBike.addOnChangeListener(addOnSliderValueChangeListener())
        sliderTransitionTwo.addOnChangeListener(addOnSliderValueChangeListener())
        sliderRun.addOnChangeListener(addOnSliderValueChangeListener())
        sliderRunPace.addOnChangeListener(addOnSliderValueChangeListener())
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            paceViewModel.runDurationValues.collect { durationList ->
                updateRunDurationUI(durationList)
            }
        }

        lifecycleScope.launch {
            paceViewModel.runPaceValues.collect { paceList ->
                updatePaceUI(paceList)
            }
        }

        lifecycleScope.launch {
            paceViewModel.triSwimPaceValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiSwimPace.text = it
            }
        }

        lifecycleScope.launch {
            paceViewModel.transitionOneValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiTransitionOneTime.text = it
            }
        }

        lifecycleScope.launch {
            paceViewModel.triBikePaceValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiBikePace.text = it
            }
        }

        lifecycleScope.launch {
            paceViewModel.transitionTwoValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiTransitionTwoTime.text = it
            }

        }

        lifecycleScope.launch {
            paceViewModel.triRunPaceValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiRunPace.text = it
            }
        }

        lifecycleScope.launch {
            paceViewModel.triDurationValue.collect { durationList ->
                updateTriathlonDurationUI(durationList)
            }
        }
    }


    private fun updatePaceUI(paceList: List<String>) {
        binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.apply {
            tvRunPaceMinutes.text = paceList[0]
            tvRunPaceSeconds.text = paceList[1]
        }

    }

    private fun updateRunDurationUI(durationList: List<String>) {
        tvRunDurationHours.text = durationList[0]
        tvRunDurationMinutes.text = durationList[1]
        tvRunDurationSeconds.text = durationList[2]


    }

    private fun updateTriathlonDurationUI(durationList: List<String>) {
        tvTriathlonDurationHours.text = durationList[0]
        tvTriathlonDurationMinutes.text = durationList[1]
        tvTriathlonDurationSeconds.text = durationList[2]


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

    }

    private fun addOnSliderValueChangeListener() =
        Slider.OnChangeListener { slider, value, fromUser ->
            when (slider.id) {
                sliderSwim.id -> {
                    submitTriathlonStagePaceToViewModel(value, TriStage.SWIM)
                }
                sliderTransitionOne.id -> {
                    submitTriathlonStagePaceToViewModel(value, TriStage.T1)
                }
                sliderBike.id -> {
                    submitTriathlonStagePaceToViewModel(value, TriStage.BIKE)
                }
                sliderTransitionTwo.id -> {
                    submitTriathlonStagePaceToViewModel(value, TriStage.T2)
                }
                sliderRun.id -> {
                    submitTriathlonStagePaceToViewModel(value, TriStage.RUN)
                }
                sliderRunPace.id -> {
                    submitPaceValuesToViewModel(value)
                }
            }
        }

    private fun submitPaceValuesToViewModel(value: Float) {
        paceViewModel.submitRunPaceValue(value)
    }

    private fun submitTriathlonStagePaceToViewModel(value: Float, stage: TriStage) {
        paceViewModel.submitTriathlonStagePace(value, stage)
    }

    private fun setupSpinnerListeners() {
        binding.dropDownBaseSpinner.onItemClickListener = activitySpinnerOnItemClickListener()
    }

    private fun activitySpinnerOnItemClickListener() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0, 1, 2, 3 -> {
                    switchRunAndTriathlonCalculatorVisibility()
                    position.setActivityTypeFromPosition()
                }

                4, 5, 6, 7 -> {
                    switchRunAndTriathlonCalculatorVisibility(false)
                    position.setActivityTypeFromPosition()
                }
            }
        }

    private fun switchRunAndTriathlonCalculatorVisibility(visibility: Boolean = true) {
        binding.layoutPaceCalculator.layoutRunning.root.isVisible = visibility
        binding.layoutPaceCalculator.layoutTriathlon.root.isVisible = !visibility
    }

    private fun Int.setActivityTypeFromPosition() {
        when (this) {
            0 -> {
                ActivityType.FIVE_KM.setActivityTypeInViewModel()
            }
            1 -> {
                ActivityType.TEN_KM.setActivityTypeInViewModel()
            }
            2 -> {
                ActivityType.HALF_MARATHON.setActivityTypeInViewModel()
            }
            3 -> {
                ActivityType.FULL_MARATHON.setActivityTypeInViewModel()
            }
            4 -> {
                ActivityType.SPRINT.setActivityTypeInViewModel()
            }
            5 -> {
                ActivityType.OLYMPIC.setActivityTypeInViewModel()
            }
            6 -> {
                ActivityType.HALF_TRIATHLON.setActivityTypeInViewModel()
            }
            7 -> {
                ActivityType.FULL_TRIATHLON.setActivityTypeInViewModel()
            }
        }
        when (this) {
            in 0..3 -> {
                submitPaceValuesToViewModel(sliderRunPace.value)
            }
            in 4..7 -> {
                submitTriathlonStagePaceToViewModel(sliderSwim.value, TriStage.SWIM)
                submitTriathlonStagePaceToViewModel(sliderTransitionOne.value, TriStage.T1)
                submitTriathlonStagePaceToViewModel(sliderBike.value, TriStage.BIKE)
                submitTriathlonStagePaceToViewModel(sliderTransitionTwo.value, TriStage.T2)
                submitTriathlonStagePaceToViewModel(sliderRun.value, TriStage.RUN)
            }
        }

    }

    private fun ActivityType.setActivityTypeInViewModel() {
        paceViewModel.setActivityType(this)
    }
}


