package dev.psuchanek.endurancepacecalculator.ui.pace

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
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.CalculatorBaseLayoutBinding
import dev.psuchanek.endurancepacecalculator.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PaceCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: CalculatorBaseLayoutBinding
    private val paceViewModel: PaceViewModel by viewModels()

    private lateinit var tiPaceMinutes: TextInputEditText
    private lateinit var tiPaceSeconds: TextInputEditText
    private lateinit var tiDurationHours: TextInputEditText
    private lateinit var tiDurationMinutes: TextInputEditText
    private lateinit var tiDurationSeconds: TextInputEditText

    private lateinit var sliderSwim: Slider
    private lateinit var sliderTransitionOne: Slider
    private lateinit var sliderBike: Slider
    private lateinit var sliderTransitionTwo: Slider
    private lateinit var sliderRun: Slider

    private lateinit var textWatcherReferenceHolder: TextWatcher


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalculatorBaseLayoutBinding.inflate(layoutInflater, container, false)
        textWatcherReferenceHolder = textWatcherForRunningPaceAndDurationCalculator(-1)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupInputTextFocusListeners()
        setupInputTextWatchers()
        setupTriSlidersTouchListeners()
        setupObservers()
        initAdapters()
        setupSpinnerListeners()
    }


    private fun initUI() {
        setupRunUI()
        setupTriUI()
    }

    private fun setupRunUI() {
        tiPaceMinutes = binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.tiPaceMinutes

        tiPaceSeconds = binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.tiPaceSeconds

        tiDurationHours =
            binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiDurationHours

        tiDurationMinutes =
            binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiDurationMinutes

        tiDurationSeconds =
            binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiDurationSeconds
        applyNumericFiltersOnTextInput()

    }


    private fun applyNumericFiltersOnTextInput() {
        tiPaceMinutes.applyFilterOnTextInput()
        tiPaceSeconds.applyFilterOnTextInput()
        tiDurationHours.applyFilterOnTextInput()
        tiDurationMinutes.applyFilterOnTextInput()
        tiDurationSeconds.applyFilterOnTextInput()
    }

    private fun TextInputEditText.applyFilterOnTextInput() {
        this.filters = arrayOf(
            InputFilterMinMax(
                InputFilterMinMax.MIN_INPUT_VALUE,
                InputFilterMinMax.MAX_INPUT_VALUE
            )
        )
    }

    private fun setupTriUI() {
        sliderSwim = binding.layoutPaceCalculator.layoutTriathlon.swimSlider
        sliderTransitionOne = binding.layoutPaceCalculator.layoutTriathlon.transitionOneSlider
        sliderBike = binding.layoutPaceCalculator.layoutTriathlon.bikeSlider
        sliderTransitionTwo = binding.layoutPaceCalculator.layoutTriathlon.transitionTwoSlider
        sliderRun = binding.layoutPaceCalculator.layoutTriathlon.runSlider

    }

    private fun setupInputTextWatchers() {
        tiPaceMinutes.addTextChangedListener(
            textWatcherForRunningPaceAndDurationCalculator(
                tiPaceMinutes.id
            )
        )
        tiPaceSeconds.addTextChangedListener(
            textWatcherForRunningPaceAndDurationCalculator(
                tiPaceSeconds.id
            )
        )


        tiDurationHours.addTextChangedListener(
            textWatcherForRunningPaceAndDurationCalculator(
                tiDurationHours.id
            )
        )
        tiDurationMinutes.addTextChangedListener(
            textWatcherForRunningPaceAndDurationCalculator(
                tiDurationMinutes.id
            )
        )
        tiDurationSeconds.addTextChangedListener(
            textWatcherForRunningPaceAndDurationCalculator(
                tiDurationSeconds.id
            )
        )

    }

    private fun setupTriSlidersTouchListeners() {
        sliderSwim.addOnChangeListener(addOnSliderValueChangeListener(sliderSwim.id))
        sliderTransitionOne.addOnChangeListener(addOnSliderValueChangeListener(sliderTransitionOne.id))
        sliderBike.addOnChangeListener(addOnSliderValueChangeListener(sliderBike.id))
        sliderTransitionTwo.addOnChangeListener(addOnSliderValueChangeListener(sliderTransitionTwo.id))
        sliderRun.addOnChangeListener(addOnSliderValueChangeListener(sliderRun.id))
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            paceViewModel.durationValues.collect { durationList ->
                updateDurationUI(durationList)
            }
        }

        lifecycleScope.launch {
            paceViewModel.paceValues.collect { paceList ->
                updatePaceUI(paceList)
            }
        }

        lifecycleScope.launch {
            paceViewModel.swimPaceValue.collect {
                binding.layoutPaceCalculator.layoutTriathlon.tiSwimPace.text = it
            }
        }
    }

    private fun updatePaceUI(paceList: List<String>) {
        if (!tiPaceMinutes.hasFocus()) tiPaceMinutes.setText(paceList[0])
        if (!tiPaceSeconds.hasFocus()) tiPaceSeconds.setText(paceList[1])
    }

    private fun updateDurationUI(durationList: List<String>) {
        if (!tiDurationHours.hasFocus()) tiDurationHours.setText(durationList[0])
        if (!tiDurationMinutes.hasFocus()) tiDurationMinutes.setText(durationList[1])
        if (!tiDurationSeconds.hasFocus()) tiDurationSeconds.setText(durationList[2])
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


    private fun setupInputTextFocusListeners() {
        tiPaceMinutes.onFocusChangeListener =
            focusChangeListenersHandlingForRunDurationAndPaceTextInputs()
        tiPaceSeconds.onFocusChangeListener =
            focusChangeListenersHandlingForRunDurationAndPaceTextInputs()


        tiDurationHours.onFocusChangeListener =
            focusChangeListenersHandlingForRunDurationAndPaceTextInputs()
        tiDurationMinutes.onFocusChangeListener =
            focusChangeListenersHandlingForRunDurationAndPaceTextInputs()
        tiDurationSeconds.onFocusChangeListener =
            focusChangeListenersHandlingForRunDurationAndPaceTextInputs()
    }

    private fun focusChangeListenersHandlingForRunDurationAndPaceTextInputs() =
        View.OnFocusChangeListener { v, hasFocus ->
            when (v.id) {
                tiPaceMinutes.id, tiPaceSeconds.id -> {
                    when (hasFocus) {
                        true -> {
                            handleTextWatchersRegistration(
                                false,
                                textWatcherForRunningPaceAndDurationCalculator(v.id),
                                DURATION_WATCHER_SET
                            )
                            v.clearTextInTextInput()

                        }
                        false -> {
                            handleTextWatchersRegistration(
                                true,
                                textWatcherForRunningPaceAndDurationCalculator(v.id),
                                DURATION_WATCHER_SET
                            )
                        }
                    }
                }

                tiDurationHours.id, tiDurationMinutes.id, tiDurationSeconds.id -> {
                    when (hasFocus) {
                        true -> {
                            handleTextWatchersRegistration(
                                false,
                                textWatcherForRunningPaceAndDurationCalculator(v.id),
                                PACE_WATCHER_SET
                            )
                            v.clearTextInTextInput()
                        }
                        false -> {
                            handleTextWatchersRegistration(
                                true,
                                textWatcherForRunningPaceAndDurationCalculator(v.id),
                                PACE_WATCHER_SET
                            )
                        }
                    }
                }
            }
        }

    private fun View.clearTextInTextInput() {
        (this as TextInputEditText).text?.clear()
    }


    private fun handleTextWatchersRegistration(
        register: Boolean = true,
        textWatcher: TextWatcher,
        watcherSet: Int
    ) {
        when (register) {
            true -> {
                if (watcherSet == DURATION_WATCHER_SET)
                    handleDurationTextWatcherRegistration(textWatcher = textWatcher)
                else
                    handlePaceTextWatcherRegistration(textWatcher = textWatcher)
            }
            false -> {
                if (watcherSet == DURATION_WATCHER_SET)
                    handleDurationTextWatcherRegistration(false, textWatcher = textWatcher)
                else
                    handlePaceTextWatcherRegistration(false, textWatcher = textWatcher)

            }
        }
    }

    private fun handleDurationTextWatcherRegistration(
        register: Boolean = true,
        textWatcher: TextWatcher
    ) {
        when (register) {
            true -> {
                tiDurationHours.addTextChangedListener(textWatcher)
                tiDurationMinutes.addTextChangedListener(textWatcher)
                tiDurationSeconds.addTextChangedListener(textWatcher)
            }
            false -> {
                tiDurationHours.removeTextChangedListener(textWatcher)
                tiDurationMinutes.removeTextChangedListener(textWatcher)
                tiDurationSeconds.removeTextChangedListener(textWatcher)
            }
        }
    }

    private fun handlePaceTextWatcherRegistration(
        register: Boolean = true,
        textWatcher: TextWatcher
    ) {
        when (register) {
            true -> {
                tiPaceMinutes.addTextChangedListener(textWatcher)
                tiPaceSeconds.addTextChangedListener(textWatcher)

            }
            false -> {
                tiPaceMinutes.removeTextChangedListener(textWatcher)
                tiPaceSeconds.removeTextChangedListener(textWatcher)
            }
        }
    }


    private fun textWatcherForRunningPaceAndDurationCalculator(id: Int) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            when (id) {
                tiPaceMinutes.id, tiPaceSeconds.id -> {
                    submitPaceValuesToViewModel(
                        if (tiPaceMinutes.text.toString()
                                .isNotBlank()
                        ) tiPaceMinutes.text.toString() else DEFAULT_TIME_VALUE,

                        if (tiPaceSeconds.text.toString()
                                .isNotBlank()
                        ) tiPaceSeconds.text.toString() else DEFAULT_TIME_VALUE
                    )
                }

                tiDurationHours.id, tiDurationMinutes.id, tiDurationSeconds.id -> {
                    submitDurationValuesToViewModel(
                        if (tiDurationHours.text.toString()
                                .isNotBlank()
                        ) tiDurationHours.text.toString() else DEFAULT_TIME_VALUE,
                        if (tiDurationMinutes.text.toString()
                                .isNotBlank()
                        ) tiDurationMinutes.text.toString() else DEFAULT_TIME_VALUE,

                        if (tiDurationSeconds.text.toString()
                                .isNotBlank()
                        ) tiDurationSeconds.text.toString() else DEFAULT_TIME_VALUE
                    )
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private fun submitDurationValuesToViewModel(hours: String, minutes: String, seconds: String) {
        paceViewModel.submitDuration(hours, minutes, seconds)
    }

    private fun submitPaceValuesToViewModel(minutes: String, seconds: String) {
        paceViewModel.submitPace(minutes, seconds)
    }

    private fun addOnSliderValueChangeListener(sliderId: Int) =
        Slider.OnChangeListener { slider, value, fromUser ->
            Timber.d(
                "DEBUG: the id which triggered: ${slider.id} and the swim id ${sliderSwim.id}"
            )
            when (slider.id) {
                sliderSwim.id -> {
                    Timber.d("DEBUG: slide value: $value")
                    paceViewModel.submitTriathlonStagePace(value, TriStage.SWIM)
                }

                sliderSwim.id -> {

                }

                sliderSwim.id -> {

                }

                sliderSwim.id -> {

                }

                sliderSwim.id -> {

                }
            }
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
        handleDurationTextWatcherRegistration(visibility, textWatcher = textWatcherReferenceHolder)
        handlePaceTextWatcherRegistration(visibility, textWatcher = textWatcherReferenceHolder)
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
            0, 1, 2, 3 -> {
                submitPaceValuesToViewModel(
                    if (tiPaceMinutes.text.toString()
                            .isNotBlank()
                    ) tiPaceMinutes.text.toString() else DEFAULT_TIME_VALUE,

                    if (tiPaceSeconds.text.toString()
                            .isNotBlank()
                    ) tiPaceSeconds.text.toString() else DEFAULT_TIME_VALUE
                )
            }
        }

    }

    private fun ActivityType.setActivityTypeInViewModel() {
        paceViewModel.setActivityType(this)
    }


    companion object {
        private const val DURATION_WATCHER_SET = 1
        private const val PACE_WATCHER_SET = 2
    }
}


