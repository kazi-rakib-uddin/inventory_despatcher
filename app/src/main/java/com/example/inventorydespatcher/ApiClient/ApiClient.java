package com.example.inventorydespatcher.ApiClient;


import com.example.inventorydespatcher.Interface.MyInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    //public static final String BASE_URL = "http://new.easytodb.com/New_Inventory/dispatcher_android_api/";
    public static final String BASE_URL = "http://tiwaryleather.com/New_Inventory/dispatcher_android_api/";

    public static Retrofit retrofit = null;
    MyInterface apiInterface;
    public static Retrofit getApiClient()
    {
        if(retrofit==null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    }



