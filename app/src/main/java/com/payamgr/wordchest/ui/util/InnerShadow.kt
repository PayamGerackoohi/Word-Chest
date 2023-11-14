package com.payamgr.wordchest.ui.util

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.innerShadow(
    color: Color = Color.Black,
    blur: Dp = 2.dp,
    spread: Dp = 2.dp,
    offset: Offset = Offset.Zero,
    pathBuilder: ContentDrawScope.() -> Path = PathBuilderUtil.rect(),
) = drawWithContent {
    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()
    val path = pathBuilder()

    drawIntoCanvas { canvas ->
        paint.color = color
        paint.isAntiAlias = true
        canvas.saveLayer(rect, paint)
        canvas.drawPath(path, paint)

        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        blur.toPx().let {
            if (it > 0)
                frameworkPaint.maskFilter = BlurMaskFilter(it, BlurMaskFilter.Blur.NORMAL)
        }
        paint.color = Color.Black
        val r = spread.toPx() / 2
        withTransform({
            scale(
                size.width relativeSubtract r,
                size.height relativeSubtract r,
            )
            translate(offset.x, offset.y)
        }) { canvas.drawPath(path, paint) }
    }
}

private infix fun Float.relativeSubtract(r: Float) = (this - r) / this
