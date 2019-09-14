package com.sahil.roomexample.retrofit;

import com.sahil.roomexample.modelMovie.MoviesResponse;
import com.sahil.roomexample.modelMovie.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UploadAPIs {

    @GET("movie/popular")
    Call<MoviesResponse> getTMDBdata(@Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("page") int page);
}
