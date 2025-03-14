package com.example.todo.api



import com.example.todo.model.RecipeDetail
import com.example.todo.model.RecipesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val RECIPE_BASE_URL = "https://api.spoonacular.com/"
//const val RECIPE_API_KEY = "9f936231ffbf466d87c547e0e415120e"
const val RECIPE_API_KEY = "8bb7956733bd400880ae0036aeaccff3"

interface RecipesApi {
    @GET("recipes/complexSearch")
    suspend fun getAllRecipes(
        @Query("apiKey") apiKey: String = RECIPE_API_KEY,
        @Query("cuisine") cuisine: String = "Asian",
        @Query("number") number: Int = 50
        ): RecipesResponse

    @GET("recipes/complexSearch")
    suspend fun getRecipesByDiet(
        @Query("cuisine") cuisine: String = "Asian",
        @Query("apiKey") apiKey: String = RECIPE_API_KEY,
        @Query("diet") diet: String? = null,
        @Query("number") number: Int = 30
    ): RecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String = RECIPE_API_KEY,
    ): RecipeDetail





    companion object {

        val recipesService: RecipesApi by lazy {
            Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipesApi::class.java)
        }
    }
}