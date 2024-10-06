package de.gg.kruiser.lifecycle

import de.gg.kruiser.core.BackstackStateId

fun OrphansTrackerHolder(): OrphansTrackerHolder = DefaultOrphansTrackerHolder

interface OrphansTrackerHolder {
    operator fun set(id: BackstackStateId, tracker: OrphansTracker)
    operator fun get(id: BackstackStateId): OrphansTracker?
    fun getOrPut(id: BackstackStateId, block: () -> OrphansTracker): OrphansTracker
}

private object DefaultOrphansTrackerHolder : OrphansTrackerHolder {
    private val trackers = mutableMapOf<BackstackStateId, OrphansTracker>()
    override fun getOrPut(id: BackstackStateId, block: () -> OrphansTracker): OrphansTracker =
        trackers.getOrPut(id, block)

    override fun set(id: BackstackStateId, tracker: OrphansTracker) = trackers.set(id, tracker)
    override fun get(id: BackstackStateId): OrphansTracker? = trackers[id]
}