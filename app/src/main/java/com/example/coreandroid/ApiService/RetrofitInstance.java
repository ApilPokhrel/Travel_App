package com.example.coreandroid.ApiService;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.coreandroid.entity.Variables.BASE_URL;

public class RetrofitInstance {

    private static Retrofit instance;
//    public static final String BASE_URL = "https://2019dea5.ngrok.io/";

    public static Retrofit getInstance(){
        if(instance == null){
            instance =  new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return instance;
    }
}
