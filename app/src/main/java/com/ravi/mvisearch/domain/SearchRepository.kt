package com.ravi.mvisearch.domain

import com.ravi.mvisearch.data.model.CocktailDto
import com.ravi.mvisearch.data.model.Drink
import com.ravi.mvisearch.util.Resource

interface SearchRepository {
    suspend fun getSearchItem(query: String): Resource<List<Drink>>
    suspend fun getCocktails(): Resource<List<Drink>>
}