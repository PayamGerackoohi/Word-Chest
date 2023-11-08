package com.payamgr.wordchest.util

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertHasRole(role: Role) = SemanticsMatcher("is '$role'") {
    it.config.getOrNull(SemanticsProperties.Role) == role
}.let { assert(it) }
