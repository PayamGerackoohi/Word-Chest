package com.payamgr.wordchest.ui.page.home

import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.airbnb.mvrx.test.MavericksTestRule
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.util.FakeRepository
import com.payamgr.wordchest.util.app
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test

@MediumTest
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mavericksTestRule = MavericksTestRule()

    @Test
    fun searchForTest() = runTest {
        MockableMavericks.initialize(app)
        val repository = spyk(FakeRepository())
        val viewModel = HomeVMImpl(HomeState(), repository, 0L)

        // Check initial state
        assertThat(viewModel.awaitState()).isEqualTo(HomeState())

        // Search
        viewModel.onSearchChanged("aaa")
        val state = viewModel.awaitState()
        assertThat(state.search).isEqualTo("aaa")
        assertThat(state.words).hasSize(3)
        coVerify { repository.searchFor(any()) }

        confirmVerified(repository)
    }
}
