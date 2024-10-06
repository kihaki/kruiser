package de.gg.kruiser.core

import platform.Foundation.NSUUID

actual fun generateBackstackItemId(): String = NSUUID().UUIDString()