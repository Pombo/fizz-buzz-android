package dev.erictruong.fizzbuzz.android.home.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * Unit tests of [WordValidator].
 */
class WordValidatorTest {

    @Test
    fun `should return result Empty given null input`() {
        // Given
        val validator = WordValidator()

        // When
        val result = validator.validate(null)

        // Then
        assertSame(WordValidator.Result.Empty, result)
    }

    @Test
    fun `should return result Empty given empty input`() {
        // Given
        val validator = WordValidator()

        // When
        val result = validator.validate("")

        // Then
        assertSame(WordValidator.Result.Empty, result)
    }

    @Test
    fun `should return result Empty given blank input`() {
        // Given
        val validator = WordValidator()

        // When
        val result = validator.validate(" ")

        // Then
        assertSame(WordValidator.Result.Empty, result)
    }

    @Test
    fun `should return result Valid given input`() {
        // Given
        val validator = WordValidator()

        // When
        val result = validator.validate("foo")

        // Then
        assertEquals(WordValidator.Result.Valid("foo"), result)
    }
}