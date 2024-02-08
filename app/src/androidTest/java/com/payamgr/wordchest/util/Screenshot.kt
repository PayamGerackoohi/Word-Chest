package com.payamgr.wordchest.util

import android.graphics.Bitmap.CompressFormat

@Suppress("EnumEntryName")
enum class Screenshot {
    Splashscreen,
    Home,
    WordDetail,
    WordDetail_SearchSection,
    WordDetail_History,
}

fun Screenshot.take(
    fileExtensions: String = "webp",
    compressFormat: CompressFormat = CompressFormat.WEBP_LOSSY,
    qualityPercentage: Int = 100,
    minDimension: Int = 400,
) = takeScreenshot(
    this,
    fileExtensions,
    compressFormat,
    qualityPercentage,
    minDimension,
)
