package arrow.android.binding.core.lang

internal fun <A, R> A.runBlocking(block: suspend A.() -> R): R =
    kotlinx.coroutines.runBlocking { block() }
