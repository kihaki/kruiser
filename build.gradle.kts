plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.compose.plugin) apply false
    alias(libs.plugins.compose.compiler) apply false
}
