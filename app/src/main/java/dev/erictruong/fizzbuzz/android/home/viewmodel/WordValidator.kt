package dev.erictruong.fizzbuzz.android.home.viewmodel

/**
 * Validator for input field of type 'word'.
 */
class WordValidator {

    fun validate(input: String?): Result {
        if (input.isNullOrBlank()) {
            return Result.Empty
        }
        val outputValue = input.toString().trim()
        return Result.Valid(outputValue)
    }

    sealed class Result {
        object Empty : Result()
        data class Valid(val outputValue: String) : Result()
    }
}

