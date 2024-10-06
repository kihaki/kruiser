package de.gg.kruiser.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import de.gg.kruiser.core.BackstackState
import de.gg.kruiser.compose.BackstackStateEvent.Change
import de.gg.kruiser.compose.BackstackStateEvent.Grow
import de.gg.kruiser.compose.BackstackStateEvent.Shrink
import de.gg.kruiser.lifecycle.compose.OnRemoveOrphansEffect
import de.gg.kruiser.lifecycle.compose.rememberOrphansTracker
import de.gg.kruiser.viewmodel.BackstackViewModelStore

@Composable
fun AnimatedStack(
    modifier: Modifier = Modifier,
    state: BackstackState = LocalBackstackState.current,
) {
    val orphansTracker = rememberOrphansTracker(state)

    val items by state.items.collectAsState()
    val saveableStateHolder = rememberSaveableStateHolder()

    val stateEvent by state.collectLastEvent()
    val backstackViewModelStore = BackstackViewModelStore()
    AnimatedContent(
        modifier = modifier,
        targetState = items.lastOrNull(),
        transitionSpec = {
            when (stateEvent) {
                Grow, Change -> (slideInHorizontally { it } togetherWith slideOutHorizontally { -it / 3 }).apply {
                    targetContentZIndex = items.size.toFloat()
                }

                Shrink -> (slideInHorizontally { -it / 3 } togetherWith slideOutHorizontally { it }).apply {
                    targetContentZIndex = (items.size - 1).toFloat()
                }
            }
        }
    ) { currentItem ->
        currentItem?.let {
            orphansTracker.track(it.id)

            CompositionLocalProvider(
                LocalViewModelStoreOwner provides backstackViewModelStore[it.id],
                LocalBackstackItem provides it,
            ) {
                key(it.id) {
                    val renderer = remember(it.destination) { it.destination.renderer() }
                    saveableStateHolder.SaveableStateProvider(it.id) { renderer.Draw() }
                }
            }

            OnRemoveOrphansEffect(orphansTracker) { orphan ->
                backstackViewModelStore.clear(orphan)
                saveableStateHolder.removeState(orphan)
            }
        }
    }

    OnRemoveOrphansEffect(orphansTracker) { orphan ->
        backstackViewModelStore.clear(orphan)
        saveableStateHolder.removeState(orphan)
    }
}