package com.payamgr.wordchest.ui.page

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.util.app
import org.junit.Rule
import org.junit.Test

@MediumTest
class HomeTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun greetTest() {
        MockableMavericks.initialize(app)
        rule.setContent { Home.Greeting() }
        rule.onNodeWithText("Hello!")
            .assertIsDisplayed()
    }
}
