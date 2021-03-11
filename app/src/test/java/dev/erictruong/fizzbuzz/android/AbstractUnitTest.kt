package dev.erictruong.fizzbuzz.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

abstract class AbstractUnitTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val coroutinesTestRule = CoroutinesTestRule()

}

@ExperimentalCoroutinesApi
fun AbstractUnitTest.runCoroutineTest(block: suspend TestCoroutineScope.() -> Unit) {
    coroutinesTestRule.testDispatcher.runBlockingTest(block)
}