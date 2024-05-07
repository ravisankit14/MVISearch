package com.ravi.mvisearch.data

import com.ravi.mvisearch.data.api.CocktailApi
import com.ravi.mvisearch.data.model.CocktailDto
import com.ravi.mvisearch.data.model.Drink
import com.ravi.mvisearch.domain.SearchRepository
import com.ravi.mvisearch.util.Resource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: CocktailApi
): SearchRepository {
    override suspend fun getSearchItem(query: String): Resource<List<Drink>> {
        return try {
            Resource.Success(
                data = api.searchCocktail(query).drinks
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message?: "Unknown Error")
        }
    }

    override suspend fun getCocktails(): Resource<List<Drink>> {
        return try {
            Resource.Success(
                data = api.getCocktails().drinks
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message?: "Unknown Error")
        }
    }
}