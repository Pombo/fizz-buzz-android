package dev.erictruong.fizzbuzz.android.home.viewmodel

import android.app.Application
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import dev.erictruong.fizzbuzz.android.AbstractUnitTest
import dev.erictruong.fizzbuzz.android.R
import dev.erictruong.fizzbuzz.android.data.RequestRepository
import dev.erictruong.fizzbuzz.android.data.model.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit test 0f [HomeViewModel]
 */
@ExperimentalCoroutinesApi
class HomeViewModelTest : AbstractUnitTest() {

    private val mockApp: Application = mock()
    private val mockRequestRepository: RequestRepository = mock()

    private val viewModel = HomeViewModel(
        coroutinesTestRule.testCoroutineContextProvider,
        mockApp,
        mockRequestRepository
    )

    @Before
    fun setUp() {
        given(mockApp.getString(R.string.input_field_error_required))
            .willReturn("input_field_error_required")

        given(mockApp.getString(R.string.input_field_error_not_integer))
            .willReturn("input_field_error_not_integer")

        given(mockApp.getString(R.string.input_field_error_lower_than, 1))
            .willReturn("input_field_error_lower_than 1")
    }

    @Test
    fun `should return initial values`() {
        assertEquals(viewModel.lowerLimit, 1)
        assertEquals(viewModel.upperLimit, 100)
        assertEquals(viewModel.numberOne, 3)
        assertEquals(viewModel.numberTwo, 5)
        assertEquals(viewModel.wordOne, "Fizz")
        assertEquals(viewModel.wordTwo, "Buzz")
    }

    @Test
    fun `should compute texts given valid form`() {
        // Given
        val lowerLimit = 1
        val upperLimit = 15
        val numberOne = 3
        val numberTwo = 5
        val wordOne = "Fizz"
        val wordTwo = "Buzz"
        given(mockApp.getString(R.string.prints_the_numbers, lowerLimit, upperLimit))
            .willReturn("prints_the_numbers")
        given(
            mockApp.getString(
                R.string.replace_the_numbers,
                numberOne, wordOne,
                numberTwo, wordTwo,
                numberOne, numberTwo, wordOne, wordTwo
            )
        ).willReturn("replace_the_numbers")
        val mockLimitTextObserver = mock<Observer<CharSequence?>>()
        val mockReplacementTextObserver = mock<Observer<CharSequence?>>()
        val mockTextObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.limitTextLive.observeForever(mockLimitTextObserver)
        viewModel.replacementTextLive.observeForever(mockReplacementTextObserver)
        viewModel.textLive.observeForever(mockTextObserver)
        viewModel.submitForm(
            lowerLimit.toString(),
            upperLimit.toString(),
            numberOne.toString(),
            numberTwo.toString(),
            wordOne,
            wordTwo
        )

        // Then
        then(mockLimitTextObserver).should().onChanged(
            check { assertEquals("prints_the_numbers", it) }
        )
        then(mockReplacementTextObserver).should().onChanged(
            check { assertEquals("replace_the_numbers", it) }
        )
        then(mockTextObserver).should().onChanged(
            check { assertEquals("1\n2\nFizz\n4\nBuzz\nFizz\n7\n8\nFizz\nBuzz\n11\nFizz\n13\n14\nFizzBuzz\n", it) }
        )
        then(mockRequestRepository).should().addOrIncrementCount(
            Request(
                lowerLimit,
                upperLimit,
                numberOne,
                numberTwo,
                wordOne,
                wordTwo,
                5,
                3,
                1,
                1
            )
        )
    }

    @Test
    fun `should not compute texts given valid form`() {
        // Given
        val mockLimitTextObserver = mock<Observer<CharSequence?>>()
        val mockReplacementTextObserver = mock<Observer<CharSequence?>>()
        val mockTextObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.limitTextLive.observeForever(mockLimitTextObserver)
        viewModel.replacementTextLive.observeForever(mockReplacementTextObserver)
        viewModel.textLive.observeForever(mockTextObserver)
        viewModel.submitForm(
            null,
            null,
            null,
            null,
            null,
            null
        )

        // Then
        then(mockLimitTextObserver).should(never()).onChanged(any())
        then(mockReplacementTextObserver).should(never()).onChanged(any())
        then(mockTextObserver).should(never()).onChanged(any())
        then(mockRequestRepository).should(never()).addOrIncrementCount(any())
    }

    @Test
    fun `should validate LowerLimit given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.lowerLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateLowerLimit("1")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate LowerLimit given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.lowerLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateLowerLimit("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should validate LowerLimit given non integer input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.lowerLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateLowerLimit("text")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_not_integer", it) }
        )
    }

    @Test
    fun `should validate LowerLimit given lower than minimum input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.lowerLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateLowerLimit("0")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_lower_than 1", it) }
        )
    }

    @Test
    fun `should validate UpperLimit given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.upperLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateUpperLimit("1")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate UpperLimit given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.upperLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateUpperLimit("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should validate UpperLimit given non integer input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.upperLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateUpperLimit("text")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_not_integer", it) }
        )
    }

    @Test
    fun `should validate UpperLimit given lower than minimum input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.upperLimitErrorLive.observeForever(mockObserver)
        val result = viewModel.validateUpperLimit("0")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_lower_than 1", it) }
        )
    }

    @Test
    fun `should validate NumberOne given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberOne("1")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate NumberOne given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberOne("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should validate NumberOne given non integer input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberOne("text")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_not_integer", it) }
        )
    }

    @Test
    fun `should validate NumberOne given lower than minimum input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberOne("0")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_lower_than 1", it) }
        )
    }

    @Test
    fun `should validate NumberTwo given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberTwo("1")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate NumberTwo given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberTwo("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should validate NumberTwo given non integer input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberTwo("text")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_not_integer", it) }
        )
    }

    @Test
    fun `should validate NumberTwo given lower than minimum input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.numberTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateNumberTwo("0")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_lower_than 1", it) }
        )
    }

    @Test
    fun `should validate WordOne given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.wordOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateWordOne("word")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate WordOne given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.wordOneErrorLive.observeForever(mockObserver)
        val result = viewModel.validateWordOne("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should validate WordTwo given valid input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.wordTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateWordTwo("word")

        // Then
        assertTrue(result)
        then(mockObserver).should().onChanged(isNull())
    }

    @Test
    fun `should validate WordTwo given empty input`() {
        // Given
        val mockObserver = mock<Observer<CharSequence?>>()

        // When
        viewModel.wordTwoErrorLive.observeForever(mockObserver)
        val result = viewModel.validateWordTwo("")

        // Then
        assertFalse(result)
        then(mockObserver).should().onChanged(
            check { assertEquals("input_field_error_required", it) }
        )
    }

    @Test
    fun `should count matched`() {
        // Given
        val string = "1No3No4NoYes5Yes"

        // When / Then
        assertEquals(2, viewModel.countMatches(string, "Yes"))
        assertEquals(3, viewModel.countMatches(string, "No"))
        assertEquals(1, viewModel.countMatches(string, "NoYes"))
    }
}