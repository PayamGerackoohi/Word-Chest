package com.payamgr.wordchest.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

//private const val SHORT_HEIGHT = 75
private const val SHORT_HEIGHT = 300

@Preview(showBackground = true, heightDp = SHORT_HEIGHT)
annotation class SinglePreview

@Preview(
    name = "1-Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
)
@Preview(
    name = "2-Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "3-Wallpaper",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Preview(
    name = "4-Wallpaper-Dark",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class ThemesPreview

@Preview(name = "1-Phone-Small", showBackground = true, device = "id:Nexus One")
@Preview(name = "2-Phone-Large", showBackground = true, device = "id:pixel_7_pro")
@Preview(name = "3-Phone-Foldable", showBackground = true, device = "spec:width=673dp,height=841dp")
@Preview(name = "4-Tablet", showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
annotation class ScreensPreview
