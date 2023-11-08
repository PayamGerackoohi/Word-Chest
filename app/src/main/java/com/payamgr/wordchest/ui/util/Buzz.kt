package com.payamgr.wordchest.ui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payamgr.wordchest.data.util.pairPartition

class BuzzState(
    hasError: MutableState<Boolean>,
    color: State<Color>,
    offset: State<Dp>,
) {
    var hasError by hasError
    val color by color
    val offset by offset
}

class BuzzColors(
    val normal: Color? = null,
    val error: Color = Color.Red,
)

@Composable
fun rememberBuzzState(
    deviation: Float = 4f,
    durationMs: Int = 300,
    cycles: Int = 2,
    colors: BuzzColors = BuzzColors(),
): BuzzState {
    val hasError = remember { mutableStateOf(false) }
    val buzzColor = animateColorAsState(
        label = "Buzz Color",
        targetValue = if (hasError.value)
            colors.error
        else
            colors.normal ?: MaterialTheme.colorScheme.onPrimary
    )
    val buzzOffset = animateDpAsState(
        label = "Buzz Offset",
        targetValue = if (hasError.value) -deviation.dp else 0.dp,
        animationSpec = if (hasError.value)
            keyframes {
                durationMillis = durationMs
                durationMillis.pairPartition(cycles).forEach { (first, second) ->
                    deviation.dp at first
                    -deviation.dp at second
                }
            }
        else
            spring(visibilityThreshold = Dp.VisibilityThreshold),
        finishedListener = { if (hasError.value) hasError.value = false }
    )
    return remember(hasError) { BuzzState(hasError, buzzColor, buzzOffset) }
}

fun Modifier.buzz(buzzState: BuzzState) = offset(x = buzzState.offset)
