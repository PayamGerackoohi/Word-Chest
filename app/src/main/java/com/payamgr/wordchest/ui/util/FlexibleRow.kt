package com.payamgr.wordchest.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payamgr.wordchest.ui.page.worddetail.WordDetail
import com.payamgr.wordchest.ui.preview.SinglePreview
import com.payamgr.wordchest.ui.theme.WordChestTheme
import java.lang.Integer.max
import kotlin.math.roundToInt

private data class D(val word: String, val pronunciation: String)

@SinglePreview
@Composable
fun FlexibleRow_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                Text("Row: Top")
                SampleView(
                    D("Word", "Pronunciation"), "1",
                    verticalAlignment = Alignment.Top,
                )
                Text("Row: Center")
                SampleView(
                    D("Word", "Pronunciation"), "1",
                    verticalAlignment = Alignment.CenterVertically,
                )
                Text("Row: Bottom")
                SampleView(
                    D("Word", "Pronunciation"), "1",
                    verticalAlignment = Alignment.Bottom,
                )
                Text("Exact Word")
                15.let { n -> SampleView(D("a".repeat(n), "b".repeat(n)), "2") }
                Text("Long Word: Single Line")
                16.let { n -> SampleView(D("a".repeat(n), "b".repeat(n)), "3") }
                Text("Long Word: Double Line")
                50.let { n -> SampleView(D("a".repeat(n), "b".repeat(n)), "4") }
            }
        }
    }
}

@Composable
private fun SampleView(
    data: D, index: String,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    FlexibleRow(
        verticalAlignment = verticalAlignment,
        horizontalSpacing = 8.dp,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(color = Color.Red)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = Color.Blue)
        ) {
            Text(
                data.word,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            WordDetail.IndexItem(
                index = index,
                modifier = Modifier.align(Alignment.Top)
            )
        }
        Text(
            "|${data.pronunciation}|",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .background(color = Color.Green)
        )
    }
}

@Composable
fun FlexibleRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val widths = measurables.map { it.maxIntrinsicWidth(constraints.minHeight) }
        val spacing = horizontalSpacing.toPx()
        val intSpacing = spacing.roundToInt()
        val totalWidth = widths.sum() + ((widths.size - 1) * spacing).roundToInt()
        if (totalWidth > constraints.maxWidth) {
            // column
            var maxWidth = 0
            var totalHeight = 0
            val placeables = measurables.map {
                it.measure(
                    constraints.copy(
                        minHeight = it.minIntrinsicHeight(constraints.maxWidth)
                    )
                ).apply {
                    maxWidth = max(maxWidth, width)
                    totalHeight += height
                }
            }
            layout(
                width = maxWidth,
                height = totalHeight,
            ) {
                var y = 0
                placeables.forEach {
                    it.place(x = 0, y = y)
                    y += it.height
                }
            }
        } else {
            // row
            var maxHeight = 0
            val placeables = measurables.mapIndexed { index, measurable ->
                maxHeight = max(maxHeight, measurable.minIntrinsicHeight(constraints.maxWidth))
                measurable.measure(
                    constraints.copy(
                        minWidth = widths[index]
                    )
                )
            }
            layout(
                width = totalWidth,
                height = maxHeight,
            ) {
//                verticalAlignment
                var x = 0
                placeables.forEach {
                    val y = when (verticalAlignment) {
                        Alignment.Top -> 0
                        Alignment.Bottom -> maxHeight - it.height
                        else -> (maxHeight - it.height) / 2
                    }
                    it.place(x = x, y = y)
                    x += it.width + intSpacing
                }
            }
        }
    }
}
