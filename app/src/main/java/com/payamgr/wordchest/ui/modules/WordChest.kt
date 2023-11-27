package com.payamgr.wordchest.ui.modules

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

object WordChest {
    @Composable
    fun WordInput(
        search: String,
        onSearchChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors: TextFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = Color.Red,
        ),
    ) {
        val focusRequester = remember { FocusRequester() }
        TextField(
            value = search,
            onValueChange = onSearchChanged,
            textStyle = MaterialTheme.typography.titleMedium,
            singleLine = true,
            colors = colors,
            shape = shape,
            modifier = modifier
                .focusRequester(focusRequester)
                .testTag("word input")
        )
    }
}
