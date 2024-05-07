package com.ravi.mvisearch.presentation.ui

sealed class MainIntent {

    data object FetchCockTails: MainIntent()

    class MainCocktails(val query: String?): MainIntent()
}