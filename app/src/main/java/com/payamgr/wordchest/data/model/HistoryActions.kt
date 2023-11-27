package com.payamgr.wordchest.data.model

/**
 * Word Detail Page actions related to history detail manipulation
 * @property show shows history list if it's true
 * @property toggleShow toggles the show state
 * @property list the list of words stacked in the history
 * @property onClicked the history item click callback at the layer of the word in the history stack
 */
data class HistoryActions(
    val show: Boolean,
    val toggleShow: () -> Unit,
    val list: List<String>,
    val onClicked: (layer: Int) -> Unit,
)
