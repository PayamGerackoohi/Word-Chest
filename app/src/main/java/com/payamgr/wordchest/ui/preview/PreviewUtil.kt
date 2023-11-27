package com.payamgr.wordchest.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

private const val SHORT_HEIGHT = 300

@Preview(showBackground = true, heightDp = SHORT_HEIGHT)
annotation class SinglePreview

/**
 * Use `PreviewScratch.kts` at `./scripts/` to generate this annotation
 */
@Preview(
    name = "01 - Material Theme - Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
)
@Preview(
    name = "02 - Material Theme - Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "03 - Red Wallpaper - Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
)
@Preview(
    name = "04 - Red Wallpaper - Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "05 - Green Wallpaper - Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
)
@Preview(
    name = "06 - Green Wallpaper - Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "07 - Blue Wallpaper - Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
)
@Preview(
    name = "08 - Blue Wallpaper - Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "09 - Yellow Wallpaper - Day",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
)
@Preview(
    name = "10 - Yellow Wallpaper - Night",
    showBackground = true,
    heightDp = SHORT_HEIGHT,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ThemesPreview

@Preview(name = "1-Phone-Small", showBackground = true, device = "id:Nexus One")
@Preview(name = "2-Phone-Large", showBackground = true, device = "id:pixel_7_pro")
@Preview(name = "3-Phone-Foldable", showBackground = true, device = "spec:width=673dp,height=841dp")
@Preview(name = "4-Tablet", showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
annotation class ScreensPreview
