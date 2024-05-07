package com.ravi.mvisearch.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.ravi.mvisearch.data.model.Drink
import com.ravi.mvisearch.presentation.theme.SearchComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onSearchClick: (query: String?) -> Unit = {
            lifecycleScope.launch {
                viewModel.mainIntent.send(MainIntent.MainCocktails(it))
            }
        }

        viewModel.processIntent(MainIntent.FetchCockTails)

        setContent {
            SearchComposeTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Cocktails",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            })
                    },
                    modifier = Modifier.background(Color.Blue)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CocktailView(onSearchClick = onSearchClick)
                    }
                }
            }
        }
    }
    @Composable
    fun CocktailView(
        mainViewModel: MainViewModel = hiltViewModel(),
        onSearchClick: (String) -> Unit
    ){
        when(val state = mainViewModel.mainState.value){
            is MainState.LOADING -> LoadingScreen()
            is MainState.SUCCESS -> CocktailList(drink = state.list, onSearchClick)
            is MainState.ERROR -> Toast.makeText(
                LocalContext.current, state.error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun CocktailList(drink: List<Drink>?, onSearchClick: (String) -> Unit){
        var searchQuery by remember { mutableStateOf("") }
        Column {
            TextField(value = searchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = { newText ->
                    searchQuery = newText
                    onSearchClick(searchQuery)
                },
                placeholder = {Text(text = "enter any drink")},
                singleLine = true
            )

            LazyColumn {
                if (!drink.isNullOrEmpty()) {
                    items(drink) { drink ->
                        DrinkItem(drink = drink)
                    }
                }
            }
        }
    }

    @Composable
    fun DrinkItem(drink: Drink){
        Row(modifier = Modifier.padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberAsyncImagePainter(model = drink.strDrinkThumb)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.FillHeight
            )

            Column(Modifier.padding(start = 4.dp)) {
                Text(text = drink.strDrink)
            }
        }
    }
}