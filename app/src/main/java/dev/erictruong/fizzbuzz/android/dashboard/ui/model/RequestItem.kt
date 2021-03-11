package dev.erictruong.fizzbuzz.android.dashboard.ui.model

data class RequestItem(
    val limits: CharSequence,
    val count: Int,
    val numberOne: Int,
    val numberTwo: Int,
    val wordOne: CharSequence,
    val wordOneHitsText: CharSequence,
    val wordOneHits: Int,
    val wordTwo: CharSequence,
    val wordTwoHitsText: CharSequence,
    val wordTwoHits: Int,
    val both: CharSequence,
    val bothHitsText: CharSequence,
    val bothHits: Int,
    val requestCount: Int
) {

    fun isSameItem(newItem: RequestItem): Boolean {
        if (limits != newItem.limits) return false
        if (numberOne != newItem.numberOne) return false
        if (numberTwo != newItem.numberTwo) return false
        if (wordOne != newItem.wordOne) return false
        if (wordTwo != newItem.wordTwo) return false
        return true
    }

}