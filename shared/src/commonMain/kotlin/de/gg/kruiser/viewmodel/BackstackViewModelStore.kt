package de.gg.kruiser.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import de.gg.kruiser.core.BackstackItemId

interface BackstackViewModelStore {
    operator fun get(item: BackstackItemId): ViewModelStoreOwner
    fun clear(item: BackstackItemId)
}

fun BackstackViewModelStore(): BackstackViewModelStore = DefaultBackstackViewModelStore

internal object DefaultBackstackViewModelStore : BackstackViewModelStore {
    private val viewModelStores = mutableMapOf<BackstackItemId, ViewModelStoreOwner>()

    override fun get(item: BackstackItemId): ViewModelStoreOwner =
        viewModelStores.getOrPut(item) { DefaultViewModelStoreOwner() }

    override fun clear(item: BackstackItemId) {
        viewModelStores.remove(item)?.viewModelStore?.clear()
    }
}

private class DefaultViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}