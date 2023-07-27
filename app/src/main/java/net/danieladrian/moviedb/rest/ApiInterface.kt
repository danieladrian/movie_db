package net.danieladrian.moviedb.rest

import net.danieladrian.moviedb.rest.params.result.ResultGenre
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre
import net.danieladrian.moviedb.rest.params.result.ResultMovieDetail
import net.danieladrian.moviedb.rest.params.result.ResultUserReview
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("genre/movie/list")
    fun getGenres(@Query("language") language:String): Call<ResultGenre>

    @GET("discover/movie")
    fun getMovieByGenre(@Query("without_genres") genreID:Int): Call<ResultMovieByGenre>

    @GET("movie/{movie_id}/reviews")
    fun getUserReviews(@Path("movie_id") movieID:Int): Call<ResultUserReview>

    @GET("movie/{movie_id}/videos")
    fun getVideos(@Path("movie_id") movieID:Int): Call<ResultUserReview>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieID:Int): Call<ResultMovieDetail>
}