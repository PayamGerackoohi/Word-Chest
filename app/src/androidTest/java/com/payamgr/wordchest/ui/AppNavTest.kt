package com.payamgr.wordchest.ui

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.util.FakeViewModelBuilder
import com.payamgr.wordchest.util.app
import com.payamgr.wordchest.util.assertCurrentRoute
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppNavTest {
    @get:Rule
    val rule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private val push: (String) -> Unit = spyk({})
    private val viewModelBuilder = FakeViewModelBuilder()

    @Before
    fun setupAppNavHost() {
        MockableMavericks.initialize(app)
        rule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            AppNav.Host(
                navController = navController,
                push = push,
                viewModelBuilder = viewModelBuilder,
            )
        }
    }

    @Test
    fun verifyStartDestination() {
        rule.onNodeWithText("Word Search").assertIsDisplayed()
        navController.assertCurrentRoute("home")
    }

    @Test
    fun homeToWordDetailsNavigationTest() = runTest {
        val inputTag = "word input"

        // search for 'aaa'
        rule.onNodeWithTag(inputTag)
            .assertTextEquals("")
            .performTextInput("aaa")

        // select the first item in the list
        rule.onNodeWithTag("word list")
            .onChildren()
            .assertCountEquals(3)[0]
            .assertContentDescriptionEquals("aaa-1: aaa")
            .assertIsDisplayed()
            .performClick()

        // verify 'push word' is called
        verify { push(any()) }

        // verify navigation to 'WordDetail'
        navController.assertCurrentRoute("word-detail/{layer}")

        // verify back press returns to home
        withContext(Dispatchers.Main) { navController.popBackStack() }
        navController.assertCurrentRoute("home")

        confirmVerified(push)
    }
}
