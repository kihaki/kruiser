package de.gg.kruiser.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

typealias BackstackItems = List<BackstackItem>
typealias BackstackItemId = String
typealias BackstackStateId = String

interface BackstackState {
    val id: BackstackStateId
    val items: StateFlow<BackstackItems>
}

interface BackstackMutator {
    fun mutate(block: (BackstackItems) -> BackstackItems)
}

data class DefaultBackstackState(
    override val id: BackstackStateId = generateBackstackItemId(),
) : BackstackState, BackstackMutator {
    private val _items = MutableStateFlow(emptyList<BackstackItem>())
    override val items: StateFlow<BackstackItems> = _items.asStateFlow()

    override fun mutate(block: (BackstackItems) -> BackstackItems) =
        _items.update(block)
}

fun BackstackItem(
    destination: Destination,
    id: BackstackItemId = generateBackstackStateId(),
) = DefaultBackstackItem(
    id = id,
    destination = destination,
)

fun listOfBackstackItems(vararg destinations: Destination) =
    destinations.toList().map { destination -> BackstackItem(destination = destination) }

fun BackstackMutator.push(destination: Destination) = push(BackstackItem(destination = destination))
fun BackstackMutator.push(item: BackstackItem) = mutate { it + item }

fun BackstackMutator.pop(count: Int = 1) = mutate { it.dropLast(count) }