package com.abhi165.noober.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.abhi165.noober.isAndroid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.lifecycle.LocalLifecycleOwner
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@Composable
internal fun <T : R, R> StateFlow<T>.collectAsStateWithLifecycleOrCollectAsState(
    context: CoroutineContext = EmptyCoroutineContext,
): State<R> {
    if(isAndroid())
    return collectAsStateWithLifecycle(initial = this.value, context = context)
    return  collectAsState(context)
}

@Composable
internal fun <T : R, R> Flow<T>.collectAsStateWithLifecycleOrCollectAsState(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext,
): State<R> {
    if(isAndroid()) {
        val lifecycleOwner = checkNotNull(LocalLifecycleOwner.current)
        return collectAsStateWithLifecycle(
            initial = initial,
            lifecycle = lifecycleOwner.lifecycle,
            context = context,
        )
    }

    return collectAsState(initial)
}