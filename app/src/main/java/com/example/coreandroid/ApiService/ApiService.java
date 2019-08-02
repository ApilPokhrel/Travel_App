package com.example.coreandroid.ApiService;

import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.RatingModel;
import com.example.coreandroid.entity.UserModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/v1/user")
    Call<UserModel> insertAllUser(@Body UserModel user);

    @POST("api/v1/user/login")
    Call<UserModel> loginUser(@Body UserModel user);

    @GET("api/v1/user/{id}")
    Call<UserModel> getUser(@HeaderMap Map<String, String> headers,@Path(value = "id", encoded = true) String id);

    @GET("api/v1/place/")
    Call<List<PlaceModel>> getAllPlace(@HeaderMap Map<String, String> headers, @Query("start") int start, @Query("limit") int limit, @Query("filter") String filter, @Query("tag") String tag, @Query("lat") String lat, @Query("lng") String lng, @Query("distance") String distance);

    @POST("api/v1/place/{id}/rating")
    Call<RatingModel> postRating(@HeaderMap Map<String, String> headers,@Path(value = "id", encoded = true) String id, @Body RatingModel ratingModel);


    @GET("api/v1/place/{id}/rating")
    Call<RatingModel> getRating(@HeaderMap Map<String, String> headers,@Path(value = "id", encoded = true) String id);

}
