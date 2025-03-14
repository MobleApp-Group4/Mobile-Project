package com.example.todo.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    val results: List<Movie>,
    val page: Int
)

data class Movie(
    var id: Int,
    var title: String,
    var overview: String,
    var popularity: Double,
    var runtime: Int,
    @SerializedName("vote_average") var vote: Double,
    @SerializedName("poster_path") var poster: String

)