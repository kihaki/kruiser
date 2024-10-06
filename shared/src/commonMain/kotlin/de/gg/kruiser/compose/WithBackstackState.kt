package de.gg.kruiser.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import de.gg.kruiser.core.BackstackMutator
import de.gg.kruiser.core.BackstackState

@Composable
fun WithBackstackState(
    state: BackstackState,
    mutator: BackstackMutator,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalBackstackState provides state,
        LocalBackstackMutator provides mutator,
        content = content,
    )
}
