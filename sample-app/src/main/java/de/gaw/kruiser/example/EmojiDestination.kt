package de.gaw.kruiser.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.gaw.kruiser.backstack.push
import de.gaw.kruiser.backstack.ui.rendering.LocalBackstackEntry
import de.gaw.kruiser.backstack.ui.util.LocalMutableBackstackState
import de.gaw.kruiser.backstack.ui.util.currentOrThrow
import de.gaw.kruiser.destination.AndroidDestination
import de.gaw.kruiser.destination.Preview
import de.gaw.kruiser.destination.Screen
import de.gaw.kruiser.example.viewmodel.LifecycleLoggingViewModel
import de.gaw.kruiser.ui.theme.KruiserSampleTheme
import kotlinx.parcelize.Parcelize
import java.util.UUID

val emojis = listOf(
    "\uD83D\uDE0E", // Sunglasses
    "\uD83D\uDD25", // Fire
    "\uD83E\uDD23", // Rofl
    "\uD83D\uDE1D", // Tongue
)

@Parcelize
data class EmojiDestination(val emoji: String) : AndroidDestination {
    override fun build(): Screen = object : Screen {
        @Composable
        override fun Content() = EmojiCard(emoji)
    }
}

class EmojiViewModel(private val emoji: String) : LifecycleLoggingViewModel() {
    override fun name(): String = "EmojiViewModel($emoji)"
}

@Composable
private fun EmojiCard(emoji: String) {
    val backstack = LocalMutableBackstackState.currentOrThrow

    @Suppress("UNUSED_VARIABLE")
    val viewModel = viewModel { EmojiViewModel(emoji) }
    Surface(
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = emoji, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.size(16.dp))
                val shortEntryId = LocalBackstackEntry.currentOrThrow.id.takeLast(5)
                Text(
                    text = "Id: $shortEntryId",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.size(16.dp))
                val randomHash = rememberSaveable {
                    UUID.randomUUID().toString().takeLast(5)
                }
                Text(text = "Saved: $randomHash", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    onClick = {
                        backstack.push(EmojiDestination(emojis.filterNot { it == emoji }.random()))
                    },
                ) {
                    Text("Next")
                }
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    onClick = { },
                ) {
                    Text("Show Bottom Sheet")
                }
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    onClick = {
                        backstack.push(NestedBackstackExampleDestination)
                    },
                ) {
                    Text("Push Stack")
                }
            }
        }
    }
}

@Preview
@Composable
private fun EmojiDestinationPreview() = KruiserSampleTheme {
    EmojiDestination(emojis.first()).Preview()
}