package com.payamgr.wordchest.ui.modules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payamgr.wordchest.ui.preview.ThemesPreview
import com.payamgr.wordchest.ui.theme.WordChestTheme

@ThemesPreview
@Composable
fun HistoryList_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val list = List(10) { it.toString() } + listOf("a\nb", "a".repeat(20))
            var expand by remember { mutableStateOf(true) }
            Column(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { expand = !expand },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                    )
                }
                History.List(
                    expand = expand,
                    list = list, 2,
                    onItemClicked = {},
                    modifier = Modifier
                        .fillMaxWidth(fraction = .5f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
object History {
    private const val ANIMATION_DURATION = 200
    private const val EXIT_ANIMATION_DURATION = 500
    private const val ANIMATION_DELAY = 20

    enum class ItemState { Previous, Current, Next }

    @Composable
    fun List(
        expand: Boolean,
        list: List<String>,
        currentLayer: Int,
        onItemClicked: (layer: Int) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 4.dp),
            reverseLayout = true,
            modifier = modifier.testTag("history list")
        ) {
            itemsIndexed(list, { _, it -> it }) { index, it ->
                val animationDelay = index * ANIMATION_DELAY
                AnimatedVisibility(
                    visible = expand,
                    enter = slideInHorizontally(
                        initialOffsetX = { w -> -w },
                        animationSpec = tween(ANIMATION_DURATION, animationDelay),
                    ) + fadeIn(animationSpec = tween(ANIMATION_DURATION, animationDelay)),
                    exit = slideOutHorizontally(
                        targetOffsetX = { w -> -w },
                        animationSpec = tween(EXIT_ANIMATION_DURATION)
                    ) + fadeOut(animationSpec = tween(EXIT_ANIMATION_DURATION)),
                    modifier = Modifier.animateItemPlacement()
                ) {
                    Item(
                        data = it,
                        state = stateOf(index, list.size - 1 - currentLayer),
                        onClick = { onItemClicked(list.size - 1 - index) },
                    )
                }
            }
        }
    }

    private fun stateOf(index: Int, currentIndex: Int) = when {
        index > currentIndex -> ItemState.Previous
        index < currentIndex -> ItemState.Next
        else -> ItemState.Current
    }

    private data class ItemColor(val container: Color, val text: Color)
    private data class ItemProperties(val color: ItemColor, val elevation: Dp)

    @Composable
    fun Item(data: String, state: ItemState, onClick: () -> Unit, modifier: Modifier = Modifier) {
        val properties = MaterialTheme.colorScheme.run {
            when (state) {
                ItemState.Next -> ItemProperties(ItemColor(tertiary, onTertiary), 4.dp)
                ItemState.Current -> ItemProperties(ItemColor(primary, onPrimary), 8.dp)
                ItemState.Previous -> ItemProperties(ItemColor(outline, surface), 2.dp)
            }
        }
        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.elevatedButtonColors(containerColor = properties.color.container),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = properties.elevation,
                pressedElevation = 0.dp,
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .semantics { contentDescription = "$state: $data" }
        ) {
            Text(
                text = data,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = properties.color.text,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
