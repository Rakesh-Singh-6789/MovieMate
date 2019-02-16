package com.example.rakeshsingh.moviemate.retrofit;

import com.example.rakeshsingh.moviemate.ModelClasses.MovieResult;
import com.example.rakeshsingh.moviemate.ModelClasses.Result;
import com.example.rakeshsingh.moviemate.ModelClasses.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroInterface {

    @GET("movie/{filter}/")
    Call<MovieResult> getMovieResultCall(@Path("filter") String filter, @Query("api_key") String apiKey);


    @GET("movie/{id}")
    Call<Result> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") int id,
                                           @Query("api_key") String api_key);
}
