package com.payamgr.wordchest.ui.page.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.R
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.data.util.StringUtil
import com.payamgr.wordchest.ui.modules.WordChest
import com.payamgr.wordchest.ui.preview.SinglePreview
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.ui.util.PathBuilderUtil
import com.payamgr.wordchest.ui.util.buzz
import com.payamgr.wordchest.ui.util.innerShadow
import com.payamgr.wordchest.ui.util.rememberBuzzState
import kotlinx.coroutines.launch

@SinglePreview
@Composable
fun HomePagePreview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Mavericks.initialize(LocalContext.current)
            val state = HomeState(
                search = StringUtil.randomString(50, false),
                words = listOf(
                    Word("a", StringUtil.randomString(10, false)),
                    Word(
                        StringUtil.randomString(50, false),
                        StringUtil.randomString(50, false)
                    ),
                ),
            )
            Home.Page(
                onItemClick = {},
                viewModel = object : HomeVM(state) {
                    override fun onSearchChanged(search: String) {}
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object Home {
    const val ROUTE = "home"

    fun NavGraphBuilder.homePage(
        navigateToWordDetail: (word: String) -> Unit,
        viewModelBuilder: @Composable () -> HomeVM,
    ) {
        composable(ROUTE) {
            Page(onItemClick = navigateToWordDetail, viewModel = viewModelBuilder())
        }
    }

    @Composable
    fun Page(onItemClick: (word: String) -> Unit, viewModel: HomeVM = mavericksViewModel()) {
        val state by viewModel.collectAsState()
        val snackbarState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val showSnackBar: (String) -> Unit = remember { { scope.launch { snackbarState.showSnackbar(it) } } }
        Scaffold(
            topBar = { AppHeader(showSnackBar) },
            snackbarHost = { SnackbarHost(hostState = snackbarState) },
        ) {
            Column(modifier = Modifier.padding(it)) {
                WordInput(state.search, viewModel::onSearchChanged)
                WordList(words = state.words, onItemClick = onItemClick)
            }
        }
    }

    @Composable
    fun AppHeader(onBuzzError: (message: String) -> Unit) {
        val buzzState = rememberBuzzState()
        val buzzErrorMessage = stringResource(R.string.not_implemented_yet)
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.word_search), modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            },
            actions = {
                IconButton(
                    enabled = !buzzState.hasError, onClick = {
                        buzzState.hasError = true
                        onBuzzError(buzzErrorMessage)
                    }, modifier = Modifier.buzz(buzzState)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Select Language",
                        tint = buzzState.color,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = Color.Red,
            ),
        )
    }

    @Composable
    fun WordInput(search: String, onSearchChanged: (String) -> Unit) {
        Surface(color = MaterialTheme.colorScheme.primary) {
            WordChest.WordInput(
                search = search,
                onSearchChanged = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .innerShadow(
                        pathBuilder = PathBuilderUtil.topRound(16.dp),
                        offset = LocalDensity.current.run { Offset(0f, 1.dp.toPx()) },
                    )
            )
        }
    }

    @Composable
    fun WordList(words: List<Word>, onItemClick: (word: String) -> Unit) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.testTag("word list")
        ) {
            items(words) { word ->
                WordCard(word, onItemClick = { onItemClick(word.value) })
            }
        }
    }

    @Composable
    fun WordCard(word: Word, onItemClick: () -> Unit) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable(onClickLabel = stringResource(R.string.show_word_detail)) { onItemClick() }
                .clearAndSetSemantics {
                    contentDescription = "${word.value}: ${word.shortDescription}"
                }) {
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
