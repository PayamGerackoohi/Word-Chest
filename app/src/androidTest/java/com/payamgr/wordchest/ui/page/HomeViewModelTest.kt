package com.payamgr.wordchest.ui.page

import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.airbnb.mvrx.test.MavericksTestRule
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.util.app
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test

@MediumTest
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mavericksTestRule = MavericksTestRule(testDispatcher = UnconfinedTestDispatcher())

    @Test
    fun searchForTest() = runTest {
        MockableMavericks.initialize(app)
        val repository = spyk(fakeRepository())
        val viewModel = HomeViewModelImpl(HomeState(), repository)

        // Check initial state
        assertThat(viewModel.awaitState()).isEqualTo(HomeState())

        // Search
        viewModel.onSearchChanged("aaa")
        assertThat(viewModel.awaitState()).isEqualTo(HomeState("aaa"))
        coVerify { repository.searchFor(any()) }

        confirmVerified(repository)
    }

    private fun fakeRepository() = object : WordRepository {
        override suspend fun searchFor(word: String): List<Word> = if (word.isBlank()) listOf()
        else List(3) { Word("Word $it", "Description $it") }
    }
}
