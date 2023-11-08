package com.payamgr.wordchest.ui.page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.R
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.data.util.StringUtil
import com.payamgr.wordchest.data.util.pairPartition
import com.payamgr.wordchest.ui.preview.SinglePreview
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.ui.util.buzz
import com.payamgr.wordchest.ui.util.rememberBuzzState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@SinglePreview
//@ThemesPreview
//@ScreensPreview
@Composable
fun PagePreview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Mavericks.initialize(LocalContext.current)
            val state = HomeState(
                search = StringUtil.randomString(50),
                words = listOf(
                    Word("a", StringUtil.randomString(10)),
                    Word(StringUtil.randomString(50), StringUtil.randomString(50)),
                ),
            )
            Home.Page(
                viewModel = object : HomeViewModel(state) {
                    override fun onSearchChanged(search: String) {}
                }
            )
        }
    }
}

@SinglePreview
@Composable
fun AppHeaderPreview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                Home.AppHeader()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object Home {
    @Composable
    fun Page(viewModel: HomeViewModel = mavericksViewModel()) {
        val state by viewModel.collectAsState()
        Column {
            AppHeader()
            WordInput(state.search, viewModel::onSearchChanged)
            WordList(state.words)
        }
    }

    @Composable
    fun AppHeader() {
        val buzzState = rememberBuzzState()
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.word_search),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            },
            actions = {
                IconButton(
                    enabled = !buzzState.hasError,
                    onClick = { buzzState.hasError = true },
                    modifier = Modifier.buzz(buzzState)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Select Language",
                        tint = buzzState.color,
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = Color.Red,
            ),
        )
    }

    @Composable
    fun WordInput(search: String, onSearchChanged: (String) -> Unit) {
        Surface(color = MaterialTheme.colorScheme.primary) {
            TextField(
                value = search,
                onValueChange = onSearchChanged,
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Red,
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("word input")
            )
        }
    }

    @Composable
    fun WordList(list: List<Word>) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.testTag("word list")
        ) {
            items(list) {
                WordCard(it)
            }
        }
    }

    @Composable
    fun WordCard(word: Word) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {
                    contentDescription = "${word.value}: ${word.shortDescription}"
                }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    word.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    word.shortDescription,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
