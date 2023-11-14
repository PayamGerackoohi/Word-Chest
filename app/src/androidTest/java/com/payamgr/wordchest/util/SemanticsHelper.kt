package com.payamgr.wordchest.util

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.navigation.NavHostController
import org.assertj.core.api.AbstractStringAssert
import org.assertj.core.api.Assertions

fun SemanticsNodeInteraction.assertHasRole(role: Role) = SemanticsMatcher("is '$role'") {
    it.config.getOrNull(SemanticsProperties.Role) == role
}.let { assert(it) }

fun NavHostController.assertCurrentRoute(route: String): AbstractStringAssert<*> =
    Assertions.assertThat(currentBackStackEntry?.destination?.route).isEqualTo(route)
