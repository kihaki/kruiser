package de.gg.kruiser.core

import java.util.UUID

actual fun generateBackstackItemId(): String = UUID.randomUUID().toString()