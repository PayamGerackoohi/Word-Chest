package com.payamgr.wordchest.ui.modules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@ThemesPreview
@Composable
fun HistoryList_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//            val list = List(10) { it.toString() } + listOf("a\nb", "a".repeat(20))
//            var list by remember { mutableStateOf(List(3) { it.toString() } + listOf("a\nb", "a".repeat(20))) }
            var list by remember { mutableStateOf(listOf<String>()) }
            var expand by remember { mutableStateOf(true) }
            Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
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
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { list = list + "Item ${list.size}" },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add",
                    )
                }
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { list = list.dropLast(1) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "remove",
                    )
                }
                History.List(
                    expand = expand,
                    list = list.reversed(),
                    currentLayer = 2,
                    onItemClicked = {},
                    modifier = Modifier
                        .fillMaxWidth(fraction = .5f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

object History {
    private const val ANIMATION_DELAY = 100
    private const val ENTER_ANIMATION_DURATION = 300
    private const val EXIT_ANIMATION_DURATION = 500

    enum class ItemState { Previous, Current, Next }

    @Composable
    fun List(
        expand: Boolean,
        list: List<String>,
        currentLayer: Int,
        onItemClicked: (layer: Int) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val animationDelayPerUnit = list.rememberAnimationDelayPerUnit()
        var expand2 by remember { mutableStateOf(false) }
        LaunchedEffect(expand) {
            delay(10)
            expand2 = expand
        }
        AnimatedVisibility(
            visible = expand,
            enter = fadeIn(animationSpec = tween(1)),
            exit = fadeOut(tween(delayMillis = ANIMATION_DELAY + EXIT_ANIMATION_DURATION)),
            modifier = modifier.animateContentSize(tween(ENTER_ANIMATION_DURATION))
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
                reverseLayout = true,
                modifier = Modifier.testTag("history list")
            ) {
                itemsIndexed(list, { _, it -> it }) { index, it ->
                    val animationDelay = index * animationDelayPerUnit
                    AnimatedVisibility(
                        visible = expand2,
                        enter = slideInHorizontally(
                            initialOffsetX = { w -> -w },
                            animationSpec = tween(ENTER_ANIMATION_DURATION, animationDelay),
                        ) + fadeIn(animationSpec = tween(ENTER_ANIMATION_DURATION, animationDelay)),
                        exit = slideOutHorizontally(
                            targetOffsetX = { w -> -w },
                            animationSpec = tween(EXIT_ANIMATION_DURATION, animationDelay),
                        ) + fadeOut(animationSpec = tween(EXIT_ANIMATION_DURATION, animationDelay)),
//                        modifier = Modifier.animateItemPlacement()
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

    @Composable
    private fun List<String>.rememberAnimationDelayPerUnit() = remember(size) {
        size.let { if (it < 1) 1 else it - 1 }.let { ANIMATION_DELAY / it }
    }
}
