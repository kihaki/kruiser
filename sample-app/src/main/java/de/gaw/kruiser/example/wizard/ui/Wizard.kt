package de.gaw.kruiser.example.wizard.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import de.gaw.kruiser.backstack.core.MutableBackstackState
import de.gaw.kruiser.backstack.pop
import de.gaw.kruiser.backstack.push
import de.gaw.kruiser.backstack.ui.rendering.LocalBackstackEntry
import de.gaw.kruiser.backstack.ui.util.LocalMutableBackstackState
import de.gaw.kruiser.backstack.ui.util.collectDerivedEntries
import de.gaw.kruiser.backstack.ui.util.currentOrThrow
import de.gaw.kruiser.backstack.util.filterDestinations
import de.gaw.kruiser.example.wizard.destination.WizardAbortWarningDialogDestination
import de.gaw.kruiser.example.wizard.destination.exampleWizardDestinations
import de.gaw.kruiser.example.wizard.popWizard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wizard(content: @Composable (PaddingValues) -> Unit) {
    val backstackState = LocalMutableBackstackState.currentOrThrow
    val entry = LocalBackstackEntry.currentOrThrow

    val wizardStateHolder = remember { WizardStateHolder() }
    val wizardState = wizardStateHolder.wizardState

    fun MutableBackstackState.showAbortWarningDialog() {
        push(WizardAbortWarningDialogDestination)
    }

    CompositionLocalProvider(LocalWizardStateHolder provides wizardStateHolder) {
        Scaffold(
            topBar = {
                Surface(shadowElevation = 2.dp) {
                    TopAppBar(
                        title = {
                            Column {
                                Text(wizardState.title)
                                val progress by animateFloatAsState(
                                    targetValue = wizardState.progress,
                                    label = "wizard-progress",
                                )
                                LinearProgressIndicator(progress = { progress })
                            }
                        },
                        actions = {
                            IconButton(onClick = backstackState::showAbortWarningDialog) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close Wizard"
                                )
                            }
                        },
                    )
                }
            },
            content = content,
            bottomBar = {
                Surface(
                    modifier = Modifier.imePadding(),
                    shadowElevation = 2.dp,
                ) {
                    BottomAppBar {
                        val wizardPages by backstackState.collectDerivedEntries {
                            filterDestinations { it in exampleWizardDestinations }
                        }
                        val backEnabled = wizardPages.size > 1
                        val pageCount = exampleWizardDestinations.size
                        ListItem(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    enabled = backEnabled,
                                    onClick = backstackState::pop,
                                ),
                            headlineContent = {
                                val textAlpha by animateFloatAsState(
                                    targetValue = if (backEnabled) 1f else .5f,
                                    label = "back-alpha",
                                )
                                Text(modifier = Modifier.alpha(textAlpha), text = "Back")
                            },
                        )
                        ListItem(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    onClick = {
                                        when {
                                            wizardPages.size < pageCount -> backstackState.push(
                                                exampleWizardDestinations[exampleWizardDestinations.indexOf(entry.destination) + 1]
                                            )

                                            else -> backstackState.popWizard()
                                        }
                                    },
                                ),
                            headlineContent = {
                                AnimatedContent(
                                    wizardPages.size < pageCount,
                                    transitionSpec = {
                                        slideInVertically { it } togetherWith
                                                slideOutVertically { -it }
                                    },
                                    label = "next-button-label-transition",
                                ) { isFinish ->
                                    Row {
                                        when (isFinish) {
                                            true -> {
                                                Text("Next")
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(
                                                    Icons.AutoMirrored.Filled.ArrowForward,
                                                    "next-icon"
                                                )
                                            }

                                            else -> {
                                                Text("Finish")
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(Icons.Filled.Done, "done-icon")
                                            }
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
            }
        )
    }
}