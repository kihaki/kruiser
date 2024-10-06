package de.gg.kruiser.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import de.gg.kruiser.core.BackstackState
import de.gg.kruiser.compose.BackstackStateEvent.Change
import de.gg.kruiser.compose.BackstackStateEvent.Grow
import de.gg.kruiser.compose.BackstackStateEvent.Shrink

/**
 * Last event that happened to the backstack, used to choose correct transitions for example.
 */
enum class BackstackStateEvent {
    Grow, Shrink, Change;
}

@Composable
fun BackstackState.collectLastEvent(): State<BackstackStateEvent> {
    var previousSize by rememberSaveable { mutableIntStateOf(items.value.size) }
    val currentItems by items.collectAsState()
    return remember {
        derivedStateOf {
            val currentSize = currentItems.size
            val sizeDiff = currentSize - previousSize
            val event = when {
                sizeDiff > 0 -> Grow
                sizeDiff < 0 -> Shrink
                else -> Change
            }
            previousSize = currentSize
            event
        }
    }
}