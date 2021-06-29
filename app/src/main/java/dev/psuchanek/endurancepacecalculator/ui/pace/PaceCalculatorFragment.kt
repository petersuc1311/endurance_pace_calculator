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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.databinding.CalculatorBaseLayoutBinding
import dev.psuchanek.endurancepacecalculator.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

@AndroidEntryPoint
class PaceCalculatorFragment : Fragment(R.layout.calculator_base_layout) {

    private lateinit var binding: CalculatorBaseLayoutBinding
    private val paceViewModel: PaceViewModel by viewModels()

    private lateinit var tiPaceMinutes: TextInputEditText
    private lateinit var tiPaceSeconds: TextInputEditText
    private lateinit var tiDurationHours: TextInputEditText
    private lateinit var tiDurationMinutes: TextInputEditText
    private lateinit var tiDurationSeconds: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalculatorBaseLayoutBinding.inflate(layoutInflater, container, false)

        return binding.root
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

    }

    private fun updatePaceUI(paceList: List<String>) {
        tiPaceMinutes.setText(paceList[0])
        tiPaceSeconds.setText(paceList[1])
    }

    private fun updateDurationUI(durationList: List<String>) {
        tiDurationHours.setText(durationList[0])
        tiDurationMinutes.setText(durationList[1])
        tiDurationSeconds.setText(durationList[2])
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupInputTextListeners()
        setupObservers()
        initAdapters()
        setupSpinnerListeners()
    }

    private fun initUI() {
        tiPaceMinutes = binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.tiMinutes
        tiPaceMinutes.applyFilterOnTextInput()

        tiPaceSeconds = binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.tiSeconds
        tiPaceSeconds.applyFilterOnTextInput()

        tiDurationHours = binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiHours
        tiDurationHours.applyFilterOnTextInput()

        tiDurationMinutes = binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiMinutes
        tiDurationMinutes.applyFilterOnTextInput()

        tiDurationSeconds = binding.layoutPaceCalculator.layoutRunning.layoutRunningTime.tiSeconds
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

    private fun setupInputTextListeners() {
        tiPaceMinutes.addTextChangedListener(textWatcherForPaceCalculator(tiPaceMinutes.id))
        tiPaceSeconds.addTextChangedListener(textWatcherForPaceCalculator(tiPaceSeconds.id))


        tiDurationHours.addTextChangedListener(textWatcherForPaceCalculator(tiDurationHours.id))
        tiDurationMinutes.addTextChangedListener(textWatcherForPaceCalculator(tiDurationMinutes.id))
        tiDurationSeconds.addTextChangedListener(textWatcherForPaceCalculator(tiDurationSeconds.id))

    }

    private fun textWatcherForPaceCalculator(id: Int) = object : TextWatcher {
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

    private fun setupSpinnerListeners() {
        binding.dropDownBaseSpinner.onItemClickListener = activitySpinnerOnItemClickListener()
        binding.layoutPaceCalculator.layoutRunning.layoutRunningPace.unitsDropDown.onItemClickListener =
            runUnitSpinnerOnItemClickListener()
    }

    private fun activitySpinnerOnItemClickListener() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0, 1, 2, 3 -> {
                    binding.layoutPaceCalculator.layoutRunning.root.isVisible = true
                    binding.layoutPaceCalculator.layoutTriathlon.root.isVisible = false
                    position.setActivityTypeFromPosition()
                }

                4, 5, 6, 7 -> {
                    binding.layoutPaceCalculator.layoutRunning.root.isVisible = false
                    binding.layoutPaceCalculator.layoutTriathlon.root.isVisible = true
                    position.setActivityTypeFromPosition()
                }
            }
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
        submitPaceValuesToViewModel(
            if (tiPaceMinutes.text.toString()
                    .isNotBlank()
            ) tiPaceMinutes.text.toString() else DEFAULT_TIME_VALUE,

            if (tiPaceSeconds.text.toString()
                    .isNotBlank()
            ) tiPaceSeconds.text.toString() else DEFAULT_TIME_VALUE
        )
    }

    private fun ActivityType.setActivityTypeInViewModel() {
        paceViewModel.setActivityType(this)
    }

    private fun runUnitSpinnerOnItemClickListener() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    //TODO: Implement unit change to pace calculator for both options
                }
                1 -> {

                }
            }
        }
}