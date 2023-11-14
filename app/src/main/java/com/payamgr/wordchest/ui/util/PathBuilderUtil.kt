package com.payamgr.wordchest.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp

object PathBuilderUtil {
    fun rect(): ContentDrawScope.() -> Path = {
        val w = size.width
        val h = size.height
        Path().apply {
            moveTo(0f, 0f)
            relativeLineTo(w, 0f)
            relativeLineTo(0f, h)
            relativeLineTo(-w, 0f)
            close()
        }
    }

    fun topRound(radius: Dp): ContentDrawScope.() -> Path = {
        val w = size.width
        val h = size.height
        Path().apply {
            val d = radius.toPx()
            moveTo(0f, d)
            arcTo(Rect(Offset(d, d), d), -180f, 90f, false)
            relativeLineTo(w - 2 * d, 0f)
            arcTo(Rect(Offset(w - d, d), d), -90f, 90f, false)
            relativeLineTo(0f, h - d)
            relativeLineTo(-w, 0f)
            close()
        }
    }
}
