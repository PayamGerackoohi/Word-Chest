package com.payamgr.wordchest.ui.page.worddetail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.model.HistoryActions
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part.Sub
import com.payamgr.wordchest.data.model.WordKey
import com.payamgr.wordchest.data.model.fake.FakeWord
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.ui.modules.History
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.ui.modules.FlexibleRow
import com.payamgr.wordchest.ui.modules.WordChest
import com.payamgr.wordchest.ui.preview.SinglePreview
import com.payamgr.wordchest.ui.util.bottomSheetPeek
import com.payamgr.wordchest.ui.util.hide
import com.payamgr.wordchest.ui.util.isHidden
import com.payamgr.wordchest.ui.util.openIfHidden
import com.payamgr.wordchest.ui.util.rememberHidableBottomSheetScaffoldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SinglePreview
@Composable
fun WordDetailPage_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Mavericks.initialize(LocalContext.current)
            WordDetail.Page(
                viewModel = WordDetailVMImpl(
                    WordDetailState(), repository = object : WordRepository {
                        private val words = FakeWord().wordData
                        override val wordHistory = MutableStateFlow(listOf("aaa", "bbb"))
                        override val currentLayer = MutableStateFlow(0)
                        override val currentWord = MutableStateFlow("")
                        override val details = MutableStateFlow(words)
                        override suspend fun push(key: WordKey) {}
                        override suspend fun pop(): Boolean = true
                        override suspend fun searchFor(word: String) =
                            (1..word.length).map { Word("$word - $it", "") }
                    }, 500L
                ),
                showWordDetail = {},
            )
        }
    }
}

@Composable
fun WordDetailPage_LongWord_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Mavericks.initialize(LocalContext.current)
            WordDetail.Page(
                viewModel = WordDetailVMImpl(
                    initialState = WordDetailState(), repository = object : WordRepository {
                        override val wordHistory = MutableStateFlow<List<String>>(listOf())
                        override val currentLayer = MutableStateFlow(0)
                        override val currentWord = MutableStateFlow("")
                        override suspend fun searchFor(word: String): List<Word> = listOf()
                        override suspend fun push(key: WordKey) {}
                        override suspend fun pop(): Boolean = true
                        override val details = MutableStateFlow(
                            (10..40 step 10).map { n ->
                                Word.Data(
                                    "a".repeat(n),
                                    "b".repeat(n),
                                    Word.Data.Section(Word.Data.Section.Type.Noun)
                                )
                            }
                        )
                    }, 500L
                ),
                showWordDetail = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object WordDetail {
    private const val ROUTE = "word-detail"

    fun NavGraphBuilder.wordDetail(
        viewModelBuilder: @Composable () -> WordDetailVM,
        showWordDetail: (key: WordKey) -> Unit,
    ) {
        composable(route = ROUTE) {
            Page(viewModel = viewModelBuilder(), showWordDetail = showWordDetail)
        }
    }

    fun NavHostController.navigateToWordDetail() = navigate(ROUTE) { launchSingleTop = true }

    @Composable
    fun Page(
        showWordDetail: (key: WordKey) -> Unit,
        viewModel: WordDetailVM = mavericksViewModel(),
    ) {
        val state by viewModel.collectAsState()
        val scope = rememberCoroutineScope()
        var showHistory by remember { mutableStateOf(false) }
        val searchSectionState = rememberHidableBottomSheetScaffoldState()
        var firstItemHeight by remember { mutableIntStateOf(0) }
        var searchPeekHeight by remember { mutableIntStateOf(0) }
        val focusManager = LocalFocusManager.current

        BackHandler(enabled = state.canPop) { viewModel.pop() }

        LaunchedEffect(searchSectionState) {
            snapshotFlow { searchSectionState.bottomSheetState.currentValue }.collect {
                if (it == SheetValue.Hidden) focusManager.clearFocus()
            }
        }

        BottomSheetScaffold(modifier = Modifier.fillMaxSize(),
            scaffoldState = searchSectionState,
            sheetShadowElevation = 4.dp,
            sheetPeekHeight = LocalDensity.current.bottomSheetPeek(searchPeekHeight),
            sheetContent = {
                SearchSection(
                    word = state.searchKey,
                    list = state.searchResults,
                    onWordChanged = { viewModel.search(it) },
                    onFirstItem = { firstItemHeight = it },
                    onClose = { scope.launch { searchSectionState.hide() } },
                    onItemClick = { word ->
                        scope.launch { searchSectionState.hide() }
                        showWordDetail(WordKey.Word(word))
                    },
                )
            }) {
            val bottomPadding by animateDpAsState(
                label = "bottomPadding",
                targetValue = if (searchSectionState.isHidden) 0.dp else it.calculateBottomPadding()
            )
            PageContent(
                data = state.data,
                historyActions = HistoryActions(
                    show = showHistory,
                    toggleShow = { showHistory = !showHistory },
                    list = state.historyList,
                    onClicked = { layer ->
                        showHistory = false
                        showWordDetail(WordKey.Layer(layer))
                    },
                ),
                searchFor = { word ->
                    viewModel.search(word)
                    showHistory = false
                    scope.launch {
                        searchPeekHeight = firstItemHeight
                        focusManager.clearFocus()
                        searchSectionState.openIfHidden()
                    }
                },
                layer = state.layer,
                modifier = Modifier.padding(bottom = bottomPadding),
            )
        }
    }

    @Composable
    fun PageContent(
        data: List<Word.Data>,
        historyActions: HistoryActions,
        searchFor: (word: String) -> Unit,
        modifier: Modifier = Modifier,
        layer: Int,
    ) {
        Box(modifier = modifier) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.testTag("word data list")
            ) {
                items(data.size) { index ->
                    WordData(
                        data[index],
                        indexAt(data.size, index),
                        searchFor = searchFor,
                    )
                }
            }
            AnimatedVisibility(
                visible = historyActions.list.isNotEmpty(),
                modifier = Modifier.fillMaxSize(),
            ) {
                Box {
                    History.List(
                        expand = historyActions.show,
                        list = historyActions.list,
                        currentLayer = layer,
                        onItemClicked = { layer -> historyActions.onClicked(layer) },
                        modifier = Modifier
                            .fillMaxWidth(fraction = .5f)
                            .align(Alignment.BottomStart)
                    )
                    FloatingActionButton(
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = historyActions.toggleShow,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Show/Hide History",
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SearchSection(
        word: String,
        list: List<Word>,
        onWordChanged: (String) -> Unit,
        onFirstItem: (height: Int) -> Unit,
        onClose: () -> Unit,
        onItemClick: (word: String) -> Unit,
    ) {
        LazyColumn(modifier = Modifier.testTag("search section")) {
            item {
                SearchSectionHeader(
                    word = word,
                    onWordChanged = onWordChanged,
                    onFirstItem = onFirstItem,
                    onClose = onClose,
                )
            }
            items(list) { (value) ->
                SearchSectionItem(word = value, onItemClick = onItemClick)
            }
        }
    }

    @Composable
    fun SearchSectionItem(
        word: String,
        onItemClick: (word: String) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search section item")
                .clearAndSetSemantics {
                    contentDescription = word
                    role = Role.Button
                }
        ) {
            Divider()
            TextButton(
                onClick = { onItemClick(word) }, shape = RectangleShape
            ) {
                Text(
                    text = word,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun SearchSectionHeader(
        word: String,
        onWordChanged: (String) -> Unit,
        onFirstItem: (height: Int) -> Unit,
        onClose: () -> Unit,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .onGloballyPositioned { onFirstItem(it.size.height) }
                .padding(bottom = 16.dp)
        ) {
            WordChest.WordInput(
                search = word,
                onSearchChanged = onWordChanged,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            )
            IconButton(
                onClick = onClose, modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Search Section",
                )
            }
        }
    }

    private fun indexAt(size: Int, index: Int) = if (size > 1) (index + 1).toString() else null

    @Composable
    fun WordData(data: Word.Data, index: String?, searchFor: (word: String) -> Unit) {
        FlexibleRow(
            horizontalSpacing = 8.dp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .testTag("word data")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    data.word,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("word title")
                )
                IndexItem(
                    index = index, modifier = Modifier
                        .align(Alignment.Top)
                        .testTag("word index")
                )
            }
            Text(
                "|${data.pronunciation}|",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.testTag("word pronunciation")
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            data.sections.forEach { Section(it, searchFor = searchFor) }
        }
    }

    @Composable
    fun IndexItem(
        index: String?,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colorScheme.outline,
        style: TextStyle = MaterialTheme.typography.labelLarge,
    ) {
        index?.let {
            Text(
                it, color = color, style = style, modifier = modifier
            )
        }
    }

    @Composable
    fun Section(section: Word.Data.Section, searchFor: (word: String) -> Unit) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("word section")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    section.type.apply {
                        Text(
                            type.label,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("word type")
                        )
                        meta?.let {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.outline,
                                fontStyle = FontStyle.Italic,
                            )
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    section.definitions.forEachIndexed { index, definition ->
                        Definition(definition, indexAt(section.definitions.size, index), searchFor = searchFor)
                    }
                }
            }
        }
    }

    @Composable
    fun Definition(data: Word.Data.Section.Definition, index: String?, searchFor: (word: String) -> Unit) {
        Row(modifier = Modifier.testTag("word definition")) {
            IndexItem(
                index = index,
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("section index")
            )
            Column {
                Part(data.value, searchFor = searchFor, modifier = Modifier.testTag("section value"))
                Column(modifier = Modifier.testTag("section samples")) {
                    data.samples.forEach { SamplePart(it, searchFor = searchFor) }
                }
            }
        }
    }

    @Composable
    fun Part(
        data: Word.Data.Section.Definition.Part,
        modifier: Modifier = Modifier,
        searchFor: (word: String) -> Unit,
    ) {
        val text = buildAnnotatedString {
            data.subs.forEach { sub ->
                withStyle(
                    style = when (sub.style) {
                        Sub.Style.Normal -> SpanStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                        )

                        Sub.Style.Sample -> SpanStyle(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontStyle = FontStyle.Italic,
                        )

                        Sub.Style.Meta -> SpanStyle(
                            color = MaterialTheme.colorScheme.outline,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                ) {
                    sub.sentenceParts.forEach { (part, isWord) ->
                        if (isWord) {
                            pushStringAnnotation(part, part)
                            append(part)
                            pop()
                        } else append(part)
                    }
                }
            }
        }
        ClickableText(text = text, modifier = modifier) { offset ->
            text.getStringAnnotations(offset, offset).firstOrNull()?.let { searchFor(it.item) }
        }
    }

    @Composable
    fun SamplePart(
        data: Word.Data.Section.Definition.Part,
        modifier: Modifier = Modifier,
        searchFor: (word: String) -> Unit,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .testTag("sample part")
                .semantics(mergeDescendants = true) {}) {
            Text("●")
            Part(data, searchFor = searchFor, modifier = modifier)
        }
    }
}
