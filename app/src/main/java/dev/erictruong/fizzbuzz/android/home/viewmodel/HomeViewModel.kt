package dev.erictruong.fizzbuzz.android.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.erictruong.fizzbuzz.android.CoroutineContextProvider
import dev.erictruong.fizzbuzz.android.R
import dev.erictruong.fizzbuzz.android.data.RequestRepository
import dev.erictruong.fizzbuzz.android.data.model.Request
import dev.erictruong.fizzbuzz.android.home.ui.HomeFragment
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * ViewModel for [HomeFragment].
 */
class HomeViewModel(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val app: Application,
    private val requestRepository: RequestRepository
) : AndroidViewModel(app) {

    //region Initial values

    var lowerLimit: Int = 1
        private set
    var upperLimit: Int = 100
        private set
    var numberOne: Int = 3
        private set
    var numberTwo: Int = 5
        private set
    var wordOne: String = "Fizz"
        private set
    var wordTwo: String = "Buzz"
        private set

    //endregion

    //region Validators

    private val upperLimitValidator = NumberValidator(lowerLimit)
    private val numberValidator = NumberValidator(1)
    private val wordValidator = WordValidator()

    //endregion

    //region LiveData: output & error texts

    val limitTextLive: LiveData<CharSequence> get() = _limitTextLive
    private val _limitTextLive = MutableLiveData<CharSequence>()

    val replacementTextLive: LiveData<CharSequence> get() = _replacementTextLive
    private val _replacementTextLive = MutableLiveData<CharSequence>()

    val textLive: LiveData<CharSequence> get() = _textLive
    private val _textLive = MutableLiveData<CharSequence>()

    val lowerLimitErrorLive: LiveData<CharSequence?> get() = _lowerLimitErrorLive
    private val _lowerLimitErrorLive = MutableLiveData<CharSequence?>()

    val upperLimitErrorLive: LiveData<CharSequence?> get() = _upperLimitErrorLive
    private val _upperLimitErrorLive = MutableLiveData<CharSequence?>()

    val numberOneErrorLive: LiveData<CharSequence?> get() = _numberOneErrorLive
    private val _numberOneErrorLive = MutableLiveData<CharSequence?>()

    val numberTwoErrorLive: LiveData<CharSequence?> get() = _numberTwoErrorLive
    private val _numberTwoErrorLive = MutableLiveData<CharSequence?>()

    val wordOneErrorLive: LiveData<CharSequence?> get() = _wordOneErrorLive
    private val _wordOneErrorLive = MutableLiveData<CharSequence?>()

    val wordTwoErrorLive: LiveData<CharSequence?> get() = _wordTwoErrorLive
    private val _wordTwoErrorLive = MutableLiveData<CharSequence?>()

    //endregion

    /**
     * Submits the form values for validation; if valid then computes texts.
     */
    fun submitForm(
        lowerLimitInput: String?,
        upperLimitInput: String?,
        numberOneInput: String?,
        numberTwoInput: String?,
        wordOneInput: String?,
        wordTwoInput: String?
    ) {
        viewModelScope.launch(coroutineContextProvider.Default) {
            val isLowerLimitValid = validateLowerLimit(lowerLimitInput)
            val isUpperLimitValid = validateUpperLimit(upperLimitInput)
            val isNumberOneValid = validateNumberOne(numberOneInput)
            val isNumberTwoValid = validateNumberTwo(numberTwoInput)
            val isWordOneValid = validateWordOne(wordOneInput)
            val isWordTwoValid = validateWordTwo(wordTwoInput)

            val isValid = isLowerLimitValid
                    && isUpperLimitValid
                    && isNumberOneValid
                    && isNumberTwoValid
                    && isWordOneValid
                    && isWordTwoValid

            if (isValid) {
                val limitText = computeLimitText()
                _limitTextLive.postValue(limitText)

                val replacementText = computeReplacementText()
                _replacementTextLive.postValue(replacementText)

                val outputText = computeOutputText()
                _textLive.postValue(outputText)

                val request = Request(
                    lowerLimit,
                    upperLimit,
                    numberOne,
                    numberTwo,
                    wordOne,
                    wordTwo,
                    countMatches(outputText, wordOne),
                    countMatches(outputText, wordTwo),
                    countMatches(outputText, wordOne + wordTwo),
                    1
                )

                requestRepository.addOrIncrementCount(request)
            }
        }
    }

    fun validateLowerLimit(input: String?): Boolean {
        when (val result = numberValidator.validate(input)) {
            is NumberValidator.Result.Valid -> {
                upperLimitValidator.minimunValue = lowerLimit + 1
                lowerLimit = result.outputValue
                _lowerLimitErrorLive.postValue(null)
                return true
            }
            is NumberValidator.Result.Empty -> {
                _lowerLimitErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
            is NumberValidator.Result.NotInteger -> {
                _lowerLimitErrorLive.postValue(app.getString(R.string.input_field_error_not_integer))
            }
            is NumberValidator.Result.LowerThan -> {
                _lowerLimitErrorLive.postValue(
                    app.getString(R.string.input_field_error_lower_than, result.minimunValue)
                )
            }
        }
        return false
    }

    fun validateUpperLimit(input: String?): Boolean {
        when (val result = upperLimitValidator.validate(input)) {
            is NumberValidator.Result.Valid -> {
                upperLimit = result.outputValue
                _upperLimitErrorLive.postValue(null)
                return true
            }
            is NumberValidator.Result.Empty -> {
                _upperLimitErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
            is NumberValidator.Result.NotInteger -> {
                _upperLimitErrorLive.postValue(app.getString(R.string.input_field_error_not_integer))
            }
            is NumberValidator.Result.LowerThan -> {
                _upperLimitErrorLive.postValue(
                    app.getString(R.string.input_field_error_lower_than, result.minimunValue)
                )
            }
        }
        return false
    }

    fun validateNumberOne(input: String?): Boolean {
        when (val result = numberValidator.validate(input)) {
            is NumberValidator.Result.Valid -> {
                numberOne = result.outputValue
                _numberOneErrorLive.postValue(null)
                return true
            }
            is NumberValidator.Result.Empty -> {
                _numberOneErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
            is NumberValidator.Result.NotInteger -> {
                _numberOneErrorLive.postValue(app.getString(R.string.input_field_error_not_integer))
            }
            is NumberValidator.Result.LowerThan -> {
                _numberOneErrorLive.postValue(
                    app.getString(R.string.input_field_error_lower_than, result.minimunValue)
                )
            }
        }
        return false
    }

    fun validateNumberTwo(input: String?): Boolean {
        when (val result = numberValidator.validate(input)) {
            is NumberValidator.Result.Valid -> {
                numberTwo = result.outputValue
                _numberTwoErrorLive.postValue(null)
                return true
            }
            is NumberValidator.Result.Empty -> {
                _numberTwoErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
            is NumberValidator.Result.NotInteger -> {
                _numberTwoErrorLive.postValue(app.getString(R.string.input_field_error_not_integer))
            }
            is NumberValidator.Result.LowerThan -> {
                _numberTwoErrorLive.postValue(
                    app.getString(R.string.input_field_error_lower_than, result.minimunValue)
                )
            }
        }
        return false
    }

    fun validateWordOne(input: String?): Boolean {
        when (val result = wordValidator.validate(input)) {
            is WordValidator.Result.Valid -> {
                wordOne = result.outputValue
                _wordOneErrorLive.postValue(null)
                return true
            }
            is WordValidator.Result.Empty -> {
                _wordOneErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
        }
        return false
    }

    fun validateWordTwo(input: String?): Boolean {
        when (val result = wordValidator.validate(input)) {
            is WordValidator.Result.Valid -> {
                wordTwo = result.outputValue
                _wordTwoErrorLive.postValue(null)
                return true
            }
            is WordValidator.Result.Empty -> {
                _wordTwoErrorLive.postValue(app.getString(R.string.input_field_error_required))
            }
        }
        return false
    }

    private fun computeReplacementText(): String {
        return app.getString(
            R.string.replace_the_numbers,
            numberOne, wordOne,
            numberTwo, wordTwo,
            numberOne, numberTwo, wordOne, wordTwo
        )
    }

    private fun computeLimitText(): String {
        return app.getString(R.string.prints_the_numbers, lowerLimit, upperLimit)
    }

    private fun computeOutputText(): String {
        return buildString {
            var flag = true
            for (i in lowerLimit..upperLimit) {
                if (i % numberOne == 0) {
                    append(wordOne)
                    flag = false
                }
                if (i % numberTwo == 0) {
                    append(wordTwo)
                    flag = false
                }
                if (flag) {
                    append(i)
                }
                appendLine()
                flag = true
            }
        }
    }

    fun countMatches(string: String, pattern: String): Int {
        val matcher = Pattern.compile(pattern).matcher(string)
        var count = 0
        while (matcher.find()) {
            count++
        }
        return count
    }
}