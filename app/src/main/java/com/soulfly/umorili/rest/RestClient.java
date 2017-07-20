package com.soulfly.umorili.rest;


import com.soulfly.umorili.ui.util.ConstansManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private UmoriliAPI umoriliAPI;

    public RestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstansManager.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        umoriliAPI = retrofit.create(UmoriliAPI.class);
    }

    public UmoriliAPI getUmoriliAPI() {
        return umoriliAPI;
    }
}
