package dev.erictruong.fizzbuzz.android.data

import android.content.SharedPreferences
import dev.erictruong.fizzbuzz.android.data.model.Request

class RequestRepository(val preferences: SharedPreferences) {

    fun all(): List<Request> {
        return preferences.all
            .filter { it.key.startsWith(REQUEST_PREFIX) }
            .map { (key, count) ->
                val splits = key.split(SEPARATOR)
                Request(
                    splits[INDEX_LOWER_LIMIT].toInt(),
                    splits[INDEX_UPPER_LIMIT].toInt(),
                    splits[INDEX_NUMBER_ONE].toInt(),
                    splits[INDEX_NUMBER_TWO].toInt(),
                    splits[INDEX_WORD_ONE],
                    splits[INDEX_WORD_TWO],
                    splits[INDEX_WORD_ONE_HITS].toInt(),
                    splits[INDEX_WORD_TWO_HITS].toInt(),
                    splits[INDEX_BOTH_WORD_HITS].toInt(),
                    count as Int
                )
            }
    }

    fun addOrIncrementCount(request: Request) {
        val requestKey = getRequestKey(request)
        val requestCount = preferences.getInt(requestKey, 0)
        preferences.edit()
            .putInt(requestKey, requestCount + 1)
            .apply()
    }

    private fun getRequestKey(request: Request) = arrayOf(
        REQUEST_PREFIX,
        request.lowerLimit,
        request.upperLimit,
        request.numberOne,
        request.numberTwo,
        request.wordOne,
        request.wordTwo,
        request.wordOneHits,
        request.wordTwoHits,
        request.bothHits
    ).joinToString(SEPARATOR)

    companion object {

        private const val SEPARATOR = ":"
        private const val REQUEST_PREFIX = "request"

        const val INDEX_LOWER_LIMIT = 1
        const val INDEX_UPPER_LIMIT = 2
        const val INDEX_NUMBER_ONE = 3
        const val INDEX_NUMBER_TWO = 4
        const val INDEX_WORD_ONE = 5
        const val INDEX_WORD_TWO = 6
        const val INDEX_WORD_ONE_HITS = 7
        const val INDEX_WORD_TWO_HITS = 8
        const val INDEX_BOTH_WORD_HITS = 9
    }
}