package dev.erictruong.fizzbuzz.android.home.viewmodel

/**
 * Validator for input field of type 'number'.
 */
class NumberValidator(var minimunValue: Int = 0) {

    fun validate(input: String?): Result {
        if (input.isNullOrBlank()) {
            return Result.Empty
        }
        val outputValue = input.toString().toIntOrNull()
        if (outputValue == null) {
            return Result.NotInteger
        }
        if (outputValue < minimunValue) {
            return Result.LowerThan(minimunValue)
        }
        return Result.Valid(outputValue)
    }

    sealed class Result {
        object Empty : Result()
        object NotInteger : Result()
        data class LowerThan(val minimunValue: Int) : Result()
        data class Valid(val outputValue: Int) : Result()
    }
}

