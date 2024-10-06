package de.gg.kruiser.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import de.gg.kruiser.core.BackstackItem
import de.gg.kruiser.core.BackstackMutator
import de.gg.kruiser.core.BackstackState

val LocalBackstackState =
    staticCompositionLocalOf<BackstackState> { error("No BackstackState provided.") }

val LocalBackstackMutator =
    staticCompositionLocalOf<BackstackMutator> { error("No BackstackMutator provided.") }

val LocalBackstackItem =
    compositionLocalOf<BackstackItem?> { null }