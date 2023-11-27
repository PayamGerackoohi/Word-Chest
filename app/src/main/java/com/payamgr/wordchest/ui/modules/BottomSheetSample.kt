package com.payamgr.wordchest.ui.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.payamgr.wordchest.ui.preview.SinglePreview
import kotlinx.coroutines.launch

@SinglePreview
@Composable
private fun BottomSheetSample_Preview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
//            BottomSheetSample()
            BottomSheetScaffoldSample()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetSample() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    Column {
        Button(onClick = { showBottomSheet = true }) {
            Text(text = "open")
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) showBottomSheet = false
                            }
                        }
                    ) {
                        Text("Hide")
                    }
                    repeat(10) { Text("$it", modifier = Modifier.padding(16.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffoldSample() {
    val state = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden,
        )
    )
    val scope = rememberCoroutineScope()
    var firstItemHeight by remember { mutableIntStateOf(0) }
    var sheetPeekHeight by remember { mutableIntStateOf(0) }
    BottomSheetScaffold(
        scaffoldState = state,
        sheetShadowElevation = 4.dp,
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight - 8.dp + LocalDensity.current.run { sheetPeekHeight.toDp() },
        sheetContent = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(color = Color.Red)
                        .onGloballyPositioned {
                            firstItemHeight = it.size.height
                        }) {
                    Text("sheet content")
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { scope.launch { state.bottomSheetState.hide() } }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
                Text("sheet content")
            }
        }) {
        Column {
            Button(onClick = {
                scope.launch {
                    sheetPeekHeight = firstItemHeight
                    state.bottomSheetState.openIfHidden()
                }
            }) {
                Text("Open")
            }
            Text("State: ${state.bottomSheetState.currentValue}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun SheetState.openIfHidden() {
    if (currentValue == SheetValue.Hidden) partialExpand()
}
