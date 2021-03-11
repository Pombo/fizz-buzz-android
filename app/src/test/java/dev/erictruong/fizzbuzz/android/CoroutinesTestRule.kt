package dev.erictruong.fizzbuzz.android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.CoroutineContext

/**
 * JUnit Rule using [TestCoroutineDispatcher].
 */
@ExperimentalCoroutinesApi
class CoroutinesTestRule(

    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    /**
     * Implémentation pour les tests du [CoroutineContextProvider].
     */
    @ExperimentalCoroutinesApi
    val testCoroutineContextProvider by lazy {
        object : CoroutineContextProvider {
            override val UI: CoroutineContext get() = testDispatcher
            override val IO: CoroutineContext get() = testDispatcher
            override val Default: CoroutineContext get() = testDispatcher
        }
    }
}

