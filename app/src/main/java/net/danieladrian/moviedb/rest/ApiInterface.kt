package net.danieladrian.moviedb.rest

import net.danieladrian.moviedb.rest.params.result.ResultGenre
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("genre/movie/list")
    fun getGenres(@Query("language") language:String): Call<ResultGenre>
}