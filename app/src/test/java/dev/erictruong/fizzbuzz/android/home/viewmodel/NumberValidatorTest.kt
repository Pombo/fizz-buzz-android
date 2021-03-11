package dev.erictruong.fizzbuzz.android.home.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * Unit tests of [NumberValidator].
 */
class NumberValidatorTest {

    @Test
    fun `should return result Empty given null input`() {
        // Given
        val validator = NumberValidator()

        // When
        val result = validator.validate(null)

        // Then
        assertSame(NumberValidator.Result.Empty, result)
    }

    @Test
    fun `should return result Empty given empty input`() {
        // Given
        val validator = NumberValidator()

        // When
        val result = validator.validate("")

        // Then
        assertSame(NumberValidator.Result.Empty, result)
    }

    @Test
    fun `should return result Empty given blank input`() {
        // Given
        val validator = NumberValidator()

        // When
        val result = validator.validate(" ")

        // Then
        assertSame(NumberValidator.Result.Empty, result)
    }

    @Test
    fun `should return result NotInteger given text input`() {
        // Given
        val validator = NumberValidator()

        // When
        val result = validator.validate("text")

        // Then
        assertSame(NumberValidator.Result.NotInteger, result)
    }

    @Test
    fun `should return result LowerThan given minimum 10`() {
        // Given
        val validator = NumberValidator(10)

        // When
        val result = validator.validate("5")

        // Then
        assertEquals(NumberValidator.Result.LowerThan(10), result)
    }

    @Test
    fun `should return result Valid given input`() {
        // Given
        val validator = NumberValidator()

        // When
        val result = validator.validate("42")

        // Then
        assertEquals(NumberValidator.Result.Valid(42), result)
    }

    @Test
    fun `should return minimum value default`() {
        // Given
        val validator = NumberValidator()

        // Then
        assertEquals(0, validator.minimunValue)
    }

    @Test
    fun `should set minimum value constructor`() {
        // Given
        val validator = NumberValidator(5)

        // Then
        assertEquals(5, validator.minimunValue)
    }

    @Test
    fun `should set minimum value setter`() {
        // Given
        val validator = NumberValidator()
        validator.minimunValue = 4

        // Then
        assertEquals(4, validator.minimunValue)
    }
}