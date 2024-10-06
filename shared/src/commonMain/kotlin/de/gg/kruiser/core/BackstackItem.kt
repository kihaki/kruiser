package de.gg.kruiser.core

import androidx.compose.runtime.Immutable

@Immutable
interface BackstackItem {
    val id: BackstackItemId
    val destination: Destination
}

data class DefaultBackstackItem(
    override val id: BackstackItemId = generateBackstackItemId(),
    override val destination: Destination,
) : BackstackItem