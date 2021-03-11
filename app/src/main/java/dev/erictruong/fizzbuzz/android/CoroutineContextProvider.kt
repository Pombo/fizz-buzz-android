package dev.erictruong.fizzbuzz.android

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {

    val UI: CoroutineContext

    val IO: CoroutineContext

    val Default: CoroutineContext
}

object CoroutineContexts : CoroutineContextProvider {
    override val UI get() = Dispatchers.Main
    override val IO get() = Dispatchers.IO
    override val Default get() = Dispatchers.Default
}