package com.ravi.mvisearch.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.mvisearch.domain.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SearchRepository
    ): ViewModel() {

    var mainIntent = Channel<MainIntent> { Channel.UNLIMITED }
    private var job: Job? = null

    var mainState = mutableStateOf<MainState>(MainState.SUCCESS(emptyList()))
        private set

    init {
        handleIntent()
    }

    fun processIntent(intent: MainIntent) {
        viewModelScope.launch {
            mainIntent.send(intent)  // Assuming mainIntent is a Channel or a SharedFlow
        }
    }

    private fun handleIntent(){
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect{
                when(it){
                    is MainIntent.FetchCockTails -> fetchCocktailList()
                    is MainIntent.MainCocktails -> fetchSearchResult(it.query.toString())
                }
            }
        }
    }

    private fun fetchCocktailList() {
        viewModelScope.launch {
            mainState.value = MainState.LOADING // try emit instead of value
            mainState.value = try{
                val res = repository.getCocktails()
                MainState.SUCCESS(res.data)
            }catch (e: Exception){
                MainState.ERROR("Unknown Data")
            }
        }
    }

    private fun fetchSearchResult(q: String) {
       job?.cancel()
       job = viewModelScope.launch {
           delay(200)
           if(q.isBlank()){
               fetchCocktailList()
           }else {
               mainState.value = try {
                   val res = repository.getSearchItem(q)
                   MainState.SUCCESS(res.data)
               } catch (e: Exception) {
                   MainState.ERROR("Unknown Data")
               }
           }
       }
    }

}