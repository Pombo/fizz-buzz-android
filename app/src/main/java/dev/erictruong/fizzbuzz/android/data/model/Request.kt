package dev.erictruong.fizzbuzz.android.data.model

data class Request(
    val lowerLimit: Int,
    val upperLimit: Int,
    val numberOne: Int,
    val numberTwo: Int,
    val wordOne: String,
    val wordTwo: String,
    val wordOneHits: Int,
    val wordTwoHits: Int,
    val bothHits: Int,
    val requestCount: Int
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (lowerLimit != other.lowerLimit) return false
        if (upperLimit != other.upperLimit) return false
        if (numberOne != other.numberOne) return false
        if (numberTwo != other.numberTwo) return false
        if (wordOne != other.wordOne) return false
        if (wordTwo != other.wordTwo) return false
        if (wordOneHits != other.wordOneHits) return false
        if (wordTwoHits != other.wordTwoHits) return false
        if (bothHits != other.bothHits) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lowerLimit
        result = 31 * result + upperLimit
        result = 31 * result + numberOne
        result = 31 * result + numberTwo
        result = 31 * result + wordOne.hashCode()
        result = 31 * result + wordTwo.hashCode()
        result = 31 * result + wordOneHits
        result = 31 * result + wordTwoHits
        result = 31 * result + bothHits
        return result
    }
}