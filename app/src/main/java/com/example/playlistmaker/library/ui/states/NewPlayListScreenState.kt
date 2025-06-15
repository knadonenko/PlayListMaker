package com.example.playlistmaker.library.ui.states

sealed class NewPlayListScreenState(val createBtnState: CreateBtnState = CreateBtnState.ENABLED) {

    class Empty(
        createBtnState: CreateBtnState = CreateBtnState.DISABLED,
    ) : NewPlayListScreenState(createBtnState)

    class HasContent(
        createBtnState: CreateBtnState = CreateBtnState.ENABLED,
    ) : NewPlayListScreenState(createBtnState)

    data object NeedsToAsk : NewPlayListScreenState()

    data object AllowedToGoOut : NewPlayListScreenState()

}