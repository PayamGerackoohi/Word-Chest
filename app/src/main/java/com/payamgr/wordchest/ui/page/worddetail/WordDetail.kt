package com.payamgr.wordchest.ui.page.worddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part.Sub
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.ui.preview.SinglePreview
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.ui.util.FlexibleRow
import kotlinx.coroutines.flow.MutableStateFlow

@SinglePreview
@Composable
fun WordDetailPage_Preview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Mavericks.initialize(LocalContext.current)
            WordDetail.Page(
                viewModel = WordDetailViewModelImpl(
                    initialState = WordDetailState(0),
                    repository = object : WordRepository {
                        override val wordHistory = MutableStateFlow<List<String>>(listOf())
                        override suspend fun wordOf(layer: Int): String = ""
                        override suspend fun searchFor(word: String): List<Word> = listOf()
                        override suspend fun detailsAt(layer: Int): List<Word.Data> = listOf(
                            10.let { n ->
                                Word.Data(
                                    "a".repeat(n),
                                    "b".repeat(n),
                                    Word.Data.Section(Word.Data.Section.Type.Noun)
                                )
                            },
                            20.let { n ->
                                Word.Data(
                                    "a".repeat(n),
                                    "b".repeat(n),
                                    Word.Data.Section(Word.Data.Section.Type.Noun)
                                )
                            },
                            30.let { n ->
                                Word.Data(
                                    "a".repeat(n),
                                    "b".repeat(n),
                                    Word.Data.Section(Word.Data.Section.Type.Noun)
                                )
                            },
                            40.let { n ->
                                Word.Data(
                                    "a".repeat(n),
                                    "b".repeat(n),
                                    Word.Data.Section(Word.Data.Section.Type.Noun)
                                )
                            },
                        )

                        override suspend fun push(layer: Int, word: String) {}
                    }
                )
            )
        }
    }
}

object WordDetail {
    private const val ROUTE = "word-detail"
    private const val LAYER_ARG = "layer"

    fun NavGraphBuilder.wordDetail(viewModelBuilder: @Composable () -> WordDetailViewModel) {
        composable(
            route = "$ROUTE/{$LAYER_ARG}",
            arguments = listOf(navArgument(LAYER_ARG) { type = NavType.IntType }),
        ) {
            val viewModel = viewModelBuilder()
            viewModel.set(it.arguments?.getInt(LAYER_ARG) ?: 1)
            Page(viewModel = viewModel)
        }
    }

    fun NavHostController.navigateToWordDetail(layer: Int = 0) = navigate("$ROUTE/$layer")

    @Composable
    fun Page(viewModel: WordDetailViewModel = mavericksViewModel()) {
        val state by viewModel.collectAsState()
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.testTag("word data list")
        ) {
            state.data.let { data ->
                items(data.size) { index -> WordData(data[index], indexAt(data.size, index)) }
            }
        }
    }

    private fun indexAt(size: Int, index: Int) = if (size > 1) (index + 1).toString() else null

    @Composable
    fun WordData(data: Word.Data, index: String?) {
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
                    index = index,
                    modifier = Modifier
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
            data.sections.forEach { Section(it) }
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
                it,
                color = color,
                style = style,
                modifier = modifier
            )
        }
    }

    @Composable
    fun Section(section: Word.Data.Section) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("word section")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
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
                        Definition(definition, indexAt(section.definitions.size, index))
                    }
                }
            }
        }
    }

    @Composable
    fun Definition(data: Word.Data.Section.Definition, index: String?) {
        Row(modifier = Modifier.testTag("word definition")) {
            IndexItem(
                index = index,
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("section index")
            )
            Column {
                Part(data.value, modifier = Modifier.testTag("section value"))
                Column(modifier = Modifier.testTag("section samples")) {
                    data.samples.forEach { SamplePart(it) }
                }
            }
        }
    }

    @Composable
    fun Part(data: Word.Data.Section.Definition.Part, modifier: Modifier = Modifier) {
        Text(
            buildAnnotatedString {
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
                        append(sub.content)
                    }
                }
            },
            modifier = modifier
        )
    }

    @Composable
    fun SamplePart(data: Word.Data.Section.Definition.Part, modifier: Modifier = Modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .testTag("sample part")
                .semantics(mergeDescendants = true) {}) {
            Text("●")
            Part(data, modifier = modifier)
        }
    }
}
