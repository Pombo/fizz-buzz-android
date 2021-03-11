package dev.erictruong.fizzbuzz.android.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import dev.erictruong.fizzbuzz.android.R
import dev.erictruong.fizzbuzz.android.home.viewmodel.HomeViewModel
import dev.erictruong.fizzbuzz.android.home.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(requireActivity().application)
    }

    private var lowerLimitInput: EditText? = null
    private var upperLimitInput: EditText? = null
    private var numberOneInput: EditText? = null
    private var numberTwoInput: EditText? = null
    private var wordOneInput: EditText? = null
    private var wordTwoInput: EditText? = null
    private var submitButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val limitText: TextView = root.findViewById(R.id.label_input_limits)
        val replacementText: TextView = root.findViewById(R.id.label_input_words)
        val outputText: TextView = root.findViewById(R.id.text_home)

        val lowerLimitInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_lower_limit)
        lowerLimitInput = root.findViewById<EditText>(R.id.input_lower_limit).apply {
            isEnabled = false // Disabled permanently
            setText(homeViewModel.lowerLimit.toString())
        }
        val upperLimitInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_upper_limit)
        upperLimitInput = root.findViewById<EditText>(R.id.input_upper_limit).apply {
            setText(homeViewModel.upperLimit.toString())
        }
        val numberOneInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_number_one)
        numberOneInput = root.findViewById<EditText>(R.id.input_number_one).apply {
            setText(homeViewModel.numberOne.toString())
        }
        val numberTwoInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_number_two)
        numberTwoInput = root.findViewById<EditText>(R.id.input_number_two).apply {
            setText(homeViewModel.numberTwo.toString())
        }
        val wordOneInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_word_one)
        wordOneInput = root.findViewById<EditText>(R.id.input_word_one).apply {
            setText(homeViewModel.wordOne)
        }
        val wordTwoInputLayout = root.findViewById<TextInputLayout>(R.id.input_layout_word_two)
        wordTwoInput = root.findViewById<EditText>(R.id.input_word_two).apply {
            setText(homeViewModel.wordTwo)
        }
        submitButton = root.findViewById<Button>(R.id.button_submit).apply {
            setOnClickListener {
                setFormEnabled(false)
                homeViewModel.submitForm(
                    lowerLimitInput?.text?.toString(),
                    upperLimitInput?.text?.toString(),
                    numberOneInput?.text?.toString(),
                    numberTwoInput?.text?.toString(),
                    wordOneInput?.text?.toString(),
                    wordTwoInput?.text?.toString()
                )
                setFormEnabled(true)
            }
        }

        // --

        homeViewModel.limitTextLive.observe(viewLifecycleOwner) { limitText.text = it }
        homeViewModel.replacementTextLive.observe(viewLifecycleOwner) { replacementText.text = it }
        homeViewModel.textLive.observe(viewLifecycleOwner) { outputText.text = it }

        listOf(
            homeViewModel.lowerLimitErrorLive to lowerLimitInputLayout,
            homeViewModel.upperLimitErrorLive to upperLimitInputLayout,
            homeViewModel.numberOneErrorLive to numberOneInputLayout,
            homeViewModel.numberTwoErrorLive to numberTwoInputLayout,
            homeViewModel.wordOneErrorLive to wordOneInputLayout,
            homeViewModel.wordTwoErrorLive to wordTwoInputLayout
        ).forEach { (liveData, view) ->
            liveData.observe(viewLifecycleOwner) {
                view.error = it
                view.isErrorEnabled = it != null
            }
        }

        return root
    }

    private fun setFormEnabled(isEnabled: Boolean) {
        upperLimitInput?.isEnabled = isEnabled
        numberOneInput?.isEnabled = isEnabled
        numberTwoInput?.isEnabled = isEnabled
        wordOneInput?.isEnabled = isEnabled
        wordTwoInput?.isEnabled = isEnabled
        submitButton?.isEnabled = isEnabled
    }
}