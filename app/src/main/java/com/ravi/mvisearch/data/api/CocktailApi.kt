package com.ravi.mvisearch.data.api

import com.ravi.mvisearch.data.model.CocktailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    @GET("v1/1/search.php?")
    suspend fun searchCocktail(@Query("s") text: String): CocktailDto

    @GET("v1/1/filter.php?c=Cocktail")
    suspend fun getCocktails(): CocktailDto
}