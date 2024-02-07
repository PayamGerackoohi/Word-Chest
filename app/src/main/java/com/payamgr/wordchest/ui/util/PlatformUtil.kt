@file:OptIn(ExperimentalMaterial3Api::class)

package com.payamgr.wordchest.ui.util

import android.os.Build
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

suspend fun BottomSheetScaffoldState.openIfHidden() = bottomSheetState.apply {
    if (currentValue == SheetValue.Hidden) partialExpand()
}

val BottomSheetScaffoldState.isHidden get() = bottomSheetState.currentValue == SheetValue.Hidden

val BottomSheetScaffoldState.isGoingToHide get() = bottomSheetState.targetValue == SheetValue.Hidden

suspend fun BottomSheetScaffoldState.hide() = bottomSheetState.hide()

@Composable
fun Density.bottomSheetPeek(target: Int) = BottomSheetDefaults.SheetPeekHeight - 8.dp + target.toDp()

@Composable
fun rememberHidableBottomSheetScaffoldState() = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden,
    )
)

val isAndroidSdk31plus = Build.VERSION.SDK_INT >= 31
