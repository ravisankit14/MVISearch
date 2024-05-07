package com.ravi.mvisearch.presentation.ui

import com.ravi.mvisearch.data.model.Drink

sealed class MainState {

    data object LOADING: MainState()

    data class SUCCESS(val list: List<Drink>?): MainState()

    data class ERROR(val error: String?) : MainState()

}